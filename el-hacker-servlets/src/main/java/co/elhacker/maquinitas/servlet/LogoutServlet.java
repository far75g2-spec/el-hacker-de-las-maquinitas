package co.elhacker.maquinitas.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet encargado de cerrar la sesión del usuario.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * Procesa la solicitud POST para finalizar la sesión.
     *
     * @param request solicitud HTTP recibida.
     * @param response respuesta HTTP enviada al navegador.
     * @throws ServletException si ocurre un error del servlet.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Obtiene la sesión actual sin crear una nueva.
        HttpSession sesion = request.getSession(false);

        // Invalida la sesión si existe, cerrando el acceso autenticado.
        if (sesion != null) {
            sesion.invalidate();
        }

        // Redirige al usuario al formulario de inicio de sesión.
        response.sendRedirect(
                request.getContextPath() + "/login"
        );
    }
}