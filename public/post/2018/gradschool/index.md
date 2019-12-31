{:title "Grad school", :date "2018-10-25"}

My primary career goal for a long time has been to be a successful startup
founder. At one time, I was planning to get a part-time job after
graduating (working only enough hours to pay for living expenses) so I could
devote most of my time to building a startup. For a few reasons I decided not to
try that, so I accepted a full-time offer from Lucid in order to save up money
and gain experience. So my plan has always been to work here temporarily and
then work on a startup full-time as soon as I'm done.

Over the past month or so as I've spent more of my spare time working on
projects in Clojure, I realized also that there's a lot of stuff in the Clojure
ecosystem I'd like to get the hang of. I started to modify my plan slightly
to leave some space between quitting Lucid and doing the startup so I could
spend time learning various Clojure libraries.

But in fact, [the more I've thought about it](../productionandlearning), the
more I want to emphasize this period of just learning. I've started
euphemistically referring to it as "going to grad school" because I want to have
a significant higher-learning experience. I don't want to go back to the
structure of formal education though, so an actual master's degree wouln't hit
the spot, and a PhD is out of the question because

1. I found out in my undergrad that I don't like trying to fit my projects in
   the category of "research"

2. I don't want to put off the startup for another 4+ years

So instead I'll just stick with my do-it-yourself,
homeschool-style mini-master's degree from the graduate school of programming at
O'Bryant University. It's not accredited, but I know the headmaster and I think
he's great.

In preparation, I started listing subjects to learn more about and included
a few projects that I've wanted to code up for a while. I realized that the
projects actually were great ways to put into practice all the things I wanted
to learn, so a natural structure for my curriculum emerged. Here are the
projects.

**Project 0:** Write an AI to play Clue

Whenever I play Clue, I feel like there's so much information and I can only
latch on to part of it. A program could keep track of every little detail. The
main part of the program (i.e. given the information you have, which cards are
in the envelope?) would be a good opportunity to practice logic programming, but
there are other components too. Which room should you travel to? What questions
should you ask? I tried to build this in my Intro to AI class but the professor
wouldn't let me because our final project was supposed to involve robots.

**Project 1:** Write a website for writing board game AIs

It turns out the Clue thing is just an instance of a more general problem I
have. I often get tired of board/card games because it feels tedious playing
them. It's like doing random arithmetic problems over and over again. However, I
often think about how interesting it would be to write AIs for these games (I've
always loved automating boring tasks). For example, although I do actually enjoy
playing Pandemic, I think writing an AI for it would be 10x more fun.
It's like a classic graph theory problem but with all these constraints thrown
in.

Wouldn't it be cool to have a website that facilitated writing AIs for board
games and using them to compete against other programmers? It'd be like robot
soccer tournaments. For any game, someone could write a server component that
would define all the rules and provide an interface for clients. Then anyone
could code up an AI client (or a client meant for humans) and plug it in.

This could also be a great tool for teaching programming, another one of my
interests. You could gradually introduce someone to programming by giving them
most of the components of an AI written with bits left out for them to fill in.
As the student progresses you could have them code more and more of the AI.

**Project 2:** Rewrite [Smart Shuffle
Player](https://play.google.com/store/apps/details?id=com.jacobobryant.moody.vanilla)
using React Native and Clojurescript

Smart Shuffle Player is what I called an app I built during my undergrad. I took
an open-source Android music app and inserted my own recommendation algorithm to
make it select which song to play next. It's basically Frankenstein's monster.
It was good enough for an undergrad research project, and I've even used it as
my main music app for over a year. But there are lots of improvements to be
made. It's not in a position where I would recommend it wholeheartedly.

Refactoring the Java code to make it nice would be no bueno&mdash;I'd rather rewrite
the thing in Clojure. Plus, there's already (relatively) good support for writing
cross-platform, native mobile apps with React Native and ClojureScript, so I
could make an iOS version too.

**The rest of the curriculum**

These projects would give me great exposure to the three main areas I want to
develop expertise in:

- Web application architecture (in Clojure)
- Mobile app development (in Clojure)
- AI/machine learning/statistics (in Clojure)

As I work on these projects, I have some ideas for supplemental learning
activities:

- Read lots of source code for Clojure web libraries, like Pedestal (HTTP
   routing), Sente (websockets) and Immutant (web server).

- Work through Doing Bayesian Data Analysis (a text book I bought a while ago
   but haven't touched).

- Finish SICP (the Bible of Lisp. I've read most of it but haven't done any of
   the exercises).

- Work on Hacker Rank problems (partly to get
   practice doing fancy algorithmic things that usually don't come up but also
   for fun. It's important to take time to enjoy a skill instead of only doing
   deliberate practice).

I'll also include time for some general ed. I have a never-ending queue of
[books](/books) I'd like to get through, and I love writing.

As time goes on I'll likely modify the curriculum. But even as I've thought
about what I have so far, I really think this'll turn into a great experience. I
don't even see it as simply "startup prep." Even if I wasn't planning to be a
startup founder, this would be a great opportunity to take my skills to the
nextLevel++ and launch me into the rest of my career. I have a somewhat mellow
complexion, so I don't get excited easily. But I'm excited about this.
