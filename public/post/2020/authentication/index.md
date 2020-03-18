{:title "Firebase Authentication With Clojure", :date "2020-2-18" :tags ["clojure"]}

Today we'll be adding Firebase Authentication to a static website that's
generated with Clojure. Part of [The Solo Hacker's Guide To
Clojure](/post/2020/guide-to-clojure/). Prerequisites: [Landing
Pages](/post/2020/landing-pages/).

**Demo**

(As always, you can also see what we'll be building at
[`cows.jacobobryant.com`](https://cows.jacobobryant.com).)

In the Mystery Cows project, checkout the `authentication` branch. Run `./task
setup` again to install some more Firebase dependencies. But before you start the dev
server, you'll need to do some setup for Firebase Authentication.

First, copy `.firebaserc` from the project you created last time to the Mystery
Cows project. The contents should look like this:

```javascript
{
  "projects": {
    "default": "your-project-id"
  }
}
```

Next, go to the Firebase console for your project
([firebase.google.com](https://firebase.google.com)). Go to Develop ->
Authentication -> Sign-in method. First enable "Email/Password," and then also
enable "Email link (passwordless sign-in)." After that, enable sign-in with
Google. If you're using a custom domain for your project, add it under
"Authorized domains."

Now you can run the dev server (`./task dev`) and open
[localhost:5000](http://localhost:5000). You should see that the mailing list
sign-up form we had before has been replaced with an actual sign-in form. If
you create an account, you should see it show up in the Firebase console.

**The code**

Run `git diff landing-page authentication` to see what changed.

[`task`](https://github.com/jacobobryant/mystery-cows/blob/authentication/task)

Note the two dependencies we've added for
[firebaseui-web](https://github.com/firebase/firebaseui-web) (a drop-in sign-in
form). You could instead link directly to the CDN, but I self-host them because
I had a user once who was blocking gstatic.com.

[`public/js/main.js`](https://github.com/jacobobryant/mystery-cows/blob/authentication/public/js/main.js)

We've replaced the mailing list sign-up code with some initialization code for
firebaseui-web. (Note that the links for Terms of Service and Privacy Policy
will give 404 errors. If you're working on a Serious Project, you can get
started with some auto-generated ones.)

[`public/css/main.css`](https://github.com/jacobobryant/mystery-cows/blob/authentication/public/css/main.css)

I've removed A CSS rule that was messing up the sign-in form. I've instead
added the CSS inline in the next file.

[`src/cows/core.clj`](https://github.com/jacobobryant/mystery-cows/blob/authentication/src/cows/core.clj)

We're now generating three separate pages: a landing page, a login page, and an
app page (which is where we'll start putting our ClojureScript next time). Be
sure to take a look at `ensure-logged-in` and `ensure-logged-out`.

**Do it yourself**

Since you've already set up authentication from the Firebase console, there's
not too much left to do for your own project. Copy over `task` and
`public/js/main.js` from Mystery Cows. Run `./task dev` and start working on
the login and app pages in `src/cows/core.clj`. When you're done, you could run
`./task deploy`, though for a Serious Project you'd want to leave the mailing
list signup form until later.

[Next time](/post/2020/add-cljs/), we'll add some simple React components to
the app page using ClojureScript, Rum and Shadow CLJS.
