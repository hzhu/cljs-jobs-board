(ns main.domready
  (:require [reagent.core :as reagent :refer [atom]]
            [dommy.utils :as utils]
            [dommy.core :as dommy]
            [dommy.core :refer-macros [sel sel1]]
            [ajax.core :as ajax]))

(enable-console-print!)

;; Handle 3rd Party iOS Browsers
(defn do-not-support[browser]
  (let [uAstring (.match(.-userAgent (.-navigator js/window)) browser)]
    (println uAstring)
    (if (= uAstring nil)
      (println "browser is supported")
      (if (> (.-length uAstring) 0)
        (doseq [new-job  (sel :#post-new-job)]
          (js/alert "Your browser is not fully supported. Use Safari :)")
          (dommy/add-class! new-job :hidden))))))

;; Add functions to be run onready
(defn doc-ready-handler []
  (let [ready-state (. js/document -readyState)]
    (if (= "complete" ready-state)
      (do
        (do-not-support "CriOS")
        ))))

;; Add handler to onreadystatechange
(defn on-doc-ready []
  (aset js/document "onreadystatechange" doc-ready-handler ))

;; Invoke onready functions
(on-doc-ready)

