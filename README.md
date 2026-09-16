# Planificador de Tareas · Backend

API REST en Spring Boot + PostgreSQL. Expone CRUD sobre `/api/tasks`, consumido por `Planificador-Tareas-Frontend`.

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

