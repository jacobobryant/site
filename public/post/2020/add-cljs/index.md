{:title "Stubbing out ClojureScript + React", :date "2020-03-18", :tags ["clojure"]}

Today we're going to expand on our simple HTML + CSS website by adding in
ClojureScript and React (via Shadow CLJS and Rum). Part of [The Solo Hacker's
Guide To Clojure](/post/2020/guide-to-clojure/). Prerequisites:
[Authentication](/post/2020/authentication/).

**Demo**

1. In the [Mystery Cows](https://github.com/jacobobryant/mystery-cows) project,
   checkout the `add-cljs` branch.

2. Run `./task setup` again to install some NPM dependencies (`shadow-cljs`,
   `react` and `react-dom`).

3. Run `./task dev`.

4. After Shadow CLJS has finished loading, go to
   [localhost:9630](http://localhost:9630) (the Shadow CLJS console). Hover
   your mouse over "Builds," and then check the "main" build.

5. Open [localhost:5000](http://localhost:5000) and log in. You should see a
   loading spinner at first, and then it should be replaced by a welcome
   message. The message uses ClojureScript to display the email address you
   logged in with. If you make a change to `src/cows/core.cljs` and save the
   file, you should see the change happen immediately.

**The code**

Run `git diff authentication add-cljs ':!package-lock.json'` to see what changed.

[`deps.edn`](https://github.com/jacobobryant/mystery-cows/blob/add-cljs/deps.edn)

We've added some dependencies needed by Shadow CLJS. We've also added
`cljsjs/firebase` which includes *externs* for Firebase. Without externs, your
code might break when doing advanced compilation. [Read
this](https://lispcast.com/clojurescript-externs/) for a good explanation.

[`shadow-cljs.edn`](https://github.com/jacobobryant/mystery-cows/blob/add-cljs/shadow-cljs.edn)

This is the configuration file for Shadow CLJS. You can learn more at
[shadow-cljs.org](http://shadow-cljs.org).

[`src/cows/core.clj`](https://github.com/jacobobryant/mystery-cows/blob/add-cljs/src/cows/core.clj)

We've added a link to the JS file that will be generated from our ClojureScript
code. We've also replaced the existing welcome message with a loading spinner.
When our ClojureScript code loads, it'll replace that HTML with a React
component.

[`src/cows/core.cljs`](https://github.com/jacobobryant/mystery-cows/blob/add-cljs/src/cows/core.cljs)

Voila, here's the CLJS code. The `main` component is pretty much the same as
code we removed from `core.clj`, but now it'll display your logged-in email
address.

The `mount` and `init` functions are special&mdash;they were referenced in `shadow-cljs.edn`. `init` will
be called once, as soon as the CLJS code finished loading. In this case, `init` waits for Firebase
to load the logged-in user's information, and then it mounts the React component.

You can read up on using React via Rum on [the Github page](https://github.com/tonsky/rum).

[`task`](https://github.com/jacobobryant/mystery-cows/blob/add-cljs/task)

We've added CLJS compilation to the `dev` and `deploy` tasks.

**Do it yourself**

In your own project, run `npm install shadow-cljs --save-dev; npm install react
react-dom --save`. Add the code from the files linked above to your own project,
changing `cows.core` to `yourproject.core`.

You should be able to run `./task dev`, start the Shadow CLJS build and see
your running ClojureScript code as we did in the demo. Run `./task deploy` once
you've got it working.

The next step is to add a lobby so that you can create and join games. The code
is already complete; see [the `lobby`
branch](https://github.com/jacobobryant/mystery-cows/tree/lobby). It includes a
chat feature for each game.

I've decided that instead of dribbling out this guide an article at a time on
my website, I'd rather sit down and finish all the code in one go. Instead of
publishing articles here, I'll put the tutorial info in the Github README pages
and in-code comments of the separate branches.

So the next milestone I publish should include a completed application, built
entirely on Firebase. After that, I'll replace Firebase (except for
authentication) with a Clojure backend (including either Crux or Datomic) that
you can deploy on Digital Ocean/EC2/etc.
