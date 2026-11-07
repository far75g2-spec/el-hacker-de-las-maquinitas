package co.elhacker.maquinitas;

import co.elhacker.maquinitas.dao.ProductoDAO;
import co.elhacker.maquinitas.modelo.Producto;

public class App {

    public static void main(String[] args) {

        ProductoDAO productoDAO = new ProductoDAO();

        // Buscar el producto con ID 1
        Producto producto = productoDAO.buscarPorId(1);

        System.out.println("================================");
        System.out.println(" EL HACKER DE LAS MAQUINITAS");
        System.out.println(" BÚSQUEDA DE PRODUCTO");
        System.out.println("================================");

        if (producto != null) {

            System.out.println("ID: " + producto.getIdProducto());
            System.out.println("Nombre: " + producto.getNombre());
            System.out.println("Descripción: " + producto.getDescripcion());
            System.out.println("Categoría: " + producto.getCategoria());
            System.out.println("Tipo: " + producto.getTipoProducto());
            System.out.println("Precio: " + producto.getPrecio());

        } else {
            System.out.println("No se encontró el producto.");
        }
    }
}
