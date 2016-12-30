(ns hphelper.frontend.reference
  "Contains a component for giving players information"
  (:require [taoensso.timbre :as log]
            [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET POST] :as ajax]
            [hphelper.frontend.shared :refer [wrap-context add-button-size] :as shared]
            ))

(def keyword-dict
  "Map of keywords to definitions in different categories.
  Some items may have extra tags."
  (atom
    {:skills {"Management" {;; Description of the skill
                            :skills_desc "Management Definition."
                            ;; The initial used
                            :init "M"}
              "Violence" {:skills_desc "Violence Definition."
                          :init "V"
                          }
              }
     }
    )
  )

(defn specialties-component
  "Component for rendering specialties"
  []
  (let [filter-atom (atom {:skills_parent nil})]
    (fn []
      (let [specialties (:specialties @keyword-dict)]
        [:div
         [:div
          (shared/switcher-toolbar filter-atom
                                   [:skills_parent]
                                   [[nil "No Filter"]
                                    ["M" "Management"] ["Su" "Subterfuge"]["V" "Violence"]
                                    ["H" "Hardware"] ["So" "Software"] ["W" "Wetware"]
                                    ["O" "Other"]
                                    ]
                                   )
          ]
         [:table {:class "table table-striped table-hover"}
          [:thead>tr
           [:td "Name"] [:td "Parent Skill"] [:td "Description"]
           ]
          [:tbody
           (->> specialties
                ;; Filter skills
                (filter #(if (not (:skills_parent @filter-atom))
                           %
                           (= (:skills_parent @filter-atom) (:skills_parent %))
                           )
                        )
                ;; Sort Alphabetically
                (sort-by :skills_name)
                ;; Make it pretty
                (map (fn [{:keys [skills_name skills_desc skills_parent]}]
                       ^{:key skills_name}
                       [:tr [:td skills_name] [:td skills_parent] [:td skills_desc]]))
                doall
                )
           ]
          ]
         "Filter: " (pr-str @filter-atom)
         ]
        )
      )
    )
  )

(defn reference-component
  "Reference page for getting information"
  []
  (let [page-atom (atom {:page nil})]
    (fn []
      [:div
       [:div
        (shared/switcher-toolbar page-atom
                                 [:page]
                                 [[:specialties "Specialties"]]
                                 )
        ]
       (case (:page @page-atom)
         :skills "Not implemented"
         :specialties [specialties-component]
         "Make a selection"
         )
       ]
      )
    )
  )

;; Get the skills from the database
(if (not (:specialties @keyword-dict))
  (ajax/GET (wrap-context "/api/db/get-skills/")
            {:response-format (ajax/json-response-format {:keywords? true})
             :handler (fn [m]
                        (log/info "Got specialties.")
                        (swap! keyword-dict assoc :specialties m)
                        )
             }
            )
  )
