# Servicio de Autentificacion (Edge / puerta)

Prototipo AUTHENTICUS 2. Servicio Spring Boot que se ejecuta en el dispositivo de cada puerta.

## Arranque

Desde esta carpeta, en Windows:

```powershell
.\gradlew.bat bootRun
```

- Servicio: http://localhost:8081
- Swagger UI: http://localhost:8081/swagger-ui/index.html

## API

### `POST /autentificacion`

Simula el paso de la tarjeta desde Postman o Swagger. Recibe el identificador del
trabajador y el vector biometrico de referencia de la tarjeta.

```json
{
  "idTrabajador": "TRA001",
  "vectorReferencia": [0.15, -0.42, 0.88, 0.31, -0.09]
}
```

Respuesta: `true` si la verificacion facial y el registro del fichaje son correctos;
`false` en caso contrario.

En Java los dos vectores se representan como `double[]`. El DTO los recibe como un
array JSON de numeros.

## Flujo

1. `ControladorTarjetaUsuario` recibe los datos de la tarjeta.
2. `ServicioTarjetaUsuario` coordina la autentificacion.
3. `ServicioBiometrico` pide a `AccesoCapturaCara` el vector de comprobacion.
4. `AccesoCapturaCara` lee `captura_rostro.json`.
5. `AccesoVerificacionFacial` envia los dos vectores por sockets al servicio de
   verificacion facial y recibe `true` o `false`.
6. Si la verificacion es correcta, `AccesoServicioCentral` envia el fichaje a
   `POST /fichajes` del servicio central y devuelve su resultado.

## Configuracion

`src/main/resources/application.properties`:

| Propiedad | Uso |
|---|---|
| `server.port` | Puerto del servicio (8081). |
| `edge.edificio.id` | Identificador del edificio de esta puerta. |
| `edge.puerta.id` | Identificador de la puerta. |
| `edge.rostro.path` | Archivo JSON con el vector biometrico capturado. |
| `verificacion.host` | Host del servicio de verificacion facial. |
| `verificacion.port` | Puerto del servicio de verificacion facial (9090). |
| `central.url` | URL base del servicio central. |

## Protocolo de sockets

El servicio de verificacion facial de Efraín usa `BufferedReader` y `PrintWriter`.
El cliente debe enviar dos lineas y leer una:

```text
vectorReferencia (valores separados por comas)
vectorComprobacion (valores separados por comas)
```

El servidor responde una linea con `true` o `false`.

## Pruebas

Desde esta carpeta, en Windows:

```powershell
.\gradlew.bat test
```

Las pruebas comprueban la lectura del JSON, el orden y el formato de los dos
vectores enviados por socket, y que un resultado facial `false` no llama al
Servicio Central. Tambien se comprueba que el fichaje se construye con
`idTrabajador`, `idEdificio`, `idPuerta` y `timestamp`.
