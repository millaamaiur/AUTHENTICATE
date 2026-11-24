# AUTHENTICATE — Prototipo 2 (AUTHENTICUS 2)

Solución de fichaje laboral del Ayuntamiento de Donostia/San Sebastián, desarrollada para la asignatura **Diseño de Software** (Universidad de Deusto).

Un trabajador pasa su tarjeta por una puerta. El servicio de autentificación comprueba su cara con el servicio de verificación facial y, si es correcta, registra el fichaje en el servicio central. El servicio central valida al usuario, guarda los datos en el servicio de almacenamiento y genera los informes.

## Servicios

El sistema son cuatro servicios independientes, cada uno en su carpeta y con su propio Gradle Wrapper:

| Carpeta | Puerto | Tecnología | Función |
|---|---|---|---|
| `servicio-central/` | 8080 | Spring Boot | API REST de usuarios, fichajes e informes. Habla con el almacenamiento por REST. |
| `servicio-almacenamiento/` | 8082 | Spring Boot + JPA + H2 | Persistencia de usuarios y fichajes. |
| `servicio-autentificacion/` | 8081 | Spring Boot | Dispositivo de la puerta: recibe la tarjeta y registra el fichaje en el central. |
| `servicio-verificacion-facial/` | 9090 | Java plano (sockets TCP) | Compara dos vectores biométricos y devuelve si coinciden. |

```text
Puerta (Postman) → servicio-autentificacion (8081) ──REST──→ servicio-central (8080) ──REST──→ servicio-almacenamiento (8082) → H2
                          │
                          └──sockets TCP──→ servicio-verificacion-facial (9090)
```

Cada servicio tiene su propio README con más detalle (autentificación y verificación facial).

## Requisitos

- Java 21
- No hace falta instalar Gradle: cada carpeta incluye su Gradle Wrapper.

## Cómo arrancarlo

El **orden importa**: el central necesita el almacenamiento en marcha, y la autentificación necesita el central y la verificación facial.

Abre una terminal por servicio. En Windows se usa `.\gradlew.bat`; en Linux o macOS, `./gradlew`.

```powershell
# 1. Almacenamiento (puerto 8082)
cd servicio-almacenamiento
.\gradlew.bat bootRun

# 2. Central (puerto 8080)
cd servicio-central
.\gradlew.bat bootRun

# 3. Verificación facial (puerto 9090), solo si se prueba la autentificación
cd servicio-verificacion-facial
.\gradlew.bat run

# 4. Autentificación (puerto 8081), solo si se prueba la puerta
cd servicio-autentificacion
.\gradlew.bat bootRun
```

Para probar el servicio central (usuarios, fichajes e informes) bastan los pasos 1 y 2.

Cada servicio con API REST tiene Swagger UI:

- Central: http://localhost:8080/swagger-ui/index.html
- Almacenamiento: http://localhost:8082/swagger-ui/index.html
- Autentificación: http://localhost:8081/swagger-ui/index.html

### Datos de prueba y base de datos

El almacenamiento guarda los datos en un fichero H2 (`./data/almacenamiento_db`), por lo que **se conservan al reiniciar**. La primera vez que arranca, con la base de datos vacía, carga 8 usuarios y sus fichajes (`config/DatosPrueba`).

Para volver a los datos iniciales, detén el almacenamiento y borra la carpeta `data/`. Esa carpeta se crea en el directorio desde el que se arranca el servicio y está excluida de git.

La consola H2 está habilitada en http://localhost:8082/h2-console (JDBC URL: `jdbc:h2:file:./data/almacenamiento_db`, usuario `sa`, sin contraseña).

## Servicio central

### Arquitectura

```text
Controlador (facade) → Servicio (service) → AccesoDatosCentral (access) → REST → servicio-almacenamiento
```

| Capa | Paquete | Responsabilidad |
|---|---|---|
| Controladores | `facade` | Recibir peticiones HTTP, llamar a los servicios y devolver el código de respuesta. |
| Servicios | `service` | Reglas de negocio e informes. |
| Acceso a datos | `access` | Llamar al servicio de almacenamiento por REST (`RestClient`). |
| Entidades | `entity` | `Usuario` y `Fichaje` (con el enum `TipoFichaje` dentro). |
| DTO | `dto` | Datos intercambiados con los clientes, documentados con `@Schema`. |

`AccesoDatosCentral` es un único `@Repository`, inyectado por constructor en los tres servicios. La dirección del almacenamiento se configura en `servicio-central/src/main/resources/application.properties`:

```properties
almacenamiento.url=http://localhost:8082
```

### Usuarios

| Método | Ruta | Operación | Respuestas |
|---|---|---|---|
| `POST` | `/usuarios` | Alta de un usuario. | `201`; `409` si el identificador ya existe |
| `PUT` | `/usuarios/{idTrabajador}/baja` | Baja: cambia el estado y registra la fecha de baja, conserva sus datos y fichajes. | `204`; `404` |
| `PUT` | `/usuarios/{idTrabajador}` | Modificación de los datos personales. | `204`; `404` |
| `GET` | `/usuarios/{idTrabajador}` | Consulta de un usuario. | `200`; `404` |
| `GET` | `/usuarios` | Consulta de todos los usuarios, también los de baja. | `200` |
| `DELETE` | `/usuarios/{idTrabajador}` | Eliminado: borra el usuario **y todos sus fichajes**. | `204`; `404` |

Ejemplo de alta (la fecha de alta y el estado los asigna el servidor):

```json
{
  "nombre": "Ane",
  "apellidos": "Etxeberria Lasa",
  "dni": "11111111A",
  "idTrabajador": "TRA001",
  "vectorBiometrico": "VB-0001"
}
```

Diferencia entre **baja** y **eliminado**: la baja deja al usuario en el sistema con estado `false` y conserva sus fichajes; el eliminado borra sus datos y todos sus fichajes.

### Fichajes

| Método | Ruta | Operación | Respuestas |
|---|---|---|---|
| `POST` | `/fichajes` | Registrar un evento de puerta. | `201`; `409` si se rechaza |

```json
{
  "idTrabajador": "TRA001",
  "idEdificio": "ED01",
  "idPuerta": "PU01",
  "timestamp": "2026-10-05T08:00:00"
}
```

El tipo lo asigna el servidor; el cliente no lo envía. Reglas:

- El usuario debe existir y estar de alta.
- El fichaje no puede ser anterior a la fecha de alta del usuario.
- El primer evento del día de un trabajador es `ENTRADA`; el segundo y los siguientes son `SALIDA`. Si hay más de dos eventos en un día, el último cuenta como salida.

### Informes

```http
GET /informes?inicio=2026-10-05&fin=2026-10-09
```

Las fechas, en formato `AAAA-MM-DD`, son obligatorias e incluyen ambos extremos. Un único informe devuelve:

- `altas`: trabajadores dados de alta en el periodo.
- `bajas`: trabajadores dados de baja en el periodo (los eliminados no aparecen).
- `fichajesCorrectosPorTrabajador`: días correctos de cada trabajador activo. Un día es **correcto** si tiene exactamente dos fichajes y entre ambos pasan al menos ocho horas.

Respuestas: `200`; `400` si alguna fecha tiene un formato incorrecto.

## Servicio de almacenamiento

Base de datos del sistema. Solo lo usa el servicio central. Todas las rutas empiezan por `/almacenamiento`.

| Método | Ruta | Operación | Respuestas |
|---|---|---|---|
| `POST` | `/almacenamiento/usuarios` | Guarda un usuario (lo crea o actualiza). | `201` |
| `GET` | `/almacenamiento/usuarios` | Todos los usuarios. | `200` |
| `GET` | `/almacenamiento/usuarios/{idTrabajador}` | Un usuario. | `200`; `404` |
| `DELETE` | `/almacenamiento/usuarios/{idTrabajador}` | Borra el usuario y sus fichajes (en una transacción). | `204`; `404` |
| `POST` | `/almacenamiento/fichajes` | Guarda un fichaje. | `201` |
| `GET` | `/almacenamiento/fichajes/{idTrabajador}?dia=AAAA-MM-DD` | Fichajes de un trabajador; `dia` es opcional. | `200` |
| `GET` | `/almacenamiento/fichajes?inicio=AAAA-MM-DD&fin=AAAA-MM-DD` | Fichajes de todos entre dos fechas. | `200` |

Los datos viajan como DTOs (`UsuarioDTO`, `FichajeDTO`), no como entidades JPA.

## Datos de prueba

Se cargan 8 usuarios: seis de alta y dos de baja. Sirven para probar el informe del **05/10/2026 al 09/10/2026**.

| Usuario | Situación | Días correctos |
|---|---|---|
| TRA001 — Ane | Activa desde antes del periodo | 2 |
| TRA002 — Jon | Alta el 06/10 | 2 |
| TRA003 — Miren | Baja el 08/10 | no cuenta; aparece en bajas |
| TRA004 — Iker | Baja el 15/09 | no aparece |
| TRA005 — Leire | Un día con tres fichajes | 1 |
| TRA006 — Maialen | Sin fichajes | 0 |
| TRA007 — Unai | Alta el 09/10, sin fichajes | 0 |
| TRA008 — Nerea | Un día de exactamente ocho horas | 1 |

Para ese intervalo, el informe debe dar: altas `TRA002` y `TRA007`; bajas `TRA003`; y `TRA001: 2`, `TRA002: 2`, `TRA005: 1`, `TRA006: 0`, `TRA007: 0`, `TRA008: 1`.

## Pruebas

Tests del servicio central, que usan una versión en memoria de `AccesoDatosCentral` y no necesitan el almacenamiento:

```powershell
cd servicio-central
.\gradlew.bat test
```

Prueba manual recomendada, con almacenamiento y central arrancados:

1. Generar el informe del 05/10 al 09/10 y comparar con la tabla anterior.
2. Dar de alta un usuario, fichar con él y consultarlo.
3. Darlo de baja y comprobar que sigue en `GET /usuarios` y que ya no puede fichar.
4. Eliminarlo (`DELETE`) y comprobar que `GET /usuarios/{id}` da `404` y que sus fichajes han desaparecido.
5. Registrar tres fichajes de un trabajador el mismo día: los tres dan `201` y ese día no cuenta como correcto en el informe.

## Tecnologías

Java 21 · Spring Boot 4.1.2 · Gradle Wrapper · springdoc-openapi 2.3.0 · H2.

## Equipo

Equipo 10 — Diseño de Software, Universidad de Deusto, campus de Donostia.

| Servicio | Responsable |
|---|---|
| Servicio central | Amaiur Milla |
| Servicio de almacenamiento | Paul (rehecho por Amaiur Milla en el prototipo 2) |
| Servicio de autentificación | Julen |
| Servicio de verificación facial | Airam |
