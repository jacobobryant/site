(ns site.core
  (:require [stasis.core :as stasis]
            [trident.staticweb :as ts]
            [trident.util :as u]
            [trident.views :as tv]
            [trident.repl :refer [defhandler]]
            [clojure.string :as str]
            [me.raynes.fs :as fs]
            [clojure.walk :as walk]
            [site.feed :as feed]
            [sc.api :as sc]
            [commonmark-hiccup.core :as cm]
            [ring.middleware.resource :refer [wrap-resource]]))

(def ^:dynamic *base-url* "http://localhost:8080")

(defn element= [& xs]
  (apply = (map #(str/replace % #"(\.|#).*" "") xs)))

(defn externalize-url [x]
  (if (and (vector? x)
           (element= :a (first x)))
    (if (str/starts-with? (or (:href (second x)) "") "/")
      (update-in x [1 :href] #(str *base-url* %))
      (assoc-in x [1 :target] "_blank"))
    x))

(defn transform [x]
  (walk/postwalk externalize-url x))

(defn format-date [date]
  (.format (new java.text.SimpleDateFormat "MMMM yyyy") date))

(def statcounter
"<script type=\"text/javascript\">
var sc_project=12140644;
var sc_invisible=1;
var sc_security=\"e36ded0c\";
var sc_https=1;
var sc_remove_link=1;
</script>
<script type=\"text/javascript\"
src=\"https://www.statcounter.com/counter/counter.js\"
async></script>
<noscript><div class=\"statcounter\"><img class=\"statcounter\"
src=\"https://c.statcounter.com/12140644/0/e36ded0c/1/\"
alt=\"site stats\"></div></noscript>")

(def subscribe [:a {:href "https://tinyletter.com/jacobobryant"} "Subscribe"])

(defn page [{:keys [active-section title description]} & contents]
  (let [f #(str (some-> % (str " | ")) "Jacob O'Bryant")
        title (f title)
        description (f (or description title))]
    ((comp ts/html transform)
     [:html
      [:head
       [:meta {:charset "utf-8"}]
       [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
       [:link {:rel "stylesheet"
               :href "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
               :integrity "sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
               :crossorigin "anonymous"}]
       [:link {:rel "stylesheet" :href "/css/custom.css"}]
       [:link {:href "/favicon.ico" :rel "shortcut icon" :type "image/x-icon"}]
       [:link {:href "/css/prism.css" :rel "stylesheet"}]
       [:title title]
       [:meta {:name "author" :content "Jacob O'Bryant"}]
       [:meta {:name "description" :content description}]]
      [:body {:style {:margin-bottom "20px"
                      :font-size "16px"
                      :font-family "\"Helvetica Neue\",Helvetica,Arial,sans-serif"}}
       [:nav.navbar.navbar-dark.bg-dark.navbar-expand-md
        [:div.container
         [:ul.navbar-nav
          [:li.nav-item
           [:a.navbar-brand {:style {:font-size "22px"} :href "/"} "Jacob O'Bryant"]]
          (for [[k title url] [[:post "Articles" "/"]
                               [:books "Books" "/books/"]
                               [:gists "Gists" "https://gist.github.com/jacobobryant"]
                               [:about "About" "/about/"]]]
            [:li.nav-item {:class (when (= k active-section) "active")}
             [:a.nav-link {:style {:font-size "18px"} :href url} title]])]]]
       (tv/gap "25px")
       [:div.container
        [:div.row
         [:div.col-lg-2.d-flex.flex-row.flex-lg-column.align-items-center.align-items-lg-start.mb-3
          {:style {:font-size "14px"}}
          [:img {:src "/about/avatar.jpg" :width "130px"}]
          [:div {:style {:width "20px" :height "10px"}}]
          [:div
           [:div "Founder @ " [:a {:href "https://lagukan.com"} "Lagukan"]]
           (tv/gap "5px")
           [:div subscribe]
           (tv/gap "5px")
           [:div [:a {:href "https://twitter.com/obryant666"} "Twitter"]]]]
         (into [:div.col-lg {:style {:max-width "600px"}}] contents)]]
       statcounter
       [:script {:src "/js/prism.js"}]]])))

(defn post? [url]
  (str/starts-with? url "/post/"))

(defn update-some [m k & args]
  (if (contains? m k)
    (apply update m k args)
    m))

(defn parse-md [{:keys [url path section]}]
  (let [text (slurp path :encoding "UTF-8")
        [metadata content] (rest (re-find #"(?s)\+\+\+(.*)\+\+\+(.*)" text))
        content (delay (cm/markdown->hiccup cm/default-config content))
        summary (delay (ts/html (first @content)))
        metadata (->> (str/split metadata #"\n")
                      (remove (comp empty? str/trim))
                      (map (fn [s]
                             (let [[k v] (map str/trim (str/split s #" = "))]
                               [(keyword k) (read-string v)])))
                      (into {}))
        {:keys [title date hide-title?] :as metadata}
        (-> metadata
            (update-some :date #(.parse (new java.text.SimpleDateFormat "yyyy-MM-dd") %))
            (assoc :summary summary))]
    (with-meta
      (fn [_]
        (page {:active-section section
               :title title}
              (when-not hide-title? (some->> title (vector :h2)))
              (some->> date format-date (vector :p))
              @content
              (when (post? url)
                (list
                  [:hr]
                  [:p [:small subscribe " to be notified about new posts and project updates."]]))))
      metadata)))

(def md-pages
  (->>
    (fs/find-files "resources/public/" #".*\.md$")
    (map (fn [f]
           (let [path (subs (.getPath f) (inc (count (.getPath fs/*cwd*))))
                 url (str (second (re-find #"resources/public(.*)\.md$" path)) "/")
                 section (keyword (second (re-find #"^/([^/]*)/" url)))]
             [url (parse-md {:url url :path path :section section})])))
    (into {})))

(defn home [_]
  (page {:active-section :post
         :description "Opinions on any topic, free of charge."}
        (for [[url post] (reverse (sort-by (comp :date meta second) md-pages))
              :let [{:keys [title draft]} (meta post)]
              :when (and (post? url)
                         (not draft))]
          [:div.mb-2 [:a {:href url} title]])))

(defn atom-feed [_]
  (feed/atom-feed
    (let [posts (->> md-pages
                     (map (fn [[url f]]
                            (-> (meta f)
                                (assoc :url url)
                                (update-some :summary deref))))
                     (filter (comp post? :url))
                     (map (fn [x] (update x :url #(str "https://jacobobryant.com" %))))
                     (sort-by :date)
                     reverse)]
      {:posts posts
       :title "Jacob O'Bryant"
       :author "Jacob O'Bryant"
       :email "foo@jacobobryant.com"
       :base-name "https://jacobobryant.com"
       :subtitle "Opinions on any topic, free of charge"
       :updated (:date (first posts))})))

(def pages
  (stasis/merge-page-sources
    {:main {"/" home
            "/post/" (ts/html [:html [:head [:meta {:http-equiv "Refresh" :content "0; url=/"}]]])
            "/atom.xml" atom-feed}
     :md-pages md-pages}))

(defn wrap-fix-charset [handler]
  (fn [req]
    (update-in (handler req) [:headers "Content-Type"]
               #(cond-> % #{"text/html"} (str "; charset=utf-8")))))

(def app (-> pages
             stasis/serve-pages
             (wrap-resource "public")
             wrap-fix-charset))

(defhandler app {:handler app})

(defn export []
  (u/sh "rsync" "-av" "--delete" "--exclude" "*.md" "resources/public/" "target")
  (binding [*base-url* "https://jacobobryant.com"]
    (stasis/export-pages pages "target")))

(comment

  (time (export))

   )
