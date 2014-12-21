(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)
(println "doop")


;-----------------------------------

(defn atom-input [value]
  [:input {:type "text"
           :value @value ;@value is the atom. It's defined and set in shared-state component?
           :on-change #(reset! value (-> % .-target .-value))}])

(defn shared-state []
  (let [val (atom "foo")]
    (fn []
      [:div
       [:p "The value is now: " @val]
       [:p "Change it here: " [atom-input val]]])))


(reagent/render-component [shared-state] (.getElementById js/document "app"))