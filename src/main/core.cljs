(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]
            [main.data :as data]
            [hickory.core :refer [as-hiccup parse parse-fragment]]
            [secretary.core :as secretary :include-macros true :refer [defroute]]
            [main.post :as post]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [ajax.core :as ajax])
  (:import goog.History))

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

(defn job-view [uid]

  (let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings/-Jf2PhXvQMs4eomrxRQe/createDate")]
    (.set fb (.-TIMESTAMP (.-ServerValue js/Firebase))
      #(println "Time recorded"))
    )



  [:div#job-view "JOB POST VIEW IS HEREEE!"

   (if (empty? (data/get-list!))
     (println "True. Atom is empty. Do not start rendering.")
     (render-jobs-list uid))

    [:a.routes {:href "#/"} "home page"]
  ]
)

(defn render-jobs-list [uid]
  (let [job (data/clicked-job uid)]
    [:div
     [:div (job "hostel_name")]
     [:div (job "job_title")]
     ;(println (map as-hiccup (parse-fragment "<h1>HELLO WORLD!</h1>")))
     [:div#job-description (map as-hiccup (parse-fragment (job "job_description")))]
     [:div (job "location")]
     [:div (job "email")]
     [:div (job "website")]]))

(defn home-view-item [data]
  (let [[uid hostelData] data
        target (str "/jobs/" uid)]
    [:li
      [:a {:href (str "#" target)}
        [:span (hostelData "hostel_name")]
        [:span (hostelData "job_title")]]]
 ))

(defn home-view []
  [:div.home
   [:h1 "WELCOME TO THE JOBS BOARD"]

   [:a.routes {:href "#/new/job"} "POST A NEW JOB"]

   [:ul (map home-view-item (data/get-list!))]])

;; ---------------------------------------------------------------------------------------
;; ROUTING ------------------------------------------------------------------------------
(secretary/set-config! :prefix "#")

(defroute "/jobs/:uid" [uid]
  (data/clicked-job uid)
  (data/set-view! #(job-view uid)))

(defroute "/new/job" {}
  (println "setting view to /new/job")
  (data/set-view! new-post-view))

(defroute "/" {}
  (println "setting view to /..")
  (data/set-view! home-view))

(doto (History.)
  (events/listen
    EventType/NAVIGATE
    (fn [event]
      (secretary/dispatch! (.-token event))))
  (.setEnabled true))

;; END OF ROUTING ------------------------------------------------------------------------
;; ---------------------------------------------------------------------------------------


;; RENDER VIEW
(defn app-view []
  [:div
    [:h1 {:on-click #(data/printAtom)} "show atom"]
    (@data/current-view)
   ]
 )

(reagent/render-component [app-view] (.getElementById js/document "app"))





