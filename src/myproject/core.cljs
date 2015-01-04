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
  [app]
  (om/transact! app :person
    (fn [person]
      (merge person {:name (apply str (reverse (:name person)))}))))

(defn handle-name-click
  [app]
  (put! (:channel @app) [:reverse nil]))

(defn dispatch
  [command params app]
  (case command
    :reverse (reverse-name app)))

;; Components

(defcomponent child-component [app owner]
  (did-mount [_]
    (log owner) ; This is the child-component itself
    (log "mounted child-component"))
  (render [_]
    (dom/h1 {:onClick #(handle-name-click app)}
      "Hi, I am " (get-in app [:person :name]) "!")))

(defcomponentk root-component [owner [:data [:person name age] channel :as app]]
  (will-mount [_]
    (go-loop []
      (let [[command params] (<! channel)]
        (dispatch command params app)
        (recur))))
  (render [_]
    (log name) ; Notice the destructuring syntax above
    (log age)
    (om/build child-component app)))

;; Global app state

(def app-state
  (atom {:person {:name "Brent" :age 28}
         :channel (chan)}))

;; Main function, to run the app you would call myproject.core.main() from
;; JavaScript - just run it here if you want (main "app") where "app" is the
;; id of the target container div.

(defn ^:export main [container-id]
  (om/root root-component app-state
           {:target (.getElementById js/document container-id)}))
