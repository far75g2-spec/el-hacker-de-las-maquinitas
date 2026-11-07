package co.elhacker.maquinitas.servlet;

import co.elhacker.maquinitas.dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/login.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String correo = request.getParameter("correo");
        String clave = request.getParameter("clave");

        try {
            int usuarioId = usuarioDAO.autenticar(correo, clave);

            if (usuarioId > 0) {
                HttpSession sesion = request.getSession();
                sesion.setAttribute("usuarioId", usuarioId);
                response.sendRedirect(
                    request.getContextPath() + "/dashboard.jsp"
                );
            } else {
                request.setAttribute("mensaje",
                        "Correo o contraseña incorrectos.");
                request.getRequestDispatcher("/login.jsp")
                       .forward(request, response);
            }

        } catch (Exception e) {
            getServletContext().log("Error durante el inicio de sesión", e);
            request.setAttribute("mensaje",
                    "No fue posible procesar el inicio de sesión.");
            request.getRequestDispatcher("/login.jsp")
                   .forward(request, response);
        }
    }
}
