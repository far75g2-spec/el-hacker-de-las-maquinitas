<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
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

    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f1f5f9;
            margin: 0;
            padding: 40px;
        }

        .contenedor {
            max-width: 700px;
            margin: auto;
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }

        h1 {
            color: #172554;
        }

        p {
            color: #475569;
        }

        button {
            background: #dc2626;
            color: white;
            border: none;
            padding: 12px 20px;
            border-radius: 6px;
            cursor: pointer;
        }
    </style>
</head>

<body>

<div class="contenedor">

    <h1>El Hacker de las Maquinitas</h1>

    <h2>Bienvenido a tu panel de usuario</h2>

    <p>Has iniciado sesión correctamente.</p>

    <p>
        Tu identificador de usuario es:
        <strong><%= session.getAttribute("usuarioId") %></strong>
    </p>

    <p>Desde aquí podrás acceder a los servicios de la plataforma.</p>

    <form action="${pageContext.request.contextPath}/logout" method="post">
        <button type="submit">Cerrar sesión</button>
    </form>

</div>

</body>
</html>