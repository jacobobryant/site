(ns user
  (:require [trident.repl :as repl]))

(defn init []
  (repl/init {:nrepl-port 7801}))
