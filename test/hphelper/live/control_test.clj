(ns hphelper.live.control-test
  (:require [hphelper.live.control :as lcon]
            [clojure.test :refer :all]
            )
)

;; Due to the way fuzzifying indicies works, we can only test to for larger jumps
(deftest test-new
  (testing "Basic operation"
    (let [uid (lcon/new-game)]
      (is (= 0 (-> (lcon/get-game uid) :indicies :HI)) "Normal happiness index should equal 0")
      (is (lcon/modify-index uid :HI 10) "Should return a map")
      (is (< 0 (-> (lcon/get-game uid) :indicies :HI)) "Happiness index should be greater than 0")
      (is (> 0 (-> (lcon/get-game uid) :indicies :SI)) "Security index should be less than 0")
      (is (lcon/modify-index uid :HI -20) "Should return a map")
      (is (> 0 (-> (lcon/get-game uid) :indicies :HI)) "Happiness index should be less than 0")
      )))

(deftest test-existing
  (testing "Basic operation with existing values"
    (let [uid (lcon/new-game {:indicies {:HI 5 :CI -5}})]
      (is (= 5 (-> (lcon/get-game uid) :indicies :HI)) "Happiness index should equal 5")
      (is (= -5 (-> (lcon/get-game uid) :indicies :CI)) "Compliance index should equal 5")
      )))

(deftest test-news
  (testing "Basic news operation"
    (let [uid (lcon/new-game)]
      (is (= '() (lcon/get-news uid)) "Should be an empty list")
      ))
    (let [newsList '("asdf")
          uid (lcon/new-game {:news newsList})]
      (is (= newsList (lcon/get-news uid)) "Should be an empty list")
      ))
