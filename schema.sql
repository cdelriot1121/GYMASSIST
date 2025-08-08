-- Esquema SQL para GYMASSIST (MySQL). Ajusta tipos si usas otro motor.
CREATE DATABASE IF NOT EXISTS gymassist CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gymassist;

-- Usuarios y administradores en una sola tabla (is_admin diferencia)
CREATE TABLE IF NOT EXISTS personas (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(120) NOT NULL,
  tp_id VARCHAR(10) NOT NULL,
  no_id VARCHAR(40) NOT NULL,
  correo VARCHAR(150) NOT NULL UNIQUE,
  contrasena VARCHAR(255) NOT NULL,
  asistencias INT NOT NULL DEFAULT 0,
  is_admin TINYINT(1) NOT NULL DEFAULT 0,
  nom_gym VARCHAR(120) NULL,
  val_mens VARCHAR(40) NULL
);

CREATE TABLE IF NOT EXISTS gimnasios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(120) NOT NULL UNIQUE,
  val_mensual VARCHAR(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS inscripciones (
  persona_id INT NOT NULL,
  gimnasio_id INT NOT NULL,
  fecha_inscripcion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (persona_id, gimnasio_id),
  FOREIGN KEY (persona_id) REFERENCES personas(id) ON DELETE CASCADE,
  FOREIGN KEY (gimnasio_id) REFERENCES gimnasios(id) ON DELETE CASCADE
);
