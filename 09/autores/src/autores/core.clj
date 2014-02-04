(ns autores.core
  (:require [crouton.html   :as html]
            [clojure.pprint :as pp]))


(defn- body
(defn -main [& args]
  (let [content ((html/parse "http://www.casadocodigo.com.br") :content)]
    (doseq [x content]
      (println "-------------------")
      (println x))
    (println "------------------------------")
    (println (map map? content))
    ))
