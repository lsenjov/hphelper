(ns hphelper.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [clojure.tools.logging :as log]
            [clojure.data.json :as json]
            [hphelper.chargen.generator :as cgen]
            [hphelper.scengen.scenform :as sgen]
            )
  (:gen-class))

(defroutes app-routes
  (GET "/char/" [] (cgen/html-print-sheet (cgen/create-character)))
  (GET "/scen/" [] (sgen/html-select-page))
  (POST "/scen/" {params :params} (str params))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-defaults site-defaults)
      ring.middleware.session/wrap-session
      )
  )
