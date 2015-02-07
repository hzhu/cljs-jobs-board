(ns main.helper-functions
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as str]
            ))

;; Turns epoch into human readable date
(defn make-date [epoch]
  (subs (.toDateString (js/Date. epoch)) 4 10))

;; Given string, returns nil or valid URL.
(defn regex-url? [string] 
  (let [regex #"^[a-zA-Z0-9\-\.]+\.(com|org|me|io|net|co|edu|uk|ca|de|jp|fr|au|us|ru|ch|it|nl|se|no|es)$"
        value (str/lower-case string)
        nil-or-url (re-matches regex value)]
        (if (nil? nil-or-url)
          (println "this isn't a valid url")
          (str (nil-or-url 0)))))

;; Given value, returns nil or valid email.
(defn regex-email? [value]
  (re-matches #"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?" value))     
       
;; If string is email, returns an anchor tag.
(defn email-to-html [string]
  (cond
    (= string (regex-email? string)) (str "<a href=\"mailto:" string "\">" string "</a>")
    (= string (regex-url? string))   (str "<a href=" string ">" string "</a>")
    (nil? (regex-url? string)) (str string)
    (nil? (regex-email? string)) (str string)))

;; Returns original string with HTML transformed emails
(defn transform-email [string]
   (let [splitted (str/split string #" ")]
     (println splitted)
     (str/join " " (vec (map email-to-html splitted)))
   ))
