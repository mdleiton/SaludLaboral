(ns trabajo-saluble.core
  (:require [svm.core :as svm])
  (:import (javax.swing SwingUtilities JFrame JLabel JTextField JButton JPanel JOptionPane  JTextArea)
           (java.awt BorderLayout)
           (java.awt FlowLayout)
           (java.awt.event ActionListener))
  (:gen-class))

(defn -main [& args]
  
    ; Load the heart scale example dataset.
    (def dataset (svm/read-dataset "data/entrenamiento"))

    ; Train a SVM model.
    (def model (svm/train-model dataset))

    ; Get the feature map you want to predict.
    (def datasetDePrueba (svm/read-dataset "data/prueba"))
    
    (def counter (atom 0))
    (defn inc-counter []
    (swap! counter inc))

    ; (for [x datasetDePrueba] )

    (def feature (last (first datasetDePrueba)))
    ;=> {1 0.708333, 2 1.0, 3 1.0, ...}

    (print (first datasetDePrueba))
    (let [[x y] (first datasetDePrueba)]
        (print x))

    ; Label it.
    ; (print (svm/predict model feature))
    ;=> 1.0

    ;Interfaz 

    (let [  f (JFrame. "Tiempo de Descanso")
          ; labeltitle (JLabel. "**********Sistema basado en parámetros ambientales del ambiente laboral.*********")
          labelhumedad (JLabel. "Humedad:")
          labeltemperatura (JLabel. "Temperatura:")
          labelruido (JLabel. "Ruido:")
          txttemperatura (JTextField. "" 25)
          txthumedad (JTextField. "" 25)
          txtruido (JTextField. "" 25)        
          buttonverificacion (JButton. "Verificar condiones" )        
  
          panel (JPanel.)
          panelTemperatura (JPanel.)
          panelHumedad (JPanel.)
          panelRuido (JPanel.)
          panelBoton (JPanel.)
          f (proxy [JFrame ActionListener]
          [] ; 
  
          (actionPerformed [e] ; nil below is the parent component
            (JOptionPane/showMessageDialog
             nil  (str "agregar funcion de predicion")                  
                 )))]

        (doto panelTemperatura
          (.add labeltemperatura)
          (.add txttemperatura))

        (doto panelHumedad
          (.add labelhumedad)
          (.add txthumedad))

        (doto panelRuido
          (.add labelruido)
          (.add txtruido))
        
        (doto panelBoton
            (.add buttonverificacion))

        (doto panel
            (.add panelTemperatura)
            (.add panelHumedad)
            (.add panelRuido)
            (.add panelBoton))   
        
        (doto f
          (.setContentPane panel)    
          (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
          (.pack)
          (.setVisible true))

        (.setSize f 400 150)

        (.addActionListener buttonverificacion f)
       
        f 
    )
)