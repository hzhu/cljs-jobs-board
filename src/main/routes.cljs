(ns main.routes
  (:require [reagent.core :as reagent :refer [atom]]
            [main.data :as data]
            [secretary.core :as secretary :include-macros true :refer [defroute]]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            )
  (:import goog.History))


(enable-console-print!)


(defn app-routes []
  (secretary/set-config! :prefix "#")

  (defroute "/jobs/:uid" [uid]
    (data/clicked-job uid)
    (data/set-view! #(job-view uid)))

  (defroute "/new/job" {}
    (println "setting view to /new/job")
    (data/set-view! new-post-view))

  (defroute "/" {}
    (println "setting view to /..")
    (data/set-view! home-view))

  (doto (History.)
    (events/listen
      EventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))