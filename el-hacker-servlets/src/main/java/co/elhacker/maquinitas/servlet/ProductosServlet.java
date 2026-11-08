package co.elhacker.maquinitas.servlet;

import co.elhacker.maquinitas.dao.ProductoDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * API para consultar y administrar productos.
 */
@WebServlet("/productos")
public class ProductosServlet extends HttpServlet {

    private ProductoDAO productoDAO;

    @Override
    public void init() {
        productoDAO = new ProductoDAO();
    }

    private boolean esAdministrador(HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);

        return sesion != null
                && "Administrador".equalsIgnoreCase(
                    String.valueOf(sesion.getAttribute("rol"))
                );
    }

    /**
     * Consulta pública del catálogo activo.
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");

        try {
            List<Map<String, Object>> productos =
                    productoDAO.listarProductos();

            StringBuilder json =
                    new StringBuilder("{\"success\":true,\"productos\":[");

            for (int i = 0; i < productos.size(); i++) {
                Map<String, Object> p = productos.get(i);

                if (i > 0) {
                    json.append(",");
                }

                json.append("{")
                    .append("\"id_producto\":").append(p.get("id_producto")).append(",")
                    .append("\"nombre\":\"").append(escapar(String.valueOf(p.get("nombre")))).append("\",")
                    .append("\"descripcion\":\"").append(escapar(String.valueOf(p.get("descripcion")))).append("\",")
                    .append("\"categoria\":\"").append(escapar(String.valueOf(p.get("categoria")))).append("\",")
                    .append("\"tipo_producto\":\"").append(escapar(String.valueOf(p.get("tipo_producto")))).append("\",")
                    .append("\"precio\":").append(p.get("precio") == null ? "null" : p.get("precio")).append(",")
                    .append("\"estado\":\"").append(escapar(String.valueOf(p.get("estado")))).append("\"")
                    .append("}");
            }

            json.append("]}");
            response.getWriter().write(json.toString());

        } catch (Exception e) {
            getServletContext().log("Error consultando productos", e);
            response.setStatus(500);
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Error al consultar el catálogo.\"}"
            );
        }
    }

    /**
     * Operaciones administrativas sobre productos.
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (!esAdministrador(request)) {
            response.setStatus(403);
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Acceso exclusivo para administradores.\"}"
            );
            return;
        }

        try {
            String accion = request.getParameter("accion");
            boolean resultado;

            if ("crear".equals(accion) || "actualizar".equals(accion)) {

                String nombre = request.getParameter("nombre");
                String descripcion = request.getParameter("descripcion");
                String categoria = request.getParameter("categoria");
                String tipo = request.getParameter("tipo_producto");
                BigDecimal precio = new BigDecimal(
                        request.getParameter("precio")
                );

                if (nombre == null || nombre.isBlank()
                        || precio.compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalArgumentException("Datos del producto no válidos.");
                }

                if ("crear".equals(accion)) {
                    resultado = productoDAO.crearProducto(
                            nombre.trim(), descripcion, categoria,
                            tipo, precio
                    );
                } else {
                    int id = Integer.parseInt(
                            request.getParameter("id_producto")
                    );

                    resultado = productoDAO.actualizarProducto(
                            id, nombre.trim(), descripcion,
                            categoria, tipo, precio
                    );
                }

            } else if ("estado".equals(accion)) {

                int id = Integer.parseInt(
                        request.getParameter("id_producto")
                );

                String estado = request.getParameter("estado");

                resultado = productoDAO.cambiarEstado(id, estado);

            } else {
                throw new IllegalArgumentException("Acción no válida.");
            }

            response.getWriter().write(
                    "{\"success\":" + resultado +
                    ",\"message\":\"" +
                    (resultado ? "Operación realizada correctamente."
                            : "No se pudo realizar la operación.") +
                    "\"}"
            );

        } catch (Exception e) {
            getServletContext().log("Error administrando productos", e);
            response.setStatus(400);
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"No fue posible procesar el producto.\"}"
            );
        }
    }

    private String escapar(String valor) {
        return valor.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
