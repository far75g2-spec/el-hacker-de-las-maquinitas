-- Migración para Bloques 2 y 3 de El Hacker de las Maquinitas.
-- Ejecutar en MySQL Workbench sobre el esquema existente el_hacker_maquinitas.
USE el_hacker_maquinitas;

-- Los valores históricos de productos.precio eran COP. Se crea precio_usdt vacío
-- para que el administrador asigne el precio correcto en USDT; no se convierten
-- automáticamente valores COP a USDT.
ALTER TABLE productos ADD COLUMN IF NOT EXISTS precio_usdt DECIMAL(18,8) NULL AFTER precio;

CREATE TABLE IF NOT EXISTS billeteras (
 id_usuario INT NOT NULL PRIMARY KEY,
 saldo_usdt DECIMAL(18,8) NOT NULL DEFAULT 0.00000000,
 actualizada_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 CONSTRAINT fk_billetera_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS movimientos_saldo (
 id_movimiento BIGINT AUTO_INCREMENT PRIMARY KEY,
 id_usuario INT NOT NULL,
 tipo VARCHAR(20) NOT NULL,
 monto_usdt DECIMAL(18,8) NOT NULL,
 saldo_resultante DECIMAL(18,8) NOT NULL,
 referencia VARCHAR(80),
 descripcion VARCHAR(255),
 fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 INDEX idx_mov_usuario_fecha(id_usuario,fecha),
 CONSTRAINT fk_mov_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS pedidos (
 id_pedido BIGINT AUTO_INCREMENT PRIMARY KEY,
 id_usuario INT NOT NULL,
 total_usdt DECIMAL(18,8) NOT NULL,
 estado VARCHAR(40) NOT NULL DEFAULT 'Pendiente de procesamiento',
 fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 INDEX idx_ped_usuario_fecha(id_usuario,fecha),
 CONSTRAINT fk_pedido_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS pedido_detalles (
 id_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
 id_pedido BIGINT NOT NULL,
 id_producto INT NULL,
 nombre_producto VARCHAR(100) NOT NULL,
 precio_unitario_usdt DECIMAL(18,8) NOT NULL,
 cantidad INT NOT NULL,
 subtotal_usdt DECIMAL(18,8) NOT NULL,
 CONSTRAINT fk_det_pedido FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido),
 CONSTRAINT fk_det_producto FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS recargas (
 id_recarga BIGINT AUTO_INCREMENT PRIMARY KEY,
 id_usuario INT NOT NULL,
 monto_usdt DECIMAL(18,8) NOT NULL,
 red VARCHAR(20) NOT NULL DEFAULT 'TRC20',
 tx_hash VARCHAR(180) NOT NULL,
 archivo_comprobante VARCHAR(80),
 estado VARCHAR(20) NOT NULL DEFAULT 'Pendiente',
 notas_admin VARCHAR(500),
 id_admin INT NULL,
 fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 fecha_revision TIMESTAMP NULL,
 UNIQUE KEY uq_recarga_tx(tx_hash),
 INDEX idx_rec_usuario_fecha(id_usuario,fecha),
 CONSTRAINT fk_rec_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
 CONSTRAINT fk_rec_admin FOREIGN KEY (id_admin) REFERENCES usuarios(id_usuario)
) ENGINE=InnoDB;

-- Crear billetera para todos los usuarios actuales; los saldos iniciales quedan en cero.
INSERT IGNORE INTO billeteras(id_usuario,saldo_usdt)
SELECT id_usuario,0.00000000 FROM usuarios;

-- Al registrar usuarios nuevos, la aplicación debe crear su billetera.
DELIMITER $$
DROP TRIGGER IF EXISTS trg_usuario_crear_billetera$$
CREATE TRIGGER trg_usuario_crear_billetera AFTER INSERT ON usuarios
FOR EACH ROW BEGIN
 INSERT IGNORE INTO billeteras(id_usuario,saldo_usdt) VALUES(NEW.id_usuario,0.00000000);
END$$
DELIMITER ;

-- Revisión de migración y precios pendientes de configurar.
SELECT COUNT(*) AS productos_sin_precio_usdt FROM productos WHERE precio_usdt IS NULL OR precio_usdt <= 0;
SELECT COUNT(*) AS billeteras_creadas FROM billeteras;
