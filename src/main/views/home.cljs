(ns main.views.home
  (:require [reagent.core :as reagent :refer [atom]]
                        [main.data :as data]
                        [main.helper-functions :as helpers]))

(enable-console-print!)


(defn home-view-item [data]
  (let [[uid hostelData] data
        target (str "/jobs/" uid)]
    [:li
      [:a {:href (str "#" target)}
        [:div.name (hostelData "hostel_name")]
        [:div.location [:b"@ "] (hostelData "location")]
        [:div.title (hostelData "job_title")]
        [:div.date (helpers/make-date (hostelData "create_date"))]]]
 ))

(defn home-view[] 
  [:div.home
   [:h1 "HOSTEL JOBS BOARD"]

   [:div#post-new-job
     [:a.routes {:href "#/new/job"} "POST A NEW JOB"]]

   [:ul (map home-view-item (data/get-list!))]])