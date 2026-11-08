package co.elhacker.maquinitas.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet encargado de finalizar la sesión del usuario.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * Invalida la sesión existente y responde al frontend.
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        HttpSession sesion = request.getSession(false);

        if (sesion != null) {
            sesion.invalidate();
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().write(
                "{\"success\":true,\"message\":\"Sesión cerrada correctamente.\"}"
        );
    }
}
