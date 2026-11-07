package co.elhacker.maquinitas.servlet;

import co.elhacker.maquinitas.dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet encargado de gestionar el inicio de sesión
 * de los usuarios registrados en la aplicación.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // Objeto encargado de consultar y validar los usuarios.
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
     * Muestra el formulario de inicio de sesión.
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

        // Carga la vista de inicio de sesión.
        request.getRequestDispatcher("/login.jsp")
               .forward(request, response);
    }

    /**
     * Procesa las credenciales enviadas por el usuario.
     *
     * @param request solicitud HTTP con las credenciales.
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

        // Obtiene el correo y la contraseña del formulario.
        String correo = request.getParameter("correo");
        String clave = request.getParameter("clave");

        try {
            // Verifica las credenciales mediante la clase UsuarioDAO.
            int usuarioId = usuarioDAO.autenticar(correo, clave);

            if (usuarioId > 0) {

                // Crea la sesión del usuario autenticado.
                HttpSession sesion = request.getSession();

                // Almacena el identificador del usuario en la sesión.
                sesion.setAttribute("usuarioId", usuarioId);

                // Redirige al panel principal de la aplicación.
                response.sendRedirect(
                    request.getContextPath() + "/dashboard.jsp"
                );

            } else {

                // Informa cuando las credenciales no son válidas.
                request.setAttribute("mensaje",
                        "Correo o contraseña incorrectos.");

                request.getRequestDispatcher("/login.jsp")
                       .forward(request, response);
            }

        } catch (Exception e) {

            // Registra los detalles del error en el servidor.
            getServletContext().log(
                    "Error durante el inicio de sesión", e
            );

            // Informa al usuario sin mostrar detalles técnicos.
            request.setAttribute("mensaje",
                    "No fue posible procesar el inicio de sesión.");

            request.getRequestDispatcher("/login.jsp")
                   .forward(request, response);
        }
    }
}