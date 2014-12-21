(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]
            [firebase.session :as session :refer [global-state global-put!]]
            [ajax.core :as ajax]))

(defn on-change [event fb] 
  (.set fb (clj->js {:text-from-app (-> event .-target .-value)}))
  (.on fb "value" (fn [snapshot] 
                    (global-put! :my-text ((js->clj (.val snapshot)) "text-from-app")))))



(defn input-field [value fb]
  [:input {:type "text"
           :value value
           :on-change #(on-change % fb)}])


(defn app-view []
	(let [fb (js/Firebase. "https://jobs-board.firebaseio.com/")] 
   		[:div
     	[:h2 "Home PAge!"]
      
      [:div
      [:p "The value is now: " (global-state :my-text)]
      [:p "Change it here: " [input-field (global-state :my-text) fb]]]
  	 ])
 )



(reagent/render-component [app-view] (.getElementById js/document "app"))


