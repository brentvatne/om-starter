(ns myproject.core
  (:require [om.core :as om]
            [om-tools.core :refer-macros [defcomponent defcomponentk]]
            [om-tools.dom :as dom :include-macros true]))

(defn log [msg]
  (.log js/console msg))

(defcomponent child-component [name owner]
  (did-mount [_]
    (log owner) ; This is the child-component itself
    (log "mounted child-component"))
  (render [_]
    (dom/h1 "Hi, I am " name "!")))

(defcomponentk parent-component [owner [:data name age]]
  (render [_]
    (log name) ; See the above destructuring syntax
    (log age)
    (om/build child-component name)))

(defn ^:export main [container-id]
  (om/root parent-component {:name "Brent" :age 28}
           {:target (.getElementById js/document container-id)}))
