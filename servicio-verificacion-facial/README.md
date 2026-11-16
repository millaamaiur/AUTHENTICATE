# Servicio de verificacion facial

Servicio del Prototipo 2 que recibe dos vectores biometricos por sockets y devuelve
si coinciden. Es una aplicacion Java 21 con Gradle; no utiliza REST ni Spring Boot.

## Arranque

Desde la carpeta `servicio-verificacion-facial`, en PowerShell:

```powershell
.\gradlew.bat run
```

Escucha en el puerto TCP **9090**. Para detenerlo, pulsa `Ctrl + C`.
El servicio de autentificacion debe configurar `verificacion.host` con la direccion
de esta maquina y `verificacion.port=9090`.

## Protocolo

Cada conexion procesa una comparacion. El cliente envia dos lineas, en este orden:

1. Vector de referencia de la tarjeta.
2. Vector capturado desde el archivo JSON.

Cada linea contiene numeros separados por comas, con punto decimal y sin corchetes.
Se permiten espacios alrededor de los numeros. Ambas lineas deben terminar con
un salto de linea. No se envian etiquetas ni el JSON completo.

Ejemplo de peticion:

```text
0.12,0.45,0.78
0.11,0.46,0.79
```

Respuesta:

```text
true
```

El servidor envia una sola linea con `true` o `false` y cierra la conexion.
El cliente puede utilizar `List<Double>` internamente: lo que recibe este servicio
es texto, que se convierte a `double[]`.

## Comparacion

- Los vectores deben tener la misma longitud y no estar vacios.
- Ningun componente puede ser `NaN` o infinito.
- Cada pareja de componentes debe tener una diferencia absoluta como maximo de
  `0.02`, con un margen numerico adicional de `1e-12` para compensar la precision
  de los `double`.
- Si alguna pareja supera ese umbral, la respuesta es `false`.

El margen permite aceptar, por ejemplo, `0.12` frente a `0.14`.
Es un criterio simplificado para el prototipo, no un algoritmo real de
reconocimiento facial.

## Errores y conexiones

Los textos vacios, componentes no numericos y componentes vacios (incluida una
coma final) producen una respuesta `false` cuando se han recibido las dos lineas.
Si el cliente termina su envio antes de completar las lineas, la entrada ausente
tambien se rechaza.

Cada lectura del socket tiene un tiempo de espera de **5000 milisegundos**.
Si se agota, el servidor registra el error y cierra la conexion **sin respuesta
booleana**. El cliente debe tratar el cierre sin respuesta como una verificacion
fallida. Este limite es de inactividad de lectura, no un plazo total de peticion.

El servidor continua aceptando conexiones y atiende cada cliente en un hilo.

## Pruebas y compilacion

```powershell
.\gradlew.bat test
.\gradlew.bat clean build
```

Las pruebas JUnit comprueban tolerancia, longitudes, vectores vacios, valores nulos,
`NaN` e infinitos. La prueba de integracion arranca el servidor real, verifica el
protocolo, las entradas incorrectas, el timeout y una nueva conexion tras el error.

**Antes de ejecutar las pruebas, detén cualquier servidor que use el puerto 9090.**
La prueba de integracion necesita ese puerto y detiene su propio proceso al finalizar.
El informe HTML se genera en `build/reports/tests/test/index.html`.
