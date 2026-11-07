<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // Verificar que exista una sesión de usuario activa.
    // Si no existe, redirigir al formulario de inicio de sesión.
    if (session.getAttribute("usuarioId") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Panel de usuario - El Hacker de las Maquinitas</title>

    <!-- Framework Bootstrap 5.3.3 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <style>
        body {
            background: linear-gradient(135deg, #0f172a, #1e293b);
            color: white;
            min-height: 100vh;
        }

        .navbar {
            background: #0b1220;
            border-bottom: 1px solid #334155;
        }

        .marca {
            color: #38bdf8 !important;
            font-weight: 800;
        }

        .panel-principal {
            background: #1e293b;
            border: 1px solid #334155;
            border-radius: 18px;
            padding: 40px;
            max-width: 850px;
            margin: 60px auto;
            box-shadow: 0 15px 40px rgba(0, 0, 0, 0.3);
        }

        .titulo {
            color: #38bdf8;
            font-weight: 800;
        }

        .texto {
            color: #cbd5e1;
        }

        .tarjeta-usuario {
            background: #0f172a;
            border: 1px solid #334155;
            border-radius: 12px;
            padding: 25px;
        }

        .identificador {
            color: #4ade80;
            font-size: 1.8rem;
            font-weight: 800;
        }

        .btn-cerrar {
            background: #dc2626;
            color: white;
            border: none;
            padding: 12px 25px;
        }

        .btn-cerrar:hover {
            background: #b91c1c;
            color: white;
        }
    </style>
</head>

<body>

    <!-- Barra de navegación principal -->
    <nav class="navbar navbar-expand-lg navbar-dark">

        <div class="container">

            <a class="navbar-brand marca"
               href="${pageContext.request.contextPath}/">
                El Hacker de las Maquinitas
            </a>

            <span class="navbar-text text-secondary">
                Panel de usuario
            </span>

        </div>

    </nav>

    <main class="container">

        <section class="panel-principal">

            <!-- Encabezado del panel -->
            <div class="text-center mb-4">

                <h1 class="titulo h2">
                    Bienvenido a tu panel de usuario
                </h1>

                <p class="texto mt-3">
                    Has iniciado sesión correctamente.
                </p>

            </div>

            <!-- Información de la sesión del usuario -->
            <div class="tarjeta-usuario text-center mb-4">

                <p class="texto mb-2">
                    Tu identificador de usuario es:
                </p>

                <div class="identificador">
                    <%= session.getAttribute("usuarioId") %>
                </div>

            </div>

            <!-- Información general de la plataforma -->
            <div class="text-center mb-4">

                <h2 class="h5 mb-3">
                    Servicios de la plataforma
                </h2>

                <p class="texto">
                    Desde aquí podrás acceder a los servicios de la
                    plataforma de venta y gestión de licencias y créditos
                    para herramientas de reparación de dispositivos móviles.
                </p>

                <span class="badge text-bg-success">
                    Sesión activa
                </span>

            </div>

            <!-- Cierre de sesión mediante petición POST -->
            <div class="text-center">

                <form action="${pageContext.request.contextPath}/logout"
                      method="POST">

                    <button type="submit" class="btn btn-cerrar">
                        Cerrar sesión
                    </button>

                </form>

            </div>

        </section>

    </main>

    <!-- Bootstrap JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
