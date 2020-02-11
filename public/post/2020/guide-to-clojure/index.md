{:title "The Solo Hacker's Guide To Clojure" :date "2020-2-4" :tags ["clojure"]}

This is the starting point for a series of articles I'm currently writing. My
target audience is people who want to use Clojure for side projects or new
startups&mdash;i.e. I'm trying to help fellow solo hackers learn to bring new
ideas to the MVP stage quickly. This involves different tradeoffs from learning
Clojure with the intent to join an established team.

#### Syllabus

This will also be an opinionated guide which teaches how I personally do web
development. Rather than give an overview of X, Y and Z, I'll say "use X" and
include a footnote that compares X to other options. I want you to feel the
leverage that can come from Clojure as soon as possible, without having to wade
through the jungle first.

This guide is breadth-first. There are many components in the Clojure
ecosystem, and my goal is to show you how to put them together, not necessarily
to teach you how the individual components work (there's usually already good
documentation for that). I'll sometimes give links to relevant resources, but
I'd also advise you to search out other resources on your own as you have
questions. At least you'll have an idea of which questions to ask.
`:slightly_smiling_face:`

Along with this guide, I'll be developing a web application called
[Mystery Cows](https://github.com/jacobobryant/mystery-cows), a cow-themed version
of the board game Clue. Most articles will have you follow this pattern:

 - Checkout a particular branch from the Mystery Cows repository.

 - Study and tinker with the code. Learn how it works on your own if you can.

 - With guidance from the article, add features to your own project that
   involve the things being taught.

(In fact, I'm hoping that the example code I write is more useful than the
articles themselves.)

So you'll need to pick a web app to build yourself. If you need ideas, I think
board games are great since they're easy to think of, they're interesting/they
involve complex logic (not just CRUD), it's easy to be a genuine user yourself,
you end up with something that's easy to show off, and you can go on to extend
them with an AI if you like.

#### Feedback

As I write, I'll need feedback from people learning Clojure so I can prioritize
what to revise. I also appreciate suggestions from experienced Clojurists. I've
created `#solo-hackers-guide` on [Clojurians Slack](http://clojurians.net/) for
this purpose. [Email](mailto:a@jacobobryant.com) is good too.

#### Changelog

You can [subscribe](https://tinyletter.com/jacobobryant) for notifications.

 - 11 Feb 2020: Add [Landing Pages](/post/2020/landing-pages/). Revise [First
   Steps](/post/2019/learn-clojure/), including replacing trident.staticweb
   with Rum. Add Syllabus, Feedback and Changelog sections to this page. Also
   `s/Crux/Datomic Cloud/` in the TODOs.

 - 4 Feb 2020: Add this page.

 - 23 Oct 2019: Add [First Steps](/post/2019/learn-clojure/).

#### 1. Language fundamentals

 - [First Steps](/post/2019/learn-clojure/)
 - TODO: write my own "Why Clojure?" article

#### 2. Frontend development

- [Landing Pages](/post/2020/landing-pages/)

TODO write articles & examples for the following:

 - Add a login form with Firebase Authentication. Set up ClojureScript with
   Shadow CLJS. Create a basic welcome page with Rum. Add CRUD operations,
   still using Firestore for persistence. Learn to make complex UIs with Rum.

 - Learn about normalization, derived values and effect isolation (i.e. state
   management) so you don't go insane later (insanity can be harmful to your
   velocity). Rejoice that you don't have to use a DI framework like I did while
   working at an Angular shop.

#### 3. Backend development

TODO write articles & examples for the following:

- Write Firebase Functions using ClojureScript. Learn when it makes sense to use
  Functions + Firestore and when it makes sense to move to a Clojure backend
  instead.

- Set up a Clojure backend with DigitalOcean and Datomic Cloud. Probably
  Terraform as well, plus Nginx and Letsencrypt. Deploy with rsync (maybe
  Github Actions?). Set up logging and alerting (optionally, come back to this
  step later).

- Set up communication between your frontend and backend. Add Ring middleware
  for authenticating Firebase tokens. Move your CRUD from Firestore to Datomic,
  using a rules system so you don't get a proliferation of endpoints. Use
  Firebase Storage (or maybe DigitalOcean Spaces?) for BLOB storage, with
  foreign keys stored in Datomic (and utility fns for making it look like
  everything's in Datomic).

- Learn about system management with Integrant (or maybe just roll your own).

- Use your editor-repl integration for testing and administration. Learn my own
  opinionated approach to testing, meant to give you reasonably high confidence
  in your code without having to spend much time writing tests.

- Consume 3rd party APIs, including things like Oauth, caching and
  self-throttling. Set up cron jobs. Provide a pagination API for your frontend.
  Any other stuff I can think of that might be useful.

#### 4. Miscellanea

TODO write articles & examples for the following:

- "Library-driven development." Set up your own collection of utility libraries
  so you can easily abstract away code that isn't specific to your project.
  Besides letting you share code between projects and with other people, I think
  doing so makes it easier to hold the application code in your head.

- Tooling. Set up an editor (probably Atom or VS Code, with a mention of
  Cursive) with plugins etc. Learn how to use it for Clojure development. Learn
  about tools.deps, Lein and Boot. (I'll include links to relevant sections in
  this article throughout the course).

- Tooling for the enlightened: learn Vim and set it up for Clojure. For the
  really enlightened: use Dvorak instead of Qwerty (ok, just kidding, sorta).

- Mobile app primer: create a basic app with CLJS + React Native.
