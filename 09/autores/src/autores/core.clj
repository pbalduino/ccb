(ns autores.core
  (:require [clojure.pprint :as pp]
            [net.cgrand.enlive-html :as en]
            [clojure.string :as str])
  (:import  [java.net URL]))

(defn- get-links [url]
  (let [links (-> url
                  URL. 
                  en/html-resource
                  (en/select [:body :section :a]))]
      (filter
        #(. % contains "livro")
        (map #(str url ((% :attrs) :href)) 
             links))))

(defn- get-author [url]
  (let [authors (-> url
                    URL.
                    en/html-resource
                    (en/select [:span.product-author-link]))]
    (str/split
      (str/replace
        (first
          ((first authors) :content))
        #"(\n|  )" "")
      #"(, | e )")))

(defn -main [& args]
  (println "Os autores com mais publicações na Casa do Código:")
  (pp/pprint
    (->> "http://www.casadocodigo.com.br"
         get-links
         (pmap get-author)
         flatten
         frequencies
         (sort-by last >)
         (partition-by last)
         first))
  (shutdown-agents))