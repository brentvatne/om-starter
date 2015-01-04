(ns myproject.core
  (:require-macros [cljs.core.async.macros :refer [go-loop]])
  (:require [om.core :as om]
            [om-tools.core :refer-macros [defcomponent defcomponentk]]
            [cljs.core.async :refer [chan put! take! <! >!]]
            [om-tools.dom :as dom :include-macros true]))

;; Helpers

(defn log [msg]
  (.log js/console msg))

(defn log-clj [obj]
  (log (clj->js obj)))

;; Handling events through core.async channels and a dispatcher

(defn reverse-name
  [person]
  (om/transact! person
    #(merge % {:name (apply str (reverse (:name %)))})))

(defn handle-name-click
  [app bus]
  (put! bus [:reverse app]))

(defn dispatch
  [command params]
  (case command
    :reverse (reverse-name params)))

;; Components

(defcomponent child-component [person owner]
  (render [_]
    (dom/h1 {:onClick #(handle-name-click person (:bus (om/get-shared owner)))}
      "Hi, I am " (get person :name) "!")))

(defcomponentk root-component [owner [:data people]]
  (render [_]
    (dom/div
      (dom/h1 "List of people")
      (apply dom/div {}
       (om/build-all child-component people)))))

;; Global app state

(def app-state
  (atom {:people [{:name "Brent" :age 28}
                  {:name "Haley" :age 25}]}))

;; Main function, to run the app you would call myproject.core.main() from
;; JavaScript - just run it here if you want (main "app") where "app" is the
;; id of the target container div.

(defn ^:export main [container-id]
  (let [bus (chan)]
    (go-loop []
      (let [[command params] (<! bus)]
        (dispatch command params)
        (recur)))

    (om/root root-component app-state
             {:shared {:bus bus}
              :target (.getElementById js/document container-id)})))
