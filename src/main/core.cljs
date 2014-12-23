(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]
            [firebase.session :as session :refer [global-state global-put!]]
            [main.post :as post]
            [ajax.core :as ajax]))



; lots of weird shit happening here. Figure it out.
; Your input value is getting saved to Firebase & Atom at the same time. Hm
(defn on-change [event fb]

  (.set fb (clj->js {:text-from-app (-> event .-target .-value)})) ;save value to Firebase
  (.on fb "value" (fn [snapshot]

                    (global-put! :my-text ((js->clj (.val snapshot)) "text-from-app"))))
                     ; the first param being passed into global-put! is :my-text. Looks like it will be the name of the atom.
  (session/printAtom)
 )


(defn input-field [value fb] ;basically the entire logic of the input
  [:div
    [:input {:type "text"
            :value value
            :on-change #(on-change % fb)}]
    ]
)

(defn handle-input-update [event]
    (println (.-value (.-target event)))
    (println (.-className (.-target event)))
  )

(defn fireBasePlayground []
  [:div
    [:input.hostel_name     {:type "text" :placeholder "Hostel Name"     :on-change #(handle-input-update %) }]
    [:input.job_description {:type "text" :placeholder "Job description" :on-change #(handle-input-update %)}]
    [:input.location        {:type "text" :placeholder "Location"        :on-change #(handle-input-update %)}]
    [:input.email           {:type "text" :placeholder "Email"           :on-change #(handle-input-update %)}]
    [:input.website         {:type "text" :placeholder "website"         :on-change #(handle-input-update %)}]
   ]
  )

(defn submit []
  (let [fb (js/Firebase. "https://jobs-board.firebaseio.com/job-listings")]
    [:a {:href "#" :on-click #(session/send2fb fb)} "submit"]
    )
  )


(defn app-view []
	(let [fb (js/Firebase. "https://jobs-board.firebaseio.com/testing")]
   		[:div
     	[:h2 "Home PAge!"]
      
      [:div
      [:p "The value is now: " (global-state :my-text)]
      [:p "Change it here: " [input-field (global-state :my-text) fb]]] ;call input-field with two parameters.
      [fireBasePlayground]
      [submit]
  	 ])
 )

; POST -> FIREBASE




(reagent/render-component [app-view] (.getElementById js/document "app"))


