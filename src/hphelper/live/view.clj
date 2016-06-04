(ns hphelper.live.view
  (:require [hphelper.live.control :as lcon]
            [hphelper.shared.sql :as sql]
            [hphelper.shared.saveload :as sl]
            [hphelper.shared.unique :as uni]
            [hphelper.shared.indicies :as indicies]
            ;; For indicies
            [hphelper.scengen.generator :as sgen]
            [clojure.tools.logging :as log]

            [hiccup.core :refer :all]
            )
  (:gen-class)
  )

(defn- html-print-single-index
  "Prints a single index in html format"
  [index]
  (str (name (key index))
       (if (> 0 (val index))
         "&#8657;" ;; Up Arrow
         (if (< 0 (val index))
           "&#8659;" ;; Down Arrow
           "&#8658;" ;; Right Arrow
           ))
       (Math/abs (val index))
       ))

(defn- html-print-indicies
  "Prints the indicies in html format"
  [inds]
  (concat (sort (map html-print-single-index
                     (filter (partial some
                                      (into #{}
                                            indicies/sectorIndicies))
                             inds)))
          (sort (map html-print-single-index
                     (remove (partial some
                                      (into #{}
                                            indicies/sectorIndicies))
                             inds)))
  ))

(defn- html-print-indicies-table
  "Prints indicies in a table in n columns"
  [inds n]
  [:table
   (map (fn [r] [:tr r])
        (partition-all
          n
          (map (fn [x] [:td x])
               (html-print-indicies inds))))
   ]
  )

(defn view-game
  "Prints a game view nicely for players"
  [baseURL uid]
  (log/trace "view-game:" baseURL uid)
  (if-let [game (lcon/get-game uid)]
    (html [:html
           [:head
            [:title "Sector indicies"]
            ]
           [:body
            [:div
             (html-print-indicies-table (:indicies game) 3)
             ]
            [:div
             (map (fn [n] [:div n])
                  (:news game))
             ]
            ]
           ])
    (html [:html [:div "Game not found"]])
    ))

(defn new-game
  "Creates a new live game by loading a completed scenario"
  [baseURL scenId]
  (if (string? scenId)
    (new-game baseURL (try (Integer/parseInt scenId)
                           (catch Exception e
                             (do (log/trace "new-game. Could not parse:" scenId)
                                 "Invalid scenId"))))
    (html [:html
           [:head
            [:meta {:http-equiv "refresh"
                    :content (str "0; url="
                                  "/live/view/"
                                  (lcon/new-game (sl/load-fullscen-from-db scenId))
                                  "/")}
             ]]])))

(defn edit-game
  "Prints a view for the GM to edit a game, also performs actions"
  ([baseURL uid]
   nil ;;TODO
   ))
