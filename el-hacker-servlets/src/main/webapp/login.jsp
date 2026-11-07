<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Iniciar sesión - El Hacker de las Maquinitas</title>

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
            padding: 20px;
        }

        .tarjeta-login {
            background: #1e293b;
            border: 1px solid #334155;
            border-radius: 18px;
            padding: 40px 35px;
            width: 100%;
            max-width: 430px;
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

        .btn-login {
            background: #16a34a;
            border: none;
            color: white;
            padding: 12px;
            font-weight: 600;
        }

        .btn-login:hover {
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

        <section class="tarjeta-login">

            <!-- Encabezado del formulario de autenticación -->
            <div class="text-center mb-4">

                <h1 class="titulo h3">
                    El Hacker de las Maquinitas
                </h1>

                <h2 class="h5 mt-3">
                    Iniciar sesión
                </h2>

                <p class="text-secondary">
                    Ingresa tus credenciales para continuar.
                </p>

            </div>

            <!-- Formulario que envía los datos al LoginServlet mediante POST -->
            <form action="${pageContext.request.contextPath}/login"
                  method="POST">

                <div class="mb-3">
                    <label for="correo" class="form-label">
                        Correo electrónico
                    </label>

                    <input type="email"
                           class="form-control"
                           id="correo"
                           name="correo"
                           placeholder="ejemplo@correo.com"
                           autocomplete="username"
                           required>
                </div>

                <div class="mb-4">
                    <label for="clave" class="form-label">
                        Contraseña
                    </label>

                    <input type="password"
                           class="form-control"
                           id="clave"
                           name="clave"
                           placeholder="Escribe tu contraseña"
                           autocomplete="current-password"
                           required>
                </div>

                <!-- Botón para enviar las credenciales -->
                <div class="d-grid">
                    <button type="submit" class="btn btn-login">
                        Ingresar a mi cuenta
                    </button>
                </div>

            </form>

            <!-- Mensajes enviados desde LoginServlet -->
            <% if (request.getAttribute("mensaje") != null) { %>

                <div class="alert alert-danger text-center mt-4 mb-0"
                     role="alert">
                    <%= request.getAttribute("mensaje") %>
                </div>

            <% } %>

            <!-- Acceso al formulario de registro -->
            <div class="text-center mt-4">

                <p class="text-secondary mb-2">
                    ¿Todavía no tienes una cuenta?
                </p>

                <a class="enlace"
                   href="${pageContext.request.contextPath}/registro">
                    Crear una cuenta
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
