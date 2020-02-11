{:title "Landing Pages With Clojure and Firebase", :date "2020-2-11" :tags ["clojure"]}

This tutorial will demonstrate a workflow for creating a landing page (with
email signup form) for a new project. Part of [The Solo Hacker's Guide To
Clojure](/post/2020/guide-to-clojure/). Prerequisites:

- [First Steps With Clojure](/post/2019/learn-clojure/), OR
- A basic understanding of Clojure fundamentals and tools.deps, and
  a basic Clojure development environment set up.

Clone the [Mystery Cows](https://github.com/jacobobryant/mystery-cows)
repository and checkout the `landing-page` branch. Run `./task setup` to
install a couple npm deps (if you don't have npm installed, do that first).
Then run `./task dev live` to start a development server.

(I'm going to assume your environment has the ability to run shell scripts. I
figure this is a reasonable assumption since Windows 10 has the Windows
Subsystem for Linux. But if you can't run shell scripts, you can just look
inside the files and run the individual commands yourself. I won't be doing
much shell scripting anyway.)

A landing page ([like this one](https://cows.jacobobryant.com)) should open in
your web browser. Whenever  a Clojure file changes on disk, the task we ran
will regenerate the `public/index.html` file and reload the page in your web
browser. Try it out: open `src/cows/core.clj` and change the nav bar so it says
"Mystery Sows" instead of "Mystery Cows" (which is a [real
thing](https://www.nationalhogfarmer.com/animal-health/increase-us-sow-mortality-real-mystery)
in case you're wondering).

(By the way, the email form will give you console errors if you try to use it
because it has a Firebase dependency which we haven't set up yet. More on that
later.)

Let's open the code.

[`src/cows/core.clj`](https://github.com/jacobobryant/mystery-cows/blob/landing-page/src/cows/core.clj)

This file uses [Rum server-side
rendering](https://github.com/tonsky/rum#server-side-rendering) to generate the
html. Things you may not have seen yet:
 - [`(for ...)`](https://kimh.github.io/clojure-by-example/#for)
 - [destructuring](https://clojure.org/guides/destructuring) (both sequential and associative)

[`deps.edn`](https://github.com/jacobobryant/mystery-cows/blob/landing-page/deps.edn)

Always worth a look.

[`task`](https://github.com/jacobobryant/mystery-cows/blob/landing-page/task)

Note that `clj -m cows.core` calls the `-main` function from `src/cows/core.clj`.

<hr>

Now, create a separate project for your own landing page:

 1. Set up a skeleton for the project. Copy over `deps.edn` and `task`, then
    add a bare-bones `src/yourproject/core.clj` file with just a "hello world"
    div. Also include a head section with the title and meta elements. Run the
    dev server and make sure it works.

 2. Generate a favicon ([favicon.io](https://favicon.io)). Place the generated
    files in `public/` and link to them from `yourproject.core`.

 3. Add Bootstrap with custom brand colors.

    Use [coolers.co](https://coolors.co/app) to generate a primary and a
    secondary color, then enter those colors into [Bootstrap
    Magic](https://pikock.github.io/bootstrap-magic/app/index.html#!/editor)
    (under Brand Colors -> `$theme-colors`). Tweak the colors as needed.

    Once that's done, save the file to `public/css/bootstrap.css` and link to it from
    `yourproject.core`.

 4. Write the HTML/CSS in `cows.core`. You could do it from scratch without too
    much effort, but I searched for "free bootstrap themes" and ended up on
    [this one](https://startbootstrap.com/themes/landing-page/). If you take that route,
    use [this site](https://htmltohiccup.herokuapp.com/) to convert the HTML to Clojure
    data structures. Wrap the output in a `(defc landing-page [] ...)` as in `cows.core`.
    You'll have to do a little editing.

    For one thing, Rum is slightly different from
    [Hiccup](https://github.com/weavejester/hiccup). Hiccup uses only strings
    for inline CSS, so you can't write something like `[:p {:style
    {:background-color "green"}} ...]`. Instead, you'd have to write `[:p
    {:style "background-color: green;"}]`. Rum is the opposite way, so if you
    get any compilation errors, you may need to convert the strings to maps
    by hand.

    Also stick any images and CSS files from the theme you copied somewhere
    under `public/`, adding the appropriate links to the head section. I'd
    recommend rewriting the CSS into `yourproject.core` in most cases, but some
    things (like media queries) can't be written inline, so feel free to leave
    those in a separate CSS file. (See
    [Garden](https://github.com/noprompt/garden) if you want a 100% Clojure solution.)

    Finally, you can break up the HTML structures into separate variables and
    components like I've done in `cows.core`. For example, the "testimonial
    items" were originally three separate blocks of HTML, but I consolidated
    them into a component and a `for`.

  5. For stock photos, I recommend [pexels.com](https://pexels.com) (that's
     where I got all the cows).

Once your landing page is looking good, the next step is to make the signup form
actually work. We'll need to set up Firebase as we'll be using that for storing
the email addresses (and for hosting).

 1. Go to [firebase.google.com](https://firebase.google.com) and set up a new project.

 2. From that website, create a Firestore database. After it's provisioned,
    click "Start collection" and set the ID to `signups`. Add a single string
    field called `email`.

 3. Follow [this guide](https://firebase.google.com/docs/cli) to install the
    Firebase CLI. Once you get past the `firebase login` part, run `firebase
    init firestore`.

 4. Replace the the new `firestore.rules` file with the one from Mystery Cows.
    Those rules will let your signup form add new email addresses, and it will
    prevent users from reading or modifying the saved addresses. Run
    `firebase deploy --only firestore:rules`.

 5. In `cows.core`, you'll notice three Firebase `:script` elements. Add those
    elements to `yourproject.core`.

    The `:src` values are special "Hosting URLs" that Firebase can use to add
    the dependencies for you along with project-specific configuration. See
    [here](https://firebase.google.com/docs/web/setup).

 6. Run `./task dev` (not `./task dev live`). This will serve your app using
    the Firebase dev server instead of the npm live server. This is necessary
    so that the Hosting URLs will work. Unfortunately, you'll have to manually
    refresh the page whenever you regenerate the `index.html` file.

Now all that's left is to hook up some JS to your signup form and then deploy
the landing page. See `public/js/main.js` and the signup form elements in
`cows.core` to see how I did the former. Once you set that up, test out the
signup form. You can check the Firebase console to make sure that the email was
saved.

Finally, run `firebase init hosting` and then `firebase deploy`. Your landing
page should be live. You can hook up a custom domain now if you want, or just
use one of the provided domains. Later, when you set up a mailing list, you can
import the emails you get from Firebase. (I use Mailgun with some custom code
myself, but Firebase somewhat recently made a convenient extension for adding
new users to Mailchimp, so that could be a decent option. We'll go more into
that eventually.)

Next time, we'll learn how to add Firebase authentication, React and
ClojureScript to your app.
