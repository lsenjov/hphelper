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

; Atom containing keywords purely for the ticker
(defonce ^:private ticker-keyword-atom
  (atom #{}))

;; Helper Functions
(defn- map-to-str
  "Changes an access map into a single string"
  [am]
  (->> am
    (map (fn [[k v]] (str (name k) " " (-> v (* 100) int (/ 100))
                          )))
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
(defn- ^Map calculate-last-access
  "Returns a string of the last access change"
  []
  (->> @game-atom
       :access
       (take 2)
       calculate-diffs
       (filter (fn [[_ v]] (not (= 0 v))))
       )
  )

;; Getting updates from server and loading into state
(defn load-updates
  "Loads a map of updates into the game atom"
  [m]
  (if (not (:updated m))
    (do
      (swap! play-atom assoc :error (:message m))
      (log/info "Could not load updates. Error:" (:message m))
      )
    (do
      ;; If we're good, dissoc the error message
      (swap! play-atom dissoc :error)
      ;; Set playing to true, if we're not already
      (swap! play-atom assoc :playing true)
      ;; Merge any updated keys
      (swap! game-atom merge
             ;; The :hps and :character keys both now arrive as edn strings, need to be converted
             (cond
               (:hps m)
               (update-in m [:hps] #(if % (cljs.reader/read-string %) %))
               (:character m)
               (update-in m [:character] #(if % (cljs.reader/read-string %) %))
               :else
               m
               )
             )
      ;; Add the updated keys to the ticker-keywords-atom
      (swap! ticker-keyword-atom
             #(apply conj
                     %
                     (-> m
                         keys
                         set
                         ;; Remove the following keys from updating
                         (disj :updated :status :serviceGroups :directives :missions))))
      )
    )
  )
(defn get-updates
  "Requests and loads updates into the game atom"
  []
  (ajax/GET (wrap-context (str "/api/" (:userlevel @play-atom) "/updates/"))
            {:response-format (ajax/json-response-format {:keywords? true})
             :handler (fn [m]
                        (log/info "Get update keys:" (keys m))
                        (load-updates m)
                        )
             :params (assoc @play-atom :lastUpdated (if-let [t (:updated @game-atom)] t 0)) ;; Move :lastUpdated somewhere
             }
            )
  )
(defn loop-updates-component
  "Continuously calls get-updates while :playing is true"
  []
  (let [update-atom (atom 0)]
    (fn []
      (log/info "loop-updates sending call.")
      (js/setTimeout #(if (:playing @play-atom) (swap! update-atom inc) nil) 3000)
      @update-atom
      (get-updates)
      nil
      )
    )
  )
(defn get-sg-name
  "Gets the name of a service group by service group id"
  [id]
  (->> @game-atom
       :serviceGroups
       (some (fn [{:keys [sg_id sg_name]}] (if (= id sg_id) sg_name nil)))
       )
  )

;; Begin display components
;; Display news
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
          [:table {:class "table-striped table-hover"
                   :style {:width "100%"}
                   }
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
;; Display cbay
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
          [:table {:class "table-striped table-hover"
                   :style {:width "100%"}
                   }
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
;; Display access
(defn access-component
  "Component for displaying cbay items (and later bidding on them)" ;; TODO cbay bids
  []
  (let [expand-atom (atom false)]
    (fn []
      (let [players (->> @game-atom :access first (map first) (map name) sort)]
        (log/info "players is: " players)
        [:div {:class "panel-info"}
         [:div {:class "panel-heading"
                :onClick #(swap! expand-atom not)}
          "ACCESS: Last Change: "
          (map-to-str (calculate-last-access))
          ]
         (if @expand-atom
           [:div {:class ""}
            [:table {:class "table-striped table-hover"
                     :style {:width "100%"}
                     }
             [:thead>tr
              (doall (map (fn [k] ^{:key k} [:th (take 5 k)]) players))
              ]
             [:tbody
              ;; Line of current totals
              [:tr
               (->> @game-atom
                    :access
                    first
                    (sort-by first)
                    (map (fn [[k v]] ^{:key k} [:td {:class (if (< v 0) "text-danger" "")} (-> v (* 100) int (/ 100))]))
                    )
               ]
              ;; If admin, add buttons for changing access. Will change values by the amount listed
              (if (= (:userlevel @play-atom) "admin")
                (for [change [20 10 5 -1 -2 -3 -4 -5 -6 -7 -8 -9 -10 -20]]
                  [:tr
                   (for [player players]
                     ^{:key player}
                     [:td>div
                      {:class "btn btn-default btn-xs btn-block"
                       :title (str "Change " player " by " change " ACCESS.")
                       :onClick #(ajax/GET (wrap-context "/api/admin/modify-access/")
                                           {:response-format (ajax/json-response-format {:keywords? true})
                                            :handler (fn [m]
                                                       (log/info "Modified access")
                                                       (get-updates)
                                                       )
                                            :params (merge @play-atom {:player (name player)
                                                                       :amount change
                                                                       })})
                       }
                      change
                      ]
                     )])
                nil)
              ;; Lines of changes to reach current total
              (->> @game-atom
                   :access
                   (take 10)
                   (partition 2 1)
                   (map calculate-diffs)
                   (map (fn [im]
                          [:tr (doall
                                 (map
                                   (fn [[_ v]] [:td (if (= 0 v) "" (-> v (* 100) int (/ 100)))])
                                   (sort-by first im)
                                   )
                                 )
                           ]
                          )
                        )
                   )
              ;; Players get buttons to send access to anyone but themselves
              (if (= "player" (:userlevel @play-atom))
                (doall
                  (for [change [1 2 3 4 5 10 20]]
                    [:tr
                     (doall
                       (for [player players]
                         [:td ^{:key player}
                          (cond
                            ;; Don't send to yourself
                            (= player (get-in @game-atom [:character :name]))
                            [:td]
                            ;; Something different for the pool TODO
                            (= player "Pool")
                            [:td {:class "btn btn-success btn-xs btn-block"
                                  :title (str "Take " change " from the public pool. \nDoing this without consensus is heavily treasonous. \nPushing the pool into negatives is heavily treasonous.")
                                  :onClick #(ajax/GET (wrap-context "/api/player/sendaccess/")
                                                      {:response-format (ajax/json-response-format {:keywords? true})
                                                       :handler (fn [m]
                                                                  (log/info "Modified access")
                                                                  (get-updates)
                                                                  )
                                                       :params (merge @play-atom {:playerto (name player)
                                                                                  :amount (- change)
                                                                                  })})}
                             change
                             ]
                            ;; Other players
                            :else
                            [:td
                             {:class "btn btn-default btn-xs btn-block"
                              :title (str "Send " change " access to " player)
                              :onClick #(ajax/GET (wrap-context "/api/player/sendaccess/")
                                                  {:response-format (ajax/json-response-format {:keywords? true})
                                                   :handler (fn [m]
                                                              (log/info "Modified access")
                                                              (get-updates)
                                                              )
                                                   :params (merge @play-atom {:playerto (name player)
                                                                              :amount change
                                                                              })})}
                             change
                             ]
                            )
                          ]
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
  )
;; Display indicies
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
          [:table {:class "table-striped table-hover"
                   :style {:width "100%"}
                   }
           [:thead>tr
            (doall (map (fn [[k _]] [:th (-> k name shared/wrap-any)])
                        (->> @game-atom :indicies first sort)))
            ]
           [:tbody
            ;; Row of current values
            [:tr
             (->> @game-atom
                  :indicies
                  first
                  (sort-by first)
                  (map (fn [[k v]] ^{:key k} [:td v]))
                  doall
                  )
             ]
            ;; If admin, add buttons for changing. Will change values by the amount listed +-up to 3
            (if (= "admin" (:userlevel @play-atom))
              (for [change [-20 -10 -5 0 +5 +10 +20]]
                [:tr
                 (for [ind (->> @game-atom :indicies first keys (map name) doall sort)]
                   [:td>td {:class "btn-warning btn-xs btn-block"
                            :onClick #(ajax/GET
                                        (wrap-context "/api/admin/modify-index/")
                                        {:response-format (ajax/json-response-format {:keywords? true})
                                         :handler (fn [m]
                                                    (log/info "Modified index")
                                                    (get-updates)
                                                    )
                                         :params (merge @play-atom {:ind (name ind)
                                                                    :amount (-> (rand-int 7) (+ -3) (+ change))
                                                                    })
                                         }
                                        )
                            }
                    change
                    ]
                   )
                 ]
                )
              )
            (->> @game-atom
                 :indicies
                 (take 10)
                 (partition 2 1)
                 (map calculate-diffs)
                 (map (partial sort-by first))
                 (map (fn [im] ^{:key im}
                        [:tr (doall
                               (map
                                 (fn [[_ v]] [:td (if (= 0 v) "" v)])
                                 im
                                 )
                               )
                         ]
                        )
                      )
                 doall
                 )
            ]
           ]
          ]
         )
       ]
      )
    )
  )
;; Display society missions
(defn display-single-society-mission
  [{:keys [ssm_id ss_id c_id ssm_text ss_name]}]
  ^{:key ssm_id} [:tr [:td (shared/wrap-any ss_name)] [:td ssm_text]]
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
          [:table {:class "table table-striped table-hover"}
           [:tbody
            (doall (map display-single-society-mission
                        (sort-by :ss_name (:missions @game-atom))))
            (doall (map (fn [i] [:tr [:td] [:td i]])
                        (get-in @game-atom [:character :msgs])
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
;; Display minions
(defn display-single-minion
  "Displays a single minion"
  [{:keys [minion_id minion_name minion_clearance minion_cost mskills bought?] :as minion} sgid]
  ^{:key minion_id}
  [:tr {:class (if bought? "success" "")}
   [:td minion_name]
   [:td minion_clearance]
   [:td minion_cost]
   [:td (shared/wrap-any mskills)]
   (if bought?
     ;; Already bought
     [:td ]
     [:td>span {:class "btn-success"
                :onClick #(ajax/GET (wrap-context "/api/player/purchaseminion/")
                                    {:response-format (ajax/json-response-format {:keywords? true})
                                     :handler (fn [m]
                                                (log/info "Purchased minion")
                                                (get-updates)
                                                )
                                     :params (merge @play-atom {:sgid sgid :minionid minion_id})
                                     }
                                    )
                }
      "buy"
      ]
     )
   ]
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
          (str sg_name ". Owner: " owner ". ")
          (if (not @expand-atom) (str "Active: " (count (filter :bought? minions))) "")
          ]
         (if (and @expand-atom (not (= 0 (count minions))))
           [:div
            [:table {:class "table table-striped table-hover"}
             [:thead
              [:tr [:th "Name"] [:th "Clear"] [:th "Cost"] [:th "Skills"] [:th "Buy"]]
              ]
             [:tbody
              (doall (map display-single-minion
                          (->> minions
                               (filter (fn [{bought? :bought?}]
                                         ;; If filterbought is false, we return true
                                         ;; If filterbought is true, and bought is false, we return false
                                         ;; If filterbought is true, and bought is true, we return true
                                         (if (and (:filterBought? @play-atom) (not bought?)) false true)
                                         )
                                       )
                               (sort-by :minion_name)
                               (sort-by :minion_cost >)
                               (sort-by (comp {"IR" 8 "R" 7 "O" 6 "Y" 5 "G" 4 "B" 3 "I" 2 "V" 1} :minion_clearance))
                               )
                          (repeat service-group-id)
                          )
                     )
              ]
             ]
            ;; If admin, show the switcher
            (if (and (= "admin" (:userlevel @play-atom)) (:showAssignGroups @play-atom))
              [:div {:class "alert alert-warning"}
               "Set Owner:"
               [:div {:class "btn-group"}
                (doall
                  (map (fn [char-name]
                         ^{:key char-name}
                         [:span {:class (add-button-size "btn btn-danger")
                                 :onClick #(ajax/GET (wrap-context "/api/admin/set-sg-owner/")
                                                     {:response-format (ajax/json-response-format {:keywords? true})
                                                      :handler (fn [m]
                                                                 (log/info "Set sg owner")
                                                                 (get-updates)
                                                                 )
                                                      :params (merge @play-atom {:sgid sg_id :newOwner (name char-name)})
                                                      }
                                                     )
                                 }
                          char-name
                          ]
                         )
                       (keys (first (:access @game-atom)))
                       )
                  )
                ]
               ]
              nil
              )
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
   [:div {:class (add-button-size (if (:filterBought? @play-atom) "btn btn-success btn-block" "btn btn-default btn-block"))
          :onClick #(swap! play-atom update-in [:filterBought?] not)}
    "Show bought minions only?"
    ]
   (if (= "admin" (:userlevel @play-atom))
     [:div {:class (add-button-size (if (:showAssignGroups @play-atom) "btn btn-warning btn-block" "btn btn-default btn-block"))
            :onClick #(swap! play-atom update-in [:showAssignGroups] not)}
      "Show assign service group panel?"
      ]
     )
   (doall
     (map (fn [sg] ^{:key sg} [single-service-group-component sg])
          (sort-by :sg_id (:serviceGroups @game-atom))
          )
     )
   ]
  )
;; Display directives
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
          [:table {:class "table table-striped table-hover"}
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
;; Display Keywords
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
;; Display secret society minions
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
          [:table {:class "table-striped table-hover"
                   :style {:width "100%"}
                   }
           [:thead
            [:tr [:th "Name"] [:th "Skills"]]
            ]
           [:tbody
            (doall (map
                     (fn [{:keys [ss_id ss_name sskills] :as ss}]
                       ^{:key ss_id}
                       [:tr
                        [:td (shared/wrap-any ss_name)]
                        [:td (shared/wrap-any sskills)]
                        ]
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
;; Display investments
(defn- investment-table-row
  "Returns a row of an investment table"
  [[k invest-map] sgs]
  (log/info "investment row. k:" k "invest-map:" invest-map "sgs:" sgs)
  ^{:key k}
  [:tr {:class (if (= (get-in @game-atom [:character :name]) (name k)) "text-success" "")}
   [:td k]
   (doall
     (map
       (fn [sg]
         ^{:key sg}
         [:td
          (let [v (get-in invest-map [(-> @game-atom :zone keyword) (keyword sg)])]
            (if (or (not v) (= 0 v))
                  "-"
                  v
                  )
            )
          ]
         )
       sgs
       )
     )
   ]
  )
(defn- create-trade-button
  "Creates a single button for buying or selling"
  ([zone group amount]
   [:div {:class "btn-default btn-xs"
           :onClick
           #(do (ajax/GET (wrap-context "/api/player/trade-investments/")
                          {:response-format (ajax/json-response-format {:keywords? true})
                           :handler (fn [m]
                                      (log/info "Synced Chars")
                                      (get-updates)
                                      )
                           :params (merge @play-atom {:group (name group) :amount amount :zone (name zone)})})
                (get-updates)
                )
           }
    amount
    ]
   )
  ([group amount]
   ;; Current zone
   (create-trade-button (:zone @game-atom) group amount)))
(defn investment-trade-row
  "Creates a row of buy and sell buttons"
  [sgs]
  [:tr
   [:td "Buy/Sell"]
   (->> sgs
        (map (fn [sg]
               ^{:key sg}
               [:td
                ;; Buy button
                (doall (map create-trade-button (repeat sg) [10 5 1 -1 -5 -10]))
                ]
               )
             )
        doall
        )
   ]
  )
(defn investment-component
  "Allows players to buy and sell investments in the sector"
  []
  (let [expand-atom (atom false)]
    (fn []
      [:div {:class "panel-info"}
       [:div {:class "panel-heading"
              :onClick #(swap! expand-atom not)}
        "Investments"
        ]
       (if @expand-atom
         [:div {:class "panel-body"}
          (let [sgs (sort ["AF" "CP" "IS" "PL" "TS" "TD" "PS" "RD" "HP"])]
            (log/info "sgs:" sgs "vestments:" (:investments @game-atom))
            [:table {:class "table-hover table-striped col-lg-12"}
             [:thead
              ;; SG abbreviations
              [:tr
               [:td ] ;; For name
               (map (fn [k] ^{:key k} [:th k]) sgs)
               ]
              ;; Prices
              [:tr
               [:th "Price"]
               (map (fn [sg] ^{:key sg} [:th (-> @game-atom :indicies first (get (keyword sg))
                                                 ;; We have the index, now to convert to price
                                                 (+ 100) (/ 100) (max 0.1)
                                                 ;; We have price, now to reduce to 2 decimal places
                                                 (* 100) int (/ 100))])
                    sgs
                    )
               ]
              ]
             [:tbody
              (investment-trade-row sgs)
              (doall
                (map
                  investment-table-row
                  (:investments @game-atom)
                  (repeat sgs)
                  )
                )
              ]
             ]
            )
          ]
         nil
         )
       ]
      )
    )
  )
;; Display news ticker
(defn- ^String news-read-access
  "Returns a string of a news reader detailing an access change"
  [^Map m]
  (case (count m)
    0 "Access held steady."
    1 (let [[k v] (first m)] (str (name k)
                                  (if (neg? v) " spent " " gained ")
                                  (shared/two-decimals (if (neg? v) (- v) v))
                                  " ACCESS"))
    ;; Used when a player sends cash to another player
    2 (let [[[k1 _] [k2 v2]] (sort-by val m)]
        (str (name k1)
             " sent "
             v2
             " ACCESS to "
             (name k2)))
    (str "Last access changes " (map-to-str m))
    )
  )
(defn- ^String news-read-indicies
  "Returns a string of a news reader detailing an indicies change. Only announces the largest change"
  []
  (->> @game-atom
       :indicies
       (take 2)
       (calculate-diffs)
       ;; Remove empty changes
       (filter (fn [[_ v]] (not (= 0 v))))
       ;; Sort be greatest change
       (sort-by (fn [[_ v]] (if (neg? v) (- v) v)) >)
       ;; Take the 3 greatest changes
       (take 3)
       ;; Turn them into anouncements
       (map (fn [[k v]]
              (str (name k)
                   (if (neg? v) " fell " " rose ")
                   (if (neg? v) (- v) v)
                   " points")))
       ;; Make it prettier
       (interpose ", ")
       (apply str)
       ;; Add the header
       (str "Latest Indicies Change: ")
       )
  )
(defn news-ticker-component
  "Takes an atom of a set of keywords, continuously changes bottom news ticker."
  [^Atom a]
  (let [ticker-atom (atom {:text "" :kw nil})]
    (fn []
      (js/setTimeout
        #(if (:playing @play-atom)
           (let [new-kw (-> @a shuffle first)]
             (log/info "Swapping Ticker. Possible Keys:" (pr-str @ticker-keyword-atom))
             (swap! a disj new-kw)
             (reset! ticker-atom
                     {:kw new-kw
                      :text (case new-kw
                              :access (str "Breaking News: " (news-read-access (calculate-last-access)))
                              :indicies (str "Breaking News: " (news-read-indicies))
                              :hps "Breaking News: IntSec updates ULTRAVIOLET consumer profiles!"
                              :zone (str "You are watching " (:zone @game-atom) " sector news! With your host, Wat-U-KNO!")
                              :keywords "Breaking News: IntSec updates their shortlisted watchwords!"
                              :investments (str "Sector investments increase " (+ 20 (rand-int 50)) "%. Invest in your economy today!")
                              :cbay (str "Cbay currently listing " (count (:cbay @game-atom)) " items above 1MC")
                              ;; Either nil or unrecognised, display a random item
                              ;; Rand-nth picks a random function (without arguments) from the list below and executes it, returning a string to display
                              ((rand-nth
                                 [;; Random news item
                                  (fn [] (rand-nth (:news @game-atom)))
                                  ;; Market update
                                  news-read-indicies
                                  ;; Richest player
                                  (fn [] (->> (:access @game-atom)
                                              first
                                              (remove (fn [[k _]] (or (= "Pool" (name k)) (= "Misc" (name k)))))
                                              (sort-by val >)
                                              first
                                              ((fn [[k v]] (str "Richest ULTRAVIOLET in the crisis room is "
                                                                (name k)
                                                                " with "
                                                                v
                                                                " ACCESS.")))
                                              ))
                                  ;; Poorest player
                                  (fn [] (->> (:access @game-atom)
                                              first
                                              (remove (fn [[k _]] (or (= "Pool" (name k)) (= "Misc" (name k)))))
                                              (sort-by val)
                                              first
                                              ((fn [[k v]] (str "Poorest ULTRAVIOLET in the crisis room is "
                                                                (name k)
                                                                " with "
                                                                v
                                                                " ACCESS.")))
                                              ))
                                  ;; Cbay advertisement
                                  (fn [] (str "Sponsored cbay listing: " (->> @game-atom :cbay rand-nth)))
                                  ;; Random item
                                  (fn [] (rand-nth ["Trust no-one! Stay Alert! Keep your laser handy!"
                                                    "The Computer Protects!"
                                                    "Communism doesn't pay!"
                                                    ]))
                                  ]
                                 ))
                              )
                      }
                     )
             )
           )
        ;; How many milliseconds to wait between refreshing screens
        4000
        )
      [:div {:class (str "navbar navbar-default navbar-fixed-bottom label " (if (:kw @ticker-atom) "label-info" ""))}
       [:div
        [:h5
         (:text @ticker-atom)
         ]
        ]
       ]
      )
    )
  )
;; Display character sheet
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
            (doall (map (fn [[stat value]] ^{:key stat} [:div (shared/wrap-any (name stat)) ": " value])
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
           [:div
            "Mutations: "
            (let [muts (get-in @game-atom [:character :mutation :description])]
              (if (string? muts)
                muts
                [:table {:class "table table-striped table-hover"}
                 [:tbody
                  (doall
                    (map (fn [{m-name :name m-desc :desc}]
                           ^{:key m-name}
                           [:tr [:td m-name] [:td m-desc]])
                         muts
                         )
                    )
                  ]
                 ]
                )
              )
            ]
           ;; Drawbacks
           (if (get-in @game-atom [:character :drawbacks])
             [:div {:class "panel-default"}
              [:div {:class "panel-heading"}
               "Drawbacks"
               ]
              (->> @game-atom
                   :character
                   :drawbacks
                   (map :text)
                   (map (fn [t]
                          [:div t]
                          ))
                  )
              ]
             nil
             )
           ]
          ]
         )
       ]
      )
    )
  )
;; Display admin panel
(defn- create-stat-roller
  "When pressed, rolls a number between 1 and 20 and displays the result in the status atom as a string"
  [^Atom status n ^String player ^String stat]
  (log/info "create-stat-roller. n:" n "player:" player "stat:" stat)
  [:div {:class (add-button-size "btn-default")
         :onClick (fn [] (let [r (inc (rand-int 20))
                               tension (inc (rand-int 20))
                               ]
                           (swap! status conj
                                  {:success (cond (= 1 r) true (= 20 r) false :else (<= r n))
                                   :roll r :diff n :player player :stat stat :tension tension}
                                  )
                           )
                    )
         }
   n
   ]
  )
(defn admin-single-player-component
  "Displays a single row of a player"
  [{p-name :name :keys [priStats] :as p-sheet} ^Atom status]
  [:tr
   [:td (:name p-sheet)]
   (doall
     (map (fn [[stat v]]
            [:td (create-stat-roller status v p-name stat)])
          priStats))
   ;; Mutation
   [:td (create-stat-roller status (get-in p-sheet [:mutation :power]) p-name "Mutation")]
   ;; Mutation list
   [:td (let [desc (get-in p-sheet [:mutation :description])]
          (if (string? desc)
            (shared/wrap-any desc)
            (->> desc
                 (map :name)
                 (map shared/wrap-any)
                 (interpose ", ")
                 )
            )
          )
    ]
   ;; Drawbacks
   [:td (let [dbs (:drawbacks p-sheet)]
          (cond
            (not dbs)
            ""
            (string? dbs)
            dbs
            :else
            (->> dbs
                 (map :text)
                 (map #(clojure.string/split % ": "))
                 (map first)
                 (interpose ", ")
                 )
            )
          )
    ]
   ]
  )
(defn convert-status-line-to-string
  [{:keys [success roll diff player stat tension message] :as s}]
  (cond
    ;; If a message exists, display that
    message
    [:span message]
    ;; If a roll exists, convert to roll
    roll
    ^{:key s}
    [:span {:class (cond
                    (= 1 roll) "text-success"
                    (= 20 roll) "text-danger"
                    success "text-primary"
                    :failure "text-warning")}
     (if success "Success! " "Failure! ")
     "Player " player " rolled " roll " to beat " diff " for " stat " with a margin of " (- roll diff) ". Tension: " tension
     ]
    ;; Otherwise?
    :else
    [:span (pr-str s)]
    )
  )
(defn admin-show-status-line
  "Displays the last n messages from the status atom"
  [^Atom status ^Integer lines]
  [:div
   (->> @status
        (take lines)
        (map (fn [r s] ^{:key r} [:div (convert-status-line-to-string s)])
             ;; To prevent "you need a key" warnings, they're annoying the hell out of me
             (range))
        )
   ]
  )
(defn sync-player-files
  "Saves all the player files back in the database"
  []
  (log/info "Sending sync request")
  (ajax/GET (wrap-context "/api/admin/sync-chars/")
            {:response-format (ajax/json-response-format {:keywords? true})
             :handler (fn [m]
                        (log/info "Synced Chars")
                        (get-updates)
                        )
             :params (merge @play-atom)})
  )
(defn admin-player-component
  "A window to show player details and tools"
  []
  (let [expand-atom (atom false)
        status-atom (atom '())
        ]
    (fn []
      [:div {:class "panel-info"}
       [:div {:class "panel-heading"
              :onClick #(swap! expand-atom not)}
        "Admin Panel"
        ]
       (if @expand-atom
         [:div
          [:div {:class "col-lg-12"}
           [:div {:class "col-lg-10"}
            [admin-show-status-line status-atom 3]
            ]
           [:div {:class "col-lg-2"}
            [:div {:class "btn btn-info"
                   :onClick sync-player-files
                   }
             "Save Players"
             ]
            ]
           ]
          [:div {:class "col-lg-12"}
           [:table {:class "table-striped table-hover col-lg-12"
                    }
            [:thead
             [:tr
              [:td "Name"] [:td "V"] [:td "M"] [:td "Su"] [:td "W"] [:td "So"] [:td "H"] [:td "Mut"] [:td "Descs"] [:td "Drawbacks"]
              ]
             ]
            [:tbody
             (->> @game-atom
                  :hps
                  (sort-by #(-> % val :name))
                  (map (fn [[uuid player-sheet]]
                         ^{:key uuid}
                         (admin-single-player-component player-sheet status-atom)
                         )
                       )
                  doall
                  )
             ]
            ]
           ]
          [:div {:class "col-lg-12"}
           [:div {:class "col-lg-2"}
            [:span {:class "btn btn-success"
                    :onClick #(ajax/GET (wrap-context "/api/admin/lock-zone/")
                                        {:response-format (ajax/json-response-format {:keywords? true})
                                         :handler (fn [m]
                                                    (log/info "Locked zone")
                                                    (get-updates)
                                                    )
                                         :params (merge @play-atom {:status true})})
                    }
             "Lock Investments"
             ]
            ]
           [:div {:class "col-lg-2"}
            [:span {:class "btn btn-default"
                    :onClick #(ajax/GET (wrap-context "/api/admin/lock-zone/")
                                        {:response-format (ajax/json-response-format {:keywords? true})
                                         :handler (fn [m]
                                                    (log/info "Locked zone")
                                                    (get-updates)
                                                    )
                                         :params (merge @play-atom {:status false})})
                    }
             "Unlock Investments"
             ]
            ]
           ]
          ]
         )
       ]
      )
    )
  )
;; Display public standing of players
(defn public-standing-component
  "Displays user's public standing, as well as upcoming live vidshows"
  []
  (let [expand-atom (atom false)]
    (fn []
      [:div {:class "panel-info"}
       [:div {:class "panel-heading"
              :onClick #(swap! expand-atom not)}
        "Public Relations"
        ]
       (if @expand-atom
         [:div {:class ""}
          [:table {:class "table-striped table-hover"
                   :style {:width "100%"}}
           [:thead
            [:tr
             [:th "Name"] [:th "Public Standing"] [:th]
             ]
            ]
           [:tbody
            (->> (get-in @game-atom [:hps])
                 (map (fn [[_ {p-name :name ps :publicStanding}]]
                        ^{:key p-name} [p-name ps]
                        )
                      )
                 (sort-by first)
                 (map (fn [[p-name ps]]
                        ^{:key p-name}
                        [:tr [:td p-name] [:td (if ps ps "-")]
                         ;; TODO buttons for modifying public standing
                         (if (= "admin" (:userlevel @play-atom))
                           [:td
                            [:span {:class (add-button-size "btn-success")
                                    :onClick #(ajax/GET (wrap-context "/api/admin/modify-public-standing/")
                                                        {:response-format (ajax/json-response-format {:keywords? true})
                                                         :handler (fn [m]
                                                                    (log/info "Modified public standing")
                                                                    (get-updates)
                                                                    )
                                                         :params (merge @play-atom {:player (name p-name)
                                                                                    :amount 1
                                                                                    })})
                                    }
                             "+1"
                             ]
                            [:span {:class (add-button-size "btn-warning")
                                    :onClick #(ajax/GET (wrap-context "/api/admin/modify-public-standing/")
                                                        {:response-format (ajax/json-response-format {:keywords? true})
                                                         :handler (fn [m]
                                                                    (log/info "Modified public standing")
                                                                    (get-updates)
                                                                    )
                                                         :params (merge @play-atom {:player (name p-name)
                                                                                    :amount -1
                                                                                    })})
                                    }
                             "-1"
                             ]
                            ]
                           nil
                           )
                         ]
                        )
                      )
                 doall
                 )
            ]
           ]
          ]
         nil
         )
       ]
      )
    )
  )

;; Display panel for the game in total
(defn game-component
  "Component for displaying and playing a game"
  [^Atom g]
  [:div
   [:h5 "Welcome High Programmer " (get-in @game-atom [:character :name])
    " to zone " (:zone @game-atom)
    ". Current Access: " (-> @game-atom :access first (get (keyword (get-in @game-atom [:character :name]))))]
   (if (= "admin" (:userlevel @play-atom))
     [admin-player-component]
     nil
     )
   [:table {:class "table-striped"
            :style {:width "100%"}
            }
    [:thead
     ]
    [:tbody
     [:tr
      [:td {:style {:width "50%"}}
       [access-component]
       [directives-component]
       [society-missions-component]
       [indicies-component]
       [investment-component]
       [cbay-component]
       [news-component]
       [keywords-component]
       (case (:userlevel @play-atom)
         "player" [character-component]
         "admin" [public-standing-component]
         nil
         )
       ]
      ;[:td {:style {:width "33%"}}
      ; "Chat window and votes here"
      ; ]
      [:td {:style {:width "50%"}}
       [program-group-component]
       [service-group-component]
       ]
      ]
     ]
    ]
   [loop-updates-component]
   [news-ticker-component ticker-keyword-atom]
   ]
  )
;; Top-level display panel for logging into games
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
   ;; So the leave game button isn't hidden
   [:br][:br]
   ;; Debug
   (if (shared/get-debug-status)
     [:div
      [:div "Play Atom:" (prn-str @play-atom)]
      [:div "Game keys:" (prn-str (keys @game-atom))]
      [:div "Game State:" (prn-str @game-atom)]
      ]
     nil
     )
   ]
  )
