

(ns example
  (:require [reagent.core :as reagent :refer [atom]]))

(defn atom-input [value]
  [:input {:type "text"
           :value @value
           :on-change #(reset! value (-> % .-target .-value))}])

(defn shared-state []
  (let [val (atom "foo")]
    (fn []
      [:div
       [:p "The value is now: " @val]
       [:p "Change it here: " [atom-input val]]])))


(reagent/render-component [app-view] (.getElementById js/document "app"))
