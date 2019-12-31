(ns feed
  (:require
    [cemerick.url :as url]
    [clojure.edn :as edn]
    [clojure.string :as str]
    [clojure.walk :as walk]
    [hiccup.core :as hiccup]
    [hiccup.util :as hutil]
    [hickory.core :as hickory]
    [hickory.render :as hickr]
    [markdown.core :as md]
    [me.raynes.fs :as fs]
    [trident.util :as u]))

(def site-root "https://jacobobryant.com")
(def cdn-root "https://cdn.jsdelivr.net/gh/jacobobryant/site/public")
(def author-name "Jacob O'Bryant")

(def feed-date-format "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

(defn rm-index [s]
  (str/replace s #"/index.html$" "/"))

(defn parse-md [file]
  (let [contents (slurp file)
        metadata (update (edn/read-string contents) :date u/parse-date "yyyy-MM-dd")
        contents (str/replace contents #"^\{[^}]*\}\s*" "")
        path (-> (.getPath file)
                 (str/replace #".md$" ".html")
                 rm-index
                 (str/replace (re-pattern (str (.getPath fs/*cwd*) "/public")) ""))]
    (merge metadata
           {:path path
            :content contents})))

(defn relative? [url]
  (nil? (u/catchall (url/url url))))

(defn format-md [{:keys [content current-path paths]}]
  (->> content
       md/md-to-html-string
       hickory/parse-fragment
       (map hickory/as-hickory)
       (walk/postwalk
         (fn [x]
           (if-some [[attr target] (->> [:href :src]
                                        (filter #(some-> x :attrs % relative?))
                                        (map #(vector % (-> x :attrs %)))
                                        first)]
             (let [normalized (->> (rm-index target)
                                   (url/url (str "http://foo.com" current-path))
                                   :path)
                   sibling-path (some paths [normalized (str normalized "/")])
                   new-target (if (some? sibling-path)
                                (str site-root sibling-path)
                                (str cdn-root normalized))]
               (assoc-in x [:attrs attr] new-target))
             x)))
       (map hickr/hickory-to-html)
       (str/join "")
       hutil/escape-html))

(defn write-feed []
  (let [posts (->> (fs/find-files "public/" #".*\.md$")
                   (map parse-md)
                   (sort-by :date)
                   reverse)
        paths (set (map :path posts))
        self-url (str cdn-root "/feed.xml")]
    (->>
      [:feed {:xmlns "http://www.w3.org/2005/Atom"}
       [:title author-name]
       [:updated (u/format-date (:date (first posts)) feed-date-format )]
       [:id self-url]
       [:link {:rel "self" :href self-url :type "application/atom+xml"}]
       [:link {:href site-root}]
       [:author
        [:name author-name]
        [:uri site-root]]
       (for [{:keys [title date tags path content] :as p} posts
             :let [url (str site-root (rm-index path))]]
         [:entry
          [:title title]
          [:id url]
          [:link {:href url}]
          [:updated (u/format-date date feed-date-format)]
          [:content {:type "html"} (format-md {:content content
                                               :current-path path
                                               :paths paths})]
          (for [t tags]
            [:category {:term t}])])]
      hiccup/html
      (str "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
      (spit "public/feed.xml"))))

(defn -main []
  (write-feed))
