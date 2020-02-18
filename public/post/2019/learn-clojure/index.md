{:title "First Steps With Clojure", :date "2019-10-23" :tags ["clojure"]}

This is a quick tutorial for writing your first Clojure program: a minimalist
static site generator, which is a great way to start doing web dev. Part of
[The Solo Hacker's Guide To Clojure](/post/2020/guide-to-clojure). Prerequisites:

- Basic terminal experience
- Comfortable with HTML and CSS
- Know a different programming language already

### Hello world

First, [install Clojure](https://clojure.org/guides/getting_started). Then type
`clj` to get an interactive prompt ("repl," short for read-eval-print-loop). In
the repl, type `(println "hello world")`:

```bash
$ clj
Clojure 1.10.0
user=> (println "hello world")
hello world
nil
user=>
```

Instead of using the default repl, you can get an enhanced repl like so:

```bash
$ clojure -Sdeps "{:deps {com.bhauman/rebel-readline {:mvn/version \"0.1.4\"}}}" -m rebel-readline.main
[Rebel readline] Type :repl/help for online help info
user=> (println "hello world")
hello world
nil
user=>
```
I recommend saving this as an alias, e.g. put the following in your `.bashrc`:
```bash
alias repl='clojure -Sdeps "{:deps {com.bhauman/rebel-readline {:mvn/version \"0.1.4\"}}}" -m rebel-readline.main'
```

#### Run from a file

I recommend starting out with the [Atom](https://atom.io) text editor. You
don't have to this right away, but before too long you should install the
parinfer plugin (`apm install parinfer`) and learn how it works. After you
install Atom, create a new project folder. Within that folder, edit a new file
`src/web/core.clj`. Your project folder should look like this:

```bash
$ tree myproject/
myproject/
└── src
    └── web
        └── core.clj
```

Put the following inside `core.clj`:
```clojure
(ns web.core)

(defn hello []
  (println "hello world"))

(defn -main []
  (hello))
```

From the terminal, you can run the file like so:
```bash
$ cd myproject
$ clj -m web.core
hello world
```

### Start learning Clojure

For actually learning the language, I recommend [Clojure for the Brave and
True](https://www.braveclojure.com/clojure-for-the-brave-and-true/). (It's
available online for free at that link, though I liked having a hard copy).

You can skip chapters 1 and 2. Work through chapter 3 before going on to the
next section in this document. Fairly soon, make sure you also understand
everything in chapters 4 and 5. These three chapters contain the fundamentals of
the language.

All of the remaining chapters are useful too. Make sure you understand them
eventually. But for now, you can just skim them a bit and refer back as needed.

Some more good resources include:

 - [Clojure by example](https://kimh.github.io/clojure-by-example/)
 - [4clojure](http://www.4clojure.com)
 - [ClojureDocs](https://clojuredocs.org)
 - [Clojurians Slack](http://clojurians.net/) (see #solo-hackers-guide and #beginners, and feel free
   to DM me)
 - Yogthos's [Clojure beginner resources](https://gist.github.com/yogthos/be323be0361c589570a6da4ccc85f58f)

### Start doing web dev

I'll now show you the first fundamental of web development: how to generate
HTML and CSS. This is a great first project because it's pretty simple, and
generating HTML + CSS is much nicer in Clojure than in any other language. It's
not too hard to extend this into a personal blog/website if you like (this
website is written with Clojure).

We need to add a library to your project. Create a file `myproject/deps.edn`
with the following contents:
```clojure
{:deps
 {rum {:mvn/version "0.11.4"
       :exclusions [cljsjs/react cljsjs/react-dom]}}}
```
Change the contents of `core.clj` to this:
```clojure
(ns web.core
  (:require [rum.core :as rum :refer [defc]]))

(defc page []
  [:p "hello world"])

(defn -main []
  (println (rum/render-static-markup (page))))
```
Now run it:
```bash
$ clj -m web.core
<p>hello world</p>
```
We can add inline css like so:
```clojure
(defc page []
  [:p {:style {:color "red"}} "hello world"])
```
```bash
$ clj -m web.core
<p style="color:red">hello world</p>
```
Because we're using plain data structures for both HTML and CSS, we can use
functional abstraction and other standard programming techniques instead of
dealing with templating languages or preprocessors. (Hallelujah).
```clojure
(defc p [color text]
  [:p {:style {:color color}} text])

(defc page []
  [:div
   (p "red" "hello world")
   (p "blue" "goodnight moon")])
```
```bash
$ clj -m web.core
<div>
  <p style="color:red">hello world</p>
  <p style="color:blue">goodnight moon</p>
</div>
```
(Indentation added).

Let's make this a little more complete, and let's have it write the HTML to a file
for us.
```clojure
(defc page []
  [:html
   [:head
    [:meta {:charset "utf-8"}]]
   [:body
    [:div
     (p "red" "hello world")
     (p "blue" "goodnight moon")]]])

(defn -main []
  (spit "public/index.html" (rum/render-static-markup (page))))
```
Before running this, you'll need to create the `public` directory.
```bash
$ mkdir public
$ clj -m web.core
$ cat public/index.html
<html>
  <head>
    <meta charset="utf-8" />
  </head>
  <body>
    <div>
      <p style="color:red">hello world</p>
      <p style="color:blue">goodnight moon</p>
    </div>
  </body>
</html>
```
You can open `index.html` in a web browser now.

For interactivity, we'll add some plain Javascript. We could use Clojurescript,
but it's overkill for simple things (in addition to increasing your payload
size, using Clojurescript can be quite complex). You can switch to Clojurescript
later when the time is right.

Add a button to the div element:
```clojure
[:div
 (p "red" "hello world")
 (p "blue" "goodnight moon")
 [:button {:onclick "alert(\"spam eggs\")"} "click me"]]
```
Regenerate the HTML and test out the button before we move on.

Let's separate the Javascript into a separate file.
```clojure
(defc page []
  [:html
   [:head
    [:meta {:charset "utf-8"}]
    [:script {:src "index.js"}]]
   [:body
    [:div
     (p "red" "hello world")
     (p "blue" "goodnight moon")
     [:button {:onclick "doSomething()"} "click me"]]]])
```
Create a file `myproject/public/index.js` with the following contents:
```javascript
function doSomething() {
  alert("spam eggs");
}
```
Regenerate and test it out.

Next, let's add some [Bootstrap](https://getbootstrap.com) to make CSS easier.
```clojure
(def bootstrap-4
  [:link {:rel "stylesheet"
          :href "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          :integrity "sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
          :crossorigin "anonymous"}])

(defc page []
  [:html
   [:head
    [:meta {:charset "utf-8"}]
    [:script {:src "index.js"}]
    bootstrap-4]
   [:body
    [:div.container
     (p "red" "hello world")
     (p "blue" "goodnight moon")
     [:button.btn.btn-primary {:onclick "doSomething()"} "click me"]]]])
```

Boom. Now you can handle all your HTML needs with panache. As mentioned before,
these few fundamentals are enough to go quite far with static site generation.

Next, learn to make full-featured [Landing Pages](/post/2020/landing-pages/).

<br>
<br>
<br>

**Notes**

[1] There are three main build systems: Leiningen (Lein for short), Boot and
tools.deps. `deps.edn` is the configuration file used in tools.deps. Lein is
the oldest and most popular. It has a large collection of pre-written build
tasks. tools.deps is simpler and it's easier to write your own build tasks (the
downside is you're more likely to have to write your own build tasks). I've
used Boot a little bit, but the immutable filesystem abstraction tended to get
in the way for me. I default to tools.deps and use Lein only if there's a
particular template already set up that I want to use.

[2] I don't actually use Atom myself, but I recommend it here because it's a
mouse-friendly editor that has working autoindent for Clojure out of the box.
