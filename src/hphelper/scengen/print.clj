(ns hphelper.scengen.print
  (:require [hiccup.core :refer :all]
            [clojure.tools.logging :as log]
            [hphelper.shared.sql :as sql]
            [hphelper.chargen.generator :as cgen]
            ;; These are just here for debugging, should not be referenced anywhere else
            [hphelper.scengen.generator]
            [hphelper.scengen.scenform]
            )
  (:gen-class)
  )

(defn- html-print-crisis
  "Prints a single crisis in html format"
  [crisis]
  [:div
   [:b (crisis :c_desc)]
   [:br]
   (interpose (html [:br]) (crisis :extraDesc))
   [:br]
   ])

(defn- html-print-crisises
  "Prints all the crisises in html"
  [scenRec]
  (html [:div
         [:b "Welcome GM. Crisis numbers are: " (interpose ", " (map :c_id (scenRec :crisises))) [:br]]
         (map html-print-crisis (scenRec :crisises))
         ]))

(defn- html-print-directive-summary
  "Prints all the directives for the GM to view"
  [scenRec]
  (assert (scenRec :directives) "Directives do not exist?!")
  (println "Directives: " (scenRec :directives))
  (html [:div
         [:b "Printing Directives:"]
         (interpose " -- "
                    (map
                      (fn [direc]
                        (assert direc "Direc is nil?!")
                        (assert (and (direc :sg_id) (direc :sgm_text)))
                        [:span [:b (sql/get-sg-by-id (direc :sg_id)) ": "] (direc :sgm_text)])
                      (scenRec :directives)))
         ]))

(defn- html-print-single-society-mission
  "Prints a single society mission in a readable format"
  [mission]
  (html [:span [:b (sql/get-ss-by-id (mission :ss_id)) ": "]
         (mission :ssm_text)]))

(defn- html-print-ssm-summary
  "Prints all the ssm for the GM to review"
  [scenRec]
  (html [:div
         [:b "Secret Society Missions: "]
         [:small
          (interpose " -- " (map html-print-single-society-mission
                             (scenRec :societies)))
          ]]))

(defn- html-print-player-sheet
  "Prints a single player sheet in a html format"
  [scenRec player]
  (log/trace "Printing player sheet for: " player)
  (html [:div
         (if (player :printSheet)
           [:div (cgen/html-print-sheet player)]
           "")
         [:div [:h3 "Welcome " (player :name)]]
         ;; TODO Indicies
         ;; TODO News
         [:div [:b "Message summary follows:"]]
         [:div (map html-print-single-society-mission
                    (filter (fn [mission] 
                              (some #{(mission :ss_id)}
                                    (map :ss_id
                                         (get player "Program Group"))))
                            (scenRec :societies)))]
         ]
        ))

(defn- html-print-player-sheets
  "Prints all the player's sheets, and possibly their character sheets, in a html format"
  [scenRec]
  (map (comp (partial html-print-player-sheet scenRec) second) (scenRec :hps))
  )

(defn html-print-scenario
  "Prints a scenario in html format"
  [scenRec]
  (html [:div
         ;[:div "DEBUG: " (str scenRec)] ;; Prints out the entire map for debugging
         (html-print-crisises scenRec)
         (html-print-directive-summary scenRec)
         (html-print-ssm-summary scenRec)
         (html-print-player-sheets scenRec)
         ]
        ))
