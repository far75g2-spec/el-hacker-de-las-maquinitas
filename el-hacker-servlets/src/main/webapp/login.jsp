<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar sesión - El Hacker de las Maquinitas</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background: #101827;
            color: white;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
        }

        .contenedor {
            background: #1e293b;
            padding: 30px;
            border-radius: 12px;
            width: 350px;
        }

        h1, h2 {
            text-align: center;
        }

        input {
            width: 100%;
            padding: 11px;
            margin: 10px 0;
            box-sizing: border-box;
            border-radius: 6px;
            border: none;
        }

        button {
            width: 100%;
            padding: 12px;
            background: #16a34a;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            margin-top: 12px;
        }

        .mensaje {
            background: #334155;
            padding: 10px;
            border-radius: 6px;
            margin-top: 15px;
            text-align: center;
        }

        a {
            color: #60a5fa;
        }
    </style>
</head>

<body>

<div class="contenedor">

    <h1>El Hacker de las Maquinitas</h1>
    <h2>Iniciar sesión</h2>

    <form action="${pageContext.request.contextPath}/login"
          method="POST">

        <input type="email"
               name="correo"
               placeholder="Correo electrónico"
               required>

        <input type="password"
               name="clave"
               placeholder="Contraseña"
               required>

        <button type="submit">Ingresar</button>

    </form>

    <% if (request.getAttribute("mensaje") != null) { %>
        <div class="mensaje">
            <%= request.getAttribute("mensaje") %>
        </div>
    <% } %>

    <p>¿No tienes cuenta?
        <a href="${pageContext.request.contextPath}/registro">
            Regístrate aquí
        </a>
    </p>

</div>

</body>
</html>
