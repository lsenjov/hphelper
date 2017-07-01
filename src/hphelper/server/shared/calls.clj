(ns hphelper.server.shared.calls
  (:require [clojure.spec.alpha :as s]
            [taoensso.timbre :as log]
            )
  (:gen-class)
  )

;; A call queue is a vector of calls, and an index.
;; callNumber is the next call to make, and will never be greater than (len callQueue)
(s/def ::privateCall (s/nilable (s/and number? pos?)))
(s/def ::owner string?)
(s/def ::paid (s/coll-of ::owner))
(s/def ::callWaiting (s/keys :req-un [::owner ::sg_abbr ::minion_id]
                             :opt-un [::privateCall ::paid]))
(s/def ::callFiltered (s/keys :req-un [::owner]
                              :opt-un [::sg_abbr ::minion_id ::privateCall ::paid]))
(s/def ::callQueue (s/and vector? (s/coll-of ::callWaiting)))
(s/def ::callNumber (s/and integer? (complement neg?)))
(s/def ::calls (s/and (s/keys :req-un [::callNumber ::callQueue])
                      (fn [{:keys [callNumber callQueue]}] (<= callNumber (count callQueue)))
                      ))

(def empty-calls
  {:callNumber 0
   :callQueue []})

(defn- add-call-inner
  "Checks the queue for if the owner already has a call in queue.
  If yes, replaces that call with the specified call.
  If no, adds it to the end"
  [cq {owner :owner :as call}]
  {:pre [(s/assert ::callQueue cq)
         (s/assert ::callWaiting call)]
   :post [(s/assert ::callQueue %)]}
  (log/trace "add-call-inner. cq:" cq "call:" call)
  (if (->> cq (map :owner) (some #{owner}))
    ;; Call already in the queue, replace it
    (mapv (fn [{c-own :owner :as c-call}]
            (if (= c-own owner)
              ;; Replace the call
              call
              ;; Don't replace it
              c-call))
          cq)
    ;; No call in the queue, add it to the end
    (conj cq call)
    ))
(defn add-call
  "Adds the specified call to the queue"
  [{:keys [callNumber callQueue] :as calls} call]
  {:pre [(s/assert ::calls calls)
         (s/assert ::callWaiting call)]
   :post [(s/assert ::calls %)]}
  (assoc calls
         :callQueue
         (apply conj (subvec callQueue 0 callNumber)
                (add-call-inner (subvec callQueue callNumber)
                                call))))
(defn next-call
  "Increases the callNumber by one, or to the length of the call queue"
  [{:keys [callNumber callQueue] :as calls}]
  {:pre [(s/assert ::calls calls)]
   :post [(s/assert ::calls %)]}
  (log/trace "next-call")
  (assoc calls :callNumber (min (inc callNumber) (count callQueue))))
(defn get-calls
  "Returns the current call queue"
  [{:keys [callNumber callQueue] :as calls}]
  {:pre [(s/assert ::calls calls)]
   :post [(s/assert ::callQueue %)]}
  (log/trace "get-calls")
  (subvec callQueue callNumber))
(defn- filter-call-player
  "Takes a single call, filters it if it's a private call the player hasn't bought in to"
  [player {:keys [owner privateCall paid] :as call}]
  {:pre [(s/assert ::callWaiting call)
         (s/assert ::owner player)]
   :post [(s/assert ::callFiltered %)]}
  (log/trace "filter-call-player" player)
  (if (and privateCall ; If it's a private call
           (not= player owner) ; It's not the owner
           (not (some #{player} paid))) ; and this person hasn't paid to listen in
    ;; Filter
    (select-keys call [:owner :privateCall])
    ;; All is fine
    call))
(defn get-calls-player
  "Returns the current call queue for a player, with private calls filtered"
  [{:keys [callNumber callQueue] :as calls} player]
  {:pre [(s/assert ::calls calls)]}
  (log/trace "get-calls-player" player)
  (mapv (partial filter-call-player player) (get-calls calls)))
(defn join-private-call
  "Adds a player to a specified private call of another"
  [{:keys [callNumber callQueue] :as calls} player target-player]
  {:pre [(s/assert ::calls calls)]
   :post [(s/assert ::calls %)]}
  (log/trace "join-private-call. player:" player "target-player:" target-player "current calls:" (get-calls calls))
  (assoc calls
         :callQueue
         (apply conj
                (subvec callQueue 0 callNumber) ; History, leave untouched
                (map (fn [{:keys [owner privateCall] :as call}]
                       (if (and (= owner target-player)
                                privateCall)
                         ;; This is the one we need
                         (do
                           (log/trace "Found call to add self to:" call)
                           (update-in call [:paid] #(if %
                                                      (-> % (conj player) distinct vec)
                                                      [player])))
                         ;; Don't need this, leave it alone
                         call))
                     (subvec callQueue callNumber)))))
(defn get-call-for-player
  "Returns the unfiltered call in the queue for a player. If no call, returns nil"
  [calls player]
  {:pre [(s/assert ::calls calls)
         (s/assert ::owner player)]
   :post [(or (nil? %)
              (s/assert ::callWaiting %))]}
  (some (fn [{owner :owner :as call}] (if (= player owner) call nil))
        (get-calls calls)))
(defn construct-call
  "Constructs a single call record for a player"
  ([player sg_abbr minion_id]
  {:post [(s/assert ::callWaiting %)]}
  {:owner player
   :sg_abbr sg_abbr
   :minion_id minion_id})
  ([player sg_abbr minion_id privateCall]
   (if privateCall
     {:owner player
      :sg_abbr sg_abbr
      :minion_id minion_id
      :privateCall privateCall
      :paid [player]}
     (construct-call player sg_abbr minion_id))))


(comment
  (as-> empty-calls v
    (add-call v {:owner "hp2" :sg_abbr "HP" :minion_id 5})
    (add-call v {:owner "hp1" :sg_abbr "AF" :minion_id 3})
    (add-call v {:owner "hp3" :sg_abbr "HP" :minion_id 5 :privateCall 2})
    (add-call v {:owner "hp4" :sg_abbr "HP" :minion_id 5 :privateCall 3 :paid ["hp1"]})
    (add-call v {:owner "hp1" :sg_abbr "RD" :minion_id 8})
    (next-call v)
    (join-private-call v "hp1" "hp3")
    (join-private-call v "hp2" "hp3")
    (get-call-for-player v "hp1")
    ;((partial get-calls-player "hp1"))
    )
  )
