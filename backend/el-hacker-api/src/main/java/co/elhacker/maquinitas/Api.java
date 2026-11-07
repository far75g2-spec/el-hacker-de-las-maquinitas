package co.elhacker.maquinitas;

import io.javalin.Javalin;
import java.util.Map;

import co.elhacker.maquinitas.dao.UsuarioDAO;
import co.elhacker.maquinitas.modelo.Usuario;
import co.elhacker.maquinitas.seguridad.Seguridad;

public class Api {

    public static void main(String[] args) {

        // =====================================
        // CONFIGURACIÓN DEL SERVIDOR
        // =====================================

        Javalin app = Javalin.create(config -> {

            config.http.defaultContentType = "application/json";

            // CORS para Live Server y cookies
            config.bundledPlugins.enableCors(cors ->
                cors.addRule(it -> {
                    it.allowHost("http://127.0.0.1:5500");
                    it.allowCredentials = true;
                })
            );

        });

        // =====================================
        // RUTA 1: ESTADO DE LA API
        // =====================================

        app.get("/api/estado", ctx -> {

            ctx.json(Map.of(
                "estado", "correcto",
                "mensaje", "El Hacker de las Maquinitas API está funcionando"
            ));

        });

        // =====================================
        // RUTA 2: REGISTRO DE USUARIOS
        // =====================================

        app.post("/api/registro", ctx -> {

            try {

                RegistroRequest datos =
                    ctx.bodyAsClass(RegistroRequest.class);

                if (datos.nombre == null ||
                    datos.correo == null ||
                    datos.contrasena == null) {

                    ctx.status(400).json(Map.of(
                        "estado", "error",
                        "mensaje", "Completa los campos obligatorios."
                    ));
                    return;
                }

                String nombre = datos.nombre.trim();
                String correo = datos.correo.trim().toLowerCase();

                String telefono = datos.telefono == null
                    ? ""
                    : datos.telefono.trim();

                String contrasena = datos.contrasena;

                if (nombre.length() < 3 ||
                    nombre.length() > 100 ||
                    correo.length() > 150 ||
                    !correo.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$") ||
                    contrasena.length() < 8 ||
                    contrasena.length() > 128 ||
                    telefono.length() > 20) {

                    ctx.status(400).json(Map.of(
                        "estado", "error",
                        "mensaje", "Revisa los datos ingresados."
                    ));
                    return;
                }

                // Generar hash de contraseña
                String hash = Seguridad.generarHash(contrasena);

                // Crear usuario
                Usuario usuario = new Usuario();

                usuario.setNombre(nombre);
                usuario.setCorreo(correo);
                usuario.setTelefono(telefono);
                usuario.setClaveHash(hash);
                usuario.setRol("Cliente");

                // Guardar en MySQL
                UsuarioDAO usuarioDAO = new UsuarioDAO();

                boolean registrado = usuarioDAO.registrar(usuario);

                if (registrado) {

                    ctx.status(201).json(Map.of(
                        "estado", "correcto",
                        "mensaje", "Usuario registrado correctamente."
                    ));

                } else {

                    ctx.status(409).json(Map.of(
                        "estado", "error",
                        "mensaje", "No se pudo registrar. Verifica si el correo ya existe."
                    ));
                }

            } catch (Exception e) {

                System.err.println(
                    "Error en el registro: " + e.getMessage()
                );

                ctx.status(500).json(Map.of(
                    "estado", "error",
                    "mensaje", "Ocurrió un error interno al registrar."
                ));
            }

        });

        // =====================================
        // RUTA 3: INICIO DE SESIÓN
        // =====================================

        app.post("/api/login", ctx -> {

            try {

                LoginRequest datos =
                    ctx.bodyAsClass(LoginRequest.class);

                if (datos.correo == null ||
                    datos.contrasena == null ||
                    datos.correo.isBlank() ||
                    datos.contrasena.isBlank()) {

                    ctx.status(400).json(Map.of(
                        "estado", "error",
                        "mensaje", "Ingresa tu correo y contraseña."
                    ));
                    return;
                }

                String correo = datos.correo.trim().toLowerCase();
                String contrasena = datos.contrasena;

                if (correo.length() > 150 ||
                    contrasena.length() > 128) {

                    ctx.status(400).json(Map.of(
                        "estado", "error",
                        "mensaje", "Los datos ingresados no son válidos."
                    ));
                    return;
                }

                // Buscar usuario en MySQL
                UsuarioDAO usuarioDAO = new UsuarioDAO();

                Usuario usuario =
                    usuarioDAO.buscarPorCorreo(correo);

                if (usuario == null) {

                    ctx.status(401).json(Map.of(
                        "estado", "error",
                        "mensaje", "Correo o contraseña incorrectos."
                    ));
                    return;
                }

                // Comprobar estado de la cuenta
                if (usuario.getEstado() == null ||
                    !usuario.getEstado().equalsIgnoreCase("Activo")) {

                    ctx.status(403).json(Map.of(
                        "estado", "error",
                        "mensaje", "Tu cuenta no está activa. Contacta al administrador."
                    ));
                    return;
                }

                // Verificar contraseña con BCrypt
                boolean claveCorrecta =
                    Seguridad.verificarClave(
                        contrasena,
                        usuario.getClaveHash()
                    );

                if (!claveCorrecta) {

                    ctx.status(401).json(Map.of(
                        "estado", "error",
                        "mensaje", "Correo o contraseña incorrectos."
                    ));
                    return;
                }

                // Invalidar una sesión anterior, si existe
                var sesionAnterior = ctx.req().getSession(false);

                if (sesionAnterior != null) {
                    sesionAnterior.invalidate();
                }

                // Crear nueva sesión en el servidor
                var sesion = ctx.req().getSession(true);

                // Tiempo máximo de inactividad: 30 minutos
                sesion.setMaxInactiveInterval(1800);

                // Guardar datos del usuario autenticado
                sesion.setAttribute(
                    "usuarioId",
                    usuario.getIdUsuario()
                );

                sesion.setAttribute(
                    "nombreUsuario",
                    usuario.getNombre()
                );

                sesion.setAttribute(
                    "correoUsuario",
                    usuario.getCorreo()
                );

                sesion.setAttribute(
                    "rolUsuario",
                    usuario.getRol()
                );

                // Respuesta de acceso autorizado
                ctx.status(200).json(Map.of(
                    "estado", "correcto",
                    "mensaje", "Inicio de sesión exitoso.",
                    "usuario", Map.of(
                        "id", usuario.getIdUsuario(),
                        "nombre", usuario.getNombre(),
                        "correo", usuario.getCorreo(),
                        "rol", usuario.getRol()
                    )
                ));

            } catch (Exception e) {

                System.err.println(
                    "Error en el inicio de sesión: " + e.getMessage()
                );

                ctx.status(500).json(Map.of(
                    "estado", "error",
                    "mensaje", "Ocurrió un error interno al iniciar sesión."
                ));
            }

        });

        // =====================================
        // RUTA 4: CONSULTAR SESIÓN ACTIVA
        // =====================================

        app.get("/api/sesion", ctx -> {

            var sesion = ctx.req().getSession(false);

            if (sesion == null ||
                sesion.getAttribute("usuarioId") == null) {

                ctx.status(401).json(Map.of(
                    "estado", "error",
                    "mensaje", "No hay una sesión activa."
                ));
                return;
            }

            ctx.json(Map.of(
                "estado", "correcto",
                "mensaje", "Sesión activa.",
                "usuario", Map.of(
                    "id", sesion.getAttribute("usuarioId"),
                    "nombre", sesion.getAttribute("nombreUsuario"),
                    "correo", sesion.getAttribute("correoUsuario"),
                    "rol", sesion.getAttribute("rolUsuario")
                )
            ));

        });

        // =====================================
        // RUTA 5: CERRAR SESIÓN
        // =====================================

        app.post("/api/logout", ctx -> {

            var sesion = ctx.req().getSession(false);

            if (sesion != null) {
                sesion.invalidate();
            }

            ctx.json(Map.of(
                "estado", "correcto",
                "mensaje", "Sesión cerrada correctamente."
            ));

        });

        // =====================================
        // INICIAR SERVIDOR
        // =====================================

        app.start(7000);

        System.out.println("--------------------------------");
        System.out.println("EL HACKER DE LAS MAQUINITAS");
        System.out.println("Servidor iniciado correctamente");
        System.out.println("Puerto: 7000");
        System.out.println("Rutas de registro y autenticación activas");
        System.out.println("--------------------------------");

    }

    // =====================================
    // DATOS DEL REGISTRO
    // =====================================

    public static class RegistroRequest {

        public String nombre;
        public String correo;
        public String telefono;
        public String contrasena;

    }

    // =====================================
    // DATOS DEL INICIO DE SESIÓN
    // =====================================

    public static class LoginRequest {

        public String correo;
        public String contrasena;

    }

}
