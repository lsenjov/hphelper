(ns hphelper.frontend.core
  (:require [taoensso.timbre :as log]
            [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET POST] :as ajax]
            [hphelper.frontend.shared :refer [wrap-context add-button-size] :as shared]
            [hphelper.frontend.chargen]
            [hphelper.frontend.play]
            [hphelper.frontend.reference]
            [hphelper.frontend.websockets]
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
  (atom {:page :play}
        )
  )

(defn front
  "The first container"
  []
  (fn []
    (let [p (:page @system-info)]
      [:div {:class ""
             :style {:width "90%"}
             }
       ;; Page Selection
       [:div.navbar.navbar-expand-lg.navbar-light.bg-light.navbar-fixed-top
        [:div {:class "container"}
         [:div {:class "navbar-header"}
          (shared/switcher-toolbar system-info
                                   [:page]
                                   [[:character "Character Tools"] [:scenario "Scenario Tools"]
                                    [:sector "Sector Tools"] [:reference "Paranoiapedia"] [:play "Play Paranoia"]
                                    ]
                                   )
          ;; Logging in and out currently not implemented, remove it
          ;;[shared/user-bar-component]
          [shared/tutorial-switcher-button]
          [shared/debug-switcher-button]
          ]
         ]
        ]
       ;; Page display
       [:div {:class "container"
              :style {:width "90%"}
              }
        [:div {:class "page-header"}
         [:br] [:br]
         (case p
           :character [hphelper.frontend.chargen/character-page]
           :scenario [:div "Scenario not implemented"]
           :sector [:div "Sector not implemented"]
           :play [hphelper.frontend.play/play-component]
           :reference [hphelper.frontend.reference/reference-component]
           [:div "Please make a page selection above"]
           )
         ]
        ]
       ;; Makes sure we have the required data in shared
       [:div (do (shared/init) nil)]
       ]
      )
    )
  )


(reagent/render-component [front]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
