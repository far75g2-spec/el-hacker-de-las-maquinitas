<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>El Hacker de las Maquinitas</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background: #0f172a;
            color: white;
            margin: 0;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .contenedor {
            text-align: center;
            background: #1e293b;
            padding: 40px;
            border-radius: 15px;
            max-width: 500px;
            width: 85%;
        }

        h1 {
            color: #38bdf8;
        }

        p {
            color: #cbd5e1;
            line-height: 1.6;
        }

        a {
            display: inline-block;
            margin: 12px;
            padding: 13px 22px;
            background: #0284c7;
            color: white;
            text-decoration: none;
            border-radius: 7px;
        }

        a:hover {
            background: #0369a1;
        }
    </style>

</head>

<body>

    <div class="contenedor">

        <h1>El Hacker de las Maquinitas</h1>

        <h2>Plataforma de servicios técnicos</h2>

        <p>
            Bienvenido a nuestra plataforma de venta y gestión
            de licencias y créditos para herramientas de reparación
            de software de dispositivos móviles.
        </p>

        <a href="${pageContext.request.contextPath}/login">
            Iniciar sesión
        </a>

        <a href="${pageContext.request.contextPath}/registro">
            Crear cuenta
        </a>

    </div>

</body>
</html>