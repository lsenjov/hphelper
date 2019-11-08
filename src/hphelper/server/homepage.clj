(ns hphelper.server.homepage
  (:require
    [hiccup.core :refer :all]
    [ring.util.anti-forgery :refer [anti-forgery-field]]
    [hphelper.server.chargen.generator :as cgen]
    [taoensso.timbre :as log]
    )
  (:gen-class)
  )

(defn render-homepage
  "Renders a static webpage, with csrf tokens for websockets to use"
  []
  (html
    [:html
     [:head
      [:meta {:charset "UTF-8"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
      [:link {:href "css/reset.css" :rel "stylesheet" :type "text/css"}]
      [:link {:href "css/bootstrap.pulse.min.css" :rel "stylesheet" :type "text/css"}]
      [:link {:href "css/screen.css" :rel "stylesheet" :type "text/css"}]
      [:link {:rel "stylesheet" :href "http://code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.min.css"}]
      [:script {:src "https://code.jquery.com/jquery-1.11.2.min.js" :type "text/javascript"} ]
      [:script {:src "http://code.jquery.com/ui/1.11.2/jquery-ui.min.js" :type "text/javascript"}]]
     [:body
      [:div#csrf (anti-forgery-field)]
      [:div#app
       [:h2 "Application loading."]]
      [:script {:src "js/compiled/hphelper.js" :type "text/javascript"}]]]))
