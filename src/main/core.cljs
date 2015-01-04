(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]
            [main.data :as data]
            [hickory.core :refer [as-hiccup parse]]
            [secretary.core :as secretary :include-macros true :refer [defroute]]
            [main.post :as post]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [ajax.core :as ajax])
  (:import goog.History)
  )

;; Quick and dirty history configuration.
(let [h (History.)]
  (goog.events/listen h EventType/NAVIGATE #(secretary/dispatch! (.-token %)))
  (doto h (.setEnabled true)))

;; grab collection from fb and set-list!
(let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings")]
  (.on fb "value" #(data/set-list! (js->clj (.val %)))))

;; helper, parse html into hiccup
(defn to-html [content]
  (as-hiccup (parse content)))

;; VIEWS
(defn new-post-view []
  (defn handle-input-update [event]
    (let [value     (aget event "target" "value")
          className (aget event "target" "className")]
      (data/setter className value)))

  (defn handle-contenteditable-update [event]
    (let [value     (aget event "target" "innerHTML")
          className (aget event "target" "className")]
       (data/setter className value))

    (data/printAtom))

  [:div
    [:input.hostel_name     {:type "text"     :placeholder "Hostel Name"     :on-change #(handle-input-update %)}]
    [:input.job_title       {:type "text"     :placeholder "Job title"       :on-change #(handle-input-update %)}]
    [:input.location        {:type "text"     :placeholder "Location"        :on-change #(handle-input-update %)}]
    [:input.email           {:type "text"     :placeholder "Email"           :on-change #(handle-input-update %)}]
    [:input.website         {:type "text"     :placeholder "website"         :on-change #(handle-input-update %)}]
    [:div.job_description   {:contentEditable true
                             :placeholder "Job description"
                             :on-blur #(handle-contenteditable-update %)}]

    (let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings")]
      [:a {:href "#" :on-click #(data/post2fb fb)} "submit"])

    [:a.routes {:href "#/"} "home page"]
  ])

(defn job-view []
  [:div#job-view "JOB POST VIEW IS HEREEE!"
    [:div "something"]
    (let [theAtom (data/get-clicked-job)]
      [:div
        [:div (theAtom "hostel_name")]
        [:div (theAtom "job_title")]
        [:div (to-html (theAtom "job_description"))]
        ; Blows up because of the line above. Not a problem of Reagent or ordering.
        ; React vomits here for some reason... Most likely because of Hickory. But why?
        [:div (theAtom "location")]
        [:div (theAtom "email")]
        [:div (theAtom "website")]])
    [:a.routes {:href "#/"} "home page"]
  ]
)

(defn home-view-item [data]
  (let [[uid hostelData] data
        target (str "/jobs/" uid)]

    ;[:a.list-item {:href (str "#" target) :data-attr uid :on-click #(secretary/dispatch! target)}
    [:a {:href (str "#" target)}
     [:div
      (hostelData "hostel_name")
      [:br ]
      (hostelData "job_title")
      ]]))

(defn home-view []
  [:div.home
    [:div
      (map home-view-item (data/get-list!))
    ]

    [:h1 "WELCOME TO THE JOBS BOARD"]
    ;[:a.routes {:on-click #(secretary/dispatch! "/new/job")} "POST A NEW JOB"]
    [:a.routes {:href "#/new/job"} "POST A NEW JOB"]
  ])

;; Routes and what view to render
(defroute "/jobs/:id" [uid]
  (println "HELLO WORLD")
  (println "id is" uid)
  (println (str "setting view to /jobs/" uid))
  (data/clicked-job "-Jeg6RtaRRpd3ft9323p")
  (data/set-view! job-view))

(defroute "/new/job" {}
  (println "setting view to /new/job")
  (data/set-view! new-post-view))

(defroute "/" {}
  (println "setting view to /..")
  (data/set-view! home-view))

(secretary/set-config! :prefix "#")

(defn app-view []
  (@data/current-view))

(secretary/dispatch! "/")
(reagent/render-component [app-view] (.getElementById js/document "app"))


