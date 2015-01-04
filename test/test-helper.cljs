(ns myproject.test-helper
  (:use [jayq.core :only [$ html]])
  (:require [om.core :as om]
            [om.dom :as dom :include-macros true]))

(def test-container
  (first ($ "<div id='testing'></div>")))

(defn om->$
  "Builds the given component by initializing it with jQuery based on the
  om.dom/render-to-str html string output."
  [c opts]
  ($ (dom/render-to-str (om/build c opts))))

(defn om-root->$
  "Builds the given component as the root, so that the state does not need
  to be passed in derefed. Returns a jQuery object that contains the
  component."
  [c opts]
  (om/root c opts {:target test-container})
  ($ test-container))
