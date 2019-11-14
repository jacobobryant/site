+++
title = "Understanding Clojure"
date = "2018-10-11"
+++

I remember once during a calculus class, I had to find the derivative of a
function. The function had some trig in it, and applying the rules of
differentiation turned hairy fast. The probability of me applying all the rules
by hand without making any algebraic mistakes was very small. I brought it up 
in class since I didn't see how the problem could be reasonably solved.
It turns out that there was a trig identity I could have used to simplify the
function *before* taking the derivative. In this case, solving the problem
wasn't that bad.

Imagine an alternate route. What if we had thought, "This derivative is way too
complicated to do by hand&mdash;Let's write a computer algebra system that will
handle all this automatically so we don't have any mistakes!" That would've been
the wrong approach. All that work we would've done is just incidental
complexity. It doesn't have to be there.

When solving a math problem, there are often two phases: setting up and/or
simplifying the problem and then actually solving it. In my class, the right way
to solve the problem required a focus on the first phase. Simplifying the
problem removed a lot of complexity from the solution. But the hypothetical
alternate case where we write a CAS focuses on the second phase: we take the
problem as-is and then come up with methods to manage the complexity of the
solution.

When dealing with such complex, abstract systems, we must take the first
strategy of focusing on the first phase. Assertion: mainstream programming
involves a lot of incidental complexity because it doesn't focus enough on the
first phase. For example, 20 years ago Peter Norvig made [these
claims](http://norvig.com/design-patterns/design-patterns.pdf):

- "Dynamic Languages have fewer language limitations"
- "Less need to get around class-restricted design"
- "Study of the Design Patterns book: 16 of 23 patterns have qualitatively
  simpler implementation in Lisp or Dylan than in C++ for at least some uses of
  each pattern"

A big part of Clojure's value proposition is that it helps you simplify the
problem so that your solutions don't have to be so complicated. [It's built with
simplicity in mind](https://www.infoq.com/presentations/Simple-Made-Easy).

As an example, consider dependency injection. In response to the question "Does
clojure need dependency injection to make code more testable," a Stack Overflow
user [responds](https://stackoverflow.com/a/15696956/1258629):

> In Clojure you usually achieve the equivalent of dependency injection with
> alternative methods. ... you definitely don't need anything like a "DI
> framework". IMHO, needing a framework for DI is really just compensating for a
> lack of sufficient features in the language itself.

The complexity of a dependency injection framework disappears thanks to the way
the language is structured. A similar argument explains why Clojure programmers
aren't in to static typing. There's nothing wrong with the idea of static
analysis, but so far in practice, it adds complexity. It's not that Clojure
programmers think static typing is inherently bad&mdash;it's just that Clojure
simplifies your programs (e.g. by separating functions from information) so
that the benefits of static typing are no longer great enough to outweigh the
cost.

These examples are necessarily controversial, so don't focus on them too much.
My point is that this philosophy of doing more problem-space simplification is at
the heart of Clojure. I happen to believe the value proposition, and that's why
I can't spend my career on mainstream technologies. I've gotta be a Clojure
hacker.
