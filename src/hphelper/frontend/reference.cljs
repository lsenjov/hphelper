(ns hphelper.frontend.reference
  "Contains a component for giving players information"
  (:require [taoensso.timbre :as log]
            [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET POST] :as ajax]
            [hphelper.frontend.shared :refer [wrap-context add-button-size] :as shared]
            ))

(def ^:private ref-drugs
  "A list of drugs, clearances, and effects"
  [{:drug-name "Visomorpain"
    :clearance "IR"
    :desc "Little Black Friend. Dulls pain and makes user happy."}
   {:drug-name "Xanitrick"
    :clearance "IR"
    :desc "Wakey Wakey."
    }
   {:drug-name "Thiahexadrine"
    :clearance "IR"
    :desc "Focusol."
    }
   {:drug-name "Gelgernine"
    :clearance "R"
    :desc "Inner Happiness."
    }
   {:drug-name "Sandallathon"
    :clearance "R"
    :desc "Sleepy-Sleepy."
    }
   {:drug-name "Pyroxidine"
    :clearance "R"
    :desc "Wide-Awake."
    }
   {:drug-name "Pyroxidine 2"
    :clearance "R"
    :desc "Wider Awake."
    }
   {:drug-name "Qualine"
    :clearance "R"
    :desc "E-Z-DUZ-IT."
    }
   {:drug-name "Persidax"
    :clearance "R"
    :desc "New You."
    }
   {:drug-name "Clonoglazeron"
    :clearance "R"
    :desc "MelloWake."
    }
   {:drug-name "Jargotan"
    :clearance "R"
    :desc "Scramble."
    }
   {:drug-name "Smilase Tentrasildenafil"
    :clearance "R"
    :desc "Smilies."
    }
   {:drug-name "Sodium Pentathol"
    :clearance "R"
    :desc "Filter Fighter."
    }
   {:drug-name "Zybenzaphrene"
    :clearance "O"
    :desc "Slumber Soft."
    }
   {:drug-name "Vulpazine"
    :clearance "O"
    :desc "Night Stalker."
    }
   {:drug-name "Calcium Carbonate"
    :clearance "O"
    :desc "Calm-n-Coat Antacid."
    }
   {:drug-name "Dioxromnurespa-butinol-3"
    :clearance "O"
    :desc "Sparkle Ultra."
    }
   {:drug-name "Asperquaint"
    :clearance "Y"
    :desc "Pep Pills."
    }
   {:drug-name "Erithermaboxadrine"
    :clearance "Y"
    :desc "Glow."
    }
   {:drug-name "Morlox"
    :clearance "Y"
    :desc "Furball."
    }
   {:drug-name "Telescopalamine"
    :clearance "G"
    :desc "Truth and Beauty. Self-finking pills."
    }
   {:drug-name "Necronomicil"
    :clearance "G"
    :desc "ReAnimator."
    }
   {:drug-name "Rolactin"
    :clearance "B"
    :desc "Happy Life."
    }
   {:drug-name "Thymoglandin"
    :clearance "B"
    :desc "Combat Quick."
    }
   {:drug-name "Oxyflucocillin"
    :clearance "B"
    :desc "Overdose Helper."
    }
   {:drug-name "Benetridin"
    :clearance "I"
    :desc "VideoLand."
    }
   {:drug-name "Diphenhydromegatoxine"
    :clearance "I"
    :desc "Regro."
    }
   {:drug-name "Dynomorphin"
    :clearance "U"
    :desc ""
    }
   {:drug-name "Hydropsionic Acid"
    :clearance "ILLEGAL"
    :desc ""
    }
   {:drug-name "Verasubsannine"
    :clearance "ILLEGAL"
    :desc "Believapills."
    }
   {:drug-name "Metamemopyroxide"
    :clearance "ILLEGAL"
    :desc "Friendly Fire."
    }
   ]
  )

(defn drugs-component
  "Component for rendering a list of drugs"
  []
  (let [sort-atom (atom {:sort-key :drug-name})]
    (fn []
      [:div
       [:div
        (shared/switcher-toolbar sort-atom
                                 [:sort-key]
                                 [[:drug-name "Name"]
                                  [:clearance "Clearance"]
                                  ]
                                 )
        ]
       [:table {:class "table table-striped table-hover"}
        [:thead>tr
         [:th "Name"] [:th "Clearance"] [:th "Description"]
         ]
        [:tbody
         (->> ref-drugs
              ((case (:sort-key @sort-atom)
                 :drug-name #(sort-by :drug-name %)
                 :clearance #(sort-by (comp {"IR" 8 "R" 7 "O" 6 "Y" 5 "G" 4 "B" 3 "I" 2 "V" 1} :clearance) %)
                 identity
                 )
               )
              (map (fn [{:keys [drug-name clearance desc]}]
                     [:tr [:td drug-name] [:td clearance] [:td desc]])
                   )
              doall
              )
         ]
        ]
       ]
      )
    )
  )

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
                       [:tr [:td skills_name] [:td (shared/wrap-skill-initial skills_parent)] [:td skills_desc]]))
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
                                 [[:specialties "Specialties"]
                                  [:drugs "Drugs"]
                                  ]
                                 )
        ]
       (case (:page @page-atom)
         :skills "Not implemented"
         :specialties [specialties-component]
         :drugs [drugs-component]
         "Make a selection"
         )
       ]
      )
    )
  )

