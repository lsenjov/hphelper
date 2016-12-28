(ns hphelper.frontend.shared
  (:require [taoensso.timbre :as log]
            [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET POST] :as ajax]
            ))

(defonce ^:private system-info
  (atom {:context (-> js/window
                      .-location
                      ;; This gets /context/index.html
                      .-pathname
                      ;; Remove the ending
                      (clojure.string/replace "/index.html" "")
                      )
         :buttonSize " btn-xs "
         }
        )
  )

(defn wrap-context
  "Adds the current context to a url"
  [url]
  (str (:context @system-info) url)
  )

(defn add-button-size
  "Adds button size to the end of a string"
  [^String classString]
  (str classString " " (:buttonSize @system-info)))

(defn get-societies
  "Gets the societies from system info"
  []
  (:societies @system-info)
  )

(defn get-mutations
  "Gets the mutations from system info"
  []
  (:mutations @system-info)
  )

(defn get-drawbacks
  "Gets the mutations from system info"
  []
  (:drawbacks @system-info)
  )

(defn init
  "Initialises empty parts of system-info. Returns an empty string"
  []
  ;; Load secret society info
  (if (not (:societies @system-info))
    (ajax/GET (wrap-context "/api/db/get-societies/")
              {:response-format (ajax/json-response-format {:keywords? true})
               :handler (fn [m]
                          (log/info "Got societies.")
                          (swap! system-info assoc :societies m)
                          )
               ;:params {}
               }
              )
    )
  ;; Load mutations info
  (if (not (:mutations @system-info))
    (ajax/GET (wrap-context "/api/db/get-mutations/")
              {:response-format (ajax/json-response-format {:keywords? true})
               :handler (fn [m]
                          (log/info "Got mutations.")
                          (swap! system-info assoc :mutations m)
                          )
               ;:params {}
               }
              )
    )
  ;; Load drawbacks info
  (if (not (:drawbacks @system-info))
    (ajax/GET (wrap-context "/api/db/get-drawbacks/")
              {:response-format (ajax/json-response-format {:keywords? true})
               :handler (fn [m]
                          (log/info "Got drawbacks.")
                          (swap! system-info assoc :drawbacks m)
                          )
               ;:params {}
               }
              )
    )
  ""
  )

(defn get-debug
  "Gets the entirety of system-info as a string"
  []
  (pr-str @system-info)
  )
