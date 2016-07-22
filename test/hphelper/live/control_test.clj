(ns hphelper.live.control-test
  (:require [hphelper.live.control :refer :all]
            [clojure.test :refer :all]

            [clojure.spec :as s]
            [hphelper.shared.spec :as ss]
            )
  )

;; Due to the way fuzzifying indicies works, we can only test to for larger jumps
;; Normal indicies no longer balance with each other
(deftest test-new
  (testing "Basic operation"
    (let [uid (new-game)]
      (is (= 0 (-> (get-game uid) :indicies first :HI)) "Normal happiness index should equal 0")
      (is (modify-index uid :HI 3) "Should return a map")
      (is (< 0 (-> (get-game uid) :indicies first :HI)) "Happiness index should be greater than 0")
      (is (modify-index uid :HI -6) "Should return a map")
      (is (> 0 (-> (get-game uid) :indicies first :HI)) "Happiness index should be less than 0")
      )))

(deftest test-existing
  (testing "Basic operation with existing values"
    (let [uid (new-game {:indicies {:HI 5 :CI -5}})]
      (println "New game with indicies set is:" (get-game uid))
      (is (= 5 (-> (get-game uid) :indicies first :HI)) "Happiness index should equal 5")
      (is (= -5 (-> (get-game uid) :indicies first :CI)) "Compliance index should equal 5")
      (is (s/valid? ::ss/liveScenario (get-game uid)) (s/explain ::ss/liveScenario (get-game uid)))
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
