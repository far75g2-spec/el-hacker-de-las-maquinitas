package co.elhacker.maquinitas.servlet;

import co.elhacker.maquinitas.dao.UsuarioDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
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
                        String.valueOf(sesion.getAttribute("rol")));
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        prepararRespuesta(response);

        if (!esAdministrador(request)) {
            escribirError(response, HttpServletResponse.SC_FORBIDDEN,
                    "Acceso exclusivo para administradores.");
            return;
        }

        try {
            List<Map<String, Object>> usuarios = usuarioDAO.listarUsuarios();
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(convertirAJson(usuarios));
        } catch (Exception e) {
            getServletContext().log("Error al listar usuarios", e);
            escribirError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error al consultar usuarios.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        prepararRespuesta(response);

        if (!esAdministrador(request)) {
            escribirError(response, HttpServletResponse.SC_FORBIDDEN,
                    "Acceso exclusivo para administradores.");
            return;
        }

        String idTexto = request.getParameter("id_usuario");
        String estado = request.getParameter("estado");

        if (idTexto == null || idTexto.isBlank()
                || estado == null || estado.isBlank()) {
            escribirError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Debe indicar id_usuario y estado.");
            return;
        }

        if (!"Activo".equalsIgnoreCase(estado)
                && !"Inactivo".equalsIgnoreCase(estado)) {
            escribirError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "El estado debe ser Activo o Inactivo.");
            return;
        }

        try {
            int id = Integer.parseInt(idTexto);
            if (id <= 0) {
                escribirError(response, HttpServletResponse.SC_BAD_REQUEST,
                        "El id_usuario debe ser un entero positivo.");
                return;
            }

            boolean actualizado = usuarioDAO.cambiarEstado(id, estado);
            if (!actualizado) {
                escribirError(response, HttpServletResponse.SC_NOT_FOUND,
                        "No se encontró el usuario indicado.");
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(
                    "{\"success\":true,\"message\":\"Estado actualizado correctamente.\"}");
        } catch (NumberFormatException e) {
            escribirError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "id_usuario debe ser un número entero válido.");
        } catch (IllegalArgumentException e) {
            escribirError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Estado no válido.");
        } catch (Exception e) {
            getServletContext().log("Error al actualizar usuario", e);
            escribirError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No fue posible actualizar el usuario.");
        }
    }

    private void prepararRespuesta(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    private void escribirError(HttpServletResponse response, int estado,
                               String mensaje) throws IOException {
        response.setStatus(estado);
        response.getWriter().write(
                "{\"success\":false,\"message\":\"" + escapar(mensaje) + "\"}");
    }

    private String convertirAJson(List<Map<String, Object>> usuarios) {
        StringBuilder json = new StringBuilder("{\"success\":true,\"usuarios\":[");

        for (int i = 0; i < usuarios.size(); i++) {
            Map<String, Object> usuario = usuarios.get(i);
            if (i > 0) json.append(',');

            Object id = usuario.get("id_usuario");
            Object saldo = usuario.get("saldo_usdt");
            BigDecimal saldoDecimal = saldo instanceof BigDecimal
                    ? (BigDecimal) saldo
                    : saldo == null ? BigDecimal.ZERO : new BigDecimal(saldo.toString());

            json.append('{')
                .append("\"id_usuario\":").append(id == null ? "null" : id).append(',')
                .append("\"nombre\":\"").append(escapar(String.valueOf(usuario.get("nombre")))).append("\",")
                .append("\"correo\":\"").append(escapar(String.valueOf(usuario.get("correo")))).append("\",")
                .append("\"telefono\":\"").append(escapar(String.valueOf(usuario.get("telefono")))).append("\",")
                .append("\"rol\":\"").append(escapar(String.valueOf(usuario.get("rol")))).append("\",")
                .append("\"estado\":\"").append(escapar(String.valueOf(usuario.get("estado")))).append("\",")
                .append("\"saldo_usdt\":").append(saldoDecimal.toPlainString())
                .append('}');
        }

        return json.append("]}").toString();
    }

    private String escapar(String valor) {
        if (valor == null || "null".equals(valor)) return "";
        return valor.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
