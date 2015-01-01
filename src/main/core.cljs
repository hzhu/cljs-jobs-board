(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]
            [main.data :as data]
            [secretary.core :as secretary :refer-macros [defroute]]
            [main.post :as post]
            [ajax.core :as ajax]))



(def current-view (atom nil))
;(println (get-in @current-view ""))

(defn new-post-view []
  (defn handle-input-update [event]
    (let [value     (aget event "target" "value")
          className (aget event "target" "className")]
      (data/setter className value)))

  [:div
    [:input.hostel_name     {:type "text" :placeholder "Hostel Name"     :on-change #(handle-input-update %)}]
    [:input.job_title       {:type "text" :placeholder "Job title"       :on-change #(handle-input-update %)}]
    [:input.job_description {:type "text" :placeholder "Job description" :on-change #(handle-input-update %)}]
    [:input.location        {:type "text" :placeholder "Location"        :on-change #(handle-input-update %)}]
    [:input.email           {:type "text" :placeholder "Email"           :on-change #(handle-input-update %)}]
    [:input.website         {:type "text" :placeholder "website"         :on-change #(handle-input-update %)}]

    (let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings")]
      [:a {:href "#" :on-click #(data/post2fb fb)} "submit"])


    [:a.routes {:on-click #(secretary/dispatch! "/")} "HOME PAGE"]

    (defroute "/" {}
      (reset! current-view home-view))
      (println "*************************")
      (println (get-in @current-view ""))
      (println "*************************")

      ; :on-click #(dispatch "/")
  ])

(let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings")]
  ;(.on fb "value" #(.log js/console (.val %)))
  (.on fb "value" #(data/set-list! (js->clj (.val %))
                     ))
  )


(defn home-view []

  (def list-info (fn [[_ hostelData]]
                [:div
                  (hostelData "hostel_name") [:br]
                  (hostelData "job_title")
                ]))


  [:div.home


    [:div
      (map list-info (data/get-list!))
    ]





    [:h1 "WELCOME TO THE JOBS BOARD"]
    [:a.routes {:on-click #(secretary/dispatch! "/new/job")} "POST A NEW JOB"]
    (defroute "/new/job" {}
      (reset! current-view new-post-view))
  ])






(reset! current-view home-view)

(defn app-view []
  (@current-view))

(reagent/render-component [app-view] (.getElementById js/document "app"))



