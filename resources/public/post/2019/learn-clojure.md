+++
title = "Learn Clojure with Web Dev"
date = "2019-10-23"
+++

I've created this document so that I have a single link I can give people for
learning Clojure. It is:

- a work in progress
- not meant to be comprehensive
- opinionated/a reflection of how I do web development

I'm trying to help people get started without being overwhelmed by all the
different options. I'll link to existing resources when possible, adding my
own as needed.

Prerequisites:

- basic terminal experience
- comfortable with HTML and CSS
- know a different programming language already

### Hello world

First, [install Clojure](https://clojure.org/guides/getting_started). Then type
`clj` to get an interactive prompt ("repl," short for read-eval-print-loop). In
the repl, type `(println "hello world")`:

```lang-bash
$ clj
Clojure 1.10.0
user=> (println "hello world")
hello world
nil
user=>
```

Instead of using the default repl, you can get an enhanced repl like so:

```lang-bash
$ clojure -Sdeps "{:deps {com.bhauman/rebel-readline {:mvn/version \"0.1.4\"}}}" -m rebel-readline.main
[Rebel readline] Type :repl/help for online help info
user=> (println "hello world")
hello world
nil
user=>
```
I recommend saving this as an alias, e.g. put the following in your `.bashrc`:
```lang-bash
alias repl='clojure -Sdeps "{:deps {com.bhauman/rebel-readline {:mvn/version \"0.1.4\"}}}" -m rebel-readline.main'
```

#### Run from a file

I recommend starting out with the [Atom](https://atom.io) text editor.[1] After you
install it, create a new project folder. Within that folder, edit a new file
`src/web/core.clj` with Atom. So your project folder should look like this:

```lang-bash
$ tree myproject/
myproject/
└── src
    └── web
        └── core.clj
```

Put the following inside `core.clj`:
```lang-clojure
(ns web.core)

(defn hello []
  (println "hello world"))

(defn -main []
  (hello))
```

From the terminal, you can run the file like so:
```lang-bash
$ cd myproject
$ clj -m web.core
hello world
```

### Start learning Clojure

For actually learning the language, I recommend
[Clojure for the Brave and True](https://www.braveclojure.com/clojure-for-the-brave-and-true/). (It's
available online for free at that link, though I liked having a hard copy).

You can skip chapters 1 and 2. Work through chapter 3 before going on to the
next section in this document. Fairly soon, make sure you also understand
everything in chapters 4 and 5. These three chapters contain the fundamentals of
the language.

All of the remaining chapters are useful too. Make sure you understand them
eventually. But for now, you can just skim them a bit and refer back as needed.

### Start doing web dev

I'll now show you the first fundamental of web development: how to generate
HTML and CSS. We'll create a static landing page. This is a great first project
because it's pretty simple, and generating HTML + CSS is much nicer in Clojure
than in any other language. You could even extend this project into a personal
blog/website if you like (this website is written with Clojure).

We need to add a library to your project. Create a file `myproject/deps.edn`
with the following contents:
```lang-clojure
{:deps
 {trident/staticweb {:mvn/version "0.1.18"}}}
```
Change the contents of `core.clj` to this:
```lang-clojure
(ns web.core
  (:require [trident.staticweb :as tsweb]))

(def landing-page
  [:p "hello world"])

(defn -main []
  (println (tsweb/html landing-page)))
```
The `tsweb/html` function[2] takes a data structure that represents HTML:
```lang-bash
$ clj -m web.core
<p>hello world</p>
```
We can add inline css like so:
```lang-clojure
(def landing-page
  [:p {:style {:color "red"}} "hello world"])
```
```lang-bash
$ clj -m web.core
<p style="color:red">hello world</p>
```
Because we're using plain data structures for both HTML and CSS, we can use
functional abstraction and other standard programming techniques instead of
dealing with templating languages or preprocessors. (Hallelujah).
```lang-clojure
(defn p [color text]
  [:p {:style {:color color}} text])

(def landing-page
  [:div
   (p "red" "hello world")
   (p "blue" "goodnight moon")])
```
```lang-bash
$ clj -m web.core
<div>
  <p style="color:red">hello world</p>
  <p style="color:blue">goodnight moon</p>
</div>
```
(Indentation added).

Let's make this a little more complete, and let's have it write the HTML to a file
for us.
```lang-clojure
(def landing-page
  [:html
   [:head
    [:meta {:charset "utf-8"}]]
   [:body
    [:div
     (p "red" "hello world")
     (p "blue" "goodnight moon")]]])

(defn -main []
  (spit "public/index.html" (tsweb/html landing-page)))
```
Before running this, you'll need to create the `public` directory.
```lang-bash
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
```lang-clojure
[:div
 (p "red" "hello world")
 (p "blue" "goodnight moon")
 [:button {:onclick "alert(\"spam eggs\")"} "click me"]]
```
Regenerate the HTML and test out the button before we move on.

Let's separate the Javascript into a separate file.
```lang-clojure
(def landing-page
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
```lang-javascript
function doSomething() {
  alert("spam eggs");
}
```
Regenerate and test it out.

Next, let's add some [Bootstrap](https://getbootstrap.com) to make CSS easier.
```lang-clojure
(def bootstrap-4
  [:link {:rel "stylesheet"
          :href "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          :integrity "sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
          :crossorigin "anonymous"}])

(def landing-page
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

Boom. Now you're all set to make a sweet landing page for your new
product/consulting business/dog. I'll let you finish that on your own, but you
can take a look at the code for [my startup's landing
page](https://github.com/jacobobryant/clj-landing-page-example) for inspiration
(yes, this whole document is just an advertisement). You can put
the `public` directory on any static website host. I use Firebase, but Github
Pages and Netlify are other popular options.

You can also see [the source for this website](https://github.com/jacobobryant/site)
for an example of creating a blog with Clojure.

### The road ahead

I'll write more in the future, but next you should learn about Clojurescript and
Reagent. (Re-frame is also common, but don't worry about that for now). Reagent
is a wrapper over React that lets you use the same syntax for HTML and CSS that
we've used here.

After that, it'll be time to get into backend development. I'll write about
simple ways to get started with that, and I'll cover getting started with
[Datomic](https://www.datomic.com) as well, a database that embraces functional
programming concepts (made by the creators of Clojure).

I'll also give some tips for using the available Clojure tooling. For one thing,
you should install the parinfer plugin for Atom (`apm install parinfer`). With
parinfer, you don't need to type any closing parentheses. Just indent your code
properly, and parinfer will infer the parentheses for you.

<br>
<br>
<br>

**Notes**

[1] I don't actually use Atom myself, but I recommend it here because it's a
mouse-friendly editor that has working autoindent for Clojure out of the box.

[2] [trident/staticweb](https://github.com/jacobobryant/trident)
is a simple library I've made that wraps
[Hiccup](https://github.com/weavejester/hiccup) and
[Garden](https://github.com/noprompt/garden), allowing you to write inline CSS à la
[Reagent](https://reagent-project.github.io/).
