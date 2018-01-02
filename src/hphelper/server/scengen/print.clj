(ns hphelper.server.scengen.print
  (:require [hiccup.core :refer :all]
            [taoensso.timbre :as log]
            [hphelper.server.shared.sql :as sql]
            [hphelper.server.chargen.print :as cprint]
            [hphelper.server.shared.indicies :refer :all]
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
         (map html-print-crisis (scenRec :crisises))
         ]))

(defn- html-print-directive-summary
  "Prints all the directives for the GM to view"
  [scenRec]
  (assert (scenRec :directives) "Directives do not exist?!")
  (html [:div
         [:b "Printing Directives:"]
         [:small
         (interpose " -- "
                    (map
                      (fn [direc]
                        (assert direc "Direc is nil?!")
                        (assert (and (direc :sg_id) (direc :sgm_text)))
                        [:span [:b (sql/get-sg-by-id (direc :sg_id)) ": "] (direc :sgm_text)])
                      (scenRec :directives)))
         ]]))

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

(defn- html-print-cbay
  "Prints cbay in html format"
  [{cbay :cbay :as scenRec}]
  [:div
   [:b "C-Bay auctions" [:br]]
   (interpose [:br] cbay)
   ]
  )

(defn- html-print-additional-messages
  "Prints additional messages, one each line"
  [{msgs :msgs :as pRec}]
  (if msgs
    [:div
     [:b "Additional Messages" [:br]]
     (map (fn [line] [:span line [:br]])
          msgs)
     ]
    ""
    ))

(defn html-print-player-sheet
  "Prints a single player sheet in a html format"
  [scenRec player]
  (log/trace "Printing player sheet for: " player)
  (html [:div
         (if (player :printSheet)
           [:div (cprint/html-print-sheet player)]
           "")
         [:div {:style "page-break-before: always;"} [:h3 "Welcome " (player :name)]]
         [:div (html-print-indicies (:indicies scenRec))]
         [:div [:b "Sector News:"] [:br]
          (interpose [:br] (scenRec :news))]
         [:div [:b "Message summary follows:"]]
         [:div (interpose [:br]
                          (distinct
                            (map html-print-single-society-mission
                                 (filter (fn [mission]
                                           (some #{(mission :ss_id)}
                                                 (map :ss_id
                                                      (or (:programGroup player) (get player "Program Group")))))
                                         (scenRec :societies)))))]
         (html-print-additional-messages player)
         (html-print-cbay scenRec)
         ]
        ))

(defn- html-print-player-sheets
  "Prints all the player's sheets, and possibly their character sheets, in a html format"
  [scenRec]
  (map (comp (partial html-print-player-sheet scenRec) second)
       (scenRec :hps))
  )

(defn- html-print-news-summary
  "Prints the news in a small format for the GM"
  [{news :news :as scenRec}]
  [:div [:b "Sector News:" ] [:small (interpose " -- " news)]])

(defn- html-print-single-minion-cell
  "Prints a single minion table cell"
  [minion]
  (assert minion "Minion does not exist?")
  [:td {:style "border: 1px solid black; font-size: small"}
   (:minion_name minion) " -- "
   (:minion_clearance minion) " -- "
   (:minion_cost minion) " -- "
   ;(:mskills (first (sql/query "SELECT mskills FROM minion_skills WHERE minion_id = ?" (:minion_id minion))))
   (:mskills minion)
   ])

(defn- html-print-minion-table
  "Prints a single sg's minion table"
  [{minions :minions :as sg}]
  (html [:div
         [:b (:sg_name sg)] [:br]
         [:table {:style "border: 1px solid black"}
          (map (fn [pair] [:tr (map html-print-single-minion-cell pair)])
               (partition-all 2 minions))
          ]
         ]
        ))

(defn- html-print-minions
  "Prints all sgs' minion tables"
  [{minions :serviceGroups :as scenRec}]
  (interpose [:br]
             (map (fn [pairSgs] [:div {:style "page-break-before: always;"}
                                 (map html-print-minion-table pairSgs)])
                  (partition-all 2 minions))))

(defn- html-print-directive-table
  "Prints the directive list in a table"
  [{directives :directives :as scenRec}]
  [:div {:style "page-break-before: always;"}
   [:table
    (map (fn [pair] [:tr (map (fn [single] [:td {:style "border: 1px solid black"}
                                             [:span [:b (sql/get-sg-by-id (single :sg_id)) ": "]
                                              (single :sgm_text)]])
                               pair)
                      ])
          (partition-all 1 (sort-by :sg_id directives)))]])

(defn- html-print-header
  "Prints a short summary at the beginning"
  [{zone :zone :as scenRec}]
  [:div
   [:b "Welcome GM. Zone is: " zone ". "
    "Crisis numbers are: " (interpose ", " (map :c_id (scenRec :crisises))) [:br]]
   ])

(defn html-print-optional
  "Prints selected parts of a scenario.
  Optional prints are:
  :gmheader GM's Header
  :gmindicies Indicies for the GM
  :gmcrisises Crisis data for GM
  :gmcbay Cbay data for GM
  :gmdirectives Directive data for GM
  :gmnews News data for GM
  :gmsocieties Societies summary for GM
  :player_n prints that specific player
  :players prints all players
  :minions prints minions for all service groups
  :directives prints directives for all service groups"
  [scenRec options]
  (log/trace (prn-str scenRec)) ;DEBUG
  (html [:div
         (if (some #{:gmheader} options) (html-print-header scenRec))
         (if (some #{:gmindicies} options) (html-print-indicies (:indicies scenRec)))
         (if (some #{:gmcrisises} options) (html-print-crisises scenRec))
         (if (some #{:gmcbay} options) (html-print-cbay scenRec))
         (if (some #{:gmdirectives} options) (html-print-directive-summary scenRec))
         (if (some #{:gmnews} options) (html-print-news-summary scenRec))
         (if (some #{:gmsocieties} options) (html-print-ssm-summary scenRec))
         (if (some #{:players} options) (html-print-player-sheets scenRec))
         (dotimes [p 6] (if (some #{(keyword (str "player_" p))} options)
                          (html-print-player-sheet (get (scenRec :hps) p))))
         (if (some #{:minions} options) (html-print-minions scenRec))
         (if (some #{:directives} options) (html-print-directive-table scenRec))
         ]
        ))

(defn html-print-scenario
  "Prints a scenario in html format"
  [scenRec]
  (html-print-optional scenRec
                       :gmheader :gmindicies :gmcrisises :gmcbay
                       :gmdirectives :gmnews :players :minions :directives)
        )
