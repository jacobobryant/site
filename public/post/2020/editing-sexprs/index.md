{:title "How to edit S-expressions in Vim without plugins" :date "2020-3-30" :tags ["clojure"]}

If you're a Vim user and learning Clojure or another lisp, you don't have to
install any plugins right away (let alone switch editors) just to make editing
S-expressions palatable. You can get by pretty well with just the built-in
features.

It boils down mainly to two keys: `%` and `=`. `%` will jump to a matching
paren (or curly/square bracket). `=` will re-indent a block of code. For
example, consider this implementation of the Fibonacci sequence:

```clojure
(defn fib [n]
  (if (#{0 1} n)
    1
    (+ (fib (- n 1)) (fib (- n 2)))))
```

Suppose we wanted to move the recursive `fib` calls into a (lazy) let binding. With
our cursor on the first line, we'd hit `o` and then add two lines:

```clojure
(defn fib [n]
  (let [fib-1 (delay (fib (- n 1)))
        fib-2 (delay (fib (- n 2)))]
  (if (#{0 1} n)
    1
    (+ (fib (- n 1)) (fib (- n 2)))))
```
With our cursor at the end of the third line, we could finish editing by hitting this sequence of keys:

1. `0jw`: move to opening paren of the `if` form.
2. `=%`: re-indent the `if` form.
3. `jjWW`:  move to the opening paren of the `(fib (- n 1))` form.
4. `c%@fib-1<esc>`: replace that form with `@fib-1`.
5. `Wc%@fib-2)<esc>`: replace the second `fib` form and add a closing paren for the `let` form.

```clojure
(defn fib [n]
  (let [fib-1 (delay (fib (- n 1)))
        fib-2 (delay (fib (- n 2)))]
    (if (#{0 1} n)
      1
      (+ @fib-1 @fib-2))))
```
For another example, say we want to add an element to the end of the div in this hiccup form:
```clojure
[:div
 [:p "I'm sorry Dave"]]
```
With our cursor on the opening bracket of the `:div` form, just hit `%i` and then `<enter>[:p "I'm afraid I can't do that"]<esc>`:
```clojure
[:div
 [:p "I'm sorry Dave"]
 [:p "I'm afraid I can't do that"]]
```

Another useful combination is `d%`, which deletes the current form. I also
sometimes like to use `v%` to visually select a form before hitting `=` to
re-indent it. (And you can hit `==` to re-indent just the current line.) In
general, you'll find that editing S-expressions is quite natural once you've
committed `%` to muscle memory.

**So you can edit S-expressions in plain Vim&mdash;but *should* you?**

If you're already in to Vim, sure. With the addition of just two plugins
([vim-fireplace](https://github.com/tpope/vim-fireplace) and [rainbow
parens](https://github.com/luochen1990/rainbow)) and `let
g:clojure_fuzzy_indent_patterns = ['.*']`, I've found Vim to be a perfectly
adequate editor for Clojure. You can always try out [more
plugins](https://juxt.pro/blog/posts/vim-1.html)/[cursive](https://cursive-ide.com/)/[Emacs](https://blog.jeaye.com/2015/10/24/emacs-vim/)
after you've settled in.
