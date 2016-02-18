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
            [hphelper.scengen.select :as ssel]
            [hphelper.shared.saveload :as sl]
            [hiccup.core :refer :all]
            )
  (:gen-class))

(defroutes app-routes
  (GET "/char/" [] (cgen/html-print-sheet-one-page (cgen/create-character)))
  (GET "/scen/" {params :params} 
       (if (params :scen_id)
         (ssel/print-crisis-page (params :scen_id))
         (ssel/print-select-page)))
  (GET "/scen/gen/" [] (sform/html-select-page))
  (POST "/scen/gen/"
        {params :params}
        (-> params
            (sform/from-select-to-scenmap)
            (sgen/create-scenario)
            ((comp ssel/print-crisis-page sl/save-scen-to-db))))
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
