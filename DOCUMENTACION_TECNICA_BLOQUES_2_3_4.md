# Documentación técnica – Bloques 2, 3 y 4

## Arquitectura
React/Vite consume endpoints `/api/*` mediante el proxy de desarrollo. Tomcat 11 ejecuta los Java Servlets. JDBC conecta con MySQL `el_hacker_maquinitas`.

## Modelo de datos añadido
- `billeteras`: saldo USDT por usuario.
- `movimientos_saldo`: libro de movimientos con monto y saldo resultante.
- `pedidos` y `pedido_detalles`: encabezado y detalle de compras, con precios históricos USDT.
- `recargas`: solicitudes TRC20, hash, comprobante, estado y auditoría de revisión.
- `productos.precio_usdt`: precio comercial en USDT, separado de los precios históricos COP.

## Reglas de negocio
1. Un pedido se calcula en el servidor; nunca se confía en los precios enviados por React.
2. La billetera se bloquea con `SELECT ... FOR UPDATE` durante el checkout.
3. Pedido, detalle, débito de saldo y movimiento se guardan en una sola transacción.
4. Una recarga solo acredita saldo cuando el administrador la aprueba.
5. La aprobación bloquea la fila de recarga y solo permite resolver solicitudes en estado Pendiente, evitando acreditación duplicada.
6. Los endpoints consultan el usuario de la sesión; el cliente no puede indicar el usuario destino.
7. El administrador se valida en backend, no solo en rutas React.
8. Montos usan `DECIMAL(18,8)`.

## API
- `GET /api/billetera`: saldo y últimos movimientos del usuario.
- `GET /api/pedidos`: pedidos propios; administrador obtiene todos.
- `POST /api/pedidos`: JSON `{ "items": [{"id_producto": 1, "cantidad": 1}] }` para checkout. Admin envía `{ "id_pedido": 1, "estado": "En proceso" }` para cambiar estado.
- `GET /api/recargas`: solicitudes propias; administrador obtiene todas.
- `POST /api/recargas`: multipart `monto_usdt`, `tx_hash`, `comprobante` (opcional). Admin envía `id_recarga`, `estado` (Aprobada/Rechazada), `notas_admin`.
- `GET /api/comprobantes/{uuid.ext}`: descarga protegida para administradores.

## Pruebas
Seguir la matriz de pruebas del archivo INSTRUCCIONES.txt. La aceptación real requiere ejecutar la migración y probar en el entorno local con las credenciales y esquema MySQL del usuario.

## Alcance y limitaciones
El módulo implementa contabilidad interna y revisión manual de pagos. No se conecta a Binance API ni a un nodo TRON, no verifica automáticamente transacciones en cadena y no inicia transferencias. No se debe anunciar una recarga como aprobada hasta verificarla externamente.
