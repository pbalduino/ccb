(ns grokpodcast.core
  (:require [clojure.pprint :as pp]
            [net.cgrand.enlive-html :as en]))

(def file-dir "C:\\Documents and Settings\\pbalduino\\Meus documentos\\grokpodcast")

(defn- get-links [url]
  "meh")

(defn -main [& args]
  (pp/pprint
    (-> "http://www.grokpodcast.com/arquivo/"
        get-links)))