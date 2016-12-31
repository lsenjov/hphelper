(ns hphelper.frontend.reference
  "Contains a component for giving players information"
  (:require [taoensso.timbre :as log]
            [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET POST] :as ajax]
            [hphelper.frontend.shared :refer [wrap-context add-button-size] :as shared]
            ))

(defn specialties-component
  "Component for rendering specialties"
  []
  (let [filter-atom (atom {:skills_parent nil})]
    (fn []
      (let [specialties (shared/get-specialties)]
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

