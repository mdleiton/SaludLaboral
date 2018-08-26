(ns trabajo-saluble.core
  (:require [svm.core :as svm])
  (:import (javax.swing SwingUtilities JFrame JLabel JTextField JButton JPanel JOptionPane  JTextArea)
           (java.awt BorderLayout)
           (java.awt FlowLayout)
           (java.awt.event ActionListener))
  (:gen-class))

(defn -main [& args]
  
    (def dataset (svm/read-dataset "data/entrenamiento"))

    (def model (svm/train-model dataset))

    (def datasetDePrueba (svm/read-dataset "data/prueba"))
    
    ; CONTADOR se lo llama con (inc-counter)
    (def counter (atom 0))
    (defn inc-counter []
    (swap! counter inc))

    (def feature (last (first datasetDePrueba)))

    (doseq [item datasetDePrueba]
        (if (= (str (int (svm/predict model (last item)))) (str (first item)))
            (inc-counter))
    )

    (print (str
        @counter
        " pruebas exitosas de clasificacion de un total de "
        (count datasetDePrueba)
        "\n"
    ))

    ;Interfaz 
)