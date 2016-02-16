(ns hphelper.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [clojure.tools.logging :as log]
            [clojure.data.json :as json]
            [hphelper.chargen.generator :as cgen]
            [hphelper.scengen.scenform :as sform]
            [hphelper.scengen.generator :as sgen]
            [hphelper.scengen.print :as sprint]
            [hiccup.core :refer :all]
            )
  (:gen-class))

(defroutes app-routes
  (GET "/char/" [] (cgen/html-print-sheet-one-page (cgen/create-character)))
  (GET "/scen/" [] (sform/html-select-page))
  (POST "/scen/"
        {params :params}
        (-> params
            (sform/from-select-to-scenmap)
            (sgen/create-scenario)
            (sprint/html-print-scenario)))
  (route/not-found
   (html [:html
          [:body
           [:a {:href "./scen/"} "Scenario Generator"][:br]
           [:a {:href "./char/"} "Character Generator"][:br]
           [:br]
           [:a {:href "https://github.com/lsenjov/hphelper"} "Source Code"][:br]
           ]]))
  )

(def app
  (-> app-routes
      (wrap-defaults site-defaults)
      ring.middleware.session/wrap-session
      )
  )
