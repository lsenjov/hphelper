(ns hphelper.chargen.generator-test
  (:require [clojure.test :refer :all]
            [hphelper.chargen.generator :refer :all]
            [clojure.spec :as s]
            [hphelper.shared.spec :as ss]))

(s/valid? ::ss/playerCharacter (create-character))
