# 💻 GYMASSIST - Sistema de Gestión para Gimnasios

<p>
<img src="https://techhubsolutions.in/wp-content/uploads/2020/05/maxresdefault.jpg" alt="Java Swing" width="170" height="100" />
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/Apache_NetBeans_Logo.svg/1776px-Apache_NetBeans_Logo.svg.png" alt="NetBeans" width="110" height="120"/>
</p>

## Acerca del proyecto

GYMASSIST es una aplicación de escritorio desarrollada en Java Swing para la gestión de gimnasios locales. Este sistema ayuda a organizar la información de clientes, planes de entrenamiento y asistencias en gimnasios que hasta ahora no cuentan con un sistema automatizado para estas tareas.

## Características implementadas

- **Sistema de autenticación**: Login para diferenciación de usuarios (administradores y clientes)
- **Registro de usuarios**: Formulario para añadir nuevos clientes al sistema
- **Panel administrativo**: Gestión de gimnasios y clientes desde una interfaz dedicada
- **Panel de usuario**: Acceso a funcionalidades específicas para clientes
- **Rutinas de entrenamiento**: Visualización de rutinas predefinidas para diferentes grupos musculares:
  - Cardio
  - Espalda
  - Pecho
  - Pierna
- **Gestión de asistencias**: Registro de visitas de usuarios al gimnasio
- **Gestión de mensualidades**: Control de pagos mensuales

## Estructura del proyecto

```
GYMASSIST/
├── src/
│   ├── logic/
│   │   ├── controller/
│   │   │   ├── AsistenciaController.java
│   │   │   ├── ConexionBD.java
│   │   │   ├── DatosController.java
│   │   │   ├── DatosLogic.java
│   │   │   ├── GimnasioController.java
│   │   │   └── MensualidadController.java
│   │   ├── main/
│   │   │   └── Execution.java
│   │   └── model/
│   │       ├── AdministradorModel.java
│   │       ├── GimnasioModel.java
│   │       └── PersonaModel.java
│   └── pantallas/
│       ├── entrenamientos/
│       │   ├── Cardio.java
│       │   ├── Espalda.java
│       │   ├── Pecho.java
│       │   └── Pierna.java
│       ├── AdminScreen.java
│       ├── LoginScreen.java
│       ├── MainScreen.java
│       ├── RegisterScreen.java
│       └── UsuarioScreen.java
```

## Estado actual del proyecto

El proyecto tiene implementada la estructura básica MVC (Modelo-Vista-Controlador) con las siguientes funcionalidades operativas:
- Interfaz gráfica completa
- Navegación entre pantallas
- Rutinas de entrenamiento visualizables
- Lógica de autenticación

## Aspectos por mejorar

Aunque el proyecto tiene una base funcional, aún queda trabajo por hacer:

### 1. **Corrección de errores en la lógica actual** (ya es hecho con integracion a motor de base de datos mysql)

   - Mejorar el manejo de los archivos `.txt` para almacenamiento temporal
   - Corregir problemas de validación en formularios
   - Optimizar la lógica de control de asistencias

### 2. **Migración a base de datos (ya esta)**

   Para una versión futura, se planea migrar el sistema a una base de datos relacional, utilizando XAMPP para gestionar MariaDB de manera local.

### 3. **Optimización de la interfaz gráfica (pendiente)** 

   - Mejorar la responsividad de algunos componentes
   - Añadir confirmaciones visuales para acciones completadas
   - Implementar validaciones más robustas en los formularios

## Tecnologías utilizadas

- Java
- Swing (para la interfaz gráfica)
- NetBeans 
- MySQL (para la base de datos)

---