(ns hphelper.live.control-test
  (:require [hphelper.live.control :refer :all]
            [clojure.test :refer :all]
            )
)

;; Due to the way fuzzifying indicies works, we can only test to for larger jumps
(deftest test-new
  (testing "Basic operation"
    (let [uid (new-game)]
      (is (= 0 (-> (get-game uid) :indicies :HI)) "Normal happiness index should equal 0")
      (is (modify-index uid :HI 10) "Should return a map")
      (is (< 0 (-> (get-game uid) :indicies :HI)) "Happiness index should be greater than 0")
      (is (> 0 (-> (get-game uid) :indicies :SI)) "Security index should be less than 0")
      (is (modify-index uid :HI -20) "Should return a map")
      (is (> 0 (-> (get-game uid) :indicies :HI)) "Happiness index should be less than 0")
      )))

(deftest test-existing
  (testing "Basic operation with existing values"
    (let [uid (new-game {:indicies {:HI 5 :CI -5}})]
      (is (= 5 (-> (get-game uid) :indicies :HI)) "Happiness index should equal 5")
      (is (= -5 (-> (get-game uid) :indicies :CI)) "Compliance index should equal 5")
      )))

(deftest test-news
  (testing "Basic news operation"
    (let [uid (new-game)]
      (is (= '() (get-news uid)) "Should be an empty list")
      ))
    (let [newsList '("asdf")
          uid (new-game {:news newsList})]
      (is (= newsList (get-news uid)) "Should be an empty list")
      ))
