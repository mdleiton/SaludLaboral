(ns trabajo-saluble.core
    (:require[svm.core :as svm] [clojure.core.matrix :as mtx]
            [incanter.io :as inio] [incanter.charts :as incharts]
            [incanter.stats :as instats] [incanter.datasets :as indatasets] [incanter.core :as incore])
    (:import(javax.swing SwingUtilities JFrame JLabel JTextField JButton JPanel JOptionPane  JTextArea)
            (java.awt FlowLayout BorderLayout GridLayout)
            (java.awt.event ActionListener))
    (:use (incanter core stats charts datasets) org.httpkit.server)
    (:gen-class))

(defn app [request]
    (with-channel request channel
    (on-close channel (fn [status] (println "channel closed: " status)))
    (on-receive channel (fn [data] ;; echo it back
                            (send! channel data)))))
  
(defonce server (atom nil))

(defn stop-server []
    (when-not (nil? @server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@server :timeout 100)
    (reset! server nil)))

(defn sugerencias
    [idCategoria]
    (case idCategoria 
         1 (str "sugerencia 1")
        -1 (str "sugerencia -1")
        -2 (str "sugerencia -2")
        -3 (str "sugerencia -3")
        (str "sugerencia -4")))

(defn getmensaje
    [model, txttemperatura , txtruido, txthumedad]
    (def codigo (svm/predict model {1 (Double/parseDouble txttemperatura), 2 (Double/parseDouble txtruido), 3 (Double/parseDouble txthumedad) })) 
    (case codigo 
         1.0 (str "Categoria: adecuado" "\n" (sugerencias 1) )
        -1.0 (str "Categoria: no adecuado por temperatura." "\n" (sugerencias -1))
        -2.0 (str "Categoria: no adecuado por Humedad." "\n" (sugerencias -2))
        -3.0  (str "Categoria: no adecuado por ruido. " "\n" (sugerencias -3))
        (str "Categoria: no adecuado " "\n" (sugerencias -4))))

(defn getestado
    [model, txttemperatura , txtruido, txthumedad]
    (def codigo (svm/predict model {1 (Double/parseDouble txttemperatura), 2 (Double/parseDouble txtruido), 3 (Double/parseDouble txthumedad) })) 
    (println codigo)
    (case codigo 
         1.0 (str "Estado actual: adecuado"  )
        -1.0 (str "Estado actual: no adecuado por temperatura.")
        -2.0 (str "Estado actual: no adecuado por Humedad." )
        -3.0  (str "Estado actual: no adecuado por ruido. " )
        (str "Estado actual: no adecuado " )))

(defn -main [& args]
    ;(reset! server (run-server #'app {:address "192.168.0.100" :port 8080, :body "todo bajo control"}  ) )
    
    ; "data/entrenamiento" y "data/prueba" no tienen vectores en comun
    ; revisar "dataGenerator.py".
    ;   -no escribe vectores repetidos en ninguno de los archivos considerando los vectores de ambos
    ;   -80% de entrenamiento
    ;   -20% de prueba
    ;   -los vectores de entrenamiento tienen igual numero de vectores por cada categoria, en este 
    ;    caso tenemos 5 categorias, 16% de vectores para cada una. (16% * 5)
    (def datasetTrain (svm/read-dataset "data/entrenamiento"))

    (def model (svm/train-model datasetTrain))

    (def datasetDePrueba (svm/read-dataset "data/prueba"))

    ; CONTADOR se lo llama con (inc-counter)
    (def counter (atom 0))
    (defn inc-counter []
    (swap! counter inc))

    (print "Realizando las pruebas...\n")
    (doseq [item datasetDePrueba]
        (if (= (str (int (svm/predict model (last item)))) (str (first item)))
            (inc-counter)))

    (def resultadoPruebas 
        (str @counter
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
                    (def data (inio/read-dataset "data/paraGraficar.csv" :header true))
            
                        ;(def matrizdata (to-matrix data))

                        ;(def X (sel matrizdata :cols (range 3)))
                        ;(def categoria (sel matrizdata :cols 3))
                        ;(def pca (principal-components X))
                        ;(def components (:rotation pca))
                        ;(def temperatura (sel components :cols 0))
                        ;(def ruido (sel components :cols 2))
                        ;(def x1 (mmult X temperatura))
                        ;(def x2 (mmult X ruido))
                        ;(view (scatter-plot x1 x2 :group-by categoria :x-label "Temperatura" :y-label "Ruido" :title "Datos " :legend true))
                    (view (scatter-plot :temperatura :ruido :group-by :clase :x-label "Temperatura"
                                        :y-label "Ruido" :title "Datos " :legend true :data data))
            
                    (doto labelestadoactual (.setText (getestado model (.getText txttemperatura) (.getText txtruido) (.getText txthumedad))))
                    (JOptionPane/showMessageDialog
                        nil  (getmensaje model (.getText txttemperatura) (.getText txtruido) (.getText txthumedad)))
                    (doto txttemperatura (.setText ""))
                    (doto txtruido (.setText ""))
                    (doto txthumedad (.setText ""))))]
            
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
