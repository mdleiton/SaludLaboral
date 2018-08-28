(ns trabajo-saluble.sugerencias
  (:require [svm.core :as svm] 
            [clojure.data.json :as json])
  (:use clojure.java.io)
  (:gen-class))

(defn  getsugerencias
  [idCategoria]
  ;; carga los datos del json con las recomendaciones
   (def all-records (json/read-str (slurp "data/recomendaciones.json")))
   
   ;;obtiene las dos ultimas sugerencias(3,4) y aleatoriamente escoge entre la primera y la segunda sugerencia por categoria. esto se hace debido a la poca cantidad de sugerencias que se tienen. 
  (case idCategoria 
    1 (str "Sugerencias: \n 1." (get (get all-records "+1") "3") "\n  2." (get (get all-records "+1") "4") "\n  3." (get (get all-records "+1")  (str (+ (rand-int 2) 1) )  ))
    -1 (str "Sugerencias: \n 1." (get (get all-records "-1") "3") "\n  2." (get (get all-records "-1") "4") "\n  3." (get (get all-records "-1")  (str (+ (rand-int 2) 1) )  ))
    -2 (str "Sugerencias: \n 1." (get (get all-records "-2") "3") "\n ." (get (get all-records "-2") "4") "\n  3." (get (get all-records "-2")  (str (+ (rand-int 2) 1) )  ))
    -3 (str "Sugerencias: \n 1." (get (get all-records "-3") "3") "\n  2." (get (get all-records "-3") "4") "\n  3." (get (get all-records "-3")  (str (+ (rand-int 2) 1) )  ))
    (str "Sugerencias: \n 1." (get (get all-records "-4") "3") "\n  2." (get (get all-records "-4") "4") "\n  3." (get (get all-records "-4")  (str (+ (rand-int 2) 1) )  ))
  ))


;; obtiene el nombre de la categoria y llama a la funcion getsugerencias para obtener 3 sugerencias.
(defn  getmensaje
  [model, txttemperatura , txtruido, txthumedad]
  (def codigo (svm/predict model {1 (Double/parseDouble txttemperatura), 2 (Double/parseDouble txtruido), 3 (Double/parseDouble txthumedad) })) 
  (case codigo 
    1.0 (str "Categoria: adecuado" "\n" (getsugerencias 1) )
    -1.0 (str "Categoria: no adecuado por temperatura." "\n" (getsugerencias -1))
    -2.0 (str "Categoria: no adecuado por Humedad." "\n" (getsugerencias -2))
    -3.0  (str "Categoria: no adecuado por ruido. " "\n" (getsugerencias -3))
    (str "Categoria: no adecuado " "\n" (getsugerencias -4)))
  )

;; sirve para obtener el nombre de la categoria
(defn  getestado
  [model, txttemperatura , txtruido, txthumedad]
  (def codigo (svm/predict model {1 (Double/parseDouble txttemperatura), 2 (Double/parseDouble txtruido), 3 (Double/parseDouble txthumedad) })) 
  (case codigo 
    1.0 (str "Estado actual: adecuado"  )
    -1.0 (str "Estado actual: no adecuado por temperatura.")
    -2.0 (str "Estado actual: no adecuado por Humedad." )
    -3.0  (str "Estado actual: no adecuado por ruido. " )
    (str "Estado actual: no adecuado " ))
  )
