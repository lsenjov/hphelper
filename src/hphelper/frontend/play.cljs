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
      (reset! game-atom m)
      )
    )
  )
(defn get-updates
  "Requests and loads updates into the game atom"
  []
  (ajax/GET (wrap-context (str "/api/" (:userlevel @play-atom) "/updates/"))
            {:response-format (ajax/json-response-format {:keywords? true})
             :handler (fn [m]
                        (load-updates m)
                        )
             :params (assoc @play-atom :lastUpdated 0) ;; Move :lastUpdated somewhere
             }
            )
  )

(defn news-component
  "Component for displaying news items"
  []
  (let [expand-atom (atom false)]
    (fn []
      [:div {:class "panel panel-info"}
       [:div {:class "panel-heading"
              :onClick #(swap! expand-atom not)}
        "News"
        ]
       (if @expand-atom
         [:div {:class "panel-body"}
          (doall (map (fn [cbi] ^{:key cbi} [:div cbi])
                      (:news @game-atom)
                      )
                 )
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
      [:div {:class "panel panel-info"}
       [:div {:class "panel-heading"
              :onClick #(swap! expand-atom not)}
        "Cbay"
        ]
       (if @expand-atom
         [:div {:class "panel-body"}
          (doall (map (fn [cbi] ^{:key cbi} [:div cbi])
                      (:cbay @game-atom)
                      )
                 )
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
(defn access-component
  "Component for displaying cbay items (and later bidding on them)" ;; TODO cbay bids
  []
  (let [expand-atom (atom false)]
    (fn []
      [:div {:class "panel panel-info"}
       [:div {:class "panel-heading"
              :onClick #(swap! expand-atom not)}
        (map-to-str (first (:access @game-atom)))
        ]
       (if @expand-atom
         [:div {:class "panel-body"}
          (doall (map (fn [am] ^{:key am} [:div (map-to-str am)])
                      (take 10 (rest (:access @game-atom)))
                      )
                 )
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
      [:div {:class "panel panel-info"}
       [:div {:class "panel-heading"
              :onClick #(swap! expand-atom not)}
        (map-to-str (first (:indicies @game-atom)))
        ]
       (if @expand-atom
         [:div {:class "panel-body"}
          (doall (map (fn [am] ^{:key am} [:div (map-to-str am)])
                      (take 10 (rest (:indicies @game-atom)))
                      )
                 )
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
  [{:keys [sg_id sg_name sg_abbr minions owner] :as sg}]
  (let [expand-atom (atom false)]
    (fn []
      [:div {:class (if (= owner (get-in @game-atom [:character :name]))
                      "panel panel-success"
                      "panel panel-info"
                      )
             }
       [:div {:class "panel-heading"
              :onClick #(swap! expand-atom not)}
        (str sg_name ". Owner: " owner)
        ]
       (if (and @expand-atom (not (= 0 (count minions))))
         [:table {:class "table table_striped"}
          [:thead
           [:tr [:th "Name"] [:th "Clearance"] [:th "Cost"] [:th "Skills"]]
           ]
          [:tbody
           (doall (map display-single-minion minions))
           ]
          ]
         )
       ]
      )
    )
  )
(defn service-group-component
  "Component for displaying cbay items (and later bidding on them)"
  []
  (let [expand-atom (atom false)]
    (fn []
      [:div {:class "panel panel-info"}
       [:div {:class "panel-heading"
              :onClick #(swap! expand-atom not)}
        "Service Groups"
        ]
       (if @expand-atom
         [:div {:class "panel-body"}
          (map (fn [sg] [single-service-group-component sg])
               (sort-by :sg_id (:serviceGroups @game-atom))
               )
          ]
         )
       ]
      )
    )
  )

(defn game-component
  "Component for displaying and playing a game"
  [^Atom g]
  [:table
   [:tbody
    [:tr
     [:td {:style {:width "50%"}}
      [access-component]
      [indicies-component]
      [cbay-component]
      [news-component]
      ]
     [:td {:style {:width "50%"}}
      [service-group-component]
      ]
     ]
    ]
   "Game goes here for zone " (:zone @g)
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
