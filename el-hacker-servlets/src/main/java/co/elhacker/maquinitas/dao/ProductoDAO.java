package co.elhacker.maquinitas.dao;

import co.elhacker.maquinitas.conexion.Conexion;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Acceso a datos y gestión del catálogo de productos.
 */
public class ProductoDAO {

    /**
     * Consulta los productos activos.
     */
    public List<Map<String, Object>> listarProductos()
            throws SQLException {

        List<Map<String, Object>> productos = new ArrayList<>();

        String sql = """
                SELECT id_producto, nombre, descripcion,
                       categoria, tipo_producto, precio_usdt AS precio,
                       estado, fecha_creacion
                FROM productos
                WHERE estado = 'Activo'
                ORDER BY id_producto DESC
                """;

        try (Connection conexion = Conexion.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                Map<String, Object> producto = new LinkedHashMap<>();

                producto.put("id_producto", resultado.getInt("id_producto"));
                producto.put("nombre", resultado.getString("nombre"));
                producto.put("descripcion", resultado.getString("descripcion"));
                producto.put("categoria", resultado.getString("categoria"));
                producto.put("tipo_producto", resultado.getString("tipo_producto"));
                producto.put("precio", resultado.getBigDecimal("precio"));
                producto.put("estado", resultado.getString("estado"));

                productos.add(producto);
            }
        }

        return productos;
    }

    /**
     * Consulta todos los productos, incluidos los inactivos.
     * Este método debe utilizarse únicamente desde operaciones administrativas.
     */
    public List<Map<String, Object>> listarTodosProductos()
            throws SQLException {

        List<Map<String, Object>> productos = new ArrayList<>();
        String sql = """
                SELECT id_producto, nombre, descripcion,
                       categoria, tipo_producto, precio_usdt AS precio, estado, fecha_creacion
                FROM productos
                ORDER BY id_producto DESC
                """;

        try (Connection conexion = Conexion.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                Map<String, Object> producto = new LinkedHashMap<>();
                producto.put("id_producto", resultado.getInt("id_producto"));
                producto.put("nombre", resultado.getString("nombre"));
                producto.put("descripcion", resultado.getString("descripcion"));
                producto.put("categoria", resultado.getString("categoria"));
                producto.put("tipo_producto", resultado.getString("tipo_producto"));
                producto.put("precio", resultado.getBigDecimal("precio"));
                producto.put("estado", resultado.getString("estado"));
                productos.add(producto);
            }
        }
        return productos;
    }

    /**
     * Registra un producto nuevo.
     */
    public boolean crearProducto(
            String nombre,
            String descripcion,
            String categoria,
            String tipoProducto,
            BigDecimal precio) throws SQLException {

        String sql = """
                INSERT INTO productos
                (nombre, descripcion, categoria, tipo_producto, precio_usdt, estado)
                VALUES (?, ?, ?, ?, ?, 'Activo')
                """;

        try (Connection conexion = Conexion.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, nombre);
            consulta.setString(2, descripcion);
            consulta.setString(3, categoria);
            consulta.setString(4, tipoProducto);
            consulta.setBigDecimal(5, precio);

            return consulta.executeUpdate() > 0;
        }
    }

    /**
     * Actualiza los datos de un producto.
     */
    public boolean actualizarProducto(
            int id,
            String nombre,
            String descripcion,
            String categoria,
            String tipoProducto,
            BigDecimal precio) throws SQLException {

        String sql = """
                UPDATE productos
                SET nombre = ?, descripcion = ?, categoria = ?,
                    tipo_producto = ?, precio_usdt = ?
                WHERE id_producto = ?
                """;

        try (Connection conexion = Conexion.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, nombre);
            consulta.setString(2, descripcion);
            consulta.setString(3, categoria);
            consulta.setString(4, tipoProducto);
            consulta.setBigDecimal(5, precio);
            consulta.setInt(6, id);

            return consulta.executeUpdate() > 0;
        }
    }

    /**
     * Activa o inactiva un producto sin eliminar su historial.
     */
    public boolean cambiarEstado(int id, String estado)
            throws SQLException {

        if (!"Activo".equalsIgnoreCase(estado)
                && !"Inactivo".equalsIgnoreCase(estado)) {
            throw new IllegalArgumentException("Estado no válido.");
        }

        String sql = """
                UPDATE productos
                SET estado = ?
                WHERE id_producto = ?
                """;

        try (Connection conexion = Conexion.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, estado);
            consulta.setInt(2, id);

            return consulta.executeUpdate() > 0;
        }
    }
}
