(ns trabajo-saluble.core
  (:require [svm.core :as svm])
  (:import (javax.swing SwingUtilities JFrame JLabel JTextField JButton JPanel JOptionPane  JTextArea)
           (java.awt BorderLayout)
           (java.awt FlowLayout)
           (java.awt.event ActionListener))
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

    ;Interfaz 
  (let [f (JFrame. "Asignación estratégica de tiempo de descanso")
        labeltitle (JLabel. "**********Sistema basado en parámetros ambientales del ambiente laboral.*********")
        labelhumedad (JLabel. "Humedad:")
        labeltemperatura (JLabel. "Temperatura:")
        labelruido (JLabel. "Ruido:")
        txthumedad (JTextField. "" 25)
        txttemperatura (JTextField. "" 25)
        txtruido (JTextField. "" 25)        
        buttonverificacion (JButton. "Verificar condicional" )
        labelsugerencias (JLabel. "Segerencias:")
        labelestadoactual (JLabel. "Estado Actual:")
        textarea ( JTextArea. "" )
        panel (JPanel.) panel1 (JPanel.)
        f (proxy [JFrame ActionListener]
        [] ; 
        (actionPerformed [e] ; nil below is the parent component
        (JOptionPane/showMessageDialog
           nil (str "agregar funcion de predicion"))))]
    
    (doto panel
      (.add labeltitle)
      (.add labelsugerencias) (.add labelhumedad) (.add txthumedad) (.add labelsugerencias)
      (.add labeltemperatura) (.add txttemperatura) (.add textarea)
      (.add labelruido) (.add txtruido)
      (.add buttonverificacion))
    
    (doto f
      (.setContentPane panel)    
      (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
      (.pack)
      (.setSize 500 300)
      (. setResizable true)
      (.setVisible true))
    (.addActionListener buttonverificacion f)
  )


	)