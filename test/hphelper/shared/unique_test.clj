(ns hphelper.shared.unique-test
  (:require [clojure.tools.logging :as log]
            [clojure.test :refer :all]
            [hphelper.shared.unique :as unique]
            )
)

(deftest test-uuid
  (testing "basic uuid gen"
    (let [uidOne (unique/uuid)
          uidTwo (unique/uuid)]
      (is (string? uidOne) "First uid should be a string")
      (is (string? uidTwo) "Second uid should be a string")
      (is (not (= uidOne uidTwo)) "uids should be different"))))

(deftest test-add
  (testing "adding and increment an integer"
    (let [mapAtom (atom {})
          item 5
          uid (unique/add-uuid-atom! mapAtom item)]
      (is (= (unique/get-uuid-atom mapAtom uid) item) "Should return the item just added to map")
      (is (= (unique/swap-uuid! mapAtom uid inc) (inc item)) "Should equal (inc item), doesn't")
      )))

(deftest test-add-complex
  (testing "adding and incrementing a map"
    (let [mapAtom (atom {})
          i 5
          item {:a i}
          uid (unique/add-uuid-atom! mapAtom item)]
      (is (= (unique/get-uuid-atom mapAtom uid) item) "Map should be the same")
      (is (= ((unique/get-uuid-atom mapAtom uid) :a) i) "Integer should still be the same")
      (is (map? (unique/swap-uuid! mapAtom uid update-in [:a] inc)) "Should still be a map")
      (is (= ((unique/get-uuid-atom mapAtom uid) :a) (inc i)) "Integer should have been increased")
      )))
