{:title "A recommender system in 30 lines of Clojure" :date "2020-4-7"}

I started doing music recommendation research about 4 years ago as an
undergrad. After many iterations, that work has evolved into
[Findka](https://findka.com), a very simple cross-domain recommender system.
I'm going to show you Findka's algorithm code and explain how it works.

First, let me explain how Findka works from the end user's perspective. After
signing up, you get a weekly email that has a list of 10 links. Right now
they're mainly links to music, video games, books, etc., but they could be
links to anything. You give each item a thumbs-up/thumbs-down rating, and eventually
Findka learns what to recommend.

You can speed up the learning process by giving Findka links to stuff you
already like. To make this convenient, there are search integrations for
several content types:

<img src="https://findka.com/img/demo.gif" />

Findka picks new recommendations exclusively from links that have been
added by other users. As of writing, there are 48 users (12 active), 326 links,
and 257 ratings (48% of them positive). At this scale, Findka gets by
just fine with a simple collaborative filtering algorithm that I wrote one
afternoon. Each Friday, I log in to an admin console, load all the data into
memory, run the algorithm, and then send the recommendations to the backend for
emailing. (The algorithm is written in ClojureScript, so it's been compiled to
JS).

And here it is:

```clojure
(defn cooccurrence [users]
  (reduce (fn [cooc path]
            (update-in cooc path (fnil inc 0)))
    {}
    (for [ratings users
          pair ratings
          other-pair ratings
          :when (not= pair other-pair)]
      (into [pair] other-pair))))

(defn recommend [{:keys [cooc user-ratings score explore
                         n exploit-ratio cutoff]
                  :or {n 10, exploit-ratio 0.7, cutoff 0.5}}]
  (let [exclude (set (keys user-ratings))
        exploit (->> user-ratings
                  (map cooc)
                  (apply merge-with #(merge-with + %1 %2))
                  (remove (comp exclude first))
                  (map #(update % 1 score))
                  (sort-by second)
                  reverse
                  (take-while #(<= cutoff (second %)))
                  (map first))
        explore (remove exclude explore)]
    (->> exploit
      (take (* exploit-ratio n))
      (#(concat % explore))
      distinct
      (take n)
      shuffle)))
```

You can use it like this:

```clojure
(let [alice {:a :like
             :b :like
             :c :dislike
             :d :like
             :e :like}
      bob   {:a :like
             :b :dislike
             :c :dislike
             :d :dislike
             :e :like}
      carol {:a :like
             :b :dislike}
      users [alice bob carol]
      explore (distinct (mapcat keys users))
      cooc (cooccurrence users)]
  (recommend
    {:n 2
     :cooc cooc
     :ratings carol
     :score (fn [{:keys [like dislike]
                  :or {like 0 dislike 0}}]
              (/ (+ like 1) (+ like dislike 2)))
     :explore explore}))
; => (:b :a)
```

Let's walk through all that.

First, we construct a co-occurrence matrix. For example:

```clojure
(def cooc (cooccurrence [{:a :like
                          :b :like
                          :c :dislike}
                         {:a :like
                          :b :dislike}]))
=> {[:a :like] {:b {:like 1, :dislike 1}, :c {:dislike 1}},
    [:b :like] {:a {:like 1}, :c {:dislike 1}},
    [:c :dislike] {:a {:like 1}, :b {:like 1}},
    [:b :dislike] {:a {:like 1}}}
```

This tells us e.g. that among users who like item `:b`, one user likes `:a` and
one user dislikes `:c` (in this case, it's the same user).
After you construct the co-occurrence matrix, you can use `recommend` to get
recommendations for an individual user. It works like so:
```clojure
(let [cutoff 0.5
      ; Using values from above
      carol ...
      cooc ...]
  (->> carol

    ; Get the slice of the co-occurrence matrix that applies to this user
    (map cooc)
    ; => ({:b {:like 1, :dislike 2},
    ;      :c {:dislike 2},
    ;      :d {:like 1, :dislike 1},
    ;      :e {:like 2}}
    ;     {:a {:like 2}, :c {:dislike 1}, :d {:dislike 1}, :e {:like 1}})

    ; Combine the slices
    (apply merge-with #(merge-with + %1 %2))
    ; => {:b {:like 1, :dislike 2},
    ;     :c {:dislike 3},
    ;     :d {:like 1, :dislike 2},
    ;     :e {:like 3},
    ;     :a {:like 2}}

    ; Take out any items the user has already rated
    (remove (comp (set (keys carol)) first))
    ; => ([:c {:dislike 3}] [:d {:like 1, :dislike 2}] [:e {:like 3}])

    ; Calculate the score for each candidate item
    (map #(update % 1 score))
    ; => ([:c 1/5] [:d 2/5] [:e 4/5])

    ; Sort by score
    (sort-by second)
    reverse
    ; => ([:e 4/5] [:d 2/5] [:c 1/5])

    ; Don't recommend items that we think they won't like
    (take-while #(<= cutoff (second %)))
    (map first)))
; => (:e)
```

After that, we mix in a few random recommendations so that
the system keeps learning instead of only recommending popular items.

Let's take a closer at the `score` function. We can parameterize it to account for our level of confidence.
```clojure
(def alpha 1)
(def beta 1)

(defn score [{:keys [like dislike]
              :or {like 0 dislike 0}}]
  (/ (+ like alpha) (+ like dislike alpha beta)))
```

The ratio
`(/ alpha (+ alpha beta))` is what score we'd give an item if we didn't know anything
about it&mdash;in this case, 0.5. If you have
some ratings, you could set this to the average rating for all items. The sum `(+ alpha beta)`
is the number of ratings we need before we start to trust the actual ratings more than the default rating.
(You can search "beta distribution" to learn more about this.)

These parameters can help us deal with balancing
*exploitation* (recommending items for which we have a lot of data) and
*exploration* (recommending "cold start" items for which we don't have much data). For example, if
we set alpha relatively high and beta relatively low, then cold start items will get an optimistically high rating. This
gives us a natural balance, since items will start out with high ratings, and thus be recommended frequently,
after which the bad items' ratings will drop. I've used this technique before in music recommendation, after
learning about it from [this paper](https://arxiv.org/pdf/1311.6355.pdf).

On the other hand, if you're displaying a list of most popular items, you'd want to instead set a low alpha and a high beta.
This will ensure that items with a single positive rating don't score higher than items with hundreds of positive ratings
and a few negative ratings. (I wish Amazon did this when you sort by average customer review.)

You could do lots of interesting things with the `score` function, like handling content moderation. If someone reports an
item, give it an `:explicit` rating, then do something like this:

```clojure
(defn score [{:keys [like dislike explicit]
              :or {like 0 dislike 0 explicit 0}}]
  (/ (+ like alpha)
    (+ like dislike alpha beta
      (if current-user-doesnt-want-to-see-explicit-items
        (* explicit 3)
        0))))
```
So we simply treat an `:explicit` rating equally to three `:dislike` ratings. We can similarly incorporate `:click`
and `:view` events.
We could even turn our system into a hybrid recommender by integrating
content-based information&mdash;if the current item is a podcast and the current user never clicks on podcasts,
lower the score.

There you have it: everything you need to make your very own recommender system.
Great for small data sets.
Note that this only works with categorical ratings. If you had numerical ratings
on a scale from 1 to 10, you could replace the co-occurrence matrix with a
cosine similarity matrix (and update `recommend` accordingly). Once your data set is large enough that you can't
work with the entire matrix at once, you can look into matrix factorization techniques
so that your recommender system can scale.

As an alternative to matrix factorization, you could use an algorithm that samples
the matrix without ever constructing the whole thing. Before starting Findka, I made a [music player](https://lagukan.com)
with a track-selection algorithm that does that. This method made it easy to incorporate new data&mdash;
if you have a listening session in the morning, the music player can start using that data in the afternoon
without having to recompute a model. I'm planning to open-source that code in a while, so
maybe this article will get a follow-up post.
