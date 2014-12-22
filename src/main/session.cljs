(ns firebase.session
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

;; ----------
;; State
(def app-state (atom {}))

(defn printAtom []
  (println "::::::::::ATOM::::::::::")
  (println "::::::::::::::::::::::::")
  (println (get-in @app-state ""))
  (println "::::::::::::::::::::::::")
  (println "::::::::::::::::::::::::"))

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