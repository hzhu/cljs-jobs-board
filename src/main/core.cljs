(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]
            [main.data :as data]
            [hickory.core :refer [as-hiccup parse parse-fragment]]
            [secretary.core :as secretary :include-macros true :refer [defroute]]

            [dommy.utils :as utils]
            [dommy.core :as dommy]
            [dommy.core :refer-macros [sel sel1]]

            [clojure.string :as str]
            
            
            
            [main.views.home-view :refer [home-view]]
            [main.views.job-view :refer [job-view]]
            [main.views.some-view :refer [some-func]]

           

            [main.domready :as domready]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [ajax.core :as ajax]
            
            [main.helper-functions :as helpers])
  ;(:use-macros [dommy.macros :only [node deftemplate]]) // breaks
  (:import goog.History))


;; grab collection from fb and set-list!  (listens to Firebase for changes in data)
(let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings")]
  (.on fb "value" #(data/set-list! (.val %))))

;(let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings-dev")]
;  (.on fb "value" #(data/set-list! (.val %))))

;; helper, parse html into hiccup


;; PREVIEW VIEW
(defn preview-view []
  [:div.preview-view.hidden
   [:a.btn {:on-click #(doseq [todo  (sel :#forms)
                                  todo2 (sel :div.preview-view)]
                            (dommy/add-class! todo2 :hidden)
                            (dommy/remove-class! todo :hidden))
               } "Go Back and Edit job"]
     (let [previewData (data/new-post)]
       [:div
         [:div.info-container
          [:div.title (previewData "job_title")]
          [:div.date "POSTED " (.slice (.toDateString (js/Date.)) 4 10)]
          [:div.name (previewData "hostel_name")]
          [:div.location (previewData "location")]
          [:div.website (previewData "website")]]



         [:div#job-description (map as-hiccup (parse-fragment (previewData "job_description")))]


         [:div.apply
           [:h3 "APPLY FOR THIS HOSTEL JOB"]
           [:p.how (map as-hiccup (parse-fragment (previewData "how")))]]

         (let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings")]
           [:a#submit.btn {:href "#/" :on-click #(data/post2fb fb)} "submit"])

;          (let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings-dev")]
;            [:a#submit.btn {:href "#/" :on-click #(data/post2fb fb)} "submit"])
       ])])






;; ---------------------------------------------------------------------------------------
;; ROUTING ------------------------------------------------------------------------------
(secretary/set-config! :prefix "#")

(defroute "/jobs/:uid" [uid]
  (data/clicked-job uid)
  (data/set-view! #(job-view uid)))

(defroute "/new/job" {}
  (println "setting view to /new/job")
  (data/set-view! some-func))


(defroute "/" {}
  (println "setting view to /..")
  ;; think about clearing post atom here.
  ;; set view to home-view
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
  [:div.container
    [:h1.hidden {:on-click #(data/printAtom)} "show atom"]
    (@data/current-view)
   ]
 )

(reagent/render-component [app-view] (.getElementById js/document "app"))



