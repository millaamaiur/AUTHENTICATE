# AUTHENTICATE — Servicio Central

Prototipo **AUTHENTICUS 1** del proyecto AUTHENTICATE, de la asignatura Diseño de Software de la Universidad de Deusto.

El servicio central recibe fichajes de las puertas, gestiona los trabajadores y genera informes de asistencia. La API puede utilizarse desde Swagger UI o Postman.

Los datos se almacenan en memoria mediante `Storage` y se pierden al reiniciar la aplicación.

## Tecnologías

Versiones declaradas en la configuración del proyecto:

- Java 21.
- Spring Boot 4.1.2.
- Gradle Wrapper 9.8.2.
- springdoc-openapi 2.3.0.

## Ejecución

El proyecto Gradle está dentro de la carpeta `authenticate`.

En Windows, desde la raíz del repositorio:

```powershell
cd authenticate
.\gradlew.bat bootRun
```

En Linux o macOS:

```bash
cd authenticate
./gradlew bootRun
```

Con la configuración actual, el servidor utiliza el puerto 8080:

- Aplicación: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Especificación OpenAPI: http://localhost:8080/v3/api-docs

## Arquitectura

Las peticiones siguen este recorrido:

```text
Controlador → Servicio → AccesoDatosCentral → Storage
```

| Capa | Paquete | Responsabilidad |
|---|---|---|
| Controladores | `facade` | Recibir peticiones HTTP, llamar a los servicios y devolver respuestas. |
| Servicios | `service` | Aplicar las reglas de negocio y generar informes. |
| Acceso a datos | `access` | Centralizar las consultas y modificaciones de los datos. |
| Entidades | `entity` | Representar los usuarios y sus fichajes. |
| DTO | `dto` | Representar los datos intercambiados mediante la API. |

### Instancia única de AccesoDatosCentral

`AccesoDatosCentral` se declara como un bean de Spring mediante `@Repository`.

Su alcance es singleton por defecto: dentro del mismo contexto de aplicación, los tres servicios reciben la misma instancia mediante inyección por constructor.

No se crean instancias manualmente con `new AccesoDatosCentral(...)`.

## Modelo de datos

### Usuario

Cada usuario contiene:

- `nombre`
- `apellidos`
- `dni`
- `idTrabajador`
- `vectorBiometrico`
- `fecAlta`
- `fecBaja`
- `estado`

El estado se representa mediante un booleano:

- `true`: usuario de alta y activo.
- `false`: usuario de baja e inactivo.

La fecha de alta se asigna en el servidor al crear el usuario. Inicialmente, la fecha de baja es `null`.

Dar de baja a un usuario cambia su estado y registra la fecha de baja. No elimina al usuario ni sus fichajes. Si ya estaba de baja, se conserva la fecha original.

No se mantiene un histórico separado de movimientos de alta y baja. Las entidades `MovimientoUsuario` y `TipoMovimientoUsuario` se han retirado.

Este prototipo no implementa una operación de eliminación definitiva ni de reactivación. El alta rechaza un identificador que ya exista, aunque el usuario esté de baja.

### Fichaje

Cada fichaje contiene:

- `idTrabajador`
- `idEdificio`
- `idPuerta`
- `timestamp`
- `tipo`

`TipoFichaje` es un enum definido dentro de `Fichaje`, con los valores:

```java
ENTRADA
SALIDA
```

El servidor asigna el tipo; el cliente no lo envía.

## API

### Usuarios

| Método | Ruta | Operación | Respuestas |
|---|---|---|---|
| `POST` | `/usuarios` | Crear un usuario activo. | `201` creado; `409` si el identificador ya existe. |
| `PUT` | `/usuarios/{idTrabajador}/baja` | Dar de baja sin borrar sus datos ni fichajes. | `204` si existe, incluso si ya estaba de baja; `404` si no existe. |
| `PUT` | `/usuarios/{idTrabajador}` | Modificar los datos personales y biométricos. | `204` modificado; `404` si no existe. |
| `GET` | `/usuarios/{idTrabajador}` | Consultar un usuario. | `200` encontrado; `404` si no existe. |
| `GET` | `/usuarios` | Consultar todos los usuarios, incluidos los de baja. | `200`. |

Ejemplo de alta:

```json
{
  "nombre": "Ane",
  "apellidos": "Etxeberria Lasa",
  "dni": "11111111A",
  "idTrabajador": "TRA001",
  "vectorBiometrico": "VB-0001"
}
```

El mismo formato se utiliza para modificar los datos de un usuario. La modificación conserva el identificador indicado en la ruta, las fechas y el estado.

Las fechas y el estado se gestionan internamente en `Usuario`; no forman parte del `UsuarioDTO` actual.

### Fichajes

| Método | Ruta | Operación | Respuestas |
|---|---|---|---|
| `POST` | `/fichajes` | Registrar un evento de puerta. | `201` registrado; `409` rechazado. |

Ejemplo:

```json
{
  "idTrabajador": "TRA001",
  "idEdificio": "ED01",
  "idPuerta": "PU01",
  "timestamp": "2026-10-05T08:00:00"
}
```

La fecha y hora se envían en formato ISO 8601, sin zona horaria.

Reglas de registro:

- El usuario debe existir y estar activo.
- El fichaje no puede ser anterior a su fecha de alta.
- El primer evento registrado para un trabajador en un día se marca como `ENTRADA`.
- El segundo se marca como `SALIDA`.
- Un tercer evento para ese trabajador en el mismo día se rechaza.

### Informes

```http
GET /informes?inicio=2026-10-05&fin=2026-10-09
```

Los parámetros `inicio` y `fin` son obligatorios y utilizan el formato `AAAA-MM-DD`.

El informe incluye ambos extremos del intervalo y devuelve:

- `altas`: identificadores cuya fecha de alta está en el periodo.
- `bajas`: identificadores cuya fecha de baja está en el periodo.
- `fichajesCorrectosPorTrabajador`: número de días correctos de cada usuario actualmente activo.

Un usuario puede aparecer tanto en altas como en bajas si ambas fechas están dentro del periodo.

Un día es correcto cuando contiene exactamente dos fichajes y entre ambos transcurren al menos ocho horas.

Los usuarios activos sin días correctos aparecen con valor `0`. Los usuarios actualmente de baja quedan fuera de este recuento, aunque se conservan sus fichajes históricos.

La respuesta correcta utiliza el código `200`.

## Documentación OpenAPI

Los siete endpoints tienen `@Operation` con un resumen y una descripción.

Los DTO `UsuarioDTO`, `FichajeDTO` e `InformeDTO` utilizan `@Schema` para documentar las clases y sus campos, con ejemplos.

En esta versión no se utilizan `@ApiResponse` ni `@ApiResponses`. Los códigos de respuesta se describen en este README.

## Datos de prueba

`Storage` carga ocho usuarios: seis activos y dos de baja.

Las fechas y los fichajes permiten probar el informe del **05/10/2026 al 09/10/2026**.

| Usuario | Situación | Días correctos esperados |
|---|---|---|
| TRA001 — Ane | Activa desde antes del periodo. | 2 |
| TRA002 — Jon | Alta el 06/10/2026. | 2 |
| TRA003 — Miren | Baja el 08/10/2026. | No aparece en el recuento; aparece en bajas. |
| TRA004 — Iker | Baja el 15/09/2026. | No aparece en el recuento ni en las bajas del periodo. |
| TRA005 — Leire | Activa; tiene un día con tres eventos. | 1 |
| TRA006 — Maialen | Activa sin fichajes. | 0 |
| TRA007 — Unai | Alta el 09/10/2026, sin fichajes. | 0 |
| TRA008 — Nerea | Activa; tiene un día de exactamente ocho horas. | 1 |

Para ese intervalo:

- Altas: `TRA002` y `TRA007`.
- Bajas: `TRA003`.
- Recuento: `TRA001: 2`, `TRA002: 2`, `TRA005: 1`, `TRA006: 0`, `TRA007: 0` y `TRA008: 1`.

El orden de los resultados no está garantizado.

Los datos se cargan directamente en memoria. Por eso incluyen un día con tres fichajes para comprobar que el informe lo descarta, aunque el servicio rechaza un tercer evento recibido por la API.

## Comprobaciones

Desde la carpeta `authenticate`, en Windows:

```powershell
.\gradlew.bat build
```

En Linux o macOS:

```bash
./gradlew build
```

Además de compilar y ejecutar las pruebas disponibles, la comprobación funcional debe incluir:

1. Abrir Swagger UI y revisar operaciones, parámetros y esquemas.
2. Consultar los usuarios de prueba.
3. Generar el informe del 05/10/2026 al 09/10/2026 y contrastar sus resultados.
4. Crear un usuario y comprobar que puede fichar.
5. Darlo de baja y comprobar que sigue siendo consultable.
6. Comprobar que sus fichajes se conservan y que se rechazan nuevos fichajes.
7. Repetir la baja y verificar que mantiene su fecha original.

## Equipo

Equipo 10 — Diseño de Software, Universidad de Deusto, campus de Donostia.