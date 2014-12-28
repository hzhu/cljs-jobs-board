(ns main.data
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(def app-state (atom {"post" { "hostel_name" "Pacific Tradewinds Backpackers"
                               "job_description" "Receptionist. Cleaner."
                               "location" "San Franciaaweosme"
                               "email" "ptdubs@gmail.com"
                               "website" "www.ahostel.com"
                              }}))

(def current-view (atom nil))

(defn get-view []
  (get-in @current-view "")
  )

(defn set-view [view]
  (reset! current-view view)
  (println "view has been set to something else"))


(defn printAtom []
  (println "::::::::::ATOM::::::::::")
  (println "::::::::::::::::::::::::")
  (println (get-in @app-state ["post"]))
  (println "::::::::::::::::::::::::")
  (println "::::::::::::::::::::::::"))

;HELPER FUNCTIONS
;Send Job Post to Firebase
(defn post2fb [fb]
  (def postMap (get-in @app-state ["post"]))
  (.push fb (clj->js postMap))
)

(defn setter [name value]
  (swap! app-state assoc-in ["post" name] value)
  (printAtom))

