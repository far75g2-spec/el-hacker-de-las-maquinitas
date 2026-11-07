package co.elhacker.maquinitas.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = obtenerVariable(
        "DB_URL",
        "jdbc:mysql://localhost:3306/el_hacker_maquinitas?useSSL=false&serverTimezone=UTC"
    );

    private static final String USUARIO = obtenerVariable(
        "DB_USER",
        "root"
    );

    private static final String CLAVE = System.getenv("DB_PASSWORD");

    private static String obtenerVariable(
            String nombre,
            String valorPredeterminado) {

        String valor = System.getenv(nombre);

        if (valor == null || valor.isBlank()) {
            return valorPredeterminado;
        }

        return valor;
    }

    public static Connection obtenerConexion() throws SQLException {

        if (CLAVE == null || CLAVE.isBlank()) {
            throw new SQLException(
                "No se ha configurado la variable de entorno DB_PASSWORD."
            );
        }

        Connection conexion = DriverManager.getConnection(
            URL,
            USUARIO,
            CLAVE
        );

        System.out.println("Conexion con MySQL exitosa.");

        return conexion;
    }
}
