# Planificador de Tareas · Backend

API REST en Spring Boot + PostgreSQL para el Planificador de Tareas. Expone operaciones CRUD sobre `/api/tasks` y las consume el frontend estático de `Planificador-Tareas-Frontend`.

## Modelo `Task`

| Campo | Tipo | Validación |
|---|---|---|
| id | Long | autogenerado |
| name | String | `@NotBlank` |
| description | String | opcional |
| dueDate | LocalDateTime | `@NotNull` |
| status | String | `@NotBlank` (PENDIENTE, EN PROGRESO, REVISIÓN, HECHO) |

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | /api/tasks | Lista todas las tareas |
| GET | /api/tasks/{id} | Obtiene una tarea por id |
| POST | /api/tasks | Crea una tarea |
| PUT | /api/tasks/{id} | Actualiza una tarea |
| DELETE | /api/tasks/{id} | Elimina una tarea |

CORS está habilitado para cualquier origen sobre `/api/**` (ver `config/CorsConfig.java`), así que el frontend puede consumir la API desde Live Server, `file://` o un dominio publicado.

## 1. Ejecutar en local (sin Docker)

1. Instala PostgreSQL y crea la base de datos:
   ```sql
   CREATE DATABASE tasks_db;
   ```
2. Revisa las credenciales en `src/main/resources/application.properties` (usuario/clave por defecto: `postgres`/`postgres`). Ajústalas a tu instalación si es necesario.
3. Levanta la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```
4. La API queda disponible en `http://localhost:8080/api/tasks`.

## 2. Ejecutar con Docker Compose (backend + PostgreSQL)

No necesitas tener PostgreSQL instalado; el `docker-compose.yml` levanta ambos servicios.

```bash
docker compose up --build
```

- Backend: `http://localhost:8080`
- PostgreSQL: `localhost:5432` (usuario/clave `postgres`/`postgres`, base `tasks_db`)

Para detener todo: `docker compose down` (agrega `-v` si además quieres borrar los datos de la base).

## 3. Desplegar en Render (opcional)

1. Sube este repositorio a GitHub.
2. En Render: **New > PostgreSQL** → crea una base de datos administrada y copia sus datos de conexión (host, puerto, usuario, clave, nombre de base).
3. En Render: **New > Web Service** → conecta el repo, entorno **Docker** (usa el `Dockerfile` de esta carpeta).
4. En la sección *Environment* del servicio agrega:
   - `SPRING_DATASOURCE_URL` = `jdbc:postgresql://<host>:<puerto>/<db>`
   - `SPRING_DATASOURCE_USERNAME` = `<usuario>`
   - `SPRING_DATASOURCE_PASSWORD` = `<clave>`
5. Despliega. Render expone la URL pública del servicio, por ejemplo `https://planificador-tareas-backend.onrender.com`.
6. Copia esa URL y pégala en el frontend, en `Planificador-Tareas-Frontend/Components/config.js`, en la constante `RENDER_API_URL` (está señalada con un comentario al inicio del archivo). Así el frontend usará el backend en la nube en lugar de `http://localhost:8080`.

## 4. Usar Supabase como base de datos (alternativa a Postgres local)

Si prefieres no instalar PostgreSQL localmente, puedes usar una base de datos Postgres gratuita de [Supabase](https://supabase.com):

1. Crea un proyecto en Supabase y define la contraseña de la base de datos.
2. En el proyecto, ve a **Project Settings → Database → Connection string** y copia los datos de conexión directa (host tipo `db.<ref>.supabase.co`, puerto `5432`, base `postgres`, usuario `postgres`).
3. **No pegues la contraseña en `application.properties`** (evita subir credenciales a git). En su lugar, defínela como variable de entorno antes de levantar la app. En PowerShell:
   ```powershell
   $env:SPRING_DATASOURCE_URL = "jdbc:postgresql://db.<tu-ref>.supabase.co:5432/postgres?sslmode=require"
   $env:SPRING_DATASOURCE_USERNAME = "postgres"
   $env:SPRING_DATASOURCE_PASSWORD = "<tu-password-de-supabase>"
   ./mvnw spring-boot:run
   ```
4. Spring Boot detecta automáticamente estas variables y sobreescribe los valores de `application.properties` (que quedan solo como fallback para Postgres local). La tabla `tasks` se crea sola gracias a `spring.jpa.hibernate.ddl-auto=update`.
5. Para Docker/Render, define esas mismas tres variables de entorno en `docker-compose.yml` o en el panel de variables del servicio, en vez de en el `application.properties`.

## Probar con Postman / cURL

```bash
curl http://localhost:8080/api/tasks

curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"name":"Comprar pan","description":"Antes de las 6pm","dueDate":"2026-09-20T18:00:00","status":"PENDIENTE"}'
```
