(ns hphelper.live.api
  (:require [hphelper.shared.sql :as sql]
            [taoensso.timbre :as log]
            [clojure.spec :as s]
            [hphelper.shared.spec :as ss]
            [clojure.data.json :as json]
            [hphelper.live.control :refer [get-game] :as lcon]
            )
  (:gen-class)
)

(def errors
  "A map of precompiled error messages for an invalid game or user id login"
  {:login {:status "error" :message "Invalid game or user id"}
   :invalidGame {:status "error" :message "Invalid game"}
   }
  )

(defn get-indicies
  "Gets the indicies of a game"
  [^String gUid]
  (log/trace "get-indicies:" gUid)
  (if-let [gi (first (:indicies (get-game gUid)))]
    {:status "ok" :indicies gi}
    (:invalidGame errors)
    ))

(defn get-cbay
  "Gets the cbay items of a game"
  [^String gUid]
  (log/trace "get-cbay:" gUid)
  (if-let [gc (:cbay (get-game gUid))]
    {:status "ok" :cbay gc}
    (:invalidGame errors)
    ))

(defn get-news
  "Gets the news items of a game"
  [^String gUid]
  (log/trace "get-news:" gUid)
  (if-let [gc (:news (get-game gUid))]
    {:status "ok" :news gc}
    (:invalidGame errors)
    ))

(defn get-current-access
  "Gets the news items of a game"
  [^String gUid]
  (log/trace "get-current-access:" gUid)
  (if-let [ga (:access (get-game gUid))]
    {:status "ok" :access ga}
    (:invalidGame errors)
    ))

(defn get-player-society-missions
  "Gets a list of the secret society missions of a player"
  [^String gUid ^String uUid]
  (let [g (get-game gUid)
        p (-> g :hps (get uUid))]
    (if p
      {:status "ok"
                       :missions (filter (fn [mission]
                                           (some #{(mission :ss_id)}
                                                 (map :ss_id
                                                      (or (:programGroup p) (get p "Program Group")))))
                                         (g :societies))
                       }
      (:login errors))))

(defn get-player-character-sheet
  "Returns a player's character sheet"
  [^String gUid ^String uUid]
  (if-let [p (-> (get-game gUid) :hps (get uUid))]
    p
    (:login errors)
    ))

(defn- is-admin-get-game
  "Returns the gamemap if the user is an admin, else nil"
  [^String gUid ^String uUid]
  (if-let [g (get-game gUid)]
    (if (= uUid (:adminPass g))
      g
      nil)
    nil))

(defn admin-debug
  "Gets the current gamemap, requires admin login"
  [^String gUid ^String uUid]
  (log/trace "admin-debug. gUid:" gUid "uUid:" uUid)
  (if-let [g (is-admin-get-game gUid uUid)]
      (json/write-str g)
      (:login errors)
    ))

(defn admin-validate-spec
  "Gets the game, validates the gamemap. requires admin login"
  [^String gUid ^String uUid]
  (log/trace "admin-validate-spec. gUid:" gUid "uUid:" uUid)
  (if-let [g (is-admin-get-game gUid uUid)]
    (s/explain-str ::ss/liveScenario g)
    (:login errors)
    ))

(defn admin-modify-index
  "Modifies an index by the required amount"
  [^String gUid ^String uUid ^String ind ^String amount]
  (log/trace "admin-modify-index." gUid uUid ind amount)
  (if-let [g (is-admin-get-game gUid uUid)]
    (if (lcon/modify-index gUid
                           (keyword ind)
                           (try (Integer/parseInt amount)
                                (catch Exception e
                                  (log/debug "Could not parse:" amount "Defaulting to 0")
                                  0)))
      (json/write-str {:status "ok"})
      (json/write-str {:status "error" :message "modify-index failed"}))
    (:login errors)
    ))

(defn- get-index
  "Gets the data from an index, returns a map"
  [^String gUid ^String uUid ind]
  (log/trace "get-index. gUid:" gUid "ind:" ind)
  (case ind
    ;; Publics
    :news (get-news gUid)
    :cbay (get-cbay gUid)
    :indicies (get-indicies gUid)
    :access (get-current-access gUid)

    ;; Players
    :hps (get-player-character-sheet gUid uUid)
    :missions (get-player-society-missions gUid uUid)
    {}
    ))

(defn get-updated-public
  "Gets all the updated items in a game"
  [^String gUid ^Integer t]
  (log/trace "get-updated-public. gUid:" gUid "time:" t)
  (if-let [g (get-game gUid)]
    (let [retKeys (keys (filter (fn [[k lastUpdate]] (< t lastUpdate)) (:updated g)))]
      (log/trace "get-updated-player. retKeys:" retKeys)
      (reduce merge
              {:status "ok" :updated t}
              (map (partial get-index gUid "") retKeys)))
    (:invalidGame errors)
    ))

(defn get-updated-player
  "Gets all the updated items for a player"
  [^String gUid ^String uUid ^Integer t]
  (log/trace "get-updated-player. gUid:" gUid "uUid:" uUid "time:" t)
  (if-let [g (get-game gUid)]
    (let [retKeys (keys (filter (fn [[k lastUpdate]] (< t lastUpdate)) (:updated g)))]
      (reduce merge
              {:status "ok" :updated t}
              (map (partial get-index gUid uUid) retKeys)))
    (:invalidGame errors)
    ))
