(ns trabajo-saluble.core
  (:require [svm.core :as svm])
  (:import (javax.swing SwingUtilities JFrame JLabel JTextField JButton JPanel JOptionPane  JTextArea)
           (java.awt BorderLayout)
           (java.awt FlowLayout)
           (java.awt.event ActionListener)
           (java.awt GridLayout))
  (:gen-class))

(defn -main [& args]
  
    ; Load the heart scale example dataset.
    (def dataset (svm/read-dataset "data/prueba"))

    ; Train a SVM model.
    (def model (svm/train-model dataset))

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
          ; labeltitle (JLabel. "**********Sistema basado en parámetros ambientales del ambiente laboral.*********")
          labelhumedad (JLabel. "Humedad:    ")
          labeltemperatura (JLabel. "Temperatura:")
          labelruido (JLabel. "Ruido:      ")
          txttemperatura (JTextField. "" 25)
          txthumedad (JTextField. "" 25)
          txtruido (JTextField. "" 25)        
          buttonverificacion (JButton. "Verificar condiciones" )    
          labelestadoactual(JLabel. "Estado actual:")
          

          f (proxy [JFrame ActionListener]
          [] ; 
  
          (actionPerformed [e] ; nil below is the parent component
            (JOptionPane/showMessageDialog
             nil  (str (svm/predict model {1 (Double/parseDouble (.getText txttemperatura)), 2 (Double/parseDouble(.getText txtruido)), 3 (Double/parseDouble(.getText txthumedad)) }))                  
                 )))]

        (doto f
          (.setLayout (GridLayout. 4 2 25 10))
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