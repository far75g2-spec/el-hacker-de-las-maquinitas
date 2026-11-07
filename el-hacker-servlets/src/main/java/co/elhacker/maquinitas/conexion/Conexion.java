package co.elhacker.maquinitas.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL =
            System.getenv().getOrDefault(
                    "DB_URL",
                    "jdbc:mysql://localhost:3306/el_hacker_maquinitas?useSSL=false&serverTimezone=UTC"
            );

    private static final String USUARIO =
            System.getenv().getOrDefault("DB_USER", "root");

    private static final String CLAVE =
            System.getenv("DB_PASSWORD");

    public static Connection obtenerConexion() throws SQLException {

        if (CLAVE == null || CLAVE.isBlank()) {
            throw new SQLException(
                    "No se ha configurado la variable DB_PASSWORD."
            );
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "No se pudo cargar el controlador JDBC de MySQL.",
                    e
            );
        }

        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
