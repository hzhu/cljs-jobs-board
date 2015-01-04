(ns main.data
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(def app-state (atom {"post" { "hostel_name" "Pacific Tradewinds Backpackers"
                               "job_title" "Front Desk Receptionist"
                               "job_description" "As a front desk receptionist at the Tradewinds hostel you will be responsible for everything in the hostel! From checking in guests to doing laundry to cooking staff meals. It's an amazing place to connect with guests!"
                               "location" "San Franciaaweosme"
                               "email" "ptdubs@gmail.com"
                               "website" "www.sfhostel.com"
                              },
                      "clicked-job" {},
                      "jobs-list" {}}))

(def current-view (atom nil))

(defn get-view []
  @current-view)

(defn set-view! [view]
  (reset! current-view view)
  ;(println "view has been set to something else -")
)


(defn printAtom []
  (println "::::::::::ATOM::::::::::")
  (println "::::::::::::::::::::::::")
  (println (get-in @app-state ["post"]))
  (println "::::::::::::::::::::::::")
  (println "::::::::::::::::::::::::"))

;HELPER FUNCTIONS
(defn post2fb [fb]
  (def postMap (get-in @app-state ["post"]))
  (.push fb (clj->js postMap))
)

(defn setter [name value]
  (swap! app-state assoc-in ["post" name] value))


(defn set-list! [value]
  (swap! app-state assoc-in ["jobs-list"] value))

(defn get-list! []
  (get-in @app-state ["jobs-list"])
)

; Set "clicked-job" atom when a job listing is clicked.
(defn clicked-job [uid]
  (let [data (get-in @app-state ["jobs-list" uid])]
    (swap! app-state assoc-in ["clicked-job"] data)
  ))

(defn get-clicked-job []
  (get-in @app-state ["clicked-job"]))


