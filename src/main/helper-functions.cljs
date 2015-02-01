(ns main.helper-functions
  (:require [reagent.core :as reagent :refer [atom]]
            ))

(defn make-date [epoch]
  (subs (.toDateString (js/Date. epoch)) 4 10))
