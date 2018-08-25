(ns trabajo-saluble.core
  (:require [svm.core :as svm])
    (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  
	; Load the heart scale example dataset.
	(def dataset (svm/read-dataset "data/heartscale"))

	; Train a SVM model.
	(def model (svm/train-model dataset))

	; Get the feature map you want to predict.
	(def feature (last (first dataset)))
	;=> {1 0.708333, 2 1.0, 3 1.0, ...}

	; Label it.
	(svm/predict model feature)
	;=> 1.0
	)