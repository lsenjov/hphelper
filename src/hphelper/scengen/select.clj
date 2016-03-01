(ns hphelper.scengen.select
  (:require 
    [clojure.tools.logging :as log]
    [hphelper.scengen.scenform :as sform]
    [hphelper.scengen.generator :as sgen]
    [hphelper.scengen.print :as sprint]
    [hphelper.shared.saveload :as sl]
    [hiccup.core :refer :all]
    [hphelper.shared.saveload :refer :all]
    ))

(defn print-select-page
  "Prints a page for selecting individual crisises"
  ([baseURL]
   (html [:html
          [:head [:title "HPHelper Scenario Generator"]]
          [:body
           [:h3 "Scenario Generator"]
           [:form {:action (str baseURL "/scen/gen/")}
            "Generate New Scenario:"
            [:input {:type "submit" :value "New Scenario"}]
            ]
           [:br]
           [:form {:action "." :method "get"}
            [:select {:name "scen_id"}
             (map (fn [i] [:option {:value i} i])
                  (reverse (sl/get-scen-ids)))
             ]
            [:input {:type "submit" :value "Load Scenario"}]
            ]
           ]
          ]
         )))

(defn print-crisis-page
  "Prints the page for selecting view options for a crisis"
  [s_id baseURL]
  (html [:html
         [:head
          [:title "Select your view"]
          ]
         [:body
          [:h3 "Selection for saved scenario: " s_id]
          [:form {:action (str baseURL "/scen/print/") :method "get"}
           [:input {:type "hidden" :name "scen_id" :value s_id}]
           [:input {:type "checkbox" :name "gmheader" :checked "on"} "Print GM's Header"] [:br]
           [:input {:type "checkbox" :name "gmindicies" :checked "on"} "Print GM's Indicies"] [:br]
           [:input {:type "checkbox" :name "gmcrisises" :checked "on"} "Print GM's Crisis Summary"] [:br]
           [:input {:type "checkbox" :name "gmcbay" :checked "on"} "Print GM's Cbay Summary"] [:br]
           [:input {:type "checkbox" :name "gmdirectives" :checked "on"} "Print GM's Directive summary"] [:br]
           [:input {:type "checkbox" :name "gmnews" :checked "on"} "Print GM's News Summary"] [:br]
           [:input {:type "checkbox" :name "gmsocieties"} "Print GM's Societies Summary"] [:br]
           [:input {:type "checkbox" :name "players" :checked "on"} "Print player sheets"] [:br]
           [:input {:type "checkbox" :name "minions" :checked "on"} "Print minion sheets"] [:br]
           [:input {:type "checkbox" :name "directives" :checked "on"} "Print directive sheets"] [:br]
           [:div [:input {:type "submit" :value "Print Scenario"}]]
           ]
          ]
         ]
        ))
