<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Crear cuenta - El Hacker de las Maquinitas</title>

    <!-- Framework Bootstrap 5.3.3 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <style>
        body {
            background: linear-gradient(135deg, #0f172a, #1e293b);
            color: white;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 25px 12px;
        }

        .tarjeta-registro {
            background: #1e293b;
            border: 1px solid #334155;
            border-radius: 18px;
            padding: 35px;
            width: 100%;
            max-width: 480px;
            box-shadow: 0 15px 40px rgba(0, 0, 0, 0.35);
        }

        .titulo {
            color: #38bdf8;
            font-weight: 800;
        }

        .form-label {
            color: #e2e8f0;
        }

        .form-control {
            background: #0f172a;
            border: 1px solid #475569;
            color: white;
            padding: 12px;
        }

        .form-control:focus {
            background: #0f172a;
            color: white;
            border-color: #38bdf8;
            box-shadow: 0 0 0 0.2rem rgba(56, 189, 248, 0.2);
        }

        .form-control::placeholder {
            color: #94a3b8;
        }

        .btn-registro {
            background: #16a34a;
            color: white;
            border: none;
            padding: 12px;
            font-weight: 600;
        }

        .btn-registro:hover {
            background: #15803d;
            color: white;
        }

        .enlace {
            color: #38bdf8;
            text-decoration: none;
        }

        .enlace:hover {
            text-decoration: underline;
        }
    </style>
</head>

<body>

    <main class="container d-flex justify-content-center">

        <section class="tarjeta-registro">

            <!-- Encabezado del formulario -->
            <div class="text-center mb-4">

                <h1 class="titulo h3">
                    El Hacker de las Maquinitas
                </h1>

                <h2 class="h5 mt-3">
                    Crear cuenta
                </h2>

                <p class="text-secondary">
                    Regístrate para acceder a nuestros servicios.
                </p>

            </div>

            <!-- Formulario conectado al RegistroServlet mediante POST -->
            <form action="${pageContext.request.contextPath}/registro"
                  method="POST">

                <div class="mb-3">
                    <label for="nombre" class="form-label">
                        Nombre completo
                    </label>

                    <input type="text"
                           class="form-control"
                           id="nombre"
                           name="nombre"
                           placeholder="Escribe tu nombre completo"
                           maxlength="100"
                           autocomplete="name"
                           required>
                </div>

                <div class="mb-3">
                    <label for="correo" class="form-label">
                        Correo electrónico
                    </label>

                    <input type="email"
                           class="form-control"
                           id="correo"
                           name="correo"
                           placeholder="ejemplo@correo.com"
                           maxlength="150"
                           autocomplete="email"
                           required>
                </div>

                <div class="mb-3">
                    <label for="telefono" class="form-label">
                        Teléfono
                    </label>

                    <input type="tel"
                           class="form-control"
                           id="telefono"
                           name="telefono"
                           placeholder="Número de teléfono"
                           maxlength="20"
                           autocomplete="tel">
                </div>

                <div class="mb-4">
                    <label for="clave" class="form-label">
                        Contraseña
                    </label>

                    <input type="password"
                           class="form-control"
                           id="clave"
                           name="clave"
                           placeholder="Crea una contraseña"
                           autocomplete="new-password"
                           required>
                </div>

                <!-- Botón para enviar los datos al Servlet -->
                <div class="d-grid">
                    <button type="submit" class="btn btn-registro">
                        Crear mi cuenta
                    </button>
                </div>

            </form>

            <!-- Mensajes enviados desde RegistroServlet -->
            <% if (request.getAttribute("mensaje") != null) { %>

                <div class="alert alert-info text-center mt-4 mb-0" role="alert">
                    <%= request.getAttribute("mensaje") %>
                </div>

            <% } %>

            <!-- Enlace para usuarios que ya tienen cuenta -->
            <div class="text-center mt-4">

                <p class="text-secondary mb-2">
                    ¿Ya tienes una cuenta?
                </p>

                <a class="enlace"
                   href="${pageContext.request.contextPath}/login">
                    Iniciar sesión
                </a>

            </div>

            <div class="text-center mt-4">
                <a href="${pageContext.request.contextPath}/"
                   class="text-secondary text-decoration-none">
                    Volver al inicio
                </a>
            </div>

        </section>

    </main>

    <!-- Bootstrap JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
