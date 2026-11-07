package co.elhacker.maquinitas.servlet;

import co.elhacker.maquinitas.dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet encargado de gestionar el registro
 * de nuevos usuarios en la aplicación web.
 */
@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    // Objeto encargado de realizar las operaciones sobre la tabla usuarios.
    private UsuarioDAO usuarioDAO;

    /**
     * Inicializa el objeto de acceso a datos.
     *
     * @throws ServletException si ocurre un error durante la inicialización.
     */
    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
    }

    /**
     * Muestra el formulario de registro.
     *
     * @param request solicitud HTTP recibida.
     * @param response respuesta HTTP enviada al navegador.
     * @throws ServletException si ocurre un error del servlet.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Carga la vista del formulario de registro.
        request.getRequestDispatcher("/registro.jsp")
               .forward(request, response);
    }

    /**
     * Procesa los datos enviados desde el formulario de registro.
     *
     * @param request solicitud HTTP con los datos del usuario.
     * @param response respuesta HTTP enviada al navegador.
     * @throws ServletException si ocurre un error del servlet.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Configura la codificación para admitir caracteres especiales.
        request.setCharacterEncoding("UTF-8");

        // Obtiene los datos enviados desde el formulario.
        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String telefono = request.getParameter("telefono");
        String clave = request.getParameter("clave");

        // Valida que los campos obligatorios tengan información.
        if (nombre == null || correo == null ||
            clave == null || nombre.isBlank() ||
            correo.isBlank() || clave.isBlank()) {

            // Informa al usuario si faltan datos obligatorios.
            request.setAttribute("mensaje",
                    "Todos los campos obligatorios deben completarse.");

            request.getRequestDispatcher("/registro.jsp")
                   .forward(request, response);
            return;
        }

        try {
            // Envía los datos al DAO para guardar el nuevo usuario.
            boolean registrado = usuarioDAO.registrar(
                    nombre, correo, telefono, clave
            );

            // Genera un mensaje según el resultado de la operación.
            if (registrado) {
                request.setAttribute("mensaje",
                        "Usuario registrado correctamente.");
            } else {
                request.setAttribute("mensaje",
                        "No fue posible registrar el usuario. Verifica el correo.");
            }

        } catch (Exception e) {

            // Registra el error en el servidor para facilitar su diagnóstico.
            getServletContext().log(
                    "Error durante el registro de usuario", e
            );

            // Muestra un mensaje general sin exponer detalles técnicos.
            request.setAttribute("mensaje",
                    "Ocurrió un error al procesar el registro.");
        }

        // Regresa al formulario para mostrar el resultado de la operación.
        request.getRequestDispatcher("/registro.jsp")
               .forward(request, response);
    }
}