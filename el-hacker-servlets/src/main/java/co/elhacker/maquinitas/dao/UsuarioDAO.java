package co.elhacker.maquinitas.dao;

import co.elhacker.maquinitas.conexion.Conexion;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Acceso a datos y administración de usuarios.
 */
public class UsuarioDAO {

    /**
     * Registra un nuevo usuario en la base de datos.
     * Controla los correos electrónicos duplicados.
     */
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
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, nombre);
            consulta.setString(2, correo);
            consulta.setString(3, telefono);
            consulta.setString(4, claveHash);

            return consulta.executeUpdate() > 0;

        } catch (SQLException e) {

            // Identifica el correo duplicado en MySQL.
            if ("23000".equals(e.getSQLState())
                    && e.getErrorCode() == 1062) {
                return false;
            }

            // Los demás errores SQL se mantienen para su diagnóstico.
            throw e;
        }
    }

    /**
     * Autentica un usuario mediante su correo y contraseña.
     */
    public int autenticar(String correo, String clave)
            throws SQLException {

        String sql = """
                SELECT id_usuario, clave_hash, estado
                FROM usuarios
                WHERE correo = ?
                """;

        try (Connection conexion = Conexion.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

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

    /**
     * Obtiene el listado de usuarios sin exponer sus contraseñas.
     */
    public List<Map<String, Object>> listarUsuarios()
            throws SQLException {

        List<Map<String, Object>> usuarios = new ArrayList<>();

        String sql = """
                SELECT u.id_usuario, u.nombre, u.correo, u.telefono,
                       u.rol, u.estado,
                       COALESCE(b.saldo_usdt, 0.00000000) AS saldo_usdt
                FROM usuarios u
                LEFT JOIN billeteras b ON b.id_usuario = u.id_usuario
                ORDER BY u.id_usuario DESC
                """;

        try (Connection conexion = Conexion.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {

                Map<String, Object> usuario = new LinkedHashMap<>();

                usuario.put("id_usuario",
                        resultado.getInt("id_usuario"));

                usuario.put("nombre",
                        resultado.getString("nombre"));

                usuario.put("correo",
                        resultado.getString("correo"));

                usuario.put("telefono",
                        resultado.getString("telefono"));

                usuario.put("rol",
                        resultado.getString("rol"));

                usuario.put("estado",
                        resultado.getString("estado"));

                // El saldo se consulta desde la billetera asociada.
                usuario.put("saldo_usdt",
                        resultado.getBigDecimal("saldo_usdt"));

                usuarios.add(usuario);
            }
        }

        return usuarios;
    }

    /**
     * Cambia el estado de un usuario.
     */
    public boolean cambiarEstado(int idUsuario, String estado)
            throws SQLException {

        if (!"Activo".equalsIgnoreCase(estado)
                && !"Inactivo".equalsIgnoreCase(estado)) {

            throw new IllegalArgumentException("Estado no válido.");
        }

        String sql = """
                UPDATE usuarios
                SET estado = ?
                WHERE id_usuario = ?
                """;

        try (Connection conexion = Conexion.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, estado);
            consulta.setInt(2, idUsuario);

            return consulta.executeUpdate() > 0;
        }
    }

    /**
     * Consulta el rol de un usuario autenticado.
     */
    public String obtenerRol(int idUsuario) throws SQLException {

        String sql = """
                SELECT rol
                FROM usuarios
                WHERE id_usuario = ?
                """;

        try (Connection conexion = Conexion.obtenerConexion();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idUsuario);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {
                    return resultado.getString("rol");
                }
            }
        }

        return null;
    }
}
