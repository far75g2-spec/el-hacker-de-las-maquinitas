package co.elhacker.maquinitas.servlet;

import co.elhacker.maquinitas.dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet encargado de registrar usuarios y responder al frontend React.
 */
@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
    }

    /**
     * Conserva la vista JSP para el acceso tradicional.
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/registro.jsp")
                .forward(request, response);
    }

    /**
     * Recibe los datos enviados desde React y registra el usuario.
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String telefono = request.getParameter("telefono");
        String clave = request.getParameter("clave");

        if (nombre == null || correo == null || clave == null
                || nombre.isBlank() || correo.isBlank() || clave.isBlank()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            enviarRespuesta(response,
                    false,
                    "Completa todos los campos obligatorios.");

            return;
        }

        try {
            boolean registrado = usuarioDAO.registrar(
                    nombre.trim(),
                    correo.trim(),
                    telefono == null ? "" : telefono.trim(),
                    clave
            );

            if (registrado) {
                response.setStatus(HttpServletResponse.SC_CREATED);

                enviarRespuesta(response,
                        true,
                        "Usuario registrado correctamente.");

            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);

                enviarRespuesta(response,
                        false,
                        "No fue posible registrar el usuario. Verifica el correo.");
            }

        } catch (Exception e) {

            getServletContext().log(
                    "Error durante el registro de usuario", e
            );

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            enviarRespuesta(response,
                    false,
                    "Ocurrió un error al procesar el registro.");
        }
    }

    /**
     * Construye una respuesta JSON sencilla para React.
     */
    private void enviarRespuesta(
            HttpServletResponse response,
            boolean success,
            String message) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String mensajeSeguro = message
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");

        try (PrintWriter out = response.getWriter()) {
            out.print(
                    "{\"success\":" + success
                    + ",\"message\":\"" + mensajeSeguro + "\"}"
            );
        }
    }
}
