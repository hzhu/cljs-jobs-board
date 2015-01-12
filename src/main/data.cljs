(ns main.data
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(def app-state (atom {"post" { "hostel_name" "Pacific Tradewinds Backpackers"
                               "job_title" "Front Desk Receptionist"
                               "job_description" "As a front desk receptionist at the Tradewinds hostel you will be responsible for everything in the hostel! From checking in guests to doing laundry to cooking staff meals. It's an amazing place to connect with guests!"
                               "location" "San Franciaaweosme"
                               "email" "ptdubs@gmail.com"
                               "website" "www.sfhostel.com"
                               "how" "To apply simply send an email to me@example.com"
                               "create_date" (.-TIMESTAMP (.-ServerValue js/Firebase))
                               "sponsored" false
                              },
                      "clicked-job" {},
                      "jobs-list" {}}))

(def current-view (atom {}))



(defn get-view []
  @current-view)

(defn set-view! [view]
  (reset! current-view view)
  ;(println "view has been set to something else -")
)


(defn printAtom []
  (println "::::::::::ATOM::::::::::")
  (println "::::::::::::::::::::::::")
  (println @app-state ["post"])
  ;(.execCommand js/document "italic" false null)
  (println "::::::::::::::::::::::::")
  (println "::::::::::::::::::::::::"))

;HELPER FUNCTIONS

;; Update atom as user enters values for new job posting
(defn setter [name value]
  (swap! app-state assoc-in ["post" name] value))

;; Submit new job (stored in atom) to Firebase
(defn post2fb [fb]
  (def postMap (get-in @app-state ["post"]))
  (.push fb (clj->js postMap)))

;; Stick entire Firebase into atom
(defn set-list! [value]
  (swap! app-state assoc-in ["jobs-list"] value))

;; To render list of all jobs available
(defn get-list! []
  (get-in @app-state ["jobs-list"]))

;; To render individual job post
(defn clicked-job [uid]
  (get-in @app-state ["jobs-list" uid]))


