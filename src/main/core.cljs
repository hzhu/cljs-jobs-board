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
    [:input.hostel_name     {:type "text"     :placeholder "Hostel Name"     :on-change #(handle-input-update %)}]
    [:input.job_title       {:type "text"     :placeholder "Job title"       :on-change #(handle-input-update %)}]
    [:input.job_description {:type "textarea" :placeholder "Job description" :on-change #(handle-input-update %)}]
    [:input.location        {:type "text"     :placeholder "Location"        :on-change #(handle-input-update %)}]
    [:input.email           {:type "text"     :placeholder "Email"           :on-change #(handle-input-update %)}]
    [:input.website         {:type "text"     :placeholder "website"         :on-change #(handle-input-update %)}]

    (let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings")]
      [:a {:href "#" :on-click #(data/post2fb fb)} "submit"])

    [:a.routes {:on-click #(secretary/dispatch! "/")} "HOME PAGE"]])

(let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings")]
  ;(.on fb "value" #(.log js/console (.val %)))
  (.on fb "value" #(data/set-list! (js->clj (.val %)))))

(defn job-view []
  [:div "JOB POST VIEW IS HEREEE!"
    [:a.routes {:on-click #(secretary/dispatch! "/")} "HOME PAGE"]
    [:a {:on-click #(data/printAtom)} "PRINT SHTUFF"]

    ;todo: get data from atom
    ;render html shizzle here
    (let [theAtom (data/get-clicked-job)]
      [:div.container
       [:div (theAtom "hostel_name")]
       [:div (theAtom "job_title")]
       [:div (theAtom "job_description")]
       [:div (theAtom "location")]
       [:div (theAtom "email")]
       [:div (theAtom "website")]]


      )])


(defn home-view []
  (def list-info (fn [[uid hostelData]]
    (defn set-post-atom [uid]
      (let [x (data/clicked-job uid)]))

    [:a.list-item {:href "#" :data-attr uid :on-click #((secretary/dispatch! "/jobs/someid")(set-post-atom uid))}
      [:div
        (hostelData "hostel_name") [:br]
        (hostelData "job_title")
      ]]))

  [:div.home
    [:div
      (map list-info (data/get-list!))
    ]

    [:h1 "WELCOME TO THE JOBS BOARD"]
    [:a.routes {:on-click #(secretary/dispatch! "/new/job")} "POST A NEW JOB"]
  ])

(reset! current-view home-view)

(defn app-view []
  (@current-view))

(reagent/render-component [app-view] (.getElementById js/document "app"))




;Routes and what view to render
(defroute "/jobs/someid" {}
  (reset! current-view job-view))

(defroute "/new/job" {}
  (reset! current-view new-post-view))

(defroute "/" {}
  (reset! current-view home-view))

