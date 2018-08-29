# Proyecto de la materia de lenguajes de programación #

2018-1T

Asignación estratégica de tiempo de descanso e interacción social basado en parámetros ambientales del ambiente laboral.

### Descripción ###

* El proyecto tiene como objetivo desarrollar una aplicación que determine la condición ambiental laboral actual (adecuada, no-adecuada-th, no-adecuada-ruido) determinada por los siguientes parámetros: Temperatura, Humedad, Nivel de Ruido. Los datos serán obtenidos de una base de datos provista para este objetivo.

### Estado actual ###
    Aplicación deskop

La aplicación con 10000 registros de temperatura, humedad, ruido entrena y genera un modelo para predecir 5 estados/categorías de condición ambiental laboral.

*   1. adecuado            +1.  
*   2. no adecuado por t   -1.  
*   3. no adecuado por h   -2.  
*   4. no adecuado por r   -3.  
*   5. no adecuado         -4.  

Esto se lo hace con la libreria svm-clojure. 

Inmediatamente se lanza un servidor en clojure en la red local. Se debería modificar la IP actual a la IP de su red. (se realizaron pruebas con en la red local propia con IP 192.168.0.100 y puerto 8080). 
Actualmente esta configurada (trabajo_saludable/src/trabajo_saludable/core.clj linea 27 )  con ip 127.0.0.1 y puerto 8080. No se podrá utilizar la aplicación movil pero podría revisar desde un navegador web la respuesta a las solicitudes que se recibirían desde la aplicación movil. (ver http://127.0.0.1:8080/). 
   
El servidor se encarga de responder a todas las solicitudes get con una cadena de texto que contiene un id, categoria y 3 sugerencias.
Este servidor es utilizado por la aplicación movil.

Luego de esto se presenta una interfaz con dos opciones:

*   Ver los resultados del entrenamiento. Para lo cual se utilizan aproximadamente 2000 registros diferentes a los de entrenamiento para probar la correcta predicción.
        Se presenta la cantidad de aciertas del modelo sobre el total de pruebas. 
*   Ingresar un dato de temperatura, ruido y humedad. Una vez ingresados se utiliza el modelo para predecir a que categoría pertenecen esos datos.
    Luego se grafican los datos de pruebas. Un grafico de dos dimensiones en el eje X los datos de temperatura y en el eje Y los datos de ruido. Cada registro de categoria se lo grafica con diferente color.
        Luego se muestra la predicción del modelo y tres sugerencias aleatorias correspodientes a la categoria. Las sugerencias se cargan desde un archivo json (trabajo_saludable/data/sugerencias.json).
    En el servidor se actualiza en mensaje de respuesta con los mismo datos que se presentaron anteriormente.
        Luego de esto se actualiza en la interfaz principal el estado actual.

Los 10000 registros de entrenamiento y los datos de prueba y testeo se los generó con un script en python (trabajo_saludable/data/data_generator.py).
Revisar capturas

    Aplicación movil
La aplicación para android presenta una interfaz muy sencilla. Esta aplicación con un hilo, cada 10 segundos solicita al servidor la categoria actual. Con el id del mensaje se verifica que no se repitan las notificaciones. Se genera una notificación por cada entrada de datos del usuario en la interfaz.
Una vez que se recibe una respuesta con un id nuevo se lanza una notificación y se agrega a una lista de estados. cada elemento de la lista contiene la categoria, 3 sugerencias y la fecha.
En esta version la url a la que realiza consultas es http://192.168.0.100:8080/. Se espera mejorar esto para que sea configurable desde la app. 

Revisar capturas

### Dependencias ###

dependencias clojure

*   org.clojure/clojure "1.8.0"
*   tw.edu.ntu.csie/libsvm "3.17"
*   org.clojure/data.csv "0.1.3"
*   net.mikera/core.matrix "0.45.0"
*   org.clojure/clojure-contrib "1.2.0"
*   clatrix "0.5.0"
*   incanter "1.5.7"
*   http-kit "2.2.0"

dependecias App movil:

* https://github.com/erf/http-request 


Revisar capturas

### Autores ###

* Lama Luis [github](https://github.com/luislama) [bitbucket](https://bitbucket.org/luislama/)

* Leiton Mauricio [github](https://github.com/mdleiton) [bitbucket](https://bitbucket.org/mdleiton/)


