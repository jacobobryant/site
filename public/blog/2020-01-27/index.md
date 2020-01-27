{:date "2020-1-27" :title "Weeklyish update #2" :tags ["blog"]}

[Findka](https://findka.com) has one milestone left before launch. I've finished the "gathering data"
part, and all that's left now is the "use the data" part. Of course, gathering
the data takes much more work. It's amusing that I still didn't quite grok that
when I started working on Findka two months ago. I began by making the
recommender system first. I didn't really think deeply about what would be the
most practical way to get data into the system until a basic prototype for that
was finished. And then of course, I realized that I had been working on things
in the wrong order.

So now Findka has a good setup for ingesting data. There are 10 integrations
which let people import data from other apps they use, but not every content
type has a good candidate for integration:

 - Movies: IMDB, Rotten Tomatoes and Metacritic, none of which have an API.
   Scraping could work, but I don't want to deal with that right now.

 - Podcasts: [Podchaser](https://podchaser.com) looks good, but no API. After
   Findka launches, I'll email them and see if they're planning to open a
   public API. There's also [Listennotes](https://listennotes.com). They have
   an API, but it doesn't let you fetch individual user ratings (actually users
   can't even rate podcasts; you can only add them to lists).

 - Products: Amazon's API no longer lets you retrieve review data (though I'm
   not sure if it previously let you get reviews for individuals or just in
   aggregate).

So for now, the Pocket integration acts as a catchall (in addition to importing
articles). When you save a link with Pocket, you just add a tag for the content
type, e.g. "movie". If you favorite the item, Findka will import it and set the
content type based on the tag. So the state of integrations is I think good
enough for now. I also have integrations for music (Spotify, Last.fm), books
(Goodreads), articles you've written (RSS), videos (YouTube), comments/posts
(Twitter, Reddit, Hacker News) and even code (Github).

I need to spend some more
[hammock](https://www.youtube.com/watch?v=f84n5oFoZBc) time on figuring out the
best way to set up the recommender system now that all the integrations are in
place. I'd like it to be based on open protocols (RSS). The people/things you
"follow" will be implemented with RSS&mdash;you'll simply have a list of RSS
links which you can edit. This'll play well with the websites that Findka
generates, since every website includes an [RSS
page](https://jacobobryant.com/feed/) with different feeds for content types
and tags. You can even specify if you want a feed for just the website owner's
content or if you also want to follow their recommendations as well.

Your main Findka feed will have two options: "smart" and "linear." Linear is just
an aggregation of the RSS feeds you follow. So it's like any other feed reader,
but it'll be built with multiple content types in mind rather than just
articles. The smart feed will be the recommender system, which will take into
account the feeds you follow, the content you've imported into your account with
integrations, and your feedback to content in the smart and linear feeds (views,
clicks, thumbs up/down).
