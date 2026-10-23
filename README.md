# FitZone – Inscripción a clases grupales

API REST para el gimnasio **FitZone** (Reto 1, Unidad 2 – Arquitectura Orientada a Servicios). Reemplaza la libreta de recepción con un servicio que inscribe a los socios en clases grupales (yoga, spinning, funcional), valida la membresía y el cupo, y registra la confirmación.

## Descripción de la solución

### Problema que resuelve

| Situación actual (libreta)                 | Cómo lo resuelve el sistema                                        |
|--------------------------------------------|--------------------------------------------------------------------|
| Clases con sobrecupo por errores de registro | Se valida el cupo (máximo 20 por clase) antes de inscribir          |
| Socios con membresía vencida inscritos     | Solo se inscriben socios con `membresiaVigente = true`             |
| Sin recordatorios, muchas inasistencias    | Se envía una confirmación y se programa un recordatorio 2 horas antes de la clase |

### Proceso modelado

1. El socio solicita inscribirse en una clase → `inscribirEnClase`
2. Se verifica que su membresía esté vigente → `consultarMembresia`
3. Se revisa que haya cupo → `consultarCupo`
4. Se registra la inscripción → `registrarInscripcion`
5. Se envía la confirmación y se programa el recordatorio → `enviarNotificacion` / `programarRecordatorio`

### Servicios y su implementación

| Servicio candidato   | Tipo     | Dominio                 | Clase en el código                                   |
|----------------------|----------|-------------------------|------------------------------------------------------|
| GestiónInscripciones | Tarea    | Clases e Inscripciones  | `InscripcionService.inscribirEnClase` (orquestador) |
| Socio                | Entidad  | Socios                  | `SocioService`                                       |
| Clase                | Entidad  | Clases e Inscripciones  | `ClaseService`                                       |
| Inscripción          | Entidad  | Clases e Inscripciones  | `IInscripcionRepository` / `InscripcionModel`        |
| Notificación         | Utilidad | Comunicación            | `NotificacionService`                                |

`InscripcionService` orquesta el flujo: llama a `SocioService` y `ClaseService` de forma síncrona (petición-respuesta), porque necesita sus respuestas para continuar o detenerse; registra la inscripción e incrementa el cupo ocupado en una sola transacción; y al final delega la confirmación a `NotificacionService` (unidireccional, en otro hilo con `@Async`), para que el envío no retrase la respuesta.

### Comunicación entre servicios

| Interacción                                              | Estilo     | Patrón                  | Implementación                                   |
|----------------------------------------------------------|------------|-------------------------|--------------------------------------------------|
| GestiónInscripciones → Socio (consultarMembresia)        | Síncrono   | Petición-respuesta      | Llamada directa a `SocioService`                 |
| GestiónInscripciones → Clase (consultarCupo)             | Síncrono   | Petición-respuesta      | Llamada directa a `ClaseService`                 |
| GestiónInscripciones → Inscripción (registrarInscripcion) | Síncrono  | Petición-respuesta      | `IInscripcionRepository.save` en la transacción  |
| GestiónInscripciones → Notificación (enviarNotificacion) | Asíncrono  | Unidireccional          | `NotificacionService.enviarAvisoConfirmacion` con `@Async` |
| Evento InscripcionRealizada → Notificación (programarRecordatorio) | Asíncrono | Publicación-suscripción | `InscripcionService` publica `InscripcionRealizadaEvent`; `NotificacionService.programarRecordatorio` lo escucha con `@TransactionalEventListener` |

**Cómo funciona el recordatorio:** el evento solo se procesa si la inscripción se guardó con éxito (después del commit). El suscriptor calcula la hora de envío (`fecha_clase` + `hora_inicio` − 2 horas) y la agenda con el `TaskScheduler` de Spring. A esa hora se guarda una notificación de tipo `RECORDATORIO`. Si faltan menos de 2 horas para la clase, el recordatorio se envía de inmediato; si la clase ya comenzó, no se programa.

> Los recordatorios programados se guardan en memoria: si la aplicación se reinicia antes de la hora de envío, ese recordatorio se pierde.

### Arquitectura

Proyecto Spring Boot organizado por capas:

```
src/main/java/com/api/fitzone/
├── controllers/   Endpoints REST (Socio, Clase, Inscripcion)
├── services/      Lógica de negocio y orquestación
├── events/        Evento InscripcionRealizada (publicación-suscripción)
├── repositories/  Acceso a datos con Spring Data JPA
└── models/        Entidades JPA: Socio, Clase, Inscripcion, Notificacion
```

**Tecnologías:** Java 21, Spring Boot 4.1 (Web MVC + Data JPA), MySQL, Maven.

### Endpoints

| Método | Ruta                                | Operación            | Descripción                                   |
|--------|-------------------------------------|----------------------|-----------------------------------------------|
| POST   | `/api/inscripciones`                | inscribirEnClase     | Inscribe a un socio en una clase              |
| GET    | `/api/clases/{idClase}/cupo?fechaClase=YYYY-MM-DD` | consultarCupo | Devuelve cupo máximo, ocupado y disponible |
| GET    | `/api/socios/{idSocio}/verificar`   | consultarMembresia   | Devuelve el socio si su membresía está vigente |

**Errores** (respuesta `400 Bad Request` con el código en el cuerpo):

| Código                                    | Cuándo ocurre                                         |
|-------------------------------------------|-------------------------------------------------------|
| `SOCIO_NO_ENCONTRADO`                     | El `idSocio` no existe                                |
| `MEMBRESIA_VENCIDA`                       | El socio existe pero su membresía no está vigente     |
| `CLASE_NO_ENCONTRADA`                     | El `idClase` no existe                                |
| `FECHA_INVALIDA`                          | `fechaClase` no tiene formato YYYY-MM-DD o no coincide con la programación de la clase |
| `CLASE_SIN_CUPO`                          | La clase ya tiene 20 inscritos                        |
| `SOCIO_YA_INSCRITO`                       | El socio ya tiene una inscripción confirmada en la clase |

## Instrucciones para ejecutarla

### Requisitos

- **JDK 21**
- **MySQL 8** corriendo en `localhost:3306`
- No hace falta instalar Maven: el proyecto incluye el wrapper (`mvnw` / `mvnw.cmd`).

### 1. Crear la base de datos

Las tablas se crean solas al arrancar (`ddl-auto=update`), pero la base de datos debe existir:

```sql
CREATE DATABASE fitzone;
```

### 2. Configurar la conexión

La configuración va directamente en `src/main/resources/application.properties`. 

```properties
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/fitzone
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA
```

| Propiedad                     | Qué cambiar                                                         |
|-------------------------------|---------------------------------------------------------------------|
| `spring.datasource.username`  | Tu usuario de MySQL                                                 |
| `spring.datasource.password`  | La contraseña de ese usuario                                        |
| `spring.datasource.url`       | Solo si MySQL no está en `localhost:3306` o la base no se llama `fitzone` |
| `server.port`                 | Solo si el puerto 8080 ya está ocupado                              |

### 3. Ejecutar la aplicación

Windows:

```bash
mvnw.cmd spring-boot:run
```

Linux / macOS:

```bash
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

### 4. Cargar datos de prueba

Con la aplicación arrancada al menos una vez (para que existan las tablas), ejecuta en MySQL:

```sql
USE fitzone;

INSERT INTO socio (id_socio, nombre, correo, membresia_vigente) VALUES
  ('SOC0000001', 'Ana López',   'ana@correo.com',   true),
  ('SOC0000002', 'Luis Pérez',  'luis@correo.com',  false);

INSERT INTO clase (id_clase, disciplina, sucursal, cupo_maximo, cupo_ocupado, fecha_clase, hora_inicio) VALUES
  ('CLA0000001', 'Yoga',      'Centro', 20, 5,  '2026-11-10', '07:00:00'),
  ('CLA0000002', 'Spinning',  'Norte',  20, 20, '2026-11-10', '18:00:00'),
  ('CLA0000003', 'Funcional', 'Sur',    20, 0,  '2026-11-11', '19:30:00');
```

### 5. Probar los endpoints

Consultar cupo:

**GET** `http://localhost:8080/api/clases/CLA0000001/cupo?fechaClase=2026-11-10`

```json
{ "idClase": "CLA0000001", "fechaClase": "2026-11-10", "cupoMaximo": 20, "cupoOcupado": 5, "cupoDisponible": 15 }
```

Verificar membresía:

**GET** `http://localhost:8080/api/socios/SOC0000001/verificar`

Inscribir en una clase:

**POST** `http://localhost:8080/api/inscripciones`

```json
{
  "idSocio": "SOC0000001",
  "idClase": "CLA0000001",
  "fechaClase": "2026-11-10"
}
```

Respuesta `201 Created` con el `folioInscripcion`, `estatus` (`CONFIRMADA`) y `fechaRegistro`.

Casos de error para probar:

| Petición                                          | Resultado esperado                         |
|---------------------------------------------------|--------------------------------------------|
| Socio `SOC0000002` (membresía vencida)            | `MEMBRESIA_VENCIDA`                        |
| Socio `SOC9999999`                                | `SOCIO_NO_ENCONTRADO`                      |
| Clase `CLA0000002` (cupo lleno)                   | `CLASE_SIN_CUPO`                           |
| Repetir la misma inscripción de `SOC0000001`      | `SOCIO_YA_INSCRITO`                        |
| Clase `CLA9999999`                                | `CLASE_NO_ENCONTRADA`                      |
| `fechaClase` `2026-12-01` con `CLA0000001`        | `FECHA_INVALIDA`                           |

Después de cada inscripción exitosa se guarda un registro de tipo `CONFIRMACION` en la tabla `notificacion`, y a la hora programada otro de tipo `RECORDATORIO`:

```sql
SELECT * FROM notificacion;
```

**Probar el recordatorio sin esperar:** crea una clase para hoy que empiece en un poco más de 2 horas, inscribe a un socio y espera un par de minutos. Por ejemplo, si son las 10:00:

```sql
INSERT INTO clase (id_clase, disciplina, sucursal, cupo_maximo, cupo_ocupado, fecha_clase, hora_inicio)
VALUES ('CLA0000004', 'Yoga', 'Centro', 20, 0, CURDATE(), '12:02:00');
```

Inscribe a `SOC0000001` en `CLA0000004` (con `fechaClase` = la fecha de hoy) y alrededor de las 10:02 aparecerá el registro `RECORDATORIO` en la tabla `notificacion`.
