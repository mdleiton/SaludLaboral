from random import randint

# Numero de vectores, de este el 80% seran de entrenamiento, el resto sera de prueba
NUMVECTORES = 6250

# rangos para los parametros
MINTEMP = 21
MAXTEMP = 29
MINHUME = 35
MAXHUME = 55
MINRUID = 6
MAXRUID = 55

# Para poder verificar que cada nuevo vector, no haya sido generado antes
datosDeEntrenamiento={'+1':[],'-1':[],'-2':[],'-3':[],'-4':[]}
datosDePrueba={'+1':[],'-1':[],'-2':[],'-3':[],'-4':[]}

def generaTemperatura():
    return randint(-18,51)
def generaHumedad():
    return randint(0,101)  
def generaRuido():
    return randint(0,101)

# DESCRIPCION CATEGORIAS
# adecuado            +1
# no adecuado por t   -1
# no adecuado por h   -2
# no adecuado por r   -3
# no adecuado         -4
def determinaClase(temperatura,humedad,ruido):
    tempMolesta = False
    humeMolesta = False
    ruidMolesta = False
    countMolestias = 0
    # Verifica rangos
    if temperatura > MAXTEMP or temperatura < MINTEMP:
        tempMolesta = True
        countMolestias += 1
    if humedad > MAXHUME or humedad < MINHUME:
        humeMolesta = True
        countMolestias += 1
    if ruido > MAXRUID or ruido < MINRUID:
        ruidMolesta = True
        countMolestias += 1
    # Devuelve categoria
    if countMolestias == 3 or countMolestias == 2:
        return "-4"
    elif countMolestias == 1:
        if ruidMolesta:
            return "-3"
        elif humeMolesta:
            return "-2"
        else:
            return "-1"
    else:
        return "+1"

def vectorCarasteristicasMasClase():
    temperatura = generaTemperatura()
    humedad = generaHumedad()
    ruido = generaRuido()
    clase = determinaClase(temperatura=temperatura,humedad=humedad,ruido=ruido)
    vectorCaracteristicas = [temperatura,humedad,ruido]
    return [vectorCaracteristicas,clase]
        
def noEstaRepetido(diccionario,vectorCaracteristicas,clase):
    for vector in diccionario[clase]:
        if vector[0] == vectorCaracteristicas[0]:
            if vector[1] == vectorCaracteristicas[1]:
                if vector[2] == vectorCaracteristicas[2]:
                    return False
    return True

#------------------------------------------------------------
#--              PARA ESCRIBIR EN LOS ARCHIVOS             --
#------------------------------------------------------------

# Archivos de Entrenamiento y de Prueba
fileEntrenamiento = open("entrenamiento","a")
fileEntrenamiento.truncate(0)
filePrueba = open("prueba","a")
filePrueba.truncate(0)

# Para graficar 
fileParaGraficar = open("paraGraficar.csv","a")
fileParaGraficar.truncate(0)
header= "temperatura,humedad,ruido,clase"
fileParaGraficar.write(header+"\n")

# CONTROLAR que el numero de vectores por cada categoria sea el 16%,
# teniendo 5 categorias, tendriamos 16% * 5 = 80%.
# Luego de conseguir el 80% para entrenamiento, generar el restante
# 20% para datos de prueba.
countAdecuados = 0
countNoAdecuadoPorTemp = 0
countNoAdecuadoPorHume = 0
countNoAdecuadoPorRuid = 0
countNoAdecuados = 0

countDatosDeEntrenamiento = 0
countDatosDePrueba = 0

def generaVectoresEntrenamiento(claseDeseada, diccionarioDeEntrenamiento, archivoDeEntrenamiento, archivoParaGraficar):
    countVectores=0
    while countVectores < int(NUMVECTORES*0.16):
        datos = vectorCarasteristicasMasClase()
        vectorCaracteristicas = datos[0]
        clase = datos[1]
        # Si no es de la clase, va al siguiente ciclo sin aumentar el contador
        if clase != claseDeseada:
            continue
        # Si no se encuentra en el diccionarioDeEntrenamiento lo agrega al diccionarioDeEntrenamiento y a los archivos
        elif noEstaRepetido(diccionario=diccionarioDeEntrenamiento,vectorCaracteristicas=vectorCaracteristicas,clase=clase):     
            diccionarioDeEntrenamiento[clase].append(vectorCaracteristicas)
            archivoDeEntrenamiento.write( clase +" 1:"+ str(vectorCaracteristicas[0]) +" 2:"+ str(vectorCaracteristicas[1]) +" 3:"+ str(vectorCaracteristicas[2]) +"\n")
            archivoParaGraficar.write(str(vectorCaracteristicas[0])+","+str(vectorCaracteristicas[1])+","+str(vectorCaracteristicas[2])+","+clase+"\n")
            countVectores += 1
    # Cuando se ingresa un numero grande como NUMVECTORES, es mejor que se vayan imprimiendo, 
    # los valores para saber por donde va el proceso
    #         print(claseDeseada+" - "+str(countVectores)+"\n")
    # print(claseDeseada+" completos--------------------------------------------\n")
    return countVectores    
            
def generaVectoresDePrueba(MAX,diccionarioDePrueba,diccionarioDeEntrenamiento, archivoDePrueba, archivoParaGraficar):
    countVectores=0
    while countVectores < MAX:
        datos = vectorCarasteristicasMasClase()
        vectorCaracteristicas = datos[0]
        clase = datos[1]
        # Si no se encuentra en el diccionarioDeEntrenamiento avanza al siguiente condicional
        if noEstaRepetido(diccionario=diccionarioDeEntrenamiento,vectorCaracteristicas=vectorCaracteristicas,clase=clase):
            # Si tampoco se encuentra en el diccionarioDePrueba, entonces lo agrega al diccionarioDePrueba y a los archivos
            if noEstaRepetido(diccionario=diccionarioDePrueba,vectorCaracteristicas=vectorCaracteristicas,clase=clase):     
                diccionarioDePrueba[clase].append(vectorCaracteristicas)
                archivoDePrueba.write( clase +" 1:"+ str(vectorCaracteristicas[0]) +" 2:"+ str(vectorCaracteristicas[1]) +" 3:"+ str(vectorCaracteristicas[2]) +"\n")
                archivoParaGraficar.write(str(vectorCaracteristicas[0])+","+str(vectorCaracteristicas[1])+","+str(vectorCaracteristicas[2])+","+clase+"\n")
                countVectores += 1
    # Cuando se ingresa un numero grande como NUMVECTORES, es mejor que se vayan imprimiendo, 
    # los valores para saber por donde va el proceso
    #             print("prueba - "+str(countVectores)+"\n")
    # print("vectores de prueba completos---------------------------------------\n")
    return countVectores    

while countDatosDeEntrenamiento + countDatosDePrueba < NUMVECTORES:
    countNoAdecuadoPorTemp = generaVectoresEntrenamiento("-1",datosDeEntrenamiento,fileEntrenamiento,fileParaGraficar)
    countNoAdecuadoPorHume = generaVectoresEntrenamiento("-2",datosDeEntrenamiento,fileEntrenamiento,fileParaGraficar)
    countNoAdecuadoPorRuid = generaVectoresEntrenamiento("-3",datosDeEntrenamiento,fileEntrenamiento,fileParaGraficar)
    countAdecuados = generaVectoresEntrenamiento("+1",datosDeEntrenamiento,fileEntrenamiento,fileParaGraficar)
    countNoAdecuados = generaVectoresEntrenamiento("-4",datosDeEntrenamiento,fileEntrenamiento,fileParaGraficar)
    countDatosDeEntrenamiento = countAdecuados + countNoAdecuadoPorTemp + countNoAdecuadoPorHume + countNoAdecuadoPorRuid + countNoAdecuados
    countDatosDePrueba = generaVectoresDePrueba(NUMVECTORES-countDatosDeEntrenamiento,datosDePrueba,datosDeEntrenamiento,filePrueba,fileParaGraficar)

print("Vectores Generados\n")
print("  Entrenamiento\n")
print("    "+str(countAdecuados)+" con condiciones adecuadas\n")
print("    "+str(countNoAdecuadoPorTemp)+" con condiciones no adecuadas de temperatura\n")
print("    "+str(countNoAdecuadoPorHume)+" con condiciones no adecuadas de humedad\n")
print("    "+str(countNoAdecuadoPorRuid)+" con condiciones no adecuadas de ruido\n")
print("    "+str(countNoAdecuados)+" con condiciones no adecuadas \n")
print("    Total de vectores de entrenamiento: "+str(countDatosDeEntrenamiento)+"\n")
print("  Prueba\n")
print("    "+str(countDatosDePrueba)+ " vectores de prueba.\n")

fileEntrenamiento.close()
filePrueba.close()

fileParaGraficar.close()