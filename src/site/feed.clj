(ns site.feed
  (:require [hiccup.core :as hiccup]
            [hiccup.util :as util]))

(defn format-date [date]
  (.format (new java.text.SimpleDateFormat "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") date))

(defn atom-entry [{:keys [base-name title date url summary]}]
  [:entry
   [:title title]
   [:id url]
   [:updated (format-date date)]
   [:summary {:type "html"} (util/escape-html summary)]
   [:link {:href url}]])

(defn atom-feed [{:keys [subtitle base-name title author email posts updated] :as opts}]
  (->>
    (let [atom-loc (str base-name "/atom.xml")]
      (into
        [:feed {:xmlns "http://www.w3.org/2005/Atom"}
         [:title title]
         [:id atom-loc]
         [:updated (format-date updated)]
         [:link {:rel "self" :href atom-loc :type "application/atom+xml"}]
         [:link {:href base-name}]
         [:subtitle subtitle]
         [:author
          [:name author]
          [:email email]
          [:uri base-name]]]
        (map (comp atom-entry #(merge opts %)) posts)))
    hiccup/html
    (str "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")))
