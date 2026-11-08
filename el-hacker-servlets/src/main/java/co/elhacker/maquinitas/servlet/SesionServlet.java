package co.elhacker.maquinitas.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Comprueba la autenticación y el rol del usuario.
 */
@WebServlet("/sesion")
public class SesionServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");

        HttpSession sesion = request.getSession(false);

        boolean autenticado = false;
        int usuarioId = 0;
        String rol = "";

        if (sesion != null) {
            Object id = sesion.getAttribute("usuarioId");
            Object rolSesion = sesion.getAttribute("rol");

            if (id instanceof Integer) {
                usuarioId = (Integer) id;
                autenticado = usuarioId > 0;
            }

            if (rolSesion instanceof String) {
                rol = (String) rolSesion;
            }
        }

        try (PrintWriter out = response.getWriter()) {
            out.print(
                "{\"authenticated\":" + autenticado +
                ",\"usuarioId\":" + usuarioId +
                ",\"rol\":\"" + escapar(rol) + "\"}"
            );
        }
    }

    private String escapar(String texto) {
        return texto.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
