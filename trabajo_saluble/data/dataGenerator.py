from random import randint

MAXTEMP = 29
MAXHUME = 65
MAXRUID = 55
NUMVECTORES = 10000

datosDeEntrenamiento={'+1':[],'-1':[]}
datosDePrueba={'+1':[],'-1':[]}

def generaTemperatura():
    return randint(23,40)
def generaHumedad():
    return randint(45,90)  
def generaRuido():
    return randint(20,75)

def determinaClase(temperatura,humedad,ruido):
    
    tempMolesta = False
    humeMolesta = False
    ruidMolesta = False
    countMolestias = 0

    if temperatura > MAXTEMP:
        tempMolesta = True
        countMolestias += 1
    if humedad > MAXHUME:
        humeMolesta = True
        countMolestias += 1
    if ruido > MAXRUID:
        ruidMolesta = True
        countMolestias += 1

    if countMolestias == 3:
        return "-1"
    elif countMolestias == 2:
        if tempMolesta and humeMolesta:
            return "-1"
        else:
            return "+1"
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
fileEntrenamiento = open("entrenamiento","a")
fileEntrenamiento.truncate(0)
filePrueba = open("prueba","a")
filePrueba.truncate(0)

countVectores = 0
while countVectores<NUMVECTORES:
    datos = vectorCarasteristicasMasClase()
    vectorCaracteristicas = datos[0]
    clase = datos[1]
    #busca en datosDeEntrenamiento
    if noEstaRepetido(diccionario=datosDeEntrenamiento,vectorCaracteristicas=vectorCaracteristicas,clase=clase):
        #el 80% debe de ser de entrenamienti, el otro de prueba
        #busca en datosDePrueba despues de verificar que no esta en datosDeEntrenamiento
        if countVectores >= int(NUMVECTORES * 0.8)-1:
            if noEstaRepetido(diccionario=datosDePrueba,vectorCaracteristicas=vectorCaracteristicas,clase=clase):
                datosDePrueba[clase].append(vectorCaracteristicas)
                newline = clase +" 1:"+ str(vectorCaracteristicas[0]) +" 2:"+ str(vectorCaracteristicas[1]) +" 3:"+ str(vectorCaracteristicas[2])
                # print("Prueba        | " + newline )
                filePrueba.write(newline+"\n")
                countVectores +=1
        else:
            datosDeEntrenamiento[clase].append(vectorCaracteristicas)
            newline = clase +" 1:"+ str(vectorCaracteristicas[0]) +" 2:"+ str(vectorCaracteristicas[1]) +" 3:"+ str(vectorCaracteristicas[2])
            # print("Entrenamiento | " + newline )
            fileEntrenamiento.write(newline+"\n")
            countVectores +=1

fileEntrenamiento.close()
filePrueba.close()
