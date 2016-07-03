(ns hphelper.chargen.generator-test
  (:require [clojure.test :refer :all]
            [hphelper.chargen.generator :refer :all]
            [schema.core :as s]
            [hphelper.shared.schema :as ss]))

(is (s/validate ss/playerCharacter (create-character)) "A random character should be of a PlayerCharacter form")
