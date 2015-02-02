(ns main.views.preview
  (:require [reagent.core :as reagent :refer [atom]]
                        [main.data :as data]
                        
                        [dommy.utils :as utils]
                        [dommy.core :as dommy]
                        [dommy.core :refer-macros [sel sel1]]

                        [hickory.core :refer [as-hiccup parse parse-fragment]]
                        [main.helper-functions :as helpers]))

  (enable-console-print!)

  ;; Transforms string HTML into Hiccup.
  (defn to-html [content]
    (as-hiccup (parse content)))

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

         (if (= js/production true)
           ;; Post job to production Firebase.
           (let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings")]
           [:a#submit.btn {:href "#/" :on-click #(data/post2fb fb)} "submit"])          
           ;; Post job to development Firebase.
           (let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings-dev")]
            [:a#submit.btn {:href "#/" :on-click #(data/post2fb fb)} "submit"]))  
       ])])