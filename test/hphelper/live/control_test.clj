(ns hphelper.live.control-test
  (:require [hphelper.live.control :as lcon]
            [clojure.test :refer :all]
            )
)

(deftest test-new
  (testing "Basic operation"
    (let [uid (lcon/new-game)]
      (is (= 0 (-> (lcon/get-game uid) :indicies :HI)) "Normal happiness index should equal 0")
      (is (lcon/modify-index uid :HI 1) "Should return a map")
      (is (= 1 (-> (lcon/get-game uid) :indicies :HI)) "Normal happiness index should equal 0")
      )))

(deftest test-existing
  (testing "Basic operation with existing values"
    (let [uid (lcon/new-game {:indicies {:HI 5 :CI -5}})]
      (is (= 5 (-> (lcon/get-game uid) :indicies :HI)) "Happiness index should equal 5")
      (is (= -5 (-> (lcon/get-game uid) :indicies :CI)) "Compliance index should equal 5")
      )))

