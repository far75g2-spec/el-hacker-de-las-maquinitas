package co.elhacker.maquinitas.dao;

import co.elhacker.maquinitas.conexion.Conexion;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase de acceso a datos de usuarios.
 * Gestiona el registro y la autenticación mediante MySQL.
 */
public class UsuarioDAO {

    /**
     * Registra un usuario nuevo en la base de datos.
     *
     * @param nombre nombre del usuario.
     * @param correo correo electrónico del usuario.
     * @param telefono número de contacto.
     * @param clave contraseña ingresada por el usuario.
     * @return true si el registro fue exitoso.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public boolean registrar(
            String nombre,
            String correo,
            String telefono,
            String clave) throws SQLException {

        // Consulta SQL para insertar los datos del nuevo usuario.
        String sql = """
                INSERT INTO usuarios
                (nombre, correo, telefono, clave_hash, rol, estado)
                VALUES (?, ?, ?, ?, 'Cliente', 'Activo')
                """;

        // Genera un hash seguro de la contraseña usando BCrypt.
        // El factor de trabajo 12 aumenta la dificultad del cálculo.
        String claveHash = BCrypt.hashpw(
                clave, BCrypt.gensalt(12)
        );

        // Abre la conexión y prepara la consulta SQL.
        // try-with-resources cierra automáticamente los recursos.
        try (Connection conexion = Conexion.obtenerConexion();
             PreparedStatement consulta =
                     conexion.prepareStatement(sql)) {

            // Asigna los valores a los parámetros de la consulta.
            consulta.setString(1, nombre);
            consulta.setString(2, correo);
            consulta.setString(3, telefono);
            consulta.setString(4, claveHash);

            // Ejecuta la inserción y verifica si se creó el registro.
            return consulta.executeUpdate() > 0;
        }
    }

    /**
     * Verifica las credenciales de acceso de un usuario.
     *
     * @param correo correo electrónico registrado.
     * @param clave contraseña ingresada.
     * @return identificador del usuario si la autenticación es correcta;
     *         retorna 0 si las credenciales no son válidas.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public int autenticar(String correo, String clave)
            throws SQLException {

        // Busca el usuario por su correo electrónico.
        String sql = """
                SELECT id_usuario, clave_hash, estado
                FROM usuarios
                WHERE correo = ?
                """;

        // Establece la conexión y prepara la consulta parametrizada.
        try (Connection conexion = Conexion.obtenerConexion();
             PreparedStatement consulta =
                     conexion.prepareStatement(sql)) {

            // Asigna el correo al parámetro de búsqueda.
            consulta.setString(1, correo);

            // Ejecuta la consulta y procesa el resultado.
            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {

                    // Recupera el hash de la contraseña y el estado de la cuenta.
                    String hash = resultado.getString("clave_hash");
                    String estado = resultado.getString("estado");

                    // Comprueba que la cuenta esté activa
                    // y que la contraseña coincida con el hash almacenado.
                    if ("Activo".equalsIgnoreCase(estado)
                            && BCrypt.checkpw(clave, hash)) {

                        // Retorna el identificador del usuario autenticado.
                        return resultado.getInt("id_usuario");
                    }
                }
            }
        }

        // Retorna 0 cuando no se encuentran credenciales válidas.
        return 0;
    }
}