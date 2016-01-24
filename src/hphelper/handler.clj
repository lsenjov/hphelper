(ns hphelper.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [clojure.tools.logging :as log]
            [clojure.data.json :as json]
            [hphelper.chargen :as cgen]
            )
  (:gen-class))

(defroutes app-routes
  (GET "/" [] (cgen/html-print-sheet (cgen/create-character)))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
