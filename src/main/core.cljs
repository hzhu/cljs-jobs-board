(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]
            [firebase.session :as session]
            [secretary.core :as secretary :refer-macros [defroute]]
            [main.post :as post]
            [ajax.core :as ajax]))



(defn new-post-view []
  (defn handle-input-update [event]
    (let [value     (aget event "target" "value")
          className (aget event "target" "className")]
      (session/setter className value)))

  [:div
    [:input.hostel_name     {:type "text" :placeholder "Hostel Name"     :on-change #(handle-input-update %) }]
    [:input.job_description {:type "text" :placeholder "Job description" :on-change #(handle-input-update %)}]
    [:input.location        {:type "text" :placeholder "Location"        :on-change #(handle-input-update %)}]
    [:input.email           {:type "text" :placeholder "Email"           :on-change #(handle-input-update %)}]
    [:input.website         {:type "text" :placeholder "website"         :on-change #(handle-input-update %)}]

    (let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings")]
      [:a {:href "#" :on-click #(session/post2fb fb)} "submit"])


    [:a.routes {:on-click #(println "go home")} "HOME PAGE"]
    ; :on-click #(dispatch "/")
  ])

(defn home-view []
  [:div.home
   [:h1 "WELCOME TO THE JOBS BOARD"]
   [:a.routes {:on-click #(println "go make new post")} "POST A NEW JOB"]])
   ; :on-click #(dispatch "/new/job")




(defn app-view []
   [:div
    [new-post-view]
    [home-view]])

(reagent/render-component [app-view] (.getElementById js/document "app"))


