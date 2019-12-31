{:title "What I've Learned Since Quitting My Job", :date "2019-11-15"}

I got my bachelor's degree in 2017 and then worked for a year as a software
engineer. Last January I quit and began working on a startup full-time with a
friend I met while in school. I realized after a few months that we weren't a
good fit as cofounders, so I bailed. I then spent some time trying to
figure out what to do with my life. In June I decided to go all-in on my
current startup, [Lagukan](https://lagukan.com).

Several days ago, I discovered a potentially critical flaw in my startup idea. I
might need to take a major change of direction (though I'm thinking of moving to
an idea that will still solve some of the same problems). This kind of thing has
happened to me quite a lot in the past year, so I thought I'd take this
opportunity to do some introspection before I go head-down coding again.

So, here are a few of the things I've been thinking about.

**Spotify is actually pretty good**

I've been in love with music since I was about 12. I started using Pandora while
in high school. I discovered a lot of new music that way, but it was far from
perfect. It was never able to fully adapt to my preferences (e.g. I love melodic
hard rock and hate metal, a distinction that was utterly beyond Pandora), and it
tended to play the same things over and over.

I started doing music recommendation research while in college, culminating in a
prototype that played from Spotify and my MP3 collection but used my own custom
algorithm. It was nice, although it left a lot to be desired. I later tried using
vanilla Spotify for a while and wasn't super impressed.

Hence, I eventually decided to pursue my recommender system project as a startup
(Lagukan). It seemed like a natural choice. I was encouraged by the response on
Hacker News to my first prototype. [Here's a
snippet](https://news.ycombinator.com/item?id=20585143) from one of my favorite
comments:

> Oh man... I haven't been able to find any recommendation algorithms that
> recommended songs I liked even 1% of. Spotify's is a trash fire. Last.fm's
> wasn't great. Pandora is okay.

Pulling off a successful music company has been historically pretty difficult,
but I was alright with that since I was working on something that was deeply
meaningful to me.

One of the most important issues in music recommendation (and more generally, in
reinforcement learning) is balancing exploration (trying out new songs) with
exploitation (playing songs you've listened to before). Lagukan has two
separate algorithms for these tasks plus a parent algorithm that decides which
sub-algorithm to use at any given moment. It's a delicate balance.
Back when I was a Pandora user, they only really addressed exploration. Spotify
seemed to have the opposite problem of doing too much exploitation.

However, I've slowly realized that I was likely just using Spotify wrong.
I've always had the assumption that a music player should be totally automatic:
just hit play, skip what you don't want to hear right now, then the algorithm
should do everything else. That's how I've built Lagukan, and that's how I tried
to use Spotify&mdash;I almost exclusively listened to Daily Mix.

This is a long-winded way of saying "I should've been using Discover Weekly."

I had read several comments on HN saying that Spotify performed much better if
you listen to Discover Weekly consistently. It makes sense&mdash;that's their
solution for those who want automated exploration, and Daily Mix is for
exploitation. Sure, it's not all wrapped up into a single totally automatic
algorithm, but the overhead is pretty low.

I've been using Spotify directly for the past few days, and it's been
disturbingly effective. It's been seeded by Lagukan's exploration from the past
month or so, but I'm going to try using just Spotify for several weeks and see
how well it does. I'm open to the possibility of not needing Lagukan personally.

Even if Spotify turns out to work well for me, there likely are other
people for whom a better recommendation algorithm would still be valuable. But
if I'm not one of those people, then I don't think I'll be able
to generate the insights needed to help them.

**You should be comfortable with cutting your losses**

It sucks to put five months into a startup built on a false hypothesis, but it's
better than sticking with it and running out of savings. I'd say it's also
better than never taking the leap at all, because the losses often aren't.

For one, my skills have grown far faster than they ever did while I was a
student or an employee. I've been able to learn everything I wanted to in
college but (ironically) never had the time for due to coursework, and I've
discovered plenty of things that I'd like to dive into more. It's the most
freeing educational experience I've had since I was a homeschooled teenager.
I've also learned quite a bit about the industries I've been trying to enter,
not to mention the emotional dynamics of not having any external validation from
being an employee or a student.

Despite failing over and over in the past year, I don't see any of it as a
waste. It feels more like do-it-yourself grad school (minus the degree at the
end). I don't think any other path would have been as good of an investment in
myself.

About three months ago, I started to get another idea
for a recommender system startup that I actually think is more promising than
what I've been doing. I stuck with Lagukan because I didn't want to get
whipsawed, constantly switching ideas before getting traction. Even if I do
ultimately decide to relegate Lagukan to side project status, maybe my work will
turn out to have been optimal: I wouldn't have had this second idea if I hadn't
dug into music recommendation.

What about giving up too easily? You certainly shouldn't do that, but if you,
in your heart, no longer believe in the idea, I think that's a pretty good
indication that it's time to switch. I've been through many discouraging
experiences with Lagukan, but this is the first time I've seriously doubted
the validity of my core value proposition.

**It helps to articulate what you want to get out of your career**

I'm a huge fan of Paul Graham. I stumbled on his essays while in high school; it
was the first time I ever heard about startups (and Lisp!). I like to think I'm
still as ambitious as 15-year-old me reading his essays for the first time, but
actually trying to start my own business without giving up requires that I
understand why it's worth my time.

First of all, I have to be a startup founder because right now there are certain
things about the world that I'd like to fix, and it would kill me to spend my
hours at a day job instead. But what else is important to me?

 - Working on my own projects. Even if I don't do it as a startup, I have to at
   least have lots of time for side projects. I love exploring and I'm a very
   self-motivated learner. I can't give up my freedom.

 - Working in Clojure. I was more language-agnostic when I graduated, but after
   a year each of doing Clojure and non-Clojure full-time, this is
   important to me.

 - Playing clarinet. I'd like to join an orchestra again, though I've put it on
   hold while I try to build a business. At some point I need to
   have at least an hour a day for practicing.

 - Teaching. This is another thing I love doing, and it's high-impact. At a
   minimum, I need time to write guides and tutorials regularly. (On the other
   extreme, some kind of education startup is my backup-backup-plan for
   Lagukan).

With all these demands, you can probably see another reason why doing a
startup is so appealing to me: it'd be great to just make a lot of money all at
once so I have freedom to work on whatever I want, regardless of if it's
profitable or not.

But if I get through all my startup ideas and fail each time, what's next? If
Stripe offered me a 30-hour-per-week position writing Clojure, I'd probably take
it. Barring that, I think consulting is the most likely way to hit all
the bullet points. I would be fine alternating between my own projects and
clients' projects. (I've also thought seriously about grad school... but I've
decided against it because I'm interested in [design, not
research](http://www.paulgraham.com/desres.html).)

The result of all this pondering is that my decision to continue being a startup
founder is based on careful reason, not emotion alone (I'm not a fan of
burn-the-ships). I'm confident that if I come to a point where the rational
thing is to no longer be a startup founder, then I'll do something else and
still be happy. That understanding helps me to not get so depressed when bad
things happen, which is important since startups run on morale. Knowing that
there are other good career alternatives also helps with cutting losses.

**Coming up**

I've also developed my own opinions about cofounders, networking, idea
validation, bootstrapping vs. VC... so let me know if you'd like to read a "part
2" of this article. Meanwhile I'll be building a prototype for my next idea.
Fortunately, I should be able to release this one within a week or two. And if
it works, Lagukan will likely make a good complementary product down the rode.
I'll keep my fingers crossed.
