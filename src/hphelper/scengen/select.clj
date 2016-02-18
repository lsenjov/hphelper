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
  []
  (html [:html
         [:head [:title "HPHelper Scenario Generator"]]
         [:body
          [:h3 "Scenario Generator"]
          [:form {:action "./gen/"}
           "Generate New Scenario:"
           [:input {:type "submit" :value "New Scenario"}]
           ]
          [:br]
          [:form {:action "." :method "get"}
           [:select {:name "scen_id"}
            (map (fn [i] [:option {:value i} i])
                 (sl/get-scen-ids))
            ]
           [:input {:type "submit" :value "Load Scenario"}]
           ]
          ]
         ]
        ))

(defn print-crisis-page
  "Prints the page for selecting view options for a crisis"
  [s_id]
  (html [:html
         [:head
          [:title "Select your view"]
          ]
         [:body
          (sprint/html-print-scenario (sl/load-scen-from-db s_id))

          ]
         ]
        ))
