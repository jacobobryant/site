+++
title = "What is Computer Science all about?"
date = "2018-09-25"
+++

I once overheard a fellow CS student lament after a programming competition that
the winners were always ACME majors (Applied and Computational Math Emphasis).
What an embarrassment; bested at programming by a bunch of Math majors. What's
going on here?

My answer is that the competitions aren't really about good programming&mdash;they're
about algorithms. I also think that algorithms are less core to programming than
most CS programs would lead you to believe.

I suspect if you made a survey and asked "What is the core curriculum of
Computer Science," many answers would focus on data structures and algorithms.
And those answers wouldn't be wrong&mdash;data structures and algorithms classes are
typical foundation classes in today's CS programs. But I don't think they are
the essence of CS.

In [one of the epistles of Paul](http://www.paulgraham.com/hp.html), we read:

> I've never liked the term "computer science." The main reason I don't like it
> is that there's no such thing. Computer science is a grab bag of tenuously
> related areas thrown together by an accident of history, like Yugoslavia. At
> one end you have people who are really mathematicians, but call what they're
> doing computer science so they can get DARPA grants. ... And then at the
> other extreme you have the hackers, who are trying to write interesting
> software, and for whom computers are just a medium of expression, as concrete
> is for architects or paint for painters. It's as if mathematicians,
> physicists, and architects all had to be in the same department.

Later on in the same essay, Paul Graham re-emphasizes that what we call Computer
Science today is not really the theoretical version of hacking. So when I talk
about the essence of "CS," I mean the essence of "what should be taught in
college to people who just want to be good programmers". (In the interest of
brevity, I'll call it "CS").

And what is that essence? It's *abstraction*. What is an effective abstraction?
How do you create effective abstractions? How can you spot bad ones? The answers
to these questions are often unrelated to the implementation details of a hash
map or the computational complexity of sorting algorithms.

The main difference I see between good code and bad code is simplicity. Good
abstractions get you simplicity. Simple code is shorter, easier to comprehend
and easier to work with. Bad abstractions (or lack of abstractions) get you the
opposite.

A classic Tony Hoare quote:

> There are two ways of constructing a software design: One way is to make it so
> simple that there are obviously no deficiencies, and the other way is to make it
> so complicated that there are no obvious deficiencies. The first method is far
> more difficult.

My mind has been particularly impressed upon by code I've reviewed from
interview candidates at Lucid recently. Often, the candidate's grasp of the
language is perfectly sufficient to solve the problems, but the code they write
is too complex. If they had framed the problem better&mdash;i.e. if they had the
right abstractions&mdash;they would be able to write better code in less time.

If we want to be better programmers, we need to develop our skills in working
with abstractions. That's the main point I'd like to get across. Having said
that, how can such skills be developed? I have some ideas which I'll present
below, but I'm including the disclaimer that I see them as only the beginning of
an important discussion, not the end.

For starters, a focus on simplicity is key. Good abstractions are a means to an end,
and that end is simplicity. That's what CS students should think about first (and
aren't we all CS students when you think about it?). Striving for simple
solutions will bring an understanding of abstraction.

An example of how to do that would be to take time to make your code elegant.
Often (especially when under time pressure, as college students usually are),
I'll focus on getting code that works first, then I'll go back and make the code
good. This is the phase where I spend time thinking about simplicity. Sometimes
I learn new tools which then give me the power to make my code simpler.

For instance, while in college I took a machine learning class. We implemented
back propagation in the second lab. I first finished the requirements using only
the knowledge I had, so I stored all the data in regular Python lists. But after
I finished the lab's requirements, I went back and learned how to use Numpy
matrices instead. This made my code simpler and 10x faster.

However, the only reason I had time to do that was because I wasn't taking a
full load of classes (it was my last semester) and I didn't have a job. In
any other semester, I wouldn't have had time. The current system in college
makes it difficult to spend time on that second, crucial phase of coding. I
think this is a major setback for CS students. It would be great if the system
not only didn't prevent you from writing good code but actually encouraged it
somehow. I'm not sure how that would best be done (have TAs grade on code style?
blech), but it's something to think about.

Reading others' code is also a great way to learn good abstractions. There's
plenty of code online to learn from in the form of Github repos, Stack Overflow
answers, etc. If you're not too much of a recluse you could even find an
experienced programmer (whether online or offline) to review your code and give
suggestions. In the vein of systemic solutions, CS courses could be structured
to facilitate learning from peers. Imagine if after a project was done,
everyone's code (except for those who opt out) was then made available. The
students' projects could be sorted by size, since brevity is a decent heuristic
for which projects will be the simplest. There's always gonna be that one
over-achieving nerd who blows the assignment out of the water, so why not use
them as a resource for other students?

That would even have the side effect of helping CS students actually talk to
each other in a comfortable setting (over code), which is a noble cause by
itself.

With simplicity always in mind as the end goal, it could then be useful
to focus explicitly on abstractions. What are different abstractions out there?
What are their pros and cons? My college education had some focus on your
standard OOP things, but I would've loved to also have classes that gave
in-depth exposure to other abstractions. Peter Norvig gives [this
advice](http://norvig.com/21-days.html):

> Learn at least a half dozen programming languages. Include one language that
> emphasizes class abstractions (like Java or C++), one that emphasizes functional
> abstraction (like Lisp or ML or Haskell), one that supports syntactic
> abstraction (like Lisp), one that supports declarative specifications (like
> Prolog or C++ templates), and one that emphasizes parallelism (like Clojure or
> Go).

College covered the first one fine, but I've had to learn the rest on my
own time.

It'd especially be great to compare and contrast different kinds of
abstractions, like object-oriented vs. functional programming, static vs.
dynamic typing, etc. Give students a chance to think for themselves and debate
with each other. As it is, I felt like my college education was just geared
towards whatever the mainstream thinking was. It'd be better for CS programs to
expose students to different ways of thinking and prepare them to evaluate
abstractions based on merit (instead of based on popularity).

I've focused a lot on changes that could be made to formal education, partially
because I'm interested in reforming education later on in my career. But in the
mean time, individuals can be proactive. However we end up focusing on
abstractions, I think doing so will not only make us better programmers, it'll
help us understand our own identities better. Maybe then it won't feel so bad to
lose coding competitions to math majors.
