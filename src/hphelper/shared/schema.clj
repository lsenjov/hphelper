(ns hphelper.shared.schema
  (:require [schema.core :as s]
            )
  (:gen-class)
  )

;; Character Sheets
(def playerCharacterStatBlock
  {s/Str s/Int})

(def societySingleRec
  {:ss_id s/Int
   :ss_name s/Str
   :sskills s/Str
   }
  )

(def playerSocietyRec
  #{societySingleRec}
  )

(def playerMutationRec
  {:description s/Str
   :power s/Int
   }
  )

(def playerCharacter
  {:priStats playerCharacterStatBlock
   :secStats playerCharacterStatBlock
   :programGroup playerSocietyRec
   :mutation playerMutationRec
   :accessRemaining s/Int
   :name s/Str
   }
  )

;; Scenario Sheets
(def scenSocietySingle
  {:ssm_id s/Int
   :ss_id s/Int
   :c_id (s/maybe s/Int)
   :ssm_text s/Str}
  )

(def serviceGroupMinion
  {:minion_id s/Int
   :minion_name s/Str
   :minion_clearance s/Str
   :minion_cost s/Int
   :sg_id s/Int}
  )

(def serviceGroupRecord
  {:sg_id s/Int
   :sg_name s/Str
   :sg_abbr s/Str
   :minions [serviceGroupMinion]
   }
  )

(def crisisRecord
  {:c_id s/Int
   :c_type s/Str
   :c_desc s/Str
   :extraDesc [s/Str]}
  )

(def directiveRecord
  {:sgm_id s/Int
   :sgm_text s/Str
   :sg_id s/Int
   ;; The crisis associated with this directive
   :c_id (s/maybe s/Int)}
  )

(def generatedScenario
  ;; Cbay items up for sale. Includes time left in the string for now
  {:cbay [s/Str]
   ;; A list of all the player characters
   :hps (s/maybe {s/Int playerCharacter})
   ;; A list of all the secret society missions
   :societies [scenSocietySingle]
   ;; Name of the zone
   :zone s/Str
   ;; A list of the service groups
   :minions [serviceGroupRecord]
   ;; The list of crisises
   :crisises [crisisRecord]
   :indicies {s/Keyword s/Int}
   :directives [directiveRecord]
   :news [s/Str]
   }
  )

(def liveScenario
  ;; Cbay items up for sale. Includes time left in the string for now
  {:cbay [s/Str]
   ;; A list of all the player characters
   :hps {s/Str playerCharacter}
   ;; A list of all the secret society missions
   :societies [scenSocietySingle]
   ;; Name of the zone
   :zone s/Str
   ;; A list of the service groups
   :minions [serviceGroupRecord]
   ;; The list of crisises
   :crisises [crisisRecord]
   ;; Current indicies and previous indicies
   :indicies [{s/Keyword s/Int}]
   :directives [directiveRecord]
   :news [s/Str]
   ;; Current access and previous access totals
   :access [{s/Str s/Int}]
   }
  )
