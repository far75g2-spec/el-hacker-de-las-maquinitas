# El Hacker de las Maquinitas

## Descripción
Aplicación web académica para consultar y administrar licencias, créditos y alquileres de herramientas de software utilizadas en el servicio técnico de teléfonos celulares.

## Tecnologías
- Frontend: React, JavaScript, Vite, HTML5 y CSS3.
- Backend web: Java 17, Jakarta Servlets 6.1, Maven y JDBC.
- Persistencia: MySQL y MySQL Connector/J.
- Seguridad: contraseñas con BCrypt y sesiones HTTP.
- Control de versiones: Git y GitHub.

## Estructura
- `frontend/react-app`: aplicación React.
- `frontend`: versión HTML/CSS/JavaScript conservada como referencia.
- `el-hacker-servlets`: API Java desplegable como WAR en Apache Tomcat.
- `backend/el-hacker-api`: módulo Java/API anterior conservado en el repositorio.
- `database`: migraciones SQL.
- `DOCUMENTACION_TECNICA_BLOQUES_2_3_4.md`: documentación técnica existente.

## Funcionalidades implementadas en la versión entregada
- Registro, inicio y cierre de sesión.
- Consulta de sesión y protección de vistas.
- Catálogo de productos y administración del catálogo.
- Administración de usuarios y estados de cuenta.
- Solicitudes de recarga USDT por red TRC20, con hash de transacción y comprobante opcional.
- Revisión administrativa de recargas: aprobación o rechazo.
- Al aprobar una recarga, el backend acredita el saldo dentro de una transacción y registra el movimiento.
- Consulta de billetera, saldo y movimientos.
- Carrito y creación de pedidos con descuento de saldo.
- Validación del saldo y bloqueo de compra cuando no es suficiente.
- Panel administrativo para consultar productos, usuarios, pedidos y recargas.
- El panel administrativo muestra el saldo USDT de cada usuario.

## Configuración local

### 1. Base de datos
1. Crear el esquema `el_hacker_maquinitas`.
2. Crear las tablas base `usuarios` y `productos` según el modelo existente del proyecto.
3. Ejecutar `database/actualizacion_bloques_2_3.sql` desde MySQL Workbench sobre ese esquema.
4. Revisar que los productos activos tengan `precio_usdt` mayor que cero.

La migración crea billeteras, movimientos, pedidos, detalles de pedido y recargas. También crea una billetera en cero al registrar usuarios nuevos.

### 2. Variables de entorno del backend
Configurar antes de iniciar Tomcat:
- `DB_URL`: por defecto `jdbc:mysql://localhost:3306/el_hacker_maquinitas?useSSL=false&serverTimezone=UTC`.
- `DB_USER`: usuario de MySQL.
- `DB_PASSWORD`: contraseña de MySQL.

No guardar contraseñas reales en el repositorio.

### 3. Backend
Desde `el-hacker-servlets` ejecutar:
```bash
mvn clean package
```
El artefacto generado será `target/el-hacker-servlets.war`. Desplegarlo en Apache Tomcat 11 compatible con Jakarta Servlet 6.1.

### 4. Frontend
Desde `frontend/react-app` ejecutar:
```bash
npm install
npm run dev
```
Vite inicia en `http://localhost:5173`. El proxy `/api` reenvía las peticiones a `http://localhost:8080/el-hacker-servlets`.

Para generar la versión de producción:
```bash
npm run build
```

## Rutas de API principales
Las rutas se consumen desde React mediante el prefijo `/api` del proxy:
- `GET /api/sesion`
- `POST /api/registro`
- `POST /api/login`
- `POST /api/logout`
- `GET /api/productos` y `POST /api/productos` (administración en POST)
- `GET /api/usuarios` y `POST /api/usuarios` (administración)
- `GET /api/billetera`
- `GET /api/recargas` y `POST /api/recargas`
- `GET /api/pedidos` y `POST /api/pedidos`

## Reglas de negocio relevantes
- Una recarga no incrementa el saldo al ser solicitada; requiere aprobación administrativa.
- Una recarga solo puede resolverse una vez mientras su estado sea `Pendiente`.
- La creación del pedido consulta precios vigentes en la base de datos; no confía en el precio enviado por el navegador.
- La compra bloquea la billetera durante la operación y registra pedido, detalle, débito y movimiento en una transacción SQL.
- Los usuarios solo consultan su propia billetera y sus propios pedidos/recargas. Las operaciones administrativas verifican el rol en el servidor.

## Control de versiones
Repositorio remoto:
https://github.com/far75g2-spec/el-hacker-de-las-maquinitas

## Estado de validación de esta entrega
Se revisó la estructura del proyecto, las rutas y la integración de frontend/backend, y se completaron cambios para visualizar el saldo y movimientos recientes en el panel de usuario y el saldo de cada usuario en el panel administrativo.

No se pudo ejecutar la compilación automática en este entorno de preparación: Maven no está disponible y las dependencias locales de Vite (`node_modules`) no están incluidas en el ZIP. Por ello, se debe realizar la comprobación final en el equipo de desarrollo con `mvn clean package` y `npm install` seguido de `npm run build`, y probar los flujos con MySQL y Tomcat activos.

## Evidencia SENA
GA7-220501096-AA4-EV03 — Componente front-end del proyecto formativo y proyectos de clase.
