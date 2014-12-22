(ns firebase.session
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

;; ----------
;; State
(def app-state (atom {"post" { :hostelname "Pacific Tradewinds"
                               :jobdescription "Doooooper. Cleaner."
                               :location "San Franciaaweosme"
                               :email "ptdubs@gmail.com"
                               :website "www.pwt.com"
                              }}))

(defn printAtom []
  (println "::::::::::ATOM::::::::::")
  (println "::::::::::::::::::::::::")
  (println (get-in @app-state ["post"]))
  (println "::::::::::::::::::::::::")
  (println "::::::::::::::::::::::::"))

(println (get-in @app-state ["post"]))


(defn send2fb [fb]
  (def postMap (get-in @app-state ["post"]))
  (.push fb (clj->js postMap))
)



;; ----------
;; Helper Functions
(defn global-state [k & [default]]
  (get @app-state k default))

(defn global-put! [k v] ;swaps the atom with k
  (println "this is K. The new item swapped into the atom.")
  (println k)
  (swap! app-state assoc k v))

(defn local-put! [a k v]
  (swap! a assoc k v))