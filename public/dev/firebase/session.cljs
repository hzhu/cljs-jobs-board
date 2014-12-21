(ns firebase.session
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

;; ----------
;; State
(def app-state (atom {}))

;; ----------
;; Helper Functions
(defn global-state [k & [default]]
  (get @app-state k default))

(println "::::::::::ATOM::::::::::")
(println "::::::::::::::::::::::::")
(println (get-in @app-state ""))
(println "::::::::::::::::::::::::")
(println "::::::::::::::::::::::::")

(defn global-put! [k v]
  (swap! app-state assoc k v))

(defn local-put! [a k v]
  (swap! a assoc k v))