{:title "The Solo Hacker's Guide To Clojure" :date "2020-2-4" :tags ["clojure"]}

*This is a continuation of my previous post "Learn Clojure with Web Dev," which
I've renamed to [First Steps With Clojure](../../2019/learn-clojure/). I've moved
some of the content from there to here, so I may sound like I'm repeating myself
a little if you've read that previously.*

This is the starting point for a series of articles I'm planning to write. My
target audience is people who want to use Clojure for side projects or new
startups&mdash;i.e. I'm trying to help fellow solo hackers learn to bring new
ideas to the MVP stage quickly, and my recommendations will be made accordingly.

This will also be an opinionated guide which teaches how I personally do web +
mobile development. Rather than give an overview of X, Y and Z, I'll say "use
X" and include a footnote that compares X to other options. I want you to feel
the leverage that can come from Clojure as soon as possible, without having to
wade through the jungle first.

[Please let me know](mailto:a@jacobobryant.com) what topics you'd like me to
write about. I'd love to hear what things you're trying to learn and build. I'm
not an expert by any means, but I've been doing Clojure full-time for over a
year now at least. And I recently started a habit of writing every Monday, so
I'm hoping to make consistent progress on this.

<!--
And one final note to ease my imposter syndrome: I don't pretend to be a huge
Clojure expert. However, I have been using Clojure in side projects since 2016
and full-time as a startup founder/bum since January 2019, and I genuinely love
figuring out the simplest ways to do application development. For those of you
further along than me, I'd be happy to hear your recommendations.
-->

### Roadmap

I'm doing this breadth-first. Currently, what follows is mostly just an outline
of what I'm planning to write.

#### 1. Language fundamentals

- [First Steps With Clojure](/post/2019/learn-clojure/).

- As recommended in that link, work through chapters 3, 4 and 5 of [Clojure for
  the Brave and
  True](https://www.braveclojure.com/clojure-for-the-brave-and-true/).

- Also see Yogthos's [Clojure beginner
  resources](https://gist.github.com/yogthos/be323be0361c589570a6da4ccc85f58f)
  and see if there's anything that tickles your fancy.

- Do interactive exercises on [4clojure](https://www.4clojure.com) until you
  start to get a feel for things.

For the rest of this guide, decide on a web application you'd like to build. If
you need ideas, I think board games are great since they involve complex logic
(not just CRUD), it's easy to be a genuine user yourself, you end up with
something that's easy to show off, and you can go on to extend it with an AI if
you like.

#### 2. Frontend development

TODO write articles & examples for the following:

- Make a "Coming soon" landing page for your app. Include an email signup form
  using JS + Firebase Firestore for persistence. No ClojureScript yet; we'll just use
  Clojure to generate the HTML files. Use Bootstrap for styling unless you
  already have another CSS toolkit you like. Deploy with Firebase Hosting. Tell
  all your friends to sign up.

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

- Set up a Clojure backend with DigitalOcean, Crux and managed Postgres.
  Probably Terraform as well, plus Nginx and Letsencrypt. Deploy with rsync
  (maybe Github Actions?). Set up logging and alerting (optionally, come back to
  this step later).

- Set up communication between your frontend and backend. Add Ring middleware
  for authenticating Firebase tokens. Move your CRUD from Firestore to Crux,
  using a rules system so you don't get a proliferation of endpoints. Use
  Firebase Storage (or maybe DigitalOcean Spaces?) for BLOB storage, with
  foreign keys stored in Crux (and utility fns for making it look like
  everything's in Crux).

- Learn about system management with Integrant (or maybe just roll your own).

- Use your editor-repl integration for testing and administration. Learn my own
  opinionated approach to testing, meant to give you reasonably high confidence
  in your code without having to spend much time writing tests.

- Consume 3rd party APIs, including things like Oauth, caching and
  self-throttling. Set up cron jobs. Provide a pagination API for your frontend.
  Any other stuff I can think of that might be useful.

- MAYBE: use Datomic Ions instead of Crux. Ions are actually what I'm familiar
  with; I've never used Crux. But I've had enough headaches from Ions/AWS that I
  don't think the overhead is worth it for solo hackers. Plus, you have to use
  the solo topology for production (which is unsupported) until your app has
  enough traction to warrant a pair of i3.large instances (on the order of a few
  hundred bucks/month). Solo topology means no direct HTTP/dealing with lambda
  warmup times and, more importantly, deploys that often require 5-30 minutes of
  downtime (in my experience). On the other hand, I believe I've worked through
  most of the quirks of Ions, so maybe it'd be useful if I wrote an article
  about it.

  Another option is using Datomic On-Prem with DigitalOcean. That would make it
  easier to migrate to Ions later on. I'll evaluate Crux vs. On-Prem when I get
  to this point in the course.

  (Also, there is Datomic Cloud without Ions. However, you have to give up
  data/code locality which I consider a killer feature. It's been especially
  helpful to me in building recommender systems, but even for just CRUD, having
  data/code locality makes it easier to introduce abstractions. I probably would
  only use Cloud without Ions for one-off services.)

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
