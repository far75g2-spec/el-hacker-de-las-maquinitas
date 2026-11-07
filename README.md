# El Hacker de las Maquinitas

## Descripción del proyecto

El Hacker de las Maquinitas es una aplicación web orientada a la comercialización y alquiler de licencias y créditos de herramientas especializadas en el servicio técnico y reparación de software de teléfonos celulares.

El proyecto busca ofrecer a los usuarios una plataforma donde puedan consultar productos, registrarse, iniciar sesión y acceder a diferentes servicios relacionados con herramientas de reparación móvil.

## Objetivo

Desarrollar una aplicación web que permita gestionar usuarios y productos mediante una arquitectura frontend y backend, utilizando Java, MySQL y JDBC para el almacenamiento y procesamiento de información.

## Tecnologías utilizadas

### Frontend
- HTML5
- CSS3
- JavaScript

### Backend
- Java 17
- Maven
- Javalin
- JDBC
- Jackson Databind
- BCrypt (jBCrypt)

### Base de datos
- MySQL
- MySQL Connector/J

### Herramientas de desarrollo
- Visual Studio Code
- MySQL Workbench
- Git
- GitHub

## Estructura del proyecto

```text
el-hacker-de-las-maquinitas/
│
├── frontend/
│   ├── index.html
│   ├── venta.html
│   ├── alquiler.html
│   ├── registro.html
│   ├── login.html
│   ├── recargar.html
│   ├── carrito.html
│   ├── dashboard.html
│   ├── ordenes.html
│   ├── estilos.css
│   └── script.js
│
├── backend/
│   └── el-hacker-api/
│       ├── pom.xml
│       └── src/main/java/co/elhacker/maquinitas/
│           ├── App.java
│           ├── Api.java
│           ├── conexion/
│           ├── dao/
│           ├── modelo/
│           └── seguridad/
│
├── .gitignore
└── README.md
```

## Base de datos

Nombre de la base de datos:

`el_hacker_maquinitas`

Tablas principales:

- **usuarios:** almacena la información de los usuarios registrados.
- **productos:** almacena la información de los productos disponibles.

La conexión entre Java y MySQL se realiza mediante JDBC y MySQL Connector/J.

## Configuración de la conexión

El backend utiliza variables de entorno para configurar la conexión a MySQL:

- `DB_URL`: dirección de conexión a la base de datos.
- `DB_USER`: usuario de MySQL.
- `DB_PASSWORD`: contraseña de MySQL.

La contraseña debe mantenerse privada y no debe publicarse en el repositorio.

## Funcionalidades implementadas

- Conexión a MySQL mediante JDBC.
- Registro y consulta de usuarios.
- Operaciones CRUD de usuarios.
- Operaciones CRUD de productos.
- Búsqueda de productos por identificador.
- Generación y verificación de contraseñas mediante BCrypt.
- Registro de usuarios mediante API.
- Inicio y cierre de sesión.
- Gestión de sesiones de usuario.
- Verificación de sesión desde el panel de usuario.

## Rutas principales de la API

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/estado` | Consulta el estado del servicio. |
| POST | `/api/registro` | Registra un usuario. |
| POST | `/api/login` | Inicia sesión. |
| GET | `/api/sesion` | Consulta la sesión activa. |
| POST | `/api/logout` | Cierra la sesión. |

## Ejecución del proyecto

1. Instalar Java 17 o una versión compatible.
2. Instalar Maven.
3. Instalar y configurar MySQL.
4. Crear la base de datos `el_hacker_maquinitas`.
5. Configurar las variables de entorno de conexión.
6. Abrir la carpeta del backend en Visual Studio Code.
7. Compilar el proyecto utilizando Maven:

```bash
mvn clean compile
```

8. Ejecutar la clase principal `App.java` desde el entorno de desarrollo.
9. Abrir el frontend mediante un servidor local.

## Control de versiones

El proyecto utiliza Git para el control de versiones y GitHub como repositorio remoto, permitiendo registrar cambios y mantener un historial del desarrollo.

## Estado del proyecto

El proyecto se encuentra en desarrollo. Los módulos CRUD de usuarios y productos, la conexión JDBC y los procesos básicos de autenticación han sido implementados y probados.

Las funcionalidades de recargas, pagos, carrito y gestión completa de órdenes requieren desarrollo e integración adicional.

## Autor

Proyecto académico desarrollado como parte del programa Tecnólogo en Análisis y Desarrollo de Software (ADSO) del Servicio Nacional de Aprendizaje SENA.

**Evidencia:** GA7-220501096-AA2-EV01.
