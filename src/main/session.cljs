(ns firebase.session
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

;; ----------
;; State
(def app-state (atom {"post" { "hostel_name" "oooods"
                               "job_description" "bbDoooooper. Cleaner."
                               "location" "San Franciaaweosme"
                               "email" "ptdubs@gmail.com"
                               "website" "ww:)jjw.pwbbbt.com"
                              }}))

(defn printAtom []
  (println "::::::::::ATOM::::::::::")
  (println "::::::::::::::::::::::::")
  (println (get-in @app-state ["post"]))
  (println "::::::::::::::::::::::::")
  (println "::::::::::::::::::::::::"))


(defn send2fb [fb]
  (def postMap (get-in @app-state ["post"]))
  (.push fb (clj->js postMap))
)

(defn setter [name value]
  (println "name:" name)
  (println "value:" value)
  (swap! app-state assoc-in ["post" name] value)
  (printAtom)
  (println "---SETTER---2")
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