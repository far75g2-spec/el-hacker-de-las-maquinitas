package co.elhacker.maquinitas.servlet;

import co.elhacker.maquinitas.dao.UsuarioDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@WebServlet("/usuarios")
public class UsuariosServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    private boolean esAdministrador(HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);

        return sesion != null
                && "Administrador".equalsIgnoreCase(
                        String.valueOf(sesion.getAttribute("rol"))
                );
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (!esAdministrador(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Acceso exclusivo para administradores.\"}"
            );
            return;
        }

        try {
            List<Map<String, Object>> usuarios =
                    usuarioDAO.listarUsuarios();

            response.getWriter().write(
                    convertirAJson(usuarios)
            );

        } catch (Exception e) {
            getServletContext().log("Error al listar usuarios", e);
            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Error al consultar usuarios.\"}"
            );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (!esAdministrador(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Acceso exclusivo para administradores.\"}"
            );
            return;
        }

        try {
            int id = Integer.parseInt(
                    request.getParameter("id_usuario")
            );

            String estado = request.getParameter("estado");

            boolean actualizado =
                    usuarioDAO.cambiarEstado(id, estado);

            response.getWriter().write(
                    "{\"success\":" + actualizado +
                    ",\"message\":\"" +
                    (actualizado
                            ? "Estado actualizado."
                            : "No se encontró el usuario.") +
                    "\"}"
            );

        } catch (Exception e) {
            getServletContext().log("Error al actualizar usuario", e);
            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"No fue posible actualizar el usuario.\"}"
            );
        }
    }

    private String convertirAJson(List<Map<String, Object>> usuarios) {
        StringBuilder json = new StringBuilder("{\"success\":true,\"usuarios\":[");

        for (int i = 0; i < usuarios.size(); i++) {
            Map<String, Object> usuario = usuarios.get(i);

            if (i > 0) {
                json.append(",");
            }

            json.append("{")
                .append("\"id_usuario\":").append(usuario.get("id_usuario")).append(",")
                .append("\"nombre\":\"").append(escapar(String.valueOf(usuario.get("nombre")))).append("\",")
                .append("\"correo\":\"").append(escapar(String.valueOf(usuario.get("correo")))).append("\",")
                .append("\"telefono\":\"").append(escapar(String.valueOf(usuario.get("telefono")))).append("\",")
                .append("\"rol\":\"").append(escapar(String.valueOf(usuario.get("rol")))).append("\",")
                .append("\"estado\":\"").append(escapar(String.valueOf(usuario.get("estado")))).append("\"")
                .append("}");
        }

        return json.append("]}").toString();
    }

    private String escapar(String valor) {
        return valor.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
