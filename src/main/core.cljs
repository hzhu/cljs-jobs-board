(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]
                        
            [secretary.core :as secretary :include-macros true :refer [defroute]]      
            [main.data :as data]
            [main.views.home :refer [home-view]]
            [main.views.job :refer [job-view]]
            [main.views.new-job :refer [new-job-view]]        
            [main.domready :as domready]
            
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            
            [ajax.core :as ajax])
  ;(:use-macros [dommy.macros :only [node deftemplate]]) // breaks
  (:import goog.History))


;; grab collection from fb and set-list!  (listens to Firebase for changes in data)
(let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings")]
  (.on fb "value" #(data/set-list! (.val %))))

;(let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings-dev")]
;  (.on fb "value" #(data/set-list! (.val %))))





;; ---------------------------------------------------------------------------------------
;; ROUTING ------------------------------------------------------------------------------
(secretary/set-config! :prefix "#")

(defroute "/jobs/:uid" [uid]
  (data/clicked-job uid)
  (data/set-view! #(job-view uid)))

(defroute "/new/job" {}
  (println "setting view to /new/job")
  (data/set-view! new-job-view))


(defroute "/" {}
  (println "setting view to /..")
  ;; think about clearing post atom here.
  ;; set view to home-view
  (data/set-view! home-view))

(doto (History.)
  (events/listen
    EventType/NAVIGATE
    (fn [event]
      (secretary/dispatch! (.-token event))))
  (.setEnabled true))

;; END OF ROUTING ------------------------------------------------------------------------
;; ---------------------------------------------------------------------------------------


;; RENDER VIEW
(defn app-view []
  [:div.container
    [:h1.hidden {:on-click #(data/printAtom)} "show atom"]
    (@data/current-view)
   ]
 )

(reagent/render-component [app-view] (.getElementById js/document "app"))



