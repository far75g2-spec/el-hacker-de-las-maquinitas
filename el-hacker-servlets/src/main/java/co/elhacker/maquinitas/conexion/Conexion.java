package co.elhacker.maquinitas.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de establecer la conexión
 * entre la aplicación web y la base de datos MySQL.
 */
public class Conexion {

    // URL de conexión. Puede configurarse mediante una variable de entorno.
    private static final String URL =
            System.getenv().getOrDefault(
                    "DB_URL",
                    "jdbc:mysql://localhost:3306/el_hacker_maquinitas?useSSL=false&serverTimezone=UTC"
            );

    // Usuario de acceso a MySQL.
    private static final String USUARIO =
            System.getenv().getOrDefault("DB_USER", "root");

    // Contraseña obtenida desde una variable de entorno.
    private static final String CLAVE =
            System.getenv("DB_PASSWORD");

    /**
     * Establece y retorna una conexión activa con MySQL.
     *
     * @return conexión con la base de datos.
     * @throws SQLException si ocurre un error de conexión.
     */
    public static Connection obtenerConexion() throws SQLException {

        // Verifica que la contraseña esté configurada.
        if (CLAVE == null || CLAVE.isBlank()) {
            throw new SQLException(
                    "No se ha configurado la variable DB_PASSWORD."
            );
        }

        // Carga el controlador JDBC de MySQL.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "No se pudo cargar el controlador JDBC de MySQL.",
                    e
            );
        }

        // Retorna la conexión utilizando los datos configurados.
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}