(ns trabajo-saluble.core
  (:require [svm.core :as svm] [clojure.core.matrix :as mtx]
            [incanter.io :as inio] [incanter.charts :as incharts]
            [incanter.stats :as instats] [incanter.datasets :as indatasets] [incanter.core :as incore])
  (:import (javax.swing SwingUtilities JFrame JLabel JTextField JButton JPanel JOptionPane  JTextArea)
           (java.awt FlowLayout BorderLayout GridLayout)
           (java.awt.event ActionListener))
  (:use (incanter core stats charts datasets))
  (:gen-class))

(defn -main [& args]
  
    ; Load the heart scale example dataset.
    ;(def dataset (svm/read-dataset "data/prueba"))

    ; Train a SVM model.
    ;(def model (svm/train-model dataset))

    ; Get the feature map you want to predict.
    ;(def datasetDePrueba (svm/read-dataset "data/prueba"))
    
    ;(def counter (atom 0))
    ;(defn inc-counter []
    ;(swap! counter inc))

    ; (for [x datasetDePrueba] )

    ;(def feature (last (first datasetDePrueba)))
    ;=> {1 0.708333, 2 1.0, 3 1.0, ...}

    ;(print (first datasetDePrueba))
    ;(let [[x y] (first datasetDePrueba)]
     ;   (print x))

    ; Label it.
    ; )
    ;=> 1.0

    ;Interfaz 

    (let [  f (JFrame. "Tiempo de Descanso")
          labelhumedad (JLabel. "Humedad:    ")
          labelpruebas(JLabel. "Resultados de predicción")
          labeltemperatura (JLabel. "Temperatura:")
          labelruido (JLabel. "Ruido:      ")
          txttemperatura (JTextField. "" 25)
          txthumedad (JTextField. "" 25)
          txtruido (JTextField. "" 25)        
          buttonpruebas (JButton. "Ver pruebas" )
          buttonverificacion (JButton. "Verificar condiciones" )    
          labelestadoactual(JLabel. "Estado actual:")
          

          f (proxy [JFrame ActionListener]
          [] ; 
  
          (actionPerformed [e] ; nil below is the parent component
            ;(JOptionPane/showMessageDialog
             ;nil  (str (svm/predict model {1 (Double/parseDouble (.getText txttemperatura)), 2 (Double/parseDouble(.getText txtruido)), 3 (Double/parseDouble(.getText txthumedad)) }))                  
              ;   )


            (def data (inio/read-dataset "lluvia.csv" :header true))
            ;Humedad
            

                  (def iris (to-matrix data))
                  ;(view iris)

                  (def X (sel iris :cols (range 4)))
                  (def species (sel iris :cols 4))
                  (def pca (principal-components X))
                  (def components (:rotation pca))
                  (def pc1 (sel components :cols 0))
                  (def pc2 (sel components :cols 1))
                  (def x1 (mmult X pc1))
                  (def x2 (mmult X pc2))

                  (view (scatter-plot x1 x2
                                  :group-by species
                                  :x-label "PC1"
                                  :y-label "PC2"
                                  :title "Iris PCA"
                                  :legend true) 
                  )

            




            ))]

          (.addActionListener
            buttonpruebas
            (reify ActionListener
            (actionPerformed
             [_ evt]
              (let [  fa (JFrame. "Información de predicción")
                    labelpruebas(JLabel. "Resultados de predicción")]                    

                  (doto fa
                    (.setLayout (GridLayout. 5 2 25 10))
                    (.add labelpruebas)
                  
                    
                    (.setResizable false)
                    (.setVisible true))
                    (.setSize fa 450 250)     
                  fa
              )

             )))

            
        (doto f
          (.setLayout (GridLayout. 5 2 25 10))
          (.add labelpruebas)
          (.add buttonpruebas)
          (.add labeltemperatura)
          (.add txttemperatura)  
          (.add labelruido)
          (.add txtruido)
          (.add labelhumedad)
          (.add txthumedad)
          (.add buttonverificacion)
          (.add labelestadoactual)
        
          (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
          (.setResizable false)
          (.setVisible true))
          (.setTitle f "sistema de predicción")
          (.setSize f 450 250)
          (.addActionListener buttonverificacion f)
       
        f 
    )
   
)