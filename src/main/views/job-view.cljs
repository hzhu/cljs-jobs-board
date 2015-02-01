(ns main.views.job-view
  (:require [reagent.core :as reagent :refer [atom]]
                        [main.data :as data]
                        [main.helper-functions :as helpers]
                        [hickory.core :refer [as-hiccup parse parse-fragment]]
))

(enable-console-print!)


;; JOB VIEW
(defn job-view [uid]
  [:div#job-view
    [:div.back
     [:a.btn {:href "#/"} (map as-hiccup (parse-fragment "&lArr; Back to All Jobs"))]

     ]

    (if (empty? (data/get-list!))
      (println "True. Atom is empty. Do not start rendering.")
      (render-jobs-list uid))])

(defn render-jobs-list [uid]
  (let [job (data/clicked-job uid)]
    [:div.job-view
     [:div.info-container
      [:div.title (job "job_title")]
      [:div.date "POSTED " (helpers/make-date (job "create_date"))]
      [:div.name (job "hostel_name")]
      [:div.location (job "location")]
      [:a.website {:href (job "website")} (job "website")]]

     [:div#job-description (map as-hiccup (parse-fragment (job "job_description")))]

     [:div.apply
        [:h3 "APPLY FOR THIS HOSTEL JOB"]
        [:p.how (map as-hiccup (parse-fragment (helpers/transform-email (job "how"))))]
        ]]))


