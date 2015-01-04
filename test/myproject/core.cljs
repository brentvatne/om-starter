(ns myproject.core.test
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:use [jayq.core :only [$ html]])
  (:require [om.core :as om]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :refer [chan]]
            [cemerick.cljs.test :as t]
            [myproject.test-helper :refer [om->$ om-root->$]]
            [myproject.core :refer [child-component root-component]]))

(enable-console-print!)

(def app-state
  (atom {:person {:name "Brent!" :age 28}
         :channel (chan)}))

(deftest child-component-test
  (is (= (.text (om->$ child-component @app-state))
         "Hi, I am Brent!!")))

(deftest parent-component
  (is (= (.text (om-root->$ root-component app-state))
         "Hi, I am Brent!!")))

(run-tests)
