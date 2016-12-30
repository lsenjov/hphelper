(ns hphelper.frontend.play
  (:require [taoensso.timbre :as log]
            [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET POST] :as ajax]
            [hphelper.frontend.shared :refer [wrap-context add-button-size] :as shared]
            ))

; Atom containing login information and game status, but not game state
(defonce ^:private play-atom
  (atom {:gameUuid nil
         :userUuid nil
         :playing false
         ;; userLevel can be public, player, or admin
         :userlevel "public"})
  )

; Atom containing game state information
(defonce ^:private game-atom
  (atom {})
  )

(defn load-updates
  "Loads a map of updates into the game atom"
  [m]
  ;; TODO currently just replaces the game atom with the map
  (if (= "error" (:status m))
    (do
      (swap! play-atom assoc :error (:message m))
      (log/info "Could not load updates. Error:" m)
      )
    (do
      (swap! play-atom dissoc :error)
      (swap! play-atom assoc :playing true)
      (swap! game-atom merge m)
      )
    )
  )
(defn get-updates
  "Requests and loads updates into the game atom"
  []
  (ajax/GET (wrap-context (str "/api/" (:userlevel @play-atom) "/updates/"))
            {:response-format (ajax/json-response-format {:keywords? true})
             :handler (fn [m]
                        (log/info "Get update:" m)
                        (load-updates m)
                        )
             :params (assoc @play-atom :lastUpdated (if-let [t (:updated @game-atom)] t 0)) ;; Move :lastUpdated somewhere
             }
            )
  )
(defn loop-updates
  "Continuously calls get-updates while :playing is true"
  []
  (log/info "loop-updates sending call.")
  (get-updates)
  ;(Thread/sleep 5000) ;; TODO Figure out how to make a thread sleep
  (if (:playing @play-atom)
    (recur)
    nil
    )
  )

(defn news-component
  "Component for displaying news items"
  []
  (let [expand-atom (atom false)]
    (fn []
      [:div {:class "panel-info"}
       [:div {:class "panel-heading"
              :onClick #(swap! expand-atom not)}
        "News"
        ]
       (if @expand-atom
         [:div {:class ""}
          [:table {:class "table-striped table-hover"}
           [:tbody
            (doall (map (fn [cbi] ^{:key cbi} [:tr>td cbi])
                        (:news @game-atom)
                        )
                   )
            ]
           ]
          ]
         )
       ]
      )
    )
  )
(defn cbay-component
  "Component for displaying cbay items (and later bidding on them)" ;; TODO cbay bids
  []
  (let [expand-atom (atom false)]
    (fn []
      [:div {:class "panel-info"}
       [:div {:class "panel-heading"
              :onClick #(swap! expand-atom not)}
        "Cbay"
        ]
       (if @expand-atom
         [:div {:class ""}
          [:table {:class "table-striped table-hover"}
           [:tbody
            (doall (map (fn [cbi] ^{:key cbi} [:tr>td cbi])
                        (:cbay @game-atom)
                        )
                   )
            ]
           ]
          ]
         )
       ]
      )
    )
  )
(defn- map-to-str
  "Changes an access map into a single string"
  [am]
  (->> am
    (map (fn [[k v]] (str (name k) " " v)))
    sort
    (interpose ", ")
    (apply str)
    )
  )
(defn calculate-diffs
  "Given 2 maps, returns a map with the numerical differences between both, (- first second)"
  [[f s]]
  (apply merge {} (map (fn [[k v]] {k (- (get f k) v)}) s))
  )
(defn access-component
  "Component for displaying cbay items (and later bidding on them)" ;; TODO cbay bids
  []
  (let [expand-atom (atom false)]
    (fn []
      [:div {:class "panel-info"}
       [:div {:class "panel-heading"
              :onClick #(swap! expand-atom not)}
        (map-to-str (first (:access @game-atom)))
        ]
       (if @expand-atom
         [:div {:class ""}
          [:table {:class "table table-striped table-hover"}
           [:thead>tr
            (doall (map (fn [[k _]] [:th (name k)]) (->> @game-atom :access first)))
            ]
           [:tbody
            (->> @game-atom
                 :access
                 (take 10)
                 (partition 2 1)
                 (map calculate-diffs)
                 (sort-by first)
                 (map (fn [im]
                        [:tr (doall
                               (map
                                 (fn [[_ v]] [:td (if (= 0 v) "" v)])
                                 im
                                 )
                               )
                         ]
                        )
                      )
                 )
            ]
           ]
          ]
         )
       ]
      )
    )
  )
(defn indicies-component
  "Component for displaying cbay items (and later bidding on them)" ;; TODO cbay bids
  []
  (let [expand-atom (atom false)]
    (fn []
      [:div {:class "panel-info"}
       [:div {:class "panel-heading"
              :onClick #(swap! expand-atom not)}
        (map-to-str (first (:indicies @game-atom)))
        ]
       (if @expand-atom
         [:div {:class ""}
          [:table {:class "table table-striped table-hover"}
           [:thead>tr
            (doall (map (fn [[k _]] [:th (name k)]) (->> @game-atom :indicies first sort)))
            ]
           [:tbody
            (->> @game-atom
                 :indicies
                 (take 10)
                 (partition 2 1)
                 (map calculate-diffs)
                 (map (partial sort-by first))
                 (map (fn [im]
                        [:tr (doall
                               (map
                                 (fn [[_ v]] [:td (if (= 0 v) "" v)])
                                 im
                                 )
                               )
                         ]
                        )
                      )
                 )
            ]
           ]
          ]
         )
       ]
      )
    )
  )
(defn display-single-society-mission
  [{:keys [ssm_id ss_id c_id ssm_text ss_name]}]
  ^{:key ssm_id} [:tr [:td ss_name] [:td ssm_text]]
  )
(defn society-missions-component
  "Component for displaying secret society missions (and later marking them as done)" ;; TODO cbay bids
  []
  (let [expand-atom (atom false)]
    (fn []
      [:div {:class "panel-info"}
       [:div {:class "panel-heading"
              :onClick #(swap! expand-atom not)}
        "Secret Society Missions"
        ]
       (if @expand-atom
         [:div {:class ""}
          [:table {:class "table-striped table-hover"}
           [:tbody
            (doall (map display-single-society-mission
                        (sort-by :ss_name (:missions @game-atom))))
            ]
           ]
          ]
         )
       ]
      )
    )
  )
(defn display-single-minion
  "Displays a single minion"
  [{:keys [minion_id minion_name minion_clearance minion_cost mskills] :as minion}]
  ^{:key minion_id} [:tr [:td minion_name] [:td minion_clearance] [:td minion_cost] [:td mskills]]
  )
(defn single-service-group-component
  "Displays a single service group"
  [{service-group-id :sg_id}]
  (let [expand-atom (atom false)]
    (fn []
      (let [{:keys [sg_id sg_name sg_abbr minions owner] :as sg}
            (some #(if (= service-group-id (:sg_id %)) % nil)
                  (:serviceGroups @game-atom))
            ]
        [:div {:class (if (= owner (get-in @game-atom [:character :name]))
                        "panel-success"
                        "panel-info"
                        )
               }
         [:div {:class "panel-heading"
                :onClick #(swap! expand-atom not)}
          (str sg_name ". Owner: " owner)
          ]
         (if (and @expand-atom (not (= 0 (count minions))))
           [:table {:class "table-striped table-hover"}
            [:thead
             [:tr [:th "Name"] [:th "Clearance"] [:th "Cost"] [:th "Skills"]]
             ]
            [:tbody
             (doall (map display-single-minion
                         (->> minions
                              (sort-by :minion_name)
                              (sort-by :minion_cost >)
                              (sort-by (comp {"IR" 8 "R" 7 "O" 6 "Y" 5 "G" 4 "B" 3 "I" 2 "V" 1} :minion_clearance))
                              )
                         )
                    )
             ]
            ]
           )
         ]
        )
      )
    )
  )
(defn service-group-component
  "Component for displaying service group minions (And later purchasing them)" ;; TODO purchasing
  []
  [:div
   (map (fn [sg] [single-service-group-component sg])
        (sort-by :sg_id (:serviceGroups @game-atom))
        )
   ]
  )
(defn get-sg-name
  "Gets the name of a service group by service group id"
  [id]
  (->> @game-atom
       :serviceGroups
       (some (fn [{:keys [sg_id sg_name]}] (if (= id sg_id) sg_name nil)))
       )
  )
(defn display-single-directive
  [{:keys [sgm_id sgm_text sg_id c_id]}]
  ^{:key sgm_id} [:tr [:td (get-sg-name sg_id)] [:td sgm_text]]
  )
(defn directives-component
  "Component for displaying directives (and later marking them as done)" ;; TODO marking as done
  []
  (let [expand-atom (atom false)]
    (fn []
      [:div {:class "panel-info"}
       [:div {:class "panel-heading"
              :onClick #(swap! expand-atom not)}
        "Directives"
        ]
       (if @expand-atom
         [:div {:class ""}
          [:table {:class "table-striped table-hover"}
           [:tbody
            (doall (map display-single-directive
                        (sort-by :sg_id (:directives @game-atom))))
            ]
           ]
          ]
         )
       ]
      )
    )
  )
(defn missions-component
  "Component for displaying both secret society missions and directives"
  []
  (let [expand-atom (atom false)]
    (fn []
      [:div {:class "panel-info"}
       [:div {:class "panel-heading"
              :onClick #(swap! expand-atom not)}
        "Directive and Society Missions"
        ]
       (if @expand-atom
         [:div {:class ""}
          [society-missions-component]
          [directives-component]
          ]
         )
       ]
      )
    )
  )
(defn keywords-component
  "Displays all the keywords for the mission"
  []
  (let [expand-atom (atom false)]
    (fn []
      [:div {:class "panel-info"}
       [:div {:class "panel-heading"
              :onClick #(swap! expand-atom not)}
        "Keywords"
        ]
       (if @expand-atom
         [:div {:class ""}
          (->> @game-atom
               :keywords
               (interpose ", ")
               )
          ]
         )
       ]
      )
    )
  )
(defn program-group-component
  "Displays a single service group"
  []
  (let [expand-atom (atom false)]
    (fn []
      [:div {:class "panel-info"}
       [:div {:class "panel-heading"
              :onClick #(swap! expand-atom not)}
        "Program Group"
        ]
       (if @expand-atom
         [:div
          "Reminder: These are treasonous secret societies! Always call using a cover identity!"
          [:table {:class "table-striped table-hover"}
           [:thead
            [:tr [:th "Name"] [:th "Skills"]]
            ]
           [:tbody
            (doall (map
                     (fn [{:keys [ss_id ss_name sskills] :as ss}]
                       ^{:key ss_id}
                       [:tr [:td ss_name] [:td sskills]]
                       )
                     (->> @game-atom :character :programGroup)
                     )
                   )
            ]
           ]
          ]
         )
       ]
      )
    )
  )

(defn character-component
  "Component for displaying a character sheet"
  []
  (let [expand-atom (atom false)]
    (fn []
      [:div {:class "panel-info"}
       [:div {:class "panel-heading"
              :onClick #(swap! expand-atom not)}
        (get-in @game-atom [:character :name]) "'s character sheet."
        ]
       (if @expand-atom
         [:div {:class "panel-body"}
          [:div {:class "panel-default"}
           [:div {:class "panel-heading"}
            "Statistics"
            ]
           [:div
            (doall (map (fn [[stat value]] ^{:key stat} [:div stat ": " value])
                        (sort-by key (get-in @game-atom [:character :priStats]))))
            ]
           ]
          [:div {:class "panel-default"}
           [:div {:class "panel-heading"}
            "Mutation"
            ]
           [:div
            "Mutation Strength: "
            (get-in @game-atom [:character :mutation :power])
            ]
           [:div ;; TODO Multiple mutations?
            "Mutations: "
            (get-in @game-atom [:character :mutation :description])
            ]
           ]
          ]
         )
       ]
      )
    )
  )

(defn game-component
  "Component for displaying and playing a game"
  [^Atom g]
  [:table {:class "table-striped"}
   [:thead
    [:tr
     [:th>h5 "Welcome High Programmer " (get-in @game-atom [:character :name]) " to zone " (:zone @game-atom)]
     ]
    ]
   [:tbody
    [:tr
     [:td {:style {:width "50%"}}
      [access-component]
      ;[missions-component] ;; Removed due to figuring out how to remove the borders easily
      [directives-component]
      [society-missions-component]
      [indicies-component]
      [cbay-component]
      [news-component]
      [keywords-component]
      [character-component]
      ]
     [:td {:style {:width "50%"}}
      [program-group-component]
      [service-group-component]
      ]
     ]
    [:tr>td
     [:div {:class "btn btn-warning"
            :onClick get-updates
            }
      "Update"
      ]
     ]
    ]
   ]
  )
(defn play-component
  "Top-end component for playing the game"
  []
  [:div
  (if (not (:playing @play-atom))
    ;; Not currently in a game, request details
    [:div
     "Game Uuid:"
     (shared/text-input play-atom [:gameUuid])
     "Player Uuid: (If spectating, leave blank)"
     (shared/text-input play-atom [:userUuid])
     [:div {:class (add-button-size "btn btn-default")
            :onClick (fn []
                       (swap! play-atom assoc :userlevel "public")
                       (get-updates)
                       )
            }
      "Spectate"
      ]
     [:div {:class (add-button-size "btn btn-default")
            :onClick (fn []
                       (swap! play-atom assoc :userlevel "player")
                       (get-updates)
                       )
            }
      "Play as Player"
      ]
     [:div {:class (add-button-size "btn btn-default")
            :onClick (fn []
                       (swap! play-atom assoc :userlevel "admin")
                       (get-updates)
                       )
            }
      "Play as Admin"
      ]
     ]
    ;; Game loaded, time to start displaying things
    [game-component game-atom]
    )
  [:div {:class (add-button-size "btn btn-danger")
         :onClick #(do (swap! play-atom assoc :playing false)
                       (reset! game-atom {})
                       )
         }
   "Leave game"
   ]
  ;; Debug
  [:br] "Play Atom:" (prn-str @play-atom)
  [:br] "Game keys:" (prn-str (keys @game-atom))
  [:br] "Game State:" (prn-str @game-atom)
  ]
  )
