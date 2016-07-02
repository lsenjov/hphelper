(ns hphelper.shared.schema
  (:require [schema.core :as s]
            )
  (:gen-class)
  )

;; Character Sheets
(def PlayerCharacterStatBlock
  {s/Str s/Int})

(def SocietySingleRec
  {:ss_id s/Int
   :ss_name s/Str
   :sskills s/Str
   }
  )

(def PlayerSocietyRec
  #{SocietySingleRec}
  )

(def PlayerMutationRec
  {:description s/Str
   :power s/Int
   }
  )

(def PlayerCharacter
  {:priStats PlayerCharacterStatBlock
   :secStats PlayerCharacterStatBlock
   :programGroup PlayerSocietyRec
   :mutation PlayerMutationRec
   :accessRemaining s/Int
   :name s/Str
   }
  )

;; Scenario Sheets
(def ScenSocietySingle
  {:ssm_id s/Int
   :ss_id s/Int
   :c_id (s/maybe s/Int)
   :ssm_text s/Str}
  )

(def ServiceGroupMinion
  {:minion_id s/Int
   :minion_name s/Str
   :minion_clearance s/Str
   :minion_cost s/Int
   :sg_id s/Int}
  )

(def ServiceGroupRecord
  {:sg_id s/Int
   :sg_name s/Str
   :sg_abbr s/Str
   :minions ServiceGroupMinion
   }
  )

(def CrisisRecord
  {:c_id s/Int
   :c_type s/Str
   :c_desc s/Str
   :extraDesc [s/Str]}
  )

(def DirectiveRecord
  {:sgm_id s/Int
   :sgm_text s/Str
   :sg_id s/Int
   :c_id s/Int}
  )

(def GeneratedScenario
  {:cbay [s/Str]
   :hps (s/maybe {s/Int PlayerCharacter})
   :societies [ScenSocietySingle]
   :zone s/Str
   :minions [ServiceGroupRecord]
   :crisises [CrisisRecord]
   :indicies {s/Keyword s/Int}
   :directives [DirectiveRecord]
   :news [s/Str]
   }
  )
