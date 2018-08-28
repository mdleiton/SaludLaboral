(ns trabajo-saluble.core
  (:require [trabajo-saluble.sugerencias :as sugerencias]
            [svm.core :as svm] [clojure.core.matrix :as mtx]
            [incanter.io :as inio] [incanter.charts :as incharts]
            [incanter.stats :as instats] [incanter.datasets :as indatasets] [incanter.core :as incore]
            [clojure.data.json :as json])
  (:import (javax.swing SwingUtilities JFrame JLabel JTextField JButton JPanel JOptionPane  JTextArea)
           (java.awt FlowLayout BorderLayout GridLayout)
           (java.awt.event ActionListener))
  (:use (incanter core stats charts datasets) org.httpkit.server)
  (:gen-class))

; mensaje de respuesta del server. se actualiza.
(def es (str "1-Estado actual: Sin estado." ))
(def idmjs (int 2 ))
; servidor que recibe las consultas de la app movil.
(defn app [request]
 {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (str es) })
  
(defonce server (atom nil))

(defn -main [& args]
    ;; iniciando servidor que recibe las consultas de la app movil.
    (reset! server (run-server #'app {:address "192.168.0.100" :port 8080}  ) )
    
    ;; generación del modelo de predicción
    (def datasetTrain (svm/read-dataset "data/prueba2"))
    (def model (svm/train-model datasetTrain))
    (def datasetDePrueba (svm/read-dataset "data/prueba"))

    ;;pruebas de funcionalidad del modelos.
    ; CONTADOR se lo llama con (inc-counter)
    (def counter (atom 0))
    (defn inc-counter []
    (swap! counter inc))

    (doseq [item datasetDePrueba]
        (if (= (str (int (svm/predict model (last item)))) (str (first item)))
            (inc-counter)))

    (def resultadoPruebas (str @counter
        " pruebas exitosas de clasificacion de un total de " (count datasetDePrueba) ",\n"
        "despues de haber realizado un entrenamiento con " (count datasetTrain) " datos.\n"))

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
  
          (actionPerformed [e] 
            ;; validar input 
            (try
                ;; actualizar datos del servidor
               (def idmjs (+ idmjs 1))
              (def es (str (str idmjs) "-" (sugerencias/getmensaje model (.getText txttemperatura) (.getText txtruido) (.getText txthumedad))))
               ;; graficar datos  
              (def data (inio/read-dataset "data/paraGraficar.csv" :header true))
              (view (scatter-plot :temperatura :ruido :group-by :clase :x-label "Temperatura"
                             :y-label "Ruido" :title "Datos " :legend true :data data) )          
              ;; actualizar estado en la ventana principal (a lado del boton de verificacion)
              (doto labelestadoactual (.setText (sugerencias/getestado model (.getText txttemperatura) (.getText txtruido) (.getText txthumedad))))
              
              ;; presentacion de la predicción
              (JOptionPane/showMessageDialog nil  (sugerencias/getmensaje model (.getText txttemperatura) (.getText txtruido) (.getText txthumedad)))
              ;; limpiezando valores de los inputs
              (doto txttemperatura (.setText ""))
              (doto txtruido (.setText ""))
              (doto txthumedad (.setText ""))
            (catch Exception e 
              (JOptionPane/showMessageDialog nil  (str "ingrese valores válidos" ))
              ))
            ))]

          ;; funcion para presentar informacion de las pruebas boton (ver pruebas)
          (.addActionListener buttonpruebas
            (reify ActionListener
              (actionPerformed [_ evt] (JOptionPane/showMessageDialog nil resultadoPruebas ))))

        (doto f
            (.setLayout (GridLayout. 5 2 25 10))
            (.add labelpruebas)
            (.add buttonpruebas)
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
