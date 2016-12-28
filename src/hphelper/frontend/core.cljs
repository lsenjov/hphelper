(ns hphelper.frontend.core
  (:require [taoensso.timbre :as log]
            [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET POST] :as ajax]
            [hphelper.frontend.chargen]
            [hphelper.frontend.shared :refer [wrap-context] :as shared]
            ))

(enable-console-print!)

(log/info "Page Load Begin")

;; define your app data so that it doesn't get over-written on reload

;; TODO change back to defonce when required
(def app-state (atom
                 {}
                 )
  )
(defonce system-info
  (atom {}
        )
  )

(defn front
  "The first container"
  []
  (fn []
    [:div {:class "container"}
     ;; Page Selection
     [:div
      [:span {:class "btn btn-default"
              :onClick #(swap! system-info assoc :page :character)
              }
       "Character Tools"
       ]
      [:span {:class "btn btn-default"
              :onClick #(swap! system-info assoc :page :scenario)
              }
       "Scenario Tools"
       ]
      [:span {:class "btn btn-default"
              :onClick #(swap! system-info assoc :page :sector)
              }
       "Sector Tools"
       ]
      ]
     ;; Page display
     [:div {:class "container"}
      (let [p (:page @system-info)]
        (case p
          :character [hphelper.frontend.chargen/character-page]
          :scenario [:div "Scenario not implemented"]
          :sector [:div "Sector not implemented"]
          [:div "Please make a page selection above"]
          )
        )
      ]
     ;; Debug data
     [:div
      (shared/init)
      ]
     ]
    )
  )


(reagent/render-component [front]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
