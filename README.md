# jacobobryant.com

This is the source for [my personal website](https://jacobobryant.com). It's
also a decent example of creating a static website with Clojure. Run `clj -e
'(init)' -r` to run a development webserver on port 8080. After making any
changes, you can reload either by running `(refresh)` from the repl or by
eval-ing the `site.core` namespace from your editor.

Deploys are done by eval-ing `(site.core/export)` and then running `./push.sh`.

This website uses:
 - Markdown for content.
 - A home-grown markdown front matter parser, mimicking Hugo.
 - Bootstrap for styling.
 - `commonmark-hiccup` to convert markdown to hiccup, which is then
   post-processed before being converted to HTML.
 - `trident.staticweb/html`, a hiccup + garden wrapper that allows inline CSS
   like in Reagent.
 - `stasis` to serve HTML in development and export to the filesystem before
   deployment.

## License

Distributed under the [EPL v2.0](LICENSE)

Copyright &copy; 2019 [Jacob O'Bryant](https://jacobobryant.com).
