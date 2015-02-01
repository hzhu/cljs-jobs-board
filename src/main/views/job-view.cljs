(ns main.views.job-view
  (:require [reagent.core :as reagent :refer [atom]]
                        [main.data :as data]
                        [main.helper-functions :as helpers]
                        [clojure.string :as str]
                        [hickory.core :refer [as-hiccup parse parse-fragment]]
))

(enable-console-print!)

(defn regex-email? [value]
  (re-matches #"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?" value))

       
(defn is-email? [string]
    (if (nil? (regex-email? string))
      (str string)
      (str "<a href=\"mailto:" string "\">" string "</a>")))

(defn transform-email [string]
   (let [splitted (str/split string #" ")]
     (println splitted)
     (str/join " " (vec (map is-email? splitted)))
   ))



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
        [:p.how (map as-hiccup (parse-fragment (transform-email (job "how"))))]
        ]]))


