(ns myproject.core.test
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:use [jayq.core :only [$ html]])
  (:require [om.core :as om]
            [om.dom :as dom :include-macros true]
            [cemerick.cljs.test :as t]
            [myproject.core :as subject]))

(enable-console-print!)

(defn om->$ [c opts]
  ($ (dom/render-to-str (om/build c opts))))

(deftest child-component-test
  (is (= (.text (om->$ subject/child-component "Some name"))
         "Hi, I am Some name!")))

(deftest parent-component
  (is (= (.text (om->$ subject/parent-component {:name "Brent" :age 28}))
         "Hi, I am Brent!")))

(run-tests)
