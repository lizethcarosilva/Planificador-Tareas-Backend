# Planificador de Tareas · Backend

API REST en Spring Boot + PostgreSQL. Expone CRUD sobre `/api/tasks` y autenticación sobre `/api/auth`, consumidos por `Planificador-Tareas-Frontend`.

## Modelo `Task`

| Campo | Tipo | Validación |
|---|---|---|
| id | Long | autogenerado |
| name | String | `@NotBlank` |
| description | String | opcional |
| dueDate | LocalDateTime | `@NotNull` |
| status | String | `@NotBlank` (PENDIENTE, EN PROGRESO, REVISIÓN, HECHO) |

## Modelo `User`

| Campo | Tipo | Validación |
|---|---|---|
| id | Long | autogenerado |
| name | String | `@NotBlank` |
| email | String | `@Email`, único |
| password | String | hasheado con BCrypt, nunca se devuelve |
| birthDate | LocalDate | opcional |

## Endpoints de tareas

| Método | Ruta | Descripción |
|---|---|---|
| GET | /api/tasks | Lista todas las tareas |
| GET | /api/tasks/{id} | Obtiene una tarea por id |
| POST | /api/tasks | Crea una tarea |
| PUT | /api/tasks/{id} | Actualiza una tarea |
| DELETE | /api/tasks/{id} | Elimina una tarea |

## Endpoints de autenticación

| Método | Ruta | Descripción |
|---|---|---|
| POST | /api/auth/register | Crea una cuenta (nombre, correo, clave, fecha de nacimiento opcional) |
| POST | /api/auth/login | Valida credenciales y devuelve `{id, name, email}` |

Al arrancar, `config/DataSeeder.java` crea el usuario de prueba `lizethcaro@correo.com` / `12345` si no existe.

CORS habilitado para cualquier origen sobre `/api/**` (`config/CorsConfig.java`).

## 1. Local (sin Docker)

Estructura de tablas de referencia en `schema.sql` (no es obligatorio ejecutarla: Hibernate crea las mismas tablas solo, gracias a `ddl-auto=update`).

```sql
CREATE DATABASE tasks_db;
```

Ajusta host/puerto/usuario/clave en `application.properties` según tu instalación local (revisa en pgAdmin: clic derecho al servidor → Properties → Connection), luego:

```bash
./mvnw spring-boot:run
```

API en `http://localhost:8080/api/tasks`.

## 2. Docker Compose (backend + PostgreSQL)

```bash
docker compose up --build
```

Backend en `http://localhost:8080`, PostgreSQL en `localhost:5432` (`postgres`/`postgres`, base `tasks_db`). `docker compose down -v` para detener y borrar los datos.

## 3. Render (opcional)

1. Sube el repo a GitHub.
2. Render → **New > PostgreSQL** (copia host, puerto, usuario, clave, db).
3. Render → **New > Web Service**, entorno **Docker** (usa este `Dockerfile`).
4. Variables de entorno del servicio: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`.
5. Copia la URL pública que te da Render y pégala en `Planificador-Tareas-Frontend/Components/config.js`, en `RENDER_API_URL`.

## 4. Supabase (alternativa a Postgres local)

No pegues la clave en `application.properties`; defínela como variable de entorno:

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:postgresql://db.<tu-ref>.supabase.co:5432/postgres?sslmode=require"
$env:SPRING_DATASOURCE_USERNAME = "postgres"
$env:SPRING_DATASOURCE_PASSWORD = "<tu-password-de-supabase>"
./mvnw spring-boot:run
```

Spring Boot sobreescribe automáticamente los valores de `application.properties` con estas variables.

## Probar con cURL

```bash
curl http://localhost:8080/api/tasks

curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"name":"Comprar pan","dueDate":"2026-09-20T18:00:00","status":"PENDIENTE"}'

curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Ana Ruiz","email":"ana@correo.com","password":"12345"}'

curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"ana@correo.com","password":"12345"}'
```
