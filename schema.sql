-- Estructura de la base de datos del Planificador de Tareas.
-- No es obligatorio ejecutar este script a mano: con la app corriendo y
-- spring.jpa.hibernate.ddl-auto=update, Hibernate crea estas mismas tablas
-- solo. Se deja aqui como referencia para revisar/crear la estructura
-- manualmente desde pgAdmin o el editor SQL de Supabase.

CREATE DATABASE tasks_db;

-- Conectate a tasks_db antes de correr lo siguiente (\c tasks_db en psql).

CREATE TABLE IF NOT EXISTS users (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    birth_date  DATE
);

CREATE TABLE IF NOT EXISTS tasks (
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  VARCHAR(2000),
    due_date     TIMESTAMP NOT NULL,
    status       VARCHAR(50) NOT NULL
);
