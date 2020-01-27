{:date "2020-01-13" :title "Weeklyish update #1" :tags ["blog"]}

I've decided to start writing weekly updates about my work, life, etc.
Typically I've only written in-depth articles on specific topics; but that
leaves out a lot of content in my head. So these blog updates will be
lower-friction and more frequent. Plus, I can always take excerpts and turn
them into separate articles later.

### Findka

#### Bye-bye Airtable

I released a new version of [Findka](https://findka.com) last week. Previously,
I had been using Airtable as a CMS for users. After you created a Findka
account, you would copy a template base into your Airtale account, and you'd
enter the API key and base ID into Findka. When Findka imported your data from
other accounts, the data would be stored in Airtable. The contents of Airtable
were used to then generate your Findka profile/website. There was even a
key-value base in the template which I used as an easy way to add configuration
options.

This saved me from the horrors of adding a bunch of CRUD to Findka; however, it
simply wasn't a great user experience. [Airtable wasn't meant to be used this
way](https://community.airtable.com/t/could-airtable-power-my-ios-app/684), and
although I really liked the setup, it was confusing for others who tried
using Findka. So I finally decided to do the sane thing and just store user
data in my own database.

I hope that at some point, easy-to-use [data
stores](https://knightcolumbia.org/content/protocols-not-platforms-a-technological-approach-to-free-speech)
become available. Basically, the idea is that users would each have their own
(managed or self-hosted) database which includes auth flows so you can give
access to other apps. You'd also have an Airtable-like interface so users could
manipulate their data directly. Then instead of handling all the CRUD myself, I
would specify a schema for data that my app needs, and users would go
through an OAuth flow that sets everything up with their personal data store.
(And if they didn't already have one, it'd be easy to sign up within the flow.)
Or even better; signing up for Findka would automatically create a free-tier
data store, which would be moved into the user's existing account if they
choose.

The best benefits of this IMO would be ease of development for me and
hackability (in the good sense) for users. To expand on the latter, imagine if
all your project management data was stored in a database that you had direct
access to (instead of in \*shudder\* JIRA). And now imagine that there was a
vibrant plugin ecosystem. If you wanted a better UI than JIRA provides for a
particular workflow, you could install a plugin for just that use-case while
continuing to use JIRA for everything else.

Anyway, that's one of the things I'd like to build at some point. I think the
time isn't quite right for it yet though.

#### Also, bye-bye Fulcro

When I started working on Findka about six weeks ago, I decided to try using
Fulcro, which I had never learned before. But ultimately I decided that it's
not for me. I spent Friday and Saturday ripping it out and replacing it with
Rum and some custom code for managing state + backend/DB communications. I'm
really happy with the result.

The whole point of Fulcro is to manage the complexity of large apps, not to
make small apps simple/easy. So there's a threshold of app size/complexity
below which it doesn't make sense to use Fulcro. At the very least, I simply
misjudged where that threshold is. Findka is higher than weekend-project level,
but it's still at the solo-developer level. Maybe Fulcro is better for larger
projects that involve more people.

The biggest issue I had with Fulcro is that there's a lot of added complexity
involved with maintaining the client-side database. One of Fulcro's main
features is automatic normalization: you define your data model as a tree
structure, inline with your UI, and then Fulcro converts that into a normalized
graph structure for you. However, in order to make the automatic normalization
work, you have to do a fair amount of work whenever you read or write data.

Also, everything has to be done through a myriad of functions; Fulcro's API is
not very data-driven. It was hard to build a mental model for how everything
worked; instead it was more like "use this function for this situation, that
function for that situation," etc. I often found myself in situations that the
documentation didn't quite cover, which meant I had to go read through the
source code (some times I was successful, other times ended in work-arounds).

The result is that I ended up being slowed down significantly by Fulcro, and
eventually I hit the breaking point. I am glad I had the experience of using it
though. It was educational, and I could see myself going back to the docs to
get ideas for how to handle various things in my new bespoke framework.

#### The new architecture

For one thing, I liked the way Fulcro structured the client DB. As an example,
a slice of your DB might look like this:

```clojure
{:item {1 {:title "foo"
           :description "bar"}
        2 {:title "spam"
           :description "eggs"
           :item-ref [:item 1]}}}
```

So you have two entities, and they have idents of `[:item 1]` and `[:item 2]`
respectively. I'm keeping roughly the same structure, and I'm using lots of
[cursors](https://github.com/tonsky/rum#cursors) and
[derivatives](https://github.com/martinklepsch/derivatives) to access individual parts.
I would love to use [FactUI](https://github.com/arachne-framework/factui),
but:
 - It doesn't have pull expressions.
 - It seems experimental, and I want to stay slightly more on the
   tried-and-true path for this project.

For server communications, I set up a single REST endpoint which takes re-frame
style events. For example:

```clojure
; Server
(defmethod handle :some-event
  [{:keys [opts] :as req}]
  (assert (= opts {:event-data "foo"}))
  "hello")

; Client
(go (println (<! (events/hit [:some-event {:event-data "foo"}]))))
=> "hello"
```

For handling CRUD (well, CUD) operations, I've made a `:save` event handler
which takes a custom transaction data structure, authorizes it using spec and
the user's authenticated ID, and then converts it to a Datomic transaction.
My new transaction format looks like so:

```clojure
(events/hit
  [:save
   {; Each key is an ident.
    [:item "https://item-url.example.com"] {:item/title "new title"
                                            ; Set a nil value to retract.
                                            :item/description nil}
    ; You can retract entire entities too.
    [:item "https://another-url.example.com"] nil
    ; Singleton entities have an ident with only one element.
    [:user] {:user.profile/id "jobryant"
             ; Component entities can be included as nested maps.
             :user/site-config {:site-config/display-name "Jacob"
                                :site-config/color "#343a40"}}}])
```

For authorization, you define some rules:

```clojure
(def rules
  ; Each key is a "table," i.e. the first element in an ident.
  {:item {; Each entity in the transaction must match the spec for its table.
          ; only-keys doesn't allow any keys outside req and opt.
          ; s/nilable means the user can retract entities from this table.
          :spec (s/nilable (only-keys :req [:item/url-0] :opt schema/item-keys))
          ; Given some context (`env`) and an entity's (client-side) ident, return
          ; a Datomic lookup ref.
          :id (fn [env [_ url]]
                [:item/key-1 [url (:user-eid env)]])
          ; If specified, the user's authenticated ID will be added to this key
          ; on the entity.
          :user-key :item/user-0}

   :user {:spec (only-keys :opt [:user.profile/id :user/site-config])
          :id (fn [env _]
                (:user-eid env))}

   :integrations {:spec (only-keys :opt [:user.integration/goodreads-id
                                         :user.integration/feed-url
                                         :user.integration/spotify-oauth])
                  :id (fn [env _]
                        (:user-eid env))}})
```

In addition, entity values will be checked against any specs that have been
registered for their keys. Component entities must have specs defined for their
keys, e.g. `(s/def :user/site-config (only-keys :opt schema/site-config-keys))`.

I'd like to publish this transaction-handling code as a library after it's more
fleshed-out and battle-tested, but in the mean time you can take a look at
the [post-authorization conversion code](https://github.com/jacobobryant/trident/blob/3c05dd401b8dcdcecd81005adc35fe3b5bb8c453/src/trident/datomic_cloud.clj#L76)
if you're interested.

### Workflow

I've reached the point now where my development environment involved opening
about nine different terminal tabs whenever I get started, so I decided to
figure out how to automate that. I've started using tmux (and tmuxp for session
management) and it's amazing. I can't believe I had gone all these years without
it. I've set my terminal-opening keyboard shortcut to `gnome-terminal -- tmux`,
so I've moved completely from the builtin tabs to tmux. My `.tmux.conf`:

```
set -g status-bg black
set -g status-fg white
set -g mouse on
set -g focus-events on
bind -n C-S-Pagedown swap-window -t +1\; select-window -t +1
bind -n C-S-Pageup swap-window -t -1\; select-window -t -1
bind -n C-Pagedown next-window
bind -n C-Pageup previous-window
bind -n C-t new-window -c "#{pane_current_path}"
bind C-h select-pane -L
bind C-j select-pane -D
bind C-k select-pane -U
bind C-l select-pane -R
bind C-b select-pane -t :.+
```

Other than figuring that out, I just had to learn how to copy-paste (hold shift
while you select/middle-mouse click) and scroll (ctrl-b PageUp to enter scroll
mode (well, "copy mode"), ctrl-c to exit).

I also finally got fed up with Vim adding extra indentation to e.g. custom
macros, like
```clojure
(some-macro [a b c]
            "uggggh")
```

And so I discovered and added the following gem to my `nvim/init.vim`:
```
let g:clojure_fuzzy_indent_patterns = ['.*']
```

This implements [Tonsky's suggestion](https://tonsky.me/blog/clojurefmt/) of
"Multi-line lists that start with a symbol are always indented with two spaces".
Like tmux, I can't believe I'd gone so long without it.

### This week

I'll add more integrations to Findka and start working on the recommender system
portion. I'm hoping to have it at an MVP level (at least for the free tier)
within a couple weeks.

Yesterday I also did a lot of thinking about my general approach to being a
startup founder, and I might make some major modifications. I've had enough
writing for now though; maybe I'll talk about it next week. But it might result
in me going back to do more work on [Lagukan](https://lagukan.com)!
