(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]
            [main.data :as data]
            [hickory.core :refer [as-hiccup parse parse-fragment]]
            [secretary.core :as secretary :include-macros true :refer [defroute]]

            [dommy.utils :as utils]
            [dommy.core :as dommy]
            [dommy.core :refer-macros [sel sel1]]

            [clojure.string :as str]

            [main.domready :as domready]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [ajax.core :as ajax])
  ;(:use-macros [dommy.macros :only [node deftemplate]]) // breaks
  (:import goog.History))

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






;; grab collection from fb and set-list!  (listens to Firebase for changes in data)
(let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings")]
  (.on fb "value" #(data/set-list! (.val %))))

;(let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings-dev")]
;  (.on fb "value" #(data/set-list! (.val %))))

;; helper, parse html into hiccup
(defn to-html [content]
  (as-hiccup (parse content)))

;; NEW JOB POST VIEW
(defn new-post-view []
  (defn handle-input-update [event]
    (let [value     (aget event "target" "value")
          className (aget event "target" "className")
          output    (transform-email value)]
          (data/setter className output)))

  (defn handle-contenteditable-update [event]
    (let [value     (aget event "target" "innerHTML")
          className (aget event "target" "className")]
       (data/setter className value))
    (println "blurred out of job description")
    )

  (defn text-ctrl [event]
    (.preventDefault event)
    (let [cmd (aget event "target" "dataset" "role")]
      (.execCommand js/document cmd false null)))

  [:div#new-job-view
   [:div#forms
       [:a.routes {:href "#/"} "Back to Home Page"]

       [:div.input-infos
          [:div.box-extra
            [:label "Hostel name:"
            [:input.hostel_name {:type "text" :on-change #(handle-input-update %)}]]
            [:p.example "Enter your hostel's name."]]

          [:div.box-extra
            [:label "Job title:"
            [:input.job_title   {:type "text" :on-change #(handle-input-update %)}]]
            [:p.example "'Front desk receptionist' or 'Cleaner'"]]

          [:div.box-extra
            [:label "Location:"
            [:input.location    {:type "text" :on-change #(handle-input-update %)}]]
            [:p.example "\"San Francisco, CA\"", " or  \"Berlin, Germany\""]]

          [:div.box-extra
            [:label "Email:"
            [:input.email      {:type "text" :on-change #(handle-input-update %)}]]]

          [:div.box-extra
            [:label "Website:"
              [:input.website  {:type "text" :on-change #(handle-input-update %)}]]
              [:p.example "Example: http://www.pactradewinds.com"]]]



       [:div.description-container
         [:label "Job description:"]
         [:div.text-controls
          [:a.bold       {:href "#" :on-click #(text-ctrl %) :data-role "bold"                } "Bold"]
          [:a.italic     {:href "#" :on-click #(text-ctrl %) :data-role "italic"              } "Italic"]
          [:a.bulletlist {:href "#" :on-click #(text-ctrl %) :data-role "insertOrderedList"   } "Numbers"]
          [:a.numberlist {:href "#" :on-click #(text-ctrl %) :data-role "insertUnorderedList" } "Bullets"]
         ]]

       [:div.job_description   {:contentEditable true
                                :on-blur #(handle-contenteditable-update %)}]

       [:label "How to apply:"
         [:br]
        ;[:input.how {:type "textarea" :on-change #(handle-input-update %)}]
        [:textarea.how {:cols 4 :rows 3 :on-change #(handle-input-update %)}]

        ]


       [:a.next-step.routes {:on-click
                #(doseq [todo  (sel :#forms)
                         todo2 (sel :.preview-view)]
                         (dommy/remove-class! todo2 :hidden)
                         (dommy/add-class! todo :hidden))
                } "PREVIEW MY JOB POST!"]
     ]

    [:div (preview-view)]
  ])

;; PREVIEW VIEW
(defn preview-view []
  [:div.preview-view.hidden
   [:a.routes {:on-click #(doseq [todo  (sel :#forms)
                                  todo2 (sel :div.preview-view)]
                            (dommy/add-class! todo2 :hidden)
                            (dommy/remove-class! todo :hidden))
               } "Go Back and Edit job"]
     (let [previewData (data/new-post)]
       [:div
         [:div.title (previewData "job_title")]
         [:div.date "POSTED " (.slice (.toDateString (js/Date.)) 4 10)]

         [:div.name (previewData "hostel_name")]
         [:div.location (previewData "location")]
         [:div.website (previewData "website")]


         (println (type (previewData "how")))
         (println (previewData "job_description"))


         [:div#job-description (map as-hiccup (parse-fragment (previewData "job_description")))]


         [:div.apply
           [:h3 "APPLY FOR THIS HOSTEL JOB"]
           [:p.how (map as-hiccup (parse-fragment (previewData "how")))]]

         (let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings")]
           [:a#submit {:href "#/" :on-click #(data/post2fb fb)} "submit"])

;          (let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings-dev")]
;            [:a#submit {:href "#/" :on-click #(data/post2fb fb)} "submit"])
       ])])

;; JOB VIEW
(defn job-view [uid]
  [:div#job-view
    [:div.header-container
     [:a.routes {:href "#/"}]
    ]

    (if (empty? (data/get-list!))
      (println "True. Atom is empty. Do not start rendering.")
      (render-jobs-list uid))])

(defn make-date [epoch]
  (subs (.toDateString (js/Date. epoch)) 4 10))

(defn render-jobs-list [uid]
  (let [job (data/clicked-job uid)]
    [:div.job-view
     [:div.info-container
      [:div.title (job "job_title")]
      [:div.date "POSTED " (make-date (job "create_date"))]
      [:div.name (job "hostel_name")]
      [:div.location (job "location")]
      [:a.website {:href (job "website")} (job "website")]]

     [:div#job-description (map as-hiccup (parse-fragment (job "job_description")))]

     [:div.apply
        [:h3 "APPLY FOR THIS HOSTEL JOB"]
        [:p.how (map as-hiccup (parse-fragment (transform-email (job "how"))))]
        ]]))

(defn home-view-item [data]
  (let [[uid hostelData] data
        target (str "/jobs/" uid)]
    [:li
      [:a {:href (str "#" target)}
        [:div.name (hostelData "hostel_name")]
        [:div.location [:b"@ "] (hostelData "location")]
        [:div.title (hostelData "job_title")]
        [:div.date (make-date (hostelData "create_date"))]]]
 ))

;; HOME VIEW
(defn home-view []
  [:div.home
   [:h1 "HOSTEL JOBS BOARD"]

   [:div#post-new-job
     [:a.routes {:href "#/new/job"} "POST A NEW JOB"]]

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
  ;; think about clearing post atom here.
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



