+++
title = "Build Your Own Content Curation Pipeline"
date = "2019-12-10"
+++

I recently added a [recommendations](/recommendations/) page to my website. The
contents of that page are generated like so:

1. I save things I like in [Pocket](https://getpocket.com).

2. [IFTTT](https://ifttt.com) transfers that data into [Airtable](https://airtable.com).

3. Every 30 minutes, a function hosted on [Repl.it](https://repl.it) does some
   post-processing on the data in Airtable.

4. In Airtable, I create a separate view for the different types of links (e.g. music, books, etc).

5. Repl.it exposes some REST endpoints for each view. The endpoints pull data
   from Airtable and return it as RSS/Atom feeds.

6. My custom static site generator pulls the feed data from Repl.it and renders the recommendations page.

To help with content discovery, Repl.it also sends the feeds to [Findka](https://findka.com),
a recommender system I started building recently.

Continue for instructions on how to set up a pipeline like this for yourself.

**Pocket → Airtable**

First, you need to set up an Airtable base with the right schema. The easiest
way is to click "Copy base" (at the bottom of the iframe):

<iframe class="airtable-embed"
src="https://airtable.com/embed/shr3IOX0OBEG3d8wD?backgroundColor=gray"
frameborder="0" onmousewheel="" width="100%" height="533" style="background:
transparent; border: 1px solid #ccc;"></iframe>

<div style="height:20px"></div>

Eventually you'll want to delete the existing records from your copy of this
base, but leave them in for now (they make good test data for generating the
feeds).

Make an IFTTT account if you haven't already, then go to
[ifttt.com/create](https://ifttt.com/create). For the "If" service, select
Pocket. You'll then have a few choices for a trigger. I didn't use Pocket
before I set up this pipeline, so I chose "Any new item" as my trigger&mdash;i.e.
everything that goes into Pocket will be treated by the pipeline as a
recommended item and will get sucked into Airtable. If you already use Pocket,
you may want to choose "New favorite item" as the trigger instead.

For the "That" service, choose Airtable. As of writing this, there's only one
available trigger: "Create a new record" (so use that one). Set the base to
the one you just created, set Table to `Links`, and set Record content to
`::airtable::Link::{{Url}} ::airtable::Title::{{Title}}
::airtable::Description::{{Excerpt}} ::airtable::Tags::{{Tags}}
::airtable::Image::{{ImageUrl}}`.

Install the [Pocket extension/app](https://getpocket.com) if needed and save
something with it. I believe IFTTT will run within an hour, but to test it you
can go to the settings page for your applet and click "Check now." Make sure the
new data shows up in Airtable.

Note: if you add tags with Pocket, you must first add those values to the Tags
field in Airtable. Otherwise, Airtable will reject the import.

You could ingest other things into Airtable as well, but Pocket is the only one
I've done so far. I'd like to include my top tracks from last.fm at some point.

**Post-processing**

You may want to derive additional information for the Airtable records. For
example, I like to add tags based on the URL. Links from goodreads.com get
tagged as "book," imdb.com as "movie," etc.

Make an account at [Repl.it](https://repl.it) if you don't already have one,
then go to [https://repl.it/@jobryant/Feeds](https://repl.it/@jobryant/Feeds)
and click "fork." Rename the `.env-TEMPLATE` file to `.env` and then enter in
the various API keys (`.env` is only visible to the repl owner). You can get
your Airtable API key from [airtable.com/account](https://airtable.com/account)
and your Base ID from [airtable.com/api](https://airtable.com/api).

If you'd like to send your public feeds to Findka automatically, get an API key
from [findka.com](https://findka.com) (under the settings tab after you log
in). Otherwise, set `EXPORT_TO_FINDKA` to `false` in `index.js`.

Hit "run." It'll take a minute for the dependencies to install, after which an
embedded browser window will open and display the message "Server running." The
URL of the embedded window will be something like
`https://OrderlySereneRuby.yourusername.repl.co`. In `index.js`, set the `ROOT`
variable to that URL. Then click "restart."

In the console, you'll see the message `Send a GET request to
https://YOUR-REPL.repl.co/process to process new Airtable records`. When you
hit that endpoint, it'll fetch all the records from Airtable where the
`Processed?` field is unchecked. Each record's fields will be passed through
the `processRecord` function, which returns the fields with any desired
changes. Those changes will be saved to Airtable, and the `Processed?` column
will be checked. Try hitting the endpoint and make sure the item you saved
with Pocket earlier gets processed.

Finally, make a free account at [siteuptime.com](https://www.siteuptime.com)
and have it ping the `/process` endpoint every 30 minutes. (You can set up an
applet on IFTTT that will ping Repl.it whenever an Airtable record is created,
but it was unreliable for me. Repl.it sleeps during inactivity, and I think
IFTTT was timing out while waiting for it to wake up. SiteUptime has been
working great though.)

**Feeds**

Go back to Airtable. Notice there are several different
[views](https://support.airtable.com/hc/en-us/articles/202624989-Guide-to-views#whats_a_view).
I've set up each view with a different filter. For example, the "music" view
includes only records that have the "music" tag.

Look at the `Feeds` table. The values in the `View name` column must exactly
match the names of any views you want to publish as feeds. You can include additional
information for each feed if you like.

When you start the server on Repl.it, it'll get the contents of the `Feeds`
table and use that to start a bunch of endpoints. You probably saw some lines
in the console output saying `Published feed at ...`. Each feed will be
published in three formats: Atom, RSS and JSON. Whenever you update the `Feeds`
table, you'll need to restart your repl.

In the build process for this website, I [download the JSON and use it to render the recommendations page](https://github.com/jacobobryant/site/blob/b5df96af893dc5ffe2ec6643df478c893b86d72b/src/site/core.clj#L209).
On that page you'll notice a bunch of `feed` links. The
build process also [downloads the Atom feeds](https://github.com/jacobobryant/site/blob/b5df96af893dc5ffe2ec6643df478c893b86d72b/src/site/core.clj#L195)
from Repl.it and [includes them as-is in this website](https://github.com/jacobobryant/site/blob/b5df96af893dc5ffe2ec6643df478c893b86d72b/src/site/core.clj#L237).

For stability, I like to keep this website static instead of fetching data from
Repl.it on-demand. But if your website is already dynamic, you could use
Javascript to render the pages instead so that the recommendations are always
up-to-date.

**Findka**

If you like feeds, do take a look at [Findka](https://findka.com). It doesn't
have enough data to do anything useful yet, but that'll change once enough people
import feeds of content they've curated.

For convenience, I'm planning to rebuild this pipeline within Findka so that
people can use it without having to do all the setup work. However, I do like
the user control and hackability that comes from using Airtable instead of
Findka's database. Maybe I'll replace the other parts of the pipeline but keep
Airtable. Let me know if you have an opinion.
