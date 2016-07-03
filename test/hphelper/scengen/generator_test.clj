(ns hphelper.scengen.generator-test
  (:require [clojure.test :refer :all]
            [hphelper.scengen.generator :refer :all]
            [schema.core :as s]
            [hphelper.shared.schema :as ss]
            )
  )

(s/validate ss/generatedScenario (create-scenario))
