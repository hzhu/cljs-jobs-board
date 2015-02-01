(ns main.views.new-job
  (:require [reagent.core :as reagent :refer [atom]]
                        [main.data :as data]
                        
                        [dommy.utils :as utils]
                        [dommy.core :as dommy]
                        [dommy.core :refer-macros [sel sel1]]
                        
                        [main.views.preview :refer [dooop]]

                        [hickory.core :refer [as-hiccup parse parse-fragment]]
                        [main.helper-functions :as helpers]))

(enable-console-print!)

(defn to-html [content]
  (as-hiccup (parse content)))
  
  (defn handle-input-update [event]
    (let [value     (aget event "target" "value")
          className (aget event "target" "className")
          output    (helpers/transform-email value)]
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
  
  
(defn new-job-view []
  [:div#new-job-view
   [:div#forms

       [:div.header-container
        [:a.routes {:href "#/"}]

        ]

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

       [:div.how-input
        [:label "How to apply:"
         [:br]
         [:textarea.how {:cols 4 :rows 3 :on-change #(handle-input-update %)}]]

        ]



       [:a.next-step.btn {:on-click
                #(doseq [todo  (sel :#forms)
                         todo2 (sel :.preview-view)]
                         (dommy/remove-class! todo2 :hidden)
                         (dommy/add-class! todo :hidden))
                } "PREVIEW MY JOB POST!"]
     ]

    ;[:div (preview-view)]
     (dooop)
  ]
  )