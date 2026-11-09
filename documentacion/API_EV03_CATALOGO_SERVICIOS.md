# GA7-220501096-AA5-EV03 — Catálogo y documentación de servicios web

**Proyecto:** El Hacker de las Maquinitas
**Tecnología:** Java 17, Jakarta Servlet 6.1, Tomcat 11, JDBC, MySQL, React y Vite.
**Base de datos:** `el_hacker_maquinitas`
**Integrantes:** Farley Stivens Garcia Cala y Yuranis Saray Garcia Herrera.

## 1. Arquitectura y dirección base

El frontend React se ejecuta en desarrollo en `http://localhost:5173`. La configuración de Vite envía las peticiones que comienzan por `/api` a `http://localhost:8080` y reemplaza `/api` por `/el-hacker-servlets`.

Por tanto, desde React o Postman con el proxy activo se puede utilizar `http://localhost:5173/api`. Para llamar directamente al backend desplegado en Tomcat, la base es `http://localhost:8080/el-hacker-servlets`.

Los endpoints usan sesiones HTTP. Después del inicio de sesión, el navegador/Postman debe conservar y enviar la cookie de sesión (`JSESSIONID`). Las operaciones administrativas verifican el rol en el backend.

## 2. Catálogo de endpoints

| Recurso | Método | Acceso | Propósito |
|---|---|---|---|
| `/registro` | GET | Público, vista JSP | Reenvía a `registro.jsp`; no es la operación REST de registro. |
| `/registro` | POST | Público | Registra un cliente. |
| `/login` | GET | Público, vista JSP | Reenvía a `login.jsp`. |
| `/login` | POST | Público | Autentica y crea sesión. |
| `/logout` | POST | Sesión opcional | Invalida la sesión actual. |
| `/sesion` | GET | Público | Informa si existe sesión autenticada y su rol. |
| `/productos` | GET | Público / administrador | Lista productos activos; el administrador puede consultar también inactivos. |
| `/productos` | POST | Administrador | Crea, actualiza o cambia el estado de un producto. |
| `/usuarios` | GET | Administrador | Lista usuarios. |
| `/usuarios` | POST | Administrador | Cambia el estado de un usuario. |
| `/billetera` | GET | Usuario autenticado | Consulta saldo USDT y movimientos recientes propios. |
| `/recargas` | GET | Usuario autenticado / administrador | Lista recargas propias o todas las recargas para administración. |
| `/recargas` | POST | Usuario autenticado / administrador | Cliente registra solicitud multipart; administrador aprueba o rechaza una solicitud. |
| `/pedidos` | GET | Usuario autenticado / administrador | Lista pedidos propios o todos los pedidos para administración. |
| `/pedidos` | POST | Usuario autenticado / administrador | Cliente crea pedido; administrador actualiza el estado. |
| `/comprobantes/{archivo}` | GET | Administrador | Visualiza/descarga comprobante permitido. |

## 3. Detalle de solicitudes

### 3.1 Registro — `POST /registro`

**Content-Type:** `application/x-www-form-urlencoded` (el servlet lee parámetros del formulario).

| Parámetro | Obligatorio | Descripción |
|---|---|---|
| `nombre` | Sí | Nombre del usuario. |
| `correo` | Sí | Correo único. |
| `telefono` | No | Teléfono; si no se envía se almacena cadena vacía. |
| `clave` | Sí | Contraseña; se almacena mediante hash BCrypt. |

**Respuestas implementadas:** `201 Created` si registra; `400 Bad Request` si faltan campos; `409 Conflict` si el registro no se completa por correo duplicado; `500 Internal Server Error` ante error inesperado.

### 3.2 Inicio de sesión — `POST /login`

**Content-Type:** `application/x-www-form-urlencoded`.

Parámetros: `correo`, `clave` (ambos obligatorios). Si las credenciales son correctas, el servidor invalida la sesión anterior, crea una sesión nueva, guarda `usuarioId` y `rol`, y establece 30 minutos de inactividad.

**Respuestas:** `200 OK`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden` si la cuenta no tiene rol asignado, `500 Internal Server Error`.

### 3.3 Cierre de sesión — `POST /logout`

No requiere cuerpo. Invalida la sesión existente y devuelve `200 OK` con `success: true`.

### 3.4 Estado de sesión — `GET /sesion`

No requiere parámetros. Respuesta de ejemplo:

```json
{
  "authenticated": true,
  "usuarioId": 12,
  "rol": "Cliente"
}
```

Sin sesión, `authenticated` es `false`, `usuarioId` es `0` y `rol` es cadena vacía.

### 3.5 Catálogo — `GET /productos`

No requiere parámetros. Devuelve `success` y `productos`. Para clientes se limita a productos con estado `Activo`; para administradores se listan todos.

### 3.6 Administración de productos — `POST /productos`

**Acceso:** administrador. Formulario `application/x-www-form-urlencoded`.

- Crear: `accion=crear`, `nombre`, `descripcion`, `categoria`, `tipo_producto`, `precio`.
- Actualizar: `accion=actualizar`, `id_producto`, `nombre`, `descripcion`, `categoria`, `tipo_producto`, `precio`.
- Cambiar estado: `accion=estado`, `id_producto`, `estado` (`Activo` o `Inactivo`).

El precio recibido se guarda en `productos.precio_usdt`. El servlet responde JSON con `success` y `message`. En el código revisado, los errores de validación/procesamiento responden `400`; falta de privilegios responde `403`.

### 3.7 Usuarios — `GET /usuarios` y `POST /usuarios`

**Acceso:** administrador.

- GET: devuelve la colección `usuarios`.
- POST: parámetros `id_usuario` y `estado` (`Activo` o `Inactivo`) para cambiar el estado.

Respuestas principales: `200 OK`, `400 Bad Request` ante datos inválidos, `403 Forbidden` sin rol administrador y `500 Internal Server Error` al consultar si falla la base de datos.

**Nota de revisión:** el DAO obtiene `saldo_usdt`, pero la versión de `UsuariosServlet.java` incluida en el ZIP base no lo incorpora al JSON. La versión corregida que se compile y pruebe localmente debe conservar ese campo si el panel administrativo lo utiliza.

### 3.8 Billetera — `GET /billetera`

**Acceso:** usuario autenticado. No acepta `id_usuario` desde el cliente: identifica al titular por la sesión.

Devuelve `saldo_usdt` y hasta los últimos 30 movimientos con tipo, monto, saldo resultante, referencia, descripción y fecha. Respuestas: `200 OK`, `401 Unauthorized`, `500 Internal Server Error`.

### 3.9 Recargas — `GET /recargas` y `POST /recargas`

**GET:** usuario autenticado; cliente consulta sus solicitudes y administrador consulta todas.

**POST de cliente:** `multipart/form-data` con:

| Campo | Obligatorio | Descripción |
|---|---|---|
| `monto_usdt` | Sí | Monto positivo; mínimo de 1 USDT y máximo 8 decimales según DAO. |
| `tx_hash` | Sí | Identificador de transacción, longitud entre 8 y 180 caracteres en servlet. |
| `comprobante` | No | JPG, PNG o PDF. Tamaño máximo configurado: 5 MB por archivo. |

La red registrada por DAO es `TRC20`. El saldo no se acredita al enviar la solicitud.

**POST de administrador:** formulario con `id_recarga`, `estado` (`Aprobada` o `Rechazada`) y `notas_admin` opcional. Solo se puede resolver una solicitud que siga en estado `Pendiente`. Al aprobar, la operación acredita el saldo y registra el movimiento dentro de una transacción.

Respuestas principales: `201 Created` al crear solicitud; `200 OK` al resolver; `400 Bad Request` por datos o decisión inválida; `401 Unauthorized`; `500 Internal Server Error`.

### 3.10 Pedidos — `GET /pedidos` y `POST /pedidos`

**GET:** usuario autenticado. Un cliente ve sus pedidos; un administrador ve todos.

**POST de cliente:** `application/json`:

```json
{
  "items": [
    { "id_producto": 1, "cantidad": 2 }
  ]
}
```

El servidor vuelve a consultar el precio y el estado de cada producto en MySQL; no confía en el precio enviado por React. Verifica cantidades, saldo y disponibilidad. Pedido, detalles, débito y movimiento se registran en una transacción.

**POST de administrador:** `application/json`:

```json
{
  "id_pedido": 25,
  "estado": "Cancelado"
}
```

Estados admitidos por el DAO: `Pendiente de procesamiento`, `En proceso`, `Completado` y `Cancelado`.

**Regla de cancelación incorporada en la corrección 01:** al cancelar un pedido pagado se devuelve el total a la billetera del cliente y se registra un movimiento de devolución. El proceso debe ejecutarse transaccionalmente y evitar que una segunda cancelación genere otra devolución. No se permite cancelar un pedido completado.

Respuestas principales: `201 Created` al crear pedido; `200 OK` al cambiar estado; `400 Bad Request` ante validación; `401 Unauthorized`; `500 Internal Server Error` ante error no esperado.

### 3.11 Comprobantes — `GET /comprobantes/{archivo}`

**Acceso:** administrador. El nombre debe corresponder a UUID con extensión `.jpg`, `.png` o `.pdf`. El archivo se busca en `uploads/hacker-comprobantes` dentro de `catalina.base`. Devuelve `403 Forbidden` sin permisos y `404 Not Found` si el nombre o archivo no existe.

## 4. Modelo de datos de los módulos USDT

La migración `database/actualizacion_bloques_2_3.sql` agrega `productos.precio_usdt` y crea:

- `billeteras`: saldo USDT por usuario.
- `movimientos_saldo`: historial de débitos, créditos y saldo resultante.
- `pedidos`: encabezado de compra.
- `pedido_detalles`: productos, cantidades y precios históricos.
- `recargas`: solicitudes TRC20, hash, comprobante y auditoría de revisión.

La migración crea billeteras para usuarios existentes y un trigger para usuarios nuevos.

**Importante:** el SQL de la carpeta `database` es una migración sobre un esquema existente; no es un instalador completo desde cero. No ejecutar sobre una base que no tenga previamente las tablas `usuarios` y `productos` con las columnas que utiliza el proyecto. Los valores antiguos `productos.precio` no se convierten automáticamente de COP a USDT; el administrador debe configurar `precio_usdt`.

## 5. Seguridad y reglas técnicas documentadas

1. Consultas JDBC parametrizadas para evitar concatenar datos de usuario en SQL.
2. Contraseñas almacenadas mediante BCrypt.
3. Sesión HTTP para identificar al usuario y su rol.
4. Validación administrativa en el backend.
5. Precios de compra consultados en la base de datos.
6. Bloqueo de billetera/pedido con `SELECT ... FOR UPDATE` durante las operaciones monetarias.
7. Transacciones para compra, aprobación de recarga y devolución de pedido.
8. Montos monetarios almacenados como `DECIMAL(18,8)`.
9. Los comprobantes se guardan con nombre UUID y se restringe su consulta a administradores.

## 6. Matriz mínima de pruebas de aceptación

| ID | Servicio | Escenario | Resultado esperado |
|---|---|---|---|
| API-01 | Registro | Datos válidos | 201 y usuario creado. |
| API-02 | Registro | Correo repetido | 409 y no crea duplicado. |
| API-03 | Login | Credenciales válidas | 200 y cookie de sesión. |
| API-04 | Login | Clave incorrecta | 401. |
| API-05 | Login | Campos vacíos | 400. |
| API-06 | Sesión | Sin iniciar sesión | `authenticated=false`. |
| API-07 | Productos | Consulta pública | Solo activos. |
| API-08 | Productos | Crear como cliente | 403. |
| API-09 | Usuarios | Consultar como cliente | 403. |
| API-10 | Billetera | Consultar sin sesión | 401. |
| API-11 | Recargas | Enviar solicitud válida | 201; saldo no cambia todavía. |
| API-12 | Recargas | Aprobar solicitud pendiente | Saldo aumenta una vez y registra movimiento. |
| API-13 | Recargas | Aprobar la misma solicitud otra vez | Rechaza la segunda resolución; no duplica saldo. |
| API-14 | Pedidos | Comprar con saldo suficiente | 201; pedido, detalle, débito y movimiento creados. |
| API-15 | Pedidos | Comprar sin saldo | 400; no crea pedido ni descuenta saldo. |
| API-16 | Pedidos | Manipular precio en el JSON | Se usa el precio de MySQL. |
| API-17 | Pedidos | Cancelar pedido pagado | Devuelve el total y registra movimiento. |
| API-18 | Pedidos | Repetir cancelación | No genera segundo reembolso. |
| API-19 | Comprobantes | Cliente intenta consultar archivo | 403. |
| API-20 | Logout | Cerrar sesión | 200; la sesión deja de estar autenticada. |

## 7. Límites de la integración de pagos

El sistema implementa saldo interno USDT y revisión manual de recargas. La documentación y la interfaz no deben afirmar que existe verificación automática en Binance/TRON, ni transferencia automática en cadena: la aprobación requiere verificación externa por parte del administrador.

## 8. Evidencias recomendadas para la entrega

- Captura de compilación Maven con `BUILD SUCCESS`.
- Capturas de Postman de registro, login y pruebas de errores.
- Capturas de consulta de catálogo y billetera.
- Captura de solicitud de recarga pendiente y su aprobación.
- Capturas de compra, cancelación y movimiento de devolución.
- Enlace del repositorio GitHub y estructura final del proyecto.
