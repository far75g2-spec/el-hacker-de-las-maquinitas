package co.elhacker.maquinitas.servlet;

import co.elhacker.maquinitas.dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/registro.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String telefono = request.getParameter("telefono");
        String clave = request.getParameter("clave");

        if (nombre == null || correo == null ||
            clave == null || nombre.isBlank() ||
            correo.isBlank() || clave.isBlank()) {

            request.setAttribute("mensaje",
                    "Todos los campos obligatorios deben completarse.");

            request.getRequestDispatcher("/registro.jsp")
                   .forward(request, response);
            return;
        }

        try {
            boolean registrado = usuarioDAO.registrar(
                    nombre, correo, telefono, clave
            );

            if (registrado) {
                request.setAttribute("mensaje",
                        "Usuario registrado correctamente.");
            } else {
                request.setAttribute("mensaje",
                        "No fue posible registrar el usuario. Verifica el correo.");
            }

        } catch (Exception e) {
            getServletContext().log(
                    "Error durante el registro de usuario", e
            );

            request.setAttribute("mensaje",
                    "Ocurrió un error al procesar el registro.");
        }

        request.getRequestDispatcher("/registro.jsp")
               .forward(request, response);
    }
}
