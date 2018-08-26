from random import randint

MINTEMP = 21
MAXTEMP = 29
MINHUME = 35
MAXHUME = 55
MINRUID = 6
MAXRUID = 55
NUMVECTORES = 10000

datosDeEntrenamiento={'+1':[],'-1':[],'-2':[],'-3':[],'-4':[]}
datosDePrueba={'+1':[],'-1':[],'-2':[],'-3':[],'-4':[]}

def generaTemperatura():
    return randint(-18,70)
def generaHumedad():
    return randint(0,101)  
def generaRuido():
    return randint(0,101)

def determinaClase(temperatura,humedad,ruido):
    tempMolesta = False
    humeMolesta = False
    ruidMolesta = False
    countMolestias = 0

    # DESCRIPCION CATEGORIAS
    # adecuado            +1
    # no adecuado por t   -1
    # no adecuado po h    -2
    # no adecuado por r   -3
    # no adecuado         -4

    if temperatura > MAXTEMP or temperatura < MINTEMP:
        tempMolesta = True
        countMolestias += 1

    if humedad > MAXHUME or humedad < MINHUME:
        humeMolesta = True
        countMolestias += 1

    if ruido > MAXRUID or ruido < MINRUID:
        ruidMolesta = True
        countMolestias += 1

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

#-------------------------------------------------------------------
#--PARA VER EN CONSOLA Y COMPROBAR QUE NO SE GUARDAN LOS REPETIDOS--
#--   no se considera el valor de NUMVECTORES                     --
#--   puede comentar el print:vector para numeros grandes         --
#-------------------------------------------------------------------
# countDatosDeEntrenamiento = 0
# while countDatosDeEntrenamiento<20:
#     datos = vectorCarasteristicasMasClase()
#     vectorCaracteristicas = datos[0]
#     clase = datos[1]
#     if noEstaRepetido(diccionario=datosDeEntrenamiento,vectorCaracteristicas=vectorCaracteristicas,clase=clase):
#         datosDeEntrenamiento[str(clase)].append(vectorCaracteristicas)
#         print("clase: " + str(clase) + " - "+ str(vectorCaracteristicas))
#         countDatosDeEntrenamiento +=1
#     else:
#         print("clase: " + str(clase) + " - "+ str(vectorCaracteristicas))
#         print("repetido")


# countDatosDePrueba = 0
# while countDatosDePrueba<5:
#     datos = vectorCarasteristicasMasClase()
#     vectorCaracteristicas = datos[0]
#     clase = datos[1]
#     #busca en datosDeEntrenamiento
#     if noEstaRepetido(diccionario=datosDeEntrenamiento,vectorCaracteristicas=vectorCaracteristicas,clase=clase):
#         #busca en datosDePrueba
#         if noEstaRepetido(diccionario=datosDePrueba,vectorCaracteristicas=vectorCaracteristicas,clase=clase):
#             datosDePrueba[str(clase)].append(vectorCaracteristicas)
#             print("clase: " + str(clase) + " - "+ str(vectorCaracteristicas))
#             countDatosDePrueba +=1
#     else:
#         print("clase: " + str(clase) + " - "+ str(vectorCaracteristicas))
#         print("repetido")


#------------------------------------------------------------
#--              PARA ESCRIBIR EN LOS ARCHIVOS             --
#------------------------------------------------------------

# Para que el algoritmo entrene
fileEntrenamiento = open("entrenamiento","a")
fileEntrenamiento.truncate(0)
filePrueba = open("prueba","a")
filePrueba.truncate(0)

# Para graficarlos
# fileAdecuado = open("adecuado.csv","a")
# fileAdecuado.truncate(0)
# fileNoAdecuadoPorTemp = open("noAdecuadoPorTemp","a")
# fileNoAdecuadoPorTemp.truncate(0)
# fileNoAdecuadoPorHume = open("noAdecuadoPorHume","a")
# fileNoAdecuadoPorHume.truncate(0)
# fileNoAdecuadoPorRuid = open("noAdecuadoPorRuid","a")
# fileNoAdecuadoPorRuid.truncate(0)
# fileNoAdecuado = open("noAdecuado.csv","a")
# fileNoAdecuado.truncate(0)

# header= "temperatura,humedad,ruido"
# fileAdecuado.write(header+"\n")
# fileNoAdecuadoPorTemp.write(header+"\n")
# fileNoAdecuadoPorHume.write(header+"\n")
# fileNoAdecuadoPorRuid.write(header+"\n")
# fileNoAdecuado.write(header+"\n")

# diccionarioCSV = {'+1':fileAdecuado,'-1':fileNoAdecuadoPorTemp,'-2':fileNoAdecuadoPorHume,'-3':fileNoAdecuadoPorRuid,'-4':fileNoAdecuado}


# Para graficar 
fileParaGraficar = open("paraGraficar.csv","a")
fileParaGraficar.truncate(0)
header= "temperatura,humedad,ruido,clase"
fileParaGraficar.write(header+"\n")

countVectores = 0
while countVectores<NUMVECTORES:
    datos = vectorCarasteristicasMasClase()
    vectorCaracteristicas = datos[0]
    clase = datos[1]
    #busca en datosDeEntrenamiento
    if noEstaRepetido(diccionario=datosDeEntrenamiento,vectorCaracteristicas=vectorCaracteristicas,clase=clase):
        #el 80% debe de ser de entrenamiento, el otro de prueba
        #busca en datosDePrueba despues de verificar que no esta en datosDeEntrenamiento
        if countVectores >= int(NUMVECTORES * 0.8)-1:
            if noEstaRepetido(diccionario=datosDePrueba,vectorCaracteristicas=vectorCaracteristicas,clase=clase):
                datosDePrueba[clase].append(vectorCaracteristicas)
                newlineRN = clase +" 1:"+ str(vectorCaracteristicas[0]) +" 2:"+ str(vectorCaracteristicas[1]) +" 3:"+ str(vectorCaracteristicas[2])
                # print("Prueba        | " + newlineRN )
                filePrueba.write(newlineRN+"\n")
                # diccionarioCSV[clase].write(str(vectorCaracteristicas[0])+","+str(vectorCaracteristicas[1])+","+str(vectorCaracteristicas[2])+"\n")
                fileParaGraficar.write(str(vectorCaracteristicas[0])+","+str(vectorCaracteristicas[1])+","+str(vectorCaracteristicas[2])+","+clase+"\n")
                countVectores +=1
        else:
            datosDeEntrenamiento[clase].append(vectorCaracteristicas)
            newlineRN = clase +" 1:"+ str(vectorCaracteristicas[0]) +" 2:"+ str(vectorCaracteristicas[1]) +" 3:"+ str(vectorCaracteristicas[2])
            # print("Entrenamiento | " + newlineRN )
            fileEntrenamiento.write(newlineRN+"\n")
            # diccionarioCSV[clase].write(str(vectorCaracteristicas[0])+","+str(vectorCaracteristicas[1])+","+str(vectorCaracteristicas[2])+"\n")
            fileParaGraficar.write(str(vectorCaracteristicas[0])+","+str(vectorCaracteristicas[1])+","+str(vectorCaracteristicas[2])+","+clase+"\n")
            countVectores +=1

fileEntrenamiento.close()
filePrueba.close()

# fileAdecuado.close()
# fileNoAdecuadoPorTemp.close()
# fileNoAdecuadoPorHume.close()
# fileNoAdecuadoPorRuid.close()
# fileNoAdecuado.close()

fileParaGraficar.close()