package co.elhacker.maquinitas;

import java.math.BigDecimal;

import co.elhacker.maquinitas.dao.ProductoDAO;
import co.elhacker.maquinitas.modelo.Producto;

public class PruebaProductoDAO {

    public static void main(String[] args) {

        ProductoDAO productoDAO = new ProductoDAO();

        // 1. INSERTAR PRODUCTO
        Producto nuevoProducto = new Producto();

        nuevoProducto.setNombre("PRODUCTO DE PRUEBA CRUD");
        nuevoProducto.setDescripcion("Registro temporal para comprobar JDBC.");
        nuevoProducto.setCategoria("Pruebas");
        nuevoProducto.setTipoProducto("Licencia");
        nuevoProducto.setPrecio(new BigDecimal("1.00"));
        nuevoProducto.setEstado("Activo");

        boolean insertado = productoDAO.insertar(nuevoProducto);

        if (!insertado) {
            System.out.println("ERROR: No fue posible insertar.");
            return;
        }

        System.out.println("1. INSERTAR: CORRECTO");

        // 2. CONSULTAR PRODUCTOS
        System.out.println("\n2. LISTAR PRODUCTOS:");

        for (Producto producto : productoDAO.listar()) {
            System.out.println(
                producto.getIdProducto() + " - " +
                producto.getNombre()
            );
        }

        // 3. BUSCAR PRODUCTO POR ID
        Producto productoEncontrado = null;

        for (Producto producto : productoDAO.listar()) {
            if ("PRODUCTO DE PRUEBA CRUD".equals(producto.getNombre())) {
                productoEncontrado = producto;
                break;
            }
        }

        if (productoEncontrado == null) {
            System.out.println("ERROR: No se encontró el producto.");
            return;
        }

        int id = productoEncontrado.getIdProducto();

        System.out.println("\n3. BUSCAR POR ID: CORRECTO");
        System.out.println("ID encontrado: " + id);

        // 4. ACTUALIZAR PRODUCTO
        productoEncontrado.setNombre("PRODUCTO CRUD ACTUALIZADO");
        productoEncontrado.setPrecio(new BigDecimal("2.00"));

        boolean actualizado = productoDAO.actualizar(productoEncontrado);

        System.out.println(
            "\n4. ACTUALIZAR: " +
            (actualizado ? "CORRECTO" : "ERROR")
        );

        // 5. ELIMINAR PRODUCTO
        boolean eliminado = productoDAO.eliminar(id);

        System.out.println(
            "\n5. ELIMINAR: " +
            (eliminado ? "CORRECTO" : "ERROR")
        );

        // 6. VERIFICAR ELIMINACIÓN
        Producto verificacion = productoDAO.buscarPorId(id);

        System.out.println(
            "\n6. VERIFICAR ELIMINACIÓN: " +
            (verificacion == null ? "CORRECTO" : "ERROR")
        );
    }
}
