(ns hphelper.server.live.view
  (:require [hphelper.server.live.control :as lcon]
            [hphelper.server.live.api :as lapi]
            [hphelper.server.shared.sql :as sql]
            [hphelper.server.shared.saveload :as sl]
            [hphelper.server.shared.unique :as uni]
            [hphelper.server.shared.indicies :as indicies]
            ;; For indicies
            [hphelper.server.scengen.generator :as sgen]
            [taoensso.timbre :as log]

            [hiccup.core :refer :all]

            [hphelper.server.scengen.print :as sprint]
            [hphelper.server.chargen.print :as cprint]
            [hphelper.server.shared.helpers :as helpers]
            )
  (:gen-class)
  )

(defn- html-print-indicies-table
  "Prints indicies in a table in n columns"
  [inds n]
  [:table
   (map (fn [r] [:tr r])
        (partition-all
          n
          (map (fn [x] [:td x])
               (indicies/html-print-indicies inds))))
   ]
  )

(defn view-game
  "Prints a game view nicely for players"
  [baseURL uid]
  (log/trace "view-game. baseURL:" baseURL "uid:" uid)
  (if-let [game (lcon/get-game uid)]
    (html [:html
           [:head
            [:title "Sector indicies"]
            [:meta {:http-equiv "refresh" :content 5}]
            ]
           [:body
            [:div
             (html-print-indicies-table (first (:indicies game)) 13)
             ]
            [:div
             (map (fn [n] [:div n])
                  (:news game))
             ]
            ]
           ])
    (html [:html [:div "Game not found"]])
    ))

(defn- print-service-group
  "Prints a single service group in a html format"
  [g]
  (html [:div {:class "data"}
         [:h3 (str (:sg_name g) ": " (:owner g))]
         (map (fn [r] [:div (str (:minion_name r)
                                 " -- " (:minion_clearance r)
                                 (if (:minion_cost r)
                                   (str " -- " (:minion_cost r))
                                   "")
                                 (if (:mskills r)
                                   (str " -- " (:mskills r))
                                   "")
                                 )])
              (:minions g)
              )
         ]
        )
  )


(defn view-game-player
  "Prints a game view nicely for a *single* player. Includes live view, session sheet, and player sheets. Reads from a livescenario, not a normal scenario"
  [baseURL guid uuid]
  (log/trace "view-game-player." baseURL guid uuid)
  (if-let [g (lcon/get-game guid)]
    (let [u (get-in g [:hps uuid])]
      (log/trace "view-game-player. HP name is:" (get u :name))
      (html [:html
             [:head
              [:title "Sector View"]
              [:meta {:http-equiv "refresh" :content 5}]
              [:link {:rel "stylesheet" :href (str baseURL "/css/style.css")}]
              [:style ".data {background-color: #270140; padding: 10px; border-right: 10px solid black; border-bottom: 5px solid black;}"]
              ]
             [:body
              [:header
               [:h2 "Sector " (:zone g)". " "High Programmer: " (:name u) [:br]
                "Sector Indicies: " (indicies/html-print-indicies (first (:indicies g))) [:br]
                (if (second (:indicies g))
                  [:div "Last Shift:"
                   (->> g
                        (:indicies)
                        (second)
                        (map (fn [[k v]] (fn [m] (update-in m [k] - v))))
                        (apply comp)
                        (#(% (first (:indicies g))))
                        (remove (fn [[k v]] (= 0 v)))
                        indicies/html-print-indicies
                        )
                   ]
                  )
                ]
               ]
              [:table
               [:tr {:style "vertical-align: top;"}
                [:td ;; Left column Begin
                 ;; Society messages
                 (if-let [missions (:missions (lapi/get-player-society-missions guid uuid))]
                   (do
                     [:div {:class "data"}
                      [:h3 "Private message summary (secret society missions)"]
                      (map (fn [{ss_id :ss_id text :ssm_text}]
                             [:div (str (sql/get-ss-by-id ss_id) ": " text)])
                           (sort-by :ss_id missions))
                      ]
                     )
                   )
                 ;; Cbay Auctions
                 [:div {:class "data"}
                  [:h3 "Upcoming cbay auctions"]
                  (map (fn [^String i] [:div i]) (:cbay g))
                  ]
                 ;; Keywords
                 [:div {:class "data"}
                  [:h3 "Keywords"]
                  (interpose ", " (:keywords g))
                  ]
                 ;; Sector news
                 [:div {:class "data"}
                  [:h3 "Sector News"]
                  (map (fn [n] [:div n])
                       (:news g))
                  ]
                 ;; Extra Messages
                 (if-let [messages (:msgs u)]
                   [:div {:class "data"}
                    [:h3 "Additional Messages"]
                    (map (fn [^String msg] [:div msg]) messages)
                    ]
                   )
                 ;; Player Sheet
                 (if u
                   [:div {:class "data"}
                    [:h3 "Character Sheet"]
                    (cprint/html-print-sheet u)]
                   nil
                   )
                 ]
                [:td ;; Right column Begin
                 ;; Directives
                 [:div {:class "data"}
                  [:h3 "Directives"]
                  (let [sgids (set (map :sg_id (filter #(= (:name u) (:owner %)) (:serviceGroups g)))) ]
                    (->> (:directives g)
                         (filter #(sgids (:sg_id %)))
                         (map (fn [{text :sgm_text sgid :sg_id}]
                                (html [:div
                                       (get-in g [:serviceGroups (helpers/get-sg-abbr g (str sgid)) :sg_abbr])
                                       ": "
                                       text
                                       [:br]
                                       ]
                                      )
                                )
                              )
                         sort
                         )
                    )
                  ;(:directives g)
                  ]
                 ;; Minions
                 (map print-service-group (:serviceGroups (lapi/get-minions guid uuid)))
                 ]
                ]
               ]
              ]
             ]
            )
      )
    )
  )

(defn- player-key
  "Returns a string with a game's uid and the player's uid for logging in"
  [baseURL uid player]
  (str (:name player)
       " Game uid: " uid
       " Character uid: " (:password player)
       " Character link: " (html [:a {:href (str baseURL "/index.html?guid=" uid "&uuid=" (:password player))} (:name player)])
       )
  )

(defn player-keys
  "Returns a string with all the player's names and their login uids. Nil if incorrect game"
  [baseURL ^String uid]
  (log/trace "player-keys. uid:" uid "hps:" (:hps (lcon/get-game uid)))
  (if-let [g (lcon/get-game uid)]
    (map (fn [[k pMap]] (player-key baseURL uid pMap)) (:hps g))
    nil))


(defn new-game
  "Creates a new live game by loading a completed scenario. Gives links to player view and GM view"
  [baseURL scenId]
  (if (string? scenId)
    (new-game baseURL (try (Integer/parseInt scenId)
                           (catch Exception e
                             (do (log/trace "new-game. Could not parse:" scenId)
                                 "Invalid scenId"))))
    (let [uid (lcon/new-game (sl/load-fullscen-from-db scenId))]
      (html [:html
             [:body
              [:a {:href (str baseURL "/live/view/" uid "/")} "Player Link"] [:br]
              [:a {:href (str baseURL "/live/view/"
                              uid "/"
                              (str (hash uid)) "/")} "GM Link"]
              [:div "Game uid:"]
              [:div uid]
              (map (fn [x] [:div x]) (player-keys baseURL uid))
              [:br]
              [:div "Admin pass: " (:adminPass (lcon/get-game uid))]
              [:a {:href (str baseURL "/index.html?guid=" uid "&uuid=" (:adminPass (lcon/get-game uid)))} "Open admin game"]
              ]
             ]
            ))))

(defn- create-self-pointing-button
  "Creates a button designed to point to edit-game"
  ([url amount index]
   [:td [:form
         {:action
          (str url
               index "/"
               amount "/")
          :method "get"}
         [:input {:type "submit" :value amount}]
         ]]))

(defn- create-self-pointing-link
  "Creates a link designed to point to edit-game"
  ([url amount index]
   [:td [:a {:href (str url index "/" amount "/")} (str index " " amount)]]))

(defn- create-sorted-indicies-list
  "Creates a list of sorted indicies, sector ones first"
  [inds]
  (let [others (remove (set indicies/sectorIndicies) (keys inds))]
    (concat (sort indicies/sectorIndicies) others)))

(def changeVals "Numbers to give the GM to change sector values by"
  [-10 -8 -5 -3 -1 1 3 5 8 10])

(defn- create-editing-table
  "Creates a html table for modifying sector values"
  [url inds]
  (log/trace "create-editing-table:" url inds)
  (let [ks (map name (create-sorted-indicies-list inds))]
    [:table
     [:tr (map (fn [k] [:td k " " (inds (keyword k))]) ks)]
     (for [r changeVals]
       [:tr (map (partial create-self-pointing-link url r)
                 ks)])
     ]
    )
  )

(defn- single-player-stats
  "Creates a table rown of player stats"
  [{pStats :priStats :as pMap}]
  [:tr
   [:td (:name pMap)]
   [:td (get pStats "Management")]
   [:td (get pStats "Violence")]
   [:td (get pStats "Subterfuge")]
   [:td (get pStats "Hardware")]
   [:td (get pStats "Software")]
   [:td (get pStats "Wetware")]
   [:td (-> pMap :mutation :description)]
   ])

(defn- print-player-stats-table
  "Creates a table of player stats for the GM's pleasure"
  [^String uid]
  [:table {:border 1} [:tr [:td] [:td "M"] [:td "V"] [:td "Su"] [:td "H"] [:td "So"] [:td "W"] [:td "Mutation"]]
   (map single-player-stats (-> (lcon/get-game uid) :hps vals))
   ]
  )

(defn- print-sg-set-table
  "Creates a table of links to set service group ownership"
  [baseURL guid]
  (log/trace "print-sg-set-table." baseURL guid)
  (let [g (lcon/get-game guid)
        aPass (:adminPass g)
        serviceGroups (->> g :serviceGroups (map :sg_abbr))
        hpNames (->> g :hps vals (map :name))]
    (log/trace "aPass:" aPass "serviceGroups:" serviceGroups "hpNames:" (pr-str hpNames))
    [:table
     (for [sg serviceGroups]
       [:tr
        [:td sg]
        (for [hpName hpNames]
          [:td
           [:a {:href (str baseURL "/api/admin/"
                           guid "/"
                           aPass "/set-sg-owner/"
                           sg "/"
                           hpName "/")
                }
            hpName
            ]
           ]
          )
        ]
       )
     ]
    )
  )

(defn edit-game
  "Prints a view for the GM to edit a game, also performs actions"
  ([baseURL uid confirm]
   (if (= confirm (str (hash uid)))
     (html
       [:html
        [:div
         [:body
          (create-editing-table
            (str baseURL "/live/view/"
                 uid "/"
                 confirm "/")
            (first (:indicies (lcon/get-game uid))))
          [:div [:a {:href (str baseURL "/api/admin/" uid "/" (:adminPass (lcon/get-game uid)) "/debug/")} "Debug Result"]]
          [:div [:h3 "Player Stats"] (print-player-stats-table uid)]
          [:div [:h3 "Set Player SG"] (print-sg-set-table baseURL uid)]
          ]]])
     "Incorrect confirmation"
     ))
  ([baseURL uid confirm index amount]
   (if (= confirm (str (hash uid)))
     (do (lcon/modify-index uid (keyword index) amount)
         (edit-game baseURL uid confirm))
     "Incorrect confirmation"))
  )
