<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro - El Hacker de las Maquinitas</title>

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

        h1 {
            text-align: center;
            font-size: 23px;
        }

        input {
            width: 100%;
            padding: 11px;
            margin: 9px 0;
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
    </style>
</head>

<body>

<div class="contenedor">

    <h1>El Hacker de las Maquinitas</h1>
    <h2>Crear cuenta</h2>

    <form action="${pageContext.request.contextPath}/registro"
          method="POST">

        <input type="text"
               name="nombre"
               placeholder="Nombre completo"
               maxlength="100"
               required>

        <input type="email"
               name="correo"
               placeholder="Correo electrónico"
               maxlength="150"
               required>

        <input type="tel"
               name="telefono"
               placeholder="Teléfono"
               maxlength="20">

        <input type="password"
               name="clave"
               placeholder="Contraseña"
               required>

        <button type="submit">Registrarme</button>

    </form>

    <% if (request.getAttribute("mensaje") != null) { %>
        <div class="mensaje">
            <%= request.getAttribute("mensaje") %>
        </div>
    <% } %>

</div>

</body>
</html>
