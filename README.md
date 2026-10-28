# AUTHENTICATE — Servicio Central

Prototipo **AUTHENTICUS 1** del proyecto AUTHENTICATE (Diseño de Software, Universidad de Deusto).

Este prototipo implementa el **servicio central** del sistema de control de accesos. Recibe los fichajes de las puertas, gestiona los usuarios (trabajadores) y genera informes de asistencia. La API se valida con Swagger / Postman.

> En este prototipo **no hay base de datos**. Los datos se guardan en memoria en la clase `Storage`, dentro de la capa de acceso, y se pierden al reiniciar.

---

## Tecnologías

- Java 21
- Spring Boot 4
- Gradle (wrapper incluido)
- springdoc-openapi (Swagger UI)

## Cómo ejecutarlo

El proyecto Gradle está en la carpeta `authenticate/`.

```bash
cd authenticate
./gradlew bootRun        # Linux / macOS
gradlew.bat bootRun      # Windows
```

El servidor arranca en `http://localhost:8080`.

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **Especificación OpenAPI:** http://localhost:8080/v3/api-docs

---

## Arquitectura

El servicio central sigue una arquitectura en tres capas. Cada petición atraviesa siempre **controlador → servicio → acceso**.

```
                ┌──────────────────────── ServicioCentral ────────────────────────┐
 Puerta  ──►    │  FichajesController  ──►  FichajesService  ──┐                  │
 App     ──►    │  UsuariosController  ──►  UsuariosService  ──┼──► AccesoDatosCentral ──► Storage
 App     ──►    │  InformesController  ──►  InformesService  ──┘                  │
                └─────────────────────────────────────────────────────────────────┘
```

| Capa | Paquete | Responsabilidad |
|---|---|---|
| Controlador | `facade` | Recibe las peticiones HTTP, llama al servicio y devuelve el código HTTP. Sin lógica de negocio. |
| Servicio | `service` | Toda la lógica de negocio: validaciones, entrada/salida, cálculo de informes. |
| Acceso | `access` | Único punto de acceso a los datos. `AccesoDatosCentral` guarda y consulta; `Storage` mantiene los datos en memoria. |
| Entidades | `entity` | `Usuario` y `Fichaje`: cómo se guardan los datos internamente. |
| DTOs | `dto` | `UsuarioDTO`, `FichajeDTO` e `InformeDTO`: lo que viaja por la API. |

En el prototipo 2, `Storage` será sustituido por el servicio de almacenamiento. Solo cambiará el interior de `AccesoDatosCentral`.

---

## API

### Usuarios — `/usuarios`

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| `POST` | `/usuarios` | Alta de un usuario | `201` creado · `409` ya existe |
| `DELETE` | `/usuarios/{idTrabajador}` | Baja de un usuario (no se borra: se marca con fecha de baja) | `204` · `404` no existe |
| `PUT` | `/usuarios/{idTrabajador}` | Modificación de un usuario | `204` · `404` no existe |
| `GET` | `/usuarios/{idTrabajador}` | Consulta de un usuario | `200` · `404` no existe |
| `GET` | `/usuarios` | Lista de todos los usuarios | `200` |

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

La fecha de alta la asigna el servidor, no el cliente.

### Fichajes — `/fichajes`

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| `POST` | `/fichajes` | Registro de un evento de puerta | `201` válido (abrir puerta) · `409` rechazado |

```json
{
  "idTrabajador": "TRA001",
  "idEdificio": "ED01",
  "idPuerta": "PU01",
  "timestamp": "2026-10-05T08:00:00"
}
```

Reglas:
- Solo se acepta si el usuario existe y está de alta.
- El 1.º evento del día de un usuario es la **entrada** y el 2.º es la **salida**.

### Informes — `/informes` *(en desarrollo)*

Informe entre dos fechas con:
- usuarios dados de **alta** en el periodo,
- usuarios dados de **baja** en el periodo,
- para cada usuario de alta, número de **fichajes correctos** (2 eventos en el mismo día con ≥ 8 h entre ellos).

---

## Datos de prueba

`Storage` carga al arrancar 5 usuarios y sus fichajes en la semana del **05/10/2026 al 09/10/2026**, pensados para cubrir todos los casos del informe.

| Usuario | Situación | Fichajes correctos esperados (05–09/10) |
|---|---|---|
| TRA001 Ane | Activa | 2 (también hay días de 6 h, sin salida y de 7 h 59 min) |
| TRA002 Jon | Alta el 06/10 (dentro del rango) | 2 |
| TRA003 Miren | Baja el 08/10 (dentro del rango) | No aparece: ya no está de alta |
| TRA004 Iker | Baja el 15/09 (antes del rango) | No aparece en ningún listado |
| TRA005 Leire | Activa, un día con 3 eventos | 1 (el día de 3 eventos no cuenta) |

---

## Equipo

Equipo 10 — Diseño de Software, Universidad de Deusto (campus de Donostia).
