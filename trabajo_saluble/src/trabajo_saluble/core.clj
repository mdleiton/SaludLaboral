(ns trabajo-saluble.core
  (:require [svm.core :as svm])
  (:import (javax.swing SwingUtilities JFrame JLabel JTextField JButton JPanel JOptionPane  JTextArea)
           (java.awt BorderLayout)
           (java.awt FlowLayout)
           (java.awt.event ActionListener)
           (java.awt GridLayout))
  (:gen-class))

(defn -main [& args]
    
    (print "Cargando archivo para entrenamiento...\n")
    (def dataset (svm/read-dataset "data/entrenamiento"))
    (print "Carga del archivo finalizada!\n")

    (print "Entrenando...\n")
    (def model (svm/train-model dataset))
    (print "Entrenamiento finalizado!\n")

    (print "Cargando archivo para pruebas...\n")
    (def datasetDePrueba (svm/read-dataset "data/prueba"))
    (print "Carga del archivo para pruebas finalizada!\n")

    ; CONTADOR se lo llama con (inc-counter)
    (def counter (atom 0))
    (defn inc-counter []
    (swap! counter inc))

    ;(def feature (last (first datasetDePrueba)))

    (print "Realizando las pruebas...\n")
    (doseq [item datasetDePrueba]
        (if (= (str (int (svm/predict model (last item)))) (str (first item)))
            (inc-counter))
    )
    (print "pruebas finalizadas!\n\n")

    (print (str
        "Resultados:\n "
        @counter
        " pruebas exitosas de clasificacion de un total de "
        (count datasetDePrueba)
        ",\n "
        "despues de haber realizado un entrenamiento con "
        (count dataset)
        " datos.\n"
    ))

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
             nil  (str (svm/predict model {1 (Double/parseDouble (.getText txttemperatura)), 2 (Double/parseDouble(.getText txthumedad)), 3 (Double/parseDouble(.getText txtruido)) }))                  
                 )))]

        (doto f
          (.setLayout (GridLayout. 4 2 25 6))
          (.add labeltemperatura)
          (.add txttemperatura)  
          (.add labelhumedad)
          (.add txthumedad)
          (.add labelruido)
          (.add txtruido)
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