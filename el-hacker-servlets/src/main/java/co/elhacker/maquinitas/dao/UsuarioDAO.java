package co.elhacker.maquinitas.dao;

import co.elhacker.maquinitas.conexion.Conexion;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public boolean registrar(
            String nombre,
            String correo,
            String telefono,
            String clave) throws SQLException {

        String sql = """
                INSERT INTO usuarios
                (nombre, correo, telefono, clave_hash, rol, estado)
                VALUES (?, ?, ?, ?, 'Cliente', 'Activo')
                """;

        String claveHash = BCrypt.hashpw(
                clave, BCrypt.gensalt(12)
        );

        try (Connection conexion = Conexion.obtenerConexion();
             PreparedStatement consulta =
                     conexion.prepareStatement(sql)) {

            consulta.setString(1, nombre);
            consulta.setString(2, correo);
            consulta.setString(3, telefono);
            consulta.setString(4, claveHash);

            return consulta.executeUpdate() > 0;
        }
    }

    public int autenticar(String correo, String clave)
            throws SQLException {

        String sql = """
                SELECT id_usuario, clave_hash, estado
                FROM usuarios
                WHERE correo = ?
                """;

        try (Connection conexion = Conexion.obtenerConexion();
             PreparedStatement consulta =
                     conexion.prepareStatement(sql)) {

            consulta.setString(1, correo);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {

                    String hash = resultado.getString("clave_hash");
                    String estado = resultado.getString("estado");

                    if ("Activo".equalsIgnoreCase(estado)
                            && BCrypt.checkpw(clave, hash)) {

                        return resultado.getInt("id_usuario");
                    }
                }
            }
        }

        return 0;
    }
}
