package co.elhacker.maquinitas.servlet;

import co.elhacker.maquinitas.dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Gestiona la autenticación y los roles de los usuarios.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String correo = request.getParameter("correo");
        String clave = request.getParameter("clave");

        if (correo == null || clave == null
                || correo.isBlank() || clave.isBlank()) {

            response.setStatus(400);
            enviarRespuesta(response, false,
                    "Ingresa tu correo y contraseña.");
            return;
        }

        try {
            int usuarioId = usuarioDAO.autenticar(
                    correo.trim(), clave
            );

            if (usuarioId <= 0) {
                response.setStatus(401);
                enviarRespuesta(response, false,
                        "Correo o contraseña incorrectos.");
                return;
            }

            String rol = usuarioDAO.obtenerRol(usuarioId);

            if (rol == null || rol.isBlank()) {
                response.setStatus(403);
                enviarRespuesta(response, false,
                        "La cuenta no tiene un rol asignado.");
                return;
            }

            HttpSession anterior = request.getSession(false);

            if (anterior != null) {
                anterior.invalidate();
            }

            HttpSession sesion = request.getSession(true);
            sesion.setAttribute("usuarioId", usuarioId);
            sesion.setAttribute("rol", rol);
            sesion.setMaxInactiveInterval(30 * 60);

            response.setStatus(200);

            try (PrintWriter out = response.getWriter()) {
                out.print(
                    "{\"success\":true," +
                    "\"message\":\"Inicio de sesión correcto.\"," +
                    "\"rol\":\"" + escapar(rol) + "\"}"
                );
            }

        } catch (Exception e) {
            getServletContext().log(
                    "Error durante el inicio de sesión", e
            );

            response.setStatus(500);
            enviarRespuesta(response, false,
                    "No fue posible procesar el inicio de sesión.");
        }
    }

    private void enviarRespuesta(
            HttpServletResponse response,
            boolean success,
            String message) throws IOException {

        try (PrintWriter out = response.getWriter()) {
            out.print(
                "{\"success\":" + success +
                ",\"message\":\"" + escapar(message) + "\"}"
            );
        }
    }

    private String escapar(String texto) {
        return texto.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
