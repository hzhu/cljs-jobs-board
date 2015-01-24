(ns main.post
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :as ajax]))

(enable-console-print!)

;;   navigator.userAgent.match('CriOS');


;; Handle 3rd Party iOS Browsers
(defn do-not-support[browser]
  (let [uAstring (.match(.-userAgent (.-navigator js/window)) browser)]
    (println uAstring)
    (if (= uAstring nil)
      (println "browser is supported")
      (if (> (.-length uAstring) 0)
        (js/alert "Your browser is not supported.")
        )
    )
   )
  )

(do-not-support "CriOS")


