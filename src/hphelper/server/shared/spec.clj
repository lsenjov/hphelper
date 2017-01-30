(ns hphelper.server.shared.spec
  (:require [clojure.spec :as s]
            )
  (:gen-class)
  )


;; Database types
;; c_id may sometimes be nil
(s/def ::c_id #(or (integer? %) (nil? %)))
(s/def ::c_type string?)
(s/def ::c_desc string?)
(s/def ::extraDesc (s/coll-of string?))
(s/def ::minion_id integer?)
(s/def ::minion_name string?)
(s/def ::minion_clearance #{"IR" "R" "O" "Y" "G" "B" "I" "V" "U"})
(s/def ::minion_cost integer?)
(s/def ::sg_id integer?)
(s/def ::sg_name string?)
(s/def ::sg_abbr #{"AF" "CP" "IS" "PL" "TS" "TD" "PS" "RD" "HP"})
(s/def ::sgm_id integer?)
(s/def ::sgm_text string?)
(s/def ::ss_id integer?)
(s/def ::ss_name string?)
(s/def ::sskills string?)
(s/def ::ssm_id integer?)
(s/def ::ssm_text string?)
(s/def ::id integer?)

;; Player specific items
(s/def ::name string?)
(s/def ::societySingleRec
  (s/keys :req-un [::ss_id ::ss_name ::sskills]))
(s/def ::programGroup
  (s/coll-of ::societySingleRec))
(s/def ::priStats
  (s/map-of string? integer?))
(s/def ::secStats
  (s/map-of string? integer?))
(s/def ::desc string?)
(s/def ::mutationMap (s/keys :req-un [::desc ::id ::name]))
(s/def ::description (s/or :old string? :new (s/coll-of ::mutationMap)))
(s/def ::power integer?)
(s/def ::mutation
  (s/keys :req-un [::description ::power]))
(s/def ::accessRemaining number?)
(s/def ::genericString string?)
(s/def ::msgs (s/coll-of ::genericString))
(s/def ::playerCharacter
  (s/keys :req-un [::priStats ::secStats ::programGroup
                   ::mutation ::accessRemaining ::name]
          :opt-un [::msgs]))

;; Now onto scenario types
(s/def ::zone (and string? #(= 3 (count %))))
(s/def ::cbay (s/coll-of string?))
(s/def ::news (s/coll-of string?))
(s/def ::societyMissionSingle (s/keys :req-un [::ssm_id ::ss_id ::c_id ::ssm_text ::ss_name]))
; Has this minion been paid for by a player?
(s/def ::bought? boolean?)
(s/def ::mskills string?)
(s/def ::serviceGroupMinion
  (s/keys :req-un [::minion_name ::minion_clearance ::minion_cost ::mskills]
          :opt-un [::bought? ::sg_id ::minion_id]
          ))
(s/def ::minions (s/coll-of ::serviceGroupMinion))
; The owner is the HP name, not the uid
(s/def ::owner string?)
(s/def ::serviceGroupRecord (s/keys :req-un [::sg_id ::sg_name ::sg_abbr ::minions]
                                    :opt-un [::owner]
                                    ))
(s/def ::crisisRecord (s/keys :req-un [::c_id ::c_type ::c_desc ::extraDesc]))
(s/def ::directiveRecord (s/keys :req-un [::sgm_id ::sgm_text ::sg_id ::c_id]))
(s/def ::socieites (s/coll-of ::societyMissionSingle))
(s/def ::serviceGroups (s/coll-of ::serviceGroupRecord))
(s/def ::crisises (s/coll-of ::crisisRecord))
(s/def ::directives (s/coll-of ::directiveRecord))
(s/def ::indicies (s/coll-of (s/map-of string? integer?)))
(s/def ::access (s/coll-of (s/map-of string? ::accessRemaining)))
(s/def ::updated (s/map-of keyword? integer?))
(s/def ::hps (s/map-of string? ::playerCharacter))
(s/def ::keywords (s/coll-of string?))
(s/def ::liveScenario
  (s/keys :req-un [::cbay ::hps ::indicies ::news ::access ::updated]
          :opt-un [::directives ::societies ::zone ::serviceGroups ::crisises ::keywords]
          )
  )

;; Storage for player investments
;; (get-in invests [player zone])
(s/def ::investments
  (s/map-of ::name (s/map-of ::zone (s/map-of ::sg_abbr number?))))
