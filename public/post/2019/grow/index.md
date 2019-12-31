{:title "Growing a Framework, or, Clojure Made Easy", :date "2019-06-11"}

*Note: I wrote this with a Clojure audience in mind, but I think it applies
more generally. It uses the terms "simple" and "easy"
[as defined by Rich Hickey](https://www.infoq.com/presentations/Simple-Made-Easy/#presentationNotes)
(see "Key Takeaways" in that link). Also, "complect" means "make more complex"
or "intertwine," i.e. to combine multiple things that should be separate.*

We all know that the Clojure community prefers libraries over frameworks.[1] The
difference between a library and a framework isn't well-defined, but I think of
it this way: a framework is just a library with a much larger scope than the
average library. This has several implications:

- Frameworks have an inversion-of-control feeling: instead of plugging a bunch
   of libraries into your code, you plug your code into the framework. (Some
   people give this as the defining quality of a framework, but I think it's
   just a side effect).

- Frameworks are more likely to need modification to handle your particular
   use case.

The latter point is why we don't like frameworks. The effort required to patch a
framework to accommodate the functionality you want is often higher than the
effort to put all the libraries together yourself. The downside is that now you
have to put all the libraries together yourself, which can be tedious.

What's really going on here is that frameworks are more likely to be complected.
You could think of a library as a single, decomplected building block ("do one
thing and do it well"). Frameworks attempt to do the work of putting the
building blocks together in a reusable way, which is a good thing! We need that
if we're going to keep building higher-level abstractions. But in the process,
frameworks often complect the building blocks, making it unnecessarily difficult
to build on top of them.

There can be a danger of dismissing efforts to assemble building blocks
altogether because "that feels like a framework, and frameworks are bad." But
frameworks aren't inherently bad. For emphasis, let me restate this another
way. Simplifying is the process of breaking complex things into building blocks
which can then be put together in many different ways. "Easifying" is just the
next step: once you have the right building blocks, you make bigger building
blocks out of them. Libraries are simple, frameworks are easy. Frameworks can
be good if they're both simple and easy.[2]

So how do you make big building blocks that stay simple? For a general treatment
of this question, I'd highly recommend the classic [Growing a
Language](https://www.cs.virginia.edu/~evans/cs655/readings/steele.pdf) which I
just reread.[3] In this particular situation, my answer is "I'm not sure, but I
have a few ideas currently and I'm going to keep thinking about it as I go." My
"few ideas currently" are:

- "Library-driven development." I think it's good to have a library where you
   can conveniently move code to as you're working on applications. That's what
   [Trident](https://github.com/jacobobryant/trident) is for me. I try to move
   as much non-application-specific code as I can into Trident. Even if some of
   the code never gets used outside the application I originally wrote it for, 1)
   it's hard to know which code will and won't be useful later, 2) it makes
   my applications much more layered, with the top layers&mdash;the ones that are
   actually in the application's project&mdash;much easier to hold in your head.

- Lots of little libraries. I keep Trident's code not just in one git repo but
   also in a single `src` directory. I then define in a config file how the code
   should be split up into artifacts. The result is that it's extremely
   convenient for me to split the code up into little libraries/artifacts that
   can be used independently. The libraries are mostly divided based on their
   external dependencies, so you should hopefully be able to use whatever slice
   of Trident you want without taking in a bunch of irrelevant dependencies.

- Solve real problems, not hypothetical ones. I'm not going out of my way to
   try to solve a bunch of problems for other people in advance. I'm just trying
   to make sure that the solutions I create for problems I have can be reused. No
   one should have to solve a problem that I've already solved. Part of that
   includes writing good documentation, which I've attempted.

There's a fourth point which needs more explaining first. Think of an artifact
as a DAG of its dependencies. And I mean "artifact" in an abstract sense; not
only jars but also individual functions. Imagine an artifact A that has
dependencies on B, C, D and E:
```
  A
 / \
B   C
 \ / \
  D   E
```
Now suppose you want only a slice of A's functionality. If everything you want
is contained in C, D and E, the problem is easy: just use C.
```
  C
 / \
D   E
```
But what if you do want A, but you just want to tweak some of the functionality
in a lower artifact, like D or E? The author of A needs to somehow write their
code in a way so that the lower layers can be modified. It's a hard problem
since you often have a lot of dependencies and it's hard to know ahead of time
in what ways they'll need to be modified.

Armin Ronacher wrote a great article related to this problem called ["Start
Writing More Classes"](http://lucumr.pocoo.org/2013/2/13/moar-classes/). Of
particular importance is this footnote at the end:

> Something else I want to mention: what's written above will most likely result
> in some sort of warmed up discussion in regards to object oriented programming
> versus something else. Or inheritance versus strategies. Or virtual methods
> versus method passing. Or whatever else hackernews finds worthy of a discussion
> this time around.
>
> All of that is entirely irrelevant to the point I'm making which is that
> monolithic pieces of code are a bad idea. And our solution to monolithic code in
> Python are classes. If your hammer of choice is Haskell then use whatever the
> equivalent in Haskell looks like. Just don't force me to fork your library
> because you decided a layered API is not something you want to expose to your
> user.

So this is my fourth point:

- Figure out the best ways to use Clojure's language constructs to provide
   layered APIs. For example, although I know *how* to use types and protocols,
   I don't yet grok *when* to introduce my own. I'm planning to keep thinking
   about things like this as I go.

So that's my philosophy. Hope you liked it!

<br ><br ><br >

[Hacker News](https://news.ycombinator.com/item?id=20164657)

[Reddit](https://www.reddit.com/r/Clojure/comments/bzrkay/growing_a_framework_or_clojure_made_easy/)

**Notes**

[1] Here's an explanation of why from [outside
Clojureland](http://lucumr.pocoo.org/2010/6/14/opening-the-flask/#why-create-your-own-framework),
and there's also [this
message](https://clojureverse.org/t/clojure-libs-frameworks-tooling-for-rapid-web-development/4017/5)
by Sean Corfield. I want to be clear that I agree with these arguments; I'm just
attempting to bring the discussion a little deeper.

[2] Although we probably wouldn't call them "frameworks" then, in the same way
that AI stops being called AI once it actually works.

Also, I'm reminded of [this blog
post](https://blog.appcanary.com/2017/hard-isnt-simple-ruby-clojure.html). I
think the author hit on some good points, although I don't agree with all his
conclusions, like this one:

  > In practice, ‘Simple Made Easy’ is an elaborate excuse for making software
  > that is hard to use.

You have to make things simple before you make things easy, and a lot of work
in Clojure has gone towards making things simple. If less work has gone into
making things easy, I think a more likely explanation is that people don't have
infinite time, and they've tried to spend the time they do have on the most
important things.

However, we can't forget that making things easy is an important next step,
which is the point I'm trying to make.

[3] I'm fascinated that this problem seems to repeat itself.
