package co.elhacker.maquinitas.dao;

import co.elhacker.maquinitas.conexion.Conexion;
import co.elhacker.maquinitas.modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // INSERTAR PRODUCTO
    public boolean insertar(Producto producto) {

        String sql = "INSERT INTO productos "
                + "(nombre, descripcion, categoria, tipo_producto, precio) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, producto.getNombre());
            consulta.setString(2, producto.getDescripcion());
            consulta.setString(3, producto.getCategoria());
            consulta.setString(4, producto.getTipoProducto());
            consulta.setBigDecimal(5, producto.getPrecio());

            return consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    // CONSULTAR TODOS LOS PRODUCTOS
    public List<Producto> listar() {

        List<Producto> productos = new ArrayList<>();

        String sql = "SELECT * FROM productos";

        try (Connection conexion = Conexion.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {

                Producto producto = new Producto();

                producto.setIdProducto(resultado.getInt("id_producto"));
                producto.setNombre(resultado.getString("nombre"));
                producto.setDescripcion(resultado.getString("descripcion"));
                producto.setCategoria(resultado.getString("categoria"));
                producto.setTipoProducto(resultado.getString("tipo_producto"));
                producto.setPrecio(resultado.getBigDecimal("precio"));
                producto.setEstado(resultado.getString("estado"));

                productos.add(producto);
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar productos: " + e.getMessage());
        }

        return productos;
    }

    // ACTUALIZAR PRODUCTO
    public boolean actualizar(Producto producto) {

        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, "
                + "categoria = ?, tipo_producto = ?, precio = ?, estado = ? "
                + "WHERE id_producto = ?";

        try (Connection conexion = Conexion.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, producto.getNombre());
            consulta.setString(2, producto.getDescripcion());
            consulta.setString(3, producto.getCategoria());
            consulta.setString(4, producto.getTipoProducto());
            consulta.setBigDecimal(5, producto.getPrecio());
            consulta.setString(6, producto.getEstado());
            consulta.setInt(7, producto.getIdProducto());

            return consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    // ELIMINAR PRODUCTO
    public boolean eliminar(int idProducto) {

        String sql = "DELETE FROM productos WHERE id_producto = ?";

        try (Connection conexion = Conexion.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idProducto);

            return consulta.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    // BUSCAR PRODUCTO POR ID
    public Producto buscarPorId(int idProducto) {

        String sql = "SELECT * FROM productos WHERE id_producto = ?";

        try (
            Connection conexion = Conexion.obtenerConexion();
            PreparedStatement consulta = conexion.prepareStatement(sql)
        ) {

            consulta.setInt(1, idProducto);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {

                    Producto producto = new Producto();

                    producto.setIdProducto(resultado.getInt("id_producto"));
                    producto.setNombre(resultado.getString("nombre"));
                    producto.setDescripcion(resultado.getString("descripcion"));
                    producto.setCategoria(resultado.getString("categoria"));
                    producto.setTipoProducto(resultado.getString("tipo_producto"));
                    producto.setPrecio(resultado.getBigDecimal("precio"));
                    producto.setEstado(resultado.getString("estado"));

                    return producto;
                }

            }

        } catch (SQLException e) {
            System.out.println("Error al buscar producto: " + e.getMessage());
        }

        return null;
    }
}
