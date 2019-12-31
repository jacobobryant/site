{:title "Building a CRUD app with Datomic Cloud Ions", :date "2019-05-09"}

*Update: published [source code](https://github.com/jacobobryant/flexbudget).*

I just released [FlexBudget](https://notjust.us), the website version of a
script I wrote several months ago to handle our budgeting needs.[1]
I built it using [Datomic Cloud
ions](https://docs.datomic.com/cloud/ions/ions.html). I started using Datomic
On-Prem sometime last year, but this was my first time using Datomic Cloud (let
alone ions).[2]

I think Clojure + Datomic opens the doors for some great innovations in web
application architecture (e.g. the ideas discussed in [this
classic](https://tonsky.me/blog/the-web-after-tomorrow/)), not to mention that
Datomic alone has spoiled me and I don't know if I could ever go back to SQL
now. But even with ions, I think there's still a lot of work left to be done on
Clojure's web dev story. What follows is a description of my experience in
setting up FlexBudget.

*Note: This post assumes you are already familiar with Clojure and Datomic.*

**Routing**

I ended up using a mono-Lambda that forwarded all
requests to the same ion, then that ion used Compojure to route the request to
the appropriate handler. For local development, I just ran a local web server
with that handler.

```language-clojure
(def handler'
  (-> routes
      ; various middleware
      wrap-catchall))

(def handler (ionize handler'))

(defn start-immutant []
  (imm/run handler' {:port 8080}))
```

(I started out using Aleph, but I was getting a weird bug where it wasn't
passing requests to my handler. I'm not sure what was going on there, but
things started working again when I used Immutant. I also tried Jetty, but it
had dependency conflicts with Datomic Cloud.)

This worked great for my little project, though maybe in the future I'll need
to stop using a mono-Lambda.

<span id="transaction-functions">**Transaction Functions**</span>

This was more complicated. To use a custom transaction function, it has to be
deployed. You can't tell the transactor (which runs in the cloud) to somehow
use a local transaction function that's only defined on your laptop. The
[official
advice](https://docs.datomic.com/cloud/operation/planning.html#transaction-functions)
is:

> Transaction functions are pure functions, so you do not need to deploy them
> anywhere for testing. You can simply invoke them as ordinary code in your REPL
> or test suite.


I think that leaves a little to be desired, though. I also want to actually use
my website while I'm developing it. I need my site, running on localhost, to be
able to send requests to my ions, also running on localhost, and then have those ions
run transaction functions that are only defined on my laptop (i.e. haven't been
deployed yet).

So, I wrote a function `eval-tx-fns` which takes a transaction function and
applies it locally. Then the "plain" transaction can be sent to the transactor.

```language-clojure
(def transact (if (:local-tx-fns? config)
                (let [lock (Object.)]
                  (fn [conn arg-map]
                    (locking lock
                      (->> #(u/eval-tx-fns (d/with-db conn) %)
                           (update arg-map :tx-data)
                           (d/transact conn)))))
                d/transact))
```

Out of laziness, I created this `transact` function and used that whenever I
needed to transact something. I also had to write a similar replacement function
for `with`. It would probably be cleaner to create my own implementation of the
Datomic client protocol as is done
[here](https://github.com/ComputeSoftware/datomic-client-memdb/blob/master/src/compute/datomic_client_memdb/core.clj#L90).

The `locking` call is used to make sure that transactions stay serialized. This
works only because during dev, all transactions to the dev database I'm using go
through a single machine (my laptop). And being a single developer using the
Solo topology, that's fine. However, it could be an issue for a
production system with [query groups for different stages](https://docs.datomic.com/cloud/operation/planning.html#stages).

The staging query group can only use transaction functions that have been deployed
to the primary group. If we update a transaction function and want staging to
run the updated version before it gets deployed to the primary group, we'll have
to apply the transaction locally in the manner I just described. If the staging
query group is limited to one instance, then we can keep the transactions
serialized by using `locking` as we did before. Otherwise, we'd have to add some
kind of external lock to ensure that only one instance is executing transaction
functions at a time.

This strategy may lower transaction throughput, but that's probably acceptable
for dev stages. An alternate solution would be running a separate production
topology for each stage, but I'm guessing that would be more expensive.[3]

**Deployment**

I wrote [this Planck
script](https://gist.github.com/jacobobryant/9c13f4cd692ff69d8f87b0d872aeb64e)
in order to automate the steps of "push, deploy, run the deploy status command
until it succeeds or fails".

I had some troubles with my deploys failing, even after I resolved dependency
conflicts and tested locally. The problem was always that I had some piece of
initialization code that started running as soon as the code was loaded. This caused
the deploy to hang and timeout. (Specifically, the `ValidateService` step would
hang, like in [this
question](https://forum.datomic.com/t/ion-deployment-failure/683)).

For example, you shouldn't load configuration with `datomic.ion/get-params`
until a request comes in. You can memoize the retrieval like so:

```language-clojure
(def get-params
  (memoize
    (fn [env]
      (-> {:path (str "/datomic-shared/" env "/bud/")}
          ion/get-params keywordize-keys))))
```

And then *don't call it until you have to*. I used Firebase for authentication,
and it requires some initialization code that fetched a secret from
`get-params`:

```language-clojure
(let [options ...] ; includes a call to get-params
  (FirebaseApp/initializeApp options))
```

One of my deployment failures was happening because I had the Firebase init code
running immediately. Deployment worked again after I wrapped it in an
`init-firebase!` function which I then called only when verifying tokens:

```language-clojure
(defn verify-token [token]
  (when (= 0 (count (FirebaseApp/getApps)))
    (init-firebase!))
  ...)
```

I also wrapped my calls to `d/client` and `d/conn` in memoized functions like in the
[ion starter
project](https://github.com/Datomic/ion-starter/blob/master/src/datomic/ion/starter.clj#L13),
but I found that they didn't get redefined when I ran
`clojure.tools.namespace.repl/refresh`. So I instead defined them as mount
states:

```language-clojure
(mount.core/defstate client :start
  (d/client (:client-cfg config)))
```

And then I added some ring middleware to start mount on the first request:
```language-clojure
(defn wrap-start-mount [handler]
  (fn [req]
    (when (contains? #{mount.core.NotStartedState
                       mount.core.DerefableState}
                     (type client))
      (mount.core/start))
    (handler req)))
```

**Interlude**

Throw in some logs with `datomic.ion.cast` and that about covers my experiences
directly with Datomic Cloud ions. It took a while to figure things out, but I'm
happy with it now, even though the transaction function thing seems a little hacky
(I'm not sure what else to do about that).

The rest of this post is about the way I set up the frontend/backend
interaction.

**DataScript**

I recently wrote a [Clue web app](https://github.com/jacobobryant/clue) that
used a single atom for storing frontend state (with Datomic on the backend).
Board games tend to have complicated data models, and I definitely felt the
impedance mismatch pains of having to project my Datomic data onto a
hierarchical atom. So with this project (even though the data model here is much
simpler right now), I definitely wanted to use DataScript.

After the user logs in, the frontend hits an `/init` endpoint which returns
their datoms:

```language-clojure
(defn datoms-for [db uid]
  (let [user-eid (:db/id (d/pull db [:db/id] [:user/uid uid]))]
    (->>
      (conj
        (vec (d/q '[:find ?e ?attr ?v :in $ ?user :where
                    [?ent :auth/owner ?user]
                    (or
                      [(identity ?ent) ?e]
                      [?ent :entry/asset ?e])
                    [?e ?a ?v]
                    [?a :db/ident ?attr]]
                  db user-eid))
        [user-eid :user/uid uid])
      (u/stringify-eids ds-schema))))
```

Basically, `datoms-for` looks for values of `:auth/owner` that correspond to the
current user. I'm not using any DB filters, but a better approach might be to do
that and then allow the frontend to send an arbitrary query.

The datoms also go through a `stringify-eids` function that I
wrote. This function takes e.g. `[[1 :foo 2] [3 :some/ref 1]]` and turns
it into `[["1" :foo 2] ["3" :some/ref "1"]]`. That way, DataScript will treat
the entity IDs as temporary, and new IDs will be assigned. This is important
because Datomic entity IDs can be larger than JavaScript's
`Number.MAX_SAFE_INTEGER`. So instead of using Datomic's entity IDs on the
frontend, I let DataScript assign its own IDs and then maintain a mapping
between DataScript's IDs and Datomic's IDs (which are stored on the frontend as
strings).

To be exact, they're actually stored as tagged literals, e.g. `#eid
"123456789"`. I'll get to this later, but this allows the frontend to send a
transaction like `[[#eid "12345789" :bar "hello"]]` to the backend, and then I
simply include an entry for `eid` in my `data_readers.clj` file.

I'm also passing a `ds-schema` ("DataScript schema") argument to
`stringify-eids`. This comes from a library that I share between the frontend
and backend:

```language-clojure
(def schema
  {:user/uid [:db.type/string :db.unique/identity]
   :user/email [:db.type/string :db.unique/identity]
   :auth/owner [:db.type/ref]
   :entry/date [:db.type/instant]
   :entry/draft [:db.type/boolean]
   ; etc
   ]})

(def datomic-schema (u/datomic-schema schema))
(def ds-schema (u/datascript-schema schema))
```

**Materialized views**

I've never actually used re-frame much. Although I'm using DataScript instead of
a normal atom for storing frontend state, there is
[re-posh](https://github.com/denistakeda/re-posh) which combines re-frame with
[Posh](https://github.com/mpdairy/posh), a library that lets you define reactive
DataScript queries. I've used Posh a little bit, but

1. It breaks on some [edge cases](https://github.com/mpdairy/posh/issues/26),
   including a case that I ran into myself.
2. You can't use `pull` inside of queries.

So instead I wrote a macro `defq`:

```language-clojure
(defq entries
  (->> @conn
       (d/q '[:find [(pull ?e [*]) ...] :where
              (or [?e :entry/draft]
                  [?e :entry/date])])))
```

`defq` takes some arbitrary code and stores it in a function. It creates a
reactive atom (`entries` in this case) and populates it with the results of the
function. Whenever I run a transaction, the function is ran again (and the atom
is repopulated with the results).

Obviously this won't be fast when you have lots of queries, but it's good enough
for now. I'll revisit it later.

Besides `defq`, I've found that using plain old `reagent.ratom/reaction` is nice
and succinct:

```language-clojure
(def entry (reaction (last @entries)))
(def draft? (reaction (:entry/draft @entry)))
```

I store all of these in a single namespace, so I can reference them
from my Reagent views with e.g. `@db/entries` or `@db/draft?`.

**Components**

I've been mainly using [re-com](https://re-com.day8.com.au/#/introduction) and
it's really nice. I had two minor annoyances though. First, all of the parameters
are defined with map destructuring. This means that when you use container
elements, you have to write `[rc/h-box :children [foo bar baz]]` instead of
just `[rc/h-box foo bar baz]`. Containers are used pretty often and having all
these `:children` can add up.

That's not too bad though, I simply defined my own `h-box` and `v-box`
components that didn't use map destructuring.

The other thing I ran into was when I used the `horizontal-tabs` component and I
wasn't able to change the colors using inline styles; I had to include a
separate css file to override the Bootstrap styles.

Going forward it'd be nice to have everything be fully customizable with inline
styles, so I'll need to decide if I want to keep using re-com and/or Bootstrap
and make some modifications or if I should roll my own. I'll admit that I'm not
much of a UI person, but it would be nice to figure out a system that works for
me (and makes it easy for me to make websites that look nice. I guess there are
people who care about that).

**Transactions on the frontend**

On the frontend I also defined a custom `transact!` function:

```language-clojure
(defn transact! [persist-fn conn tx & queries]
  (let [tx-result (d/transact! conn tx)]
    (apply invalidate! queries)
    (go (let [tx (u/translate-eids (:schema @conn) (::eids @conn) tx)
              eids (<! (persist-fn tx))
              tempids (reverse-tempids tx-result eids)]
          (swap! conn update ::eids merge tempids)))
    tx-result))
```

It does several things:

1. It applies the transaction to the frontend database immediately. Currently I
   don't have anything in place to rollback if the transaction fails on the
   backend; that's part of my future work.

2. `invalidate!` is what updates the queries that I defined earlier with `defq`.

3. `translate-eids` traverses the transaction, replacing DataScript's entity IDs
   with the tagged-literal Datomic IDs like I mentioned before. For example,
   given a transaction of `[[:db/add 1 :foo "bar"]]` and an entity ID mapping of `{1
   #eid "12345"}`, the return value would be `[[:db/add #eid "12345" :foo "bar"]]`
   (surprise). Unfortunately we can't do something simple with
   `clojure.walk/postwalk` like "if an element is a key in the entity ID map,
   replace it with the value" because we don't know if the number is actually an
   entity ID or just a number. The only way to know is to traverse the
   transaction according to the
   [grammar](https://docs.datomic.com/cloud/transactions/transaction-data-reference.html#orgb749e75)
   and replace entity IDs along the way. It was a little tedious to write but
   not super complicated.

4. `persist-fn` takes the transaction and sends it to the backend. The backend
   returns the entity IDs of any newly created entities. For example, if you
   transacted `[[:db/add "tmp-id" :foo "bar"]]` and the new entity ID assigned by
   Datomic was 12345, the backend (and thus `persist-fn`)
   would return `{"tmp-id" #eid "12345"}`.

5. `reverse-tempids` will use that return value to map the entity IDs assigned
   by DataScript to the ones assigned by Datomic. Continuing the
   previous example, if DataScript assigned an entity ID of 4, then the return
   value of `reverse-tempids` would be `{4 #eid "12345"}`.


**Transactions on the backend**

This is one of the nicest parts of the architecture in my opinion. I've set
things up so that the frontend can send arbitrary transactions and the backend
will analyze them to find out if the current user is authorized to make them.
That way I didn't have to code up a new endpoint for each kind of edit the user
can make.

I set up a single endpoint, `/tx`, to receive transactions. Upon receipt, it
first makes sure the transaction doesn't include any transaction functions that
haven't been whitelisted. Then we run the transaction through a transaction
function called `authorize`. This function speculatively runs the transaction
using `d/with`. Then it analyzes the result to find out which entities were
affected.

Each entity that was changed must pass an app-specific authorizer function.
Here's an example of an authorizer function; it will allow a user to create a
message entity as long as they are listed as the sender of that message:

```language-clojure
(s/def ::message (u/ent-spec :req [:message/text :message/sender]))

(def authorizers
  {[nil ::message]
  (fn [{:keys [uid eid datoms db-before db-after before after]}]
    (not-empty
      (d/q '[:find ?e :in $ ?e ?user :where
             [?e :message/sender ?user]]
        db-after eid [:user/uid uid])))})
```
I'll dissect this now:

- `ent-spec` is basically a custom version of `s/keys` that works with Datomic
   entities. Also, keys are only allowed if they are listed in either `:req` or
   `:opt`. the frontend can't attach an attribute to an entity unless we give
   them explicit permission to do so.

- The keys in `authorizers` are a pair of specs. The first spec defines what
   type the entity had before running the transaction, and the second spec
   defines what type it had after. I call this the entity's "signature." `nil`
   means that the entity didn't exist. So in this case, we're saying that this
   function only applies to newly created `::message` entities.

- For each entity in the transaction, `authorize` will look for an authorizer
   function that has a matching signature. If it finds one, it'll pass the
   entity along with some other information to the function. If the function
   returns truthy, then the change is authorized. If there aren't any matching
   authorizer functions that return truthy, then the change is unauthorized and
   `authorize` will throw an exception.

The authorizer function receives an argument that includes the following keys:

- `uid`: the ID of the authenticated user. In my case, this is an ID assigned
   by Firebase Authentication.

- `eid`: an entity ID from the result of the current transaction.

- `datoms`: the subset of datoms added or retracted by the current transaction
   that apply to `eid`.

- `db-before` and `db-after`: these are taken from the transaction result.

- `before` and `after`: these are the result of `(d/pull db '[*] eid)` with
   `db-before` and `db-after`, respectively (or `nil` if the entity didn't
   exist).

So, if you provide the specs and the authorizer functions, then `authorize` can
take care of the rest. It separates the logic of what changes are allowed from
how those changes are delivered to the backend; so for the latter we can say
"send them all to the same place, and send them in whatever form you want."

**Future work**

One of the key takeaways here is that the vast majority of my time was not spent
focusing on just the application logic.
Eric Normand has described the need for a ["boring web
framework."](https://lispcast.com/clojure-needs-grow-boring-web-framework-boring-data-science/)
I think it's a good analysis. As far as I can tell, Clojure has had solid
adoption among [innovators and early
adopters](https://en.wikipedia.org/wiki/Technology_adoption_life_cycle). You
can do some cool things if you take the time to set it up yourself, and
this is more-or-less fine for people who already know Clojure. But if we automate this
process,
Clojure will have a much better chance at [crossing the
chasm](https://en.wikipedia.org/wiki/Crossing_the_Chasm) into the early
majority.[4]

As I've built FlexBudget, I've tried to keep as many things separated into
libraries as possible. My plan is to continue this process and try to create a
web framework that allows even Clojure beginners to get up and running with a
Clojure + Datomic stack that has all these architectural components I've
described. I'm also going to add more components like realtime communications.
Especially if/when [reactive Datalog becomes
available](https://www.reddit.com/r/Clojure/comments/b46vl1/reactive_datalog_for_datomic/),
I think this framework could be a great boon for web app development.

<br>
<br>
<br>

[Russian translation](https://vectorified.com/ru-ion)

**Notes**

[1] I think most approaches to budgeting, e.g. zero-sum budgeting, force you to
go into too much detail. When I'm monitoring resource usage, I don't care
exactly how the resource is being used&mdash;I just want a high level "is it OK or
is it not OK." If there's a problem, then I'll use a profiler/disk usage
analyzer/etc to dig deeper. I use FlexBudget to give me the high-level "is it
OK," but it's not meant as a profiler.


[2] Before I actually read the documentation for ions, I thought they were just
some kind of hack to let you define transaction functions in Cloud. For anyone
who doesn't already know, they're actually much more than that. They allow you
to reuse the Datomic Cloud infrastructure for deploying your application, so
you don't have to deal with setting up your own infrastructure. It's a big step
towards the Holy Grail of only having to think about your application logic.

Given that, I'm much more ok with the >= $30/month price tag of Datomic
Cloud. I used to think it was too much just for a side project that isn't
making any money, but now I'm fine with it because of the time it saves me. Or
rather, the time it will save me now that I know how to use it.

[3] I was about to talk about the possibility of Cognitect making it so each
query group could have its own transactor, but then I realized that would simply
reduce to running separate production topologies. So I'm doubtful if there are
any possible solutions that are better than what I've described.

[4] In respect to the "libraries vs. frameworks" debate: the problem with
frameworks isn't that they do a lot for you, it's that they're hard to change
if you want something different. With enough care, we could make a
library/framework that sets up a lot of defaults for you but still allows you
to customize it however you need.
