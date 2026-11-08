package co.elhacker.maquinitas.servlet;

import co.elhacker.maquinitas.util.Autenticacion;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/comprobantes/*")
public class ComprobanteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!Autenticacion.admin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String nombre = request.getPathInfo();
        if (nombre == null || !nombre.matches("/[a-f0-9-]{36}\\.(jpg|png|pdf)")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Path archivo = Paths.get(System.getProperty("catalina.base", System.getProperty("java.io.tmpdir")),
                "uploads", "hacker-comprobantes", nombre.substring(1)).normalize();
        if (!Files.isRegularFile(archivo)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String extension = nombre.substring(nombre.lastIndexOf('.') + 1);
        response.setContentType(extension.equals("pdf") ? "application/pdf" : extension.equals("png") ? "image/png" : "image/jpeg");
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("Content-Disposition", "inline; filename=\"comprobante." + extension + "\"");
        Files.copy(archivo, response.getOutputStream());
    }
}
