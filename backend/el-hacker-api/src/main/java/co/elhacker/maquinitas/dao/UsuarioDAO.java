package co.elhacker.maquinitas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.elhacker.maquinitas.conexion.Conexion;
import co.elhacker.maquinitas.modelo.Usuario;

public class UsuarioDAO {

    // =====================================
    // INSERTAR USUARIO
    // =====================================

    public boolean registrar(Usuario usuario) {

        String sql = "INSERT INTO usuarios "
                + "(nombre, correo, telefono, clave_hash, rol) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (
            Connection conexion = Conexion.obtenerConexion();
            PreparedStatement consulta = conexion.prepareStatement(sql)
        ) {

            consulta.setString(1, usuario.getNombre());
            consulta.setString(2, usuario.getCorreo());
            consulta.setString(3, usuario.getTelefono());
            consulta.setString(4, usuario.getClaveHash());
            consulta.setString(5, usuario.getRol());

            return consulta.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println("Error al registrar usuario: "
                    + e.getMessage());

            return false;
        }
    }

    // =====================================
    // CONSULTAR TODOS LOS USUARIOS
    // =====================================

    public List<Usuario> listar() {

        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT id_usuario, nombre, correo, telefono, "
                + "clave_hash, rol, estado "
                + "FROM usuarios ORDER BY id_usuario";

        try (
            Connection conexion = Conexion.obtenerConexion();
            PreparedStatement consulta = conexion.prepareStatement(sql);
            ResultSet resultado = consulta.executeQuery()
        ) {

            while (resultado.next()) {

                Usuario usuario = new Usuario();

                usuario.setIdUsuario(resultado.getInt("id_usuario"));
                usuario.setNombre(resultado.getString("nombre"));
                usuario.setCorreo(resultado.getString("correo"));
                usuario.setTelefono(resultado.getString("telefono"));
                usuario.setClaveHash(resultado.getString("clave_hash"));
                usuario.setRol(resultado.getString("rol"));
                usuario.setEstado(resultado.getString("estado"));

                usuarios.add(usuario);
            }

        } catch (SQLException e) {

            System.out.println("Error al consultar usuarios: "
                    + e.getMessage());
        }

        return usuarios;
    }

    // =====================================
    // BUSCAR USUARIO POR CORREO
    // =====================================

    public Usuario buscarPorCorreo(String correo) {

        String sql = "SELECT id_usuario, nombre, correo, telefono, "
                + "clave_hash, rol, estado "
                + "FROM usuarios WHERE correo = ?";

        try (
            Connection conexion = Conexion.obtenerConexion();
            PreparedStatement consulta = conexion.prepareStatement(sql)
        ) {

            consulta.setString(1, correo);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {

                    Usuario usuario = new Usuario();

                    usuario.setIdUsuario(resultado.getInt("id_usuario"));
                    usuario.setNombre(resultado.getString("nombre"));
                    usuario.setCorreo(resultado.getString("correo"));
                    usuario.setTelefono(resultado.getString("telefono"));
                    usuario.setClaveHash(resultado.getString("clave_hash"));
                    usuario.setRol(resultado.getString("rol"));
                    usuario.setEstado(resultado.getString("estado"));

                    return usuario;
                }
            }

        } catch (SQLException e) {

            System.out.println("Error al buscar usuario: "
                    + e.getMessage());
        }

        return null;
    }

    // =====================================
    // ACTUALIZAR USUARIO
    // =====================================

    public boolean actualizar(Usuario usuario) {

        String sql = "UPDATE usuarios SET nombre = ?, correo = ?, "
                + "telefono = ?, rol = ?, estado = ? "
                + "WHERE id_usuario = ?";

        try (
            Connection conexion = Conexion.obtenerConexion();
            PreparedStatement consulta = conexion.prepareStatement(sql)
        ) {

            consulta.setString(1, usuario.getNombre());
            consulta.setString(2, usuario.getCorreo());
            consulta.setString(3, usuario.getTelefono());
            consulta.setString(4, usuario.getRol());
            consulta.setString(5, usuario.getEstado());
            consulta.setInt(6, usuario.getIdUsuario());

            return consulta.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println("Error al actualizar usuario: "
                    + e.getMessage());

            return false;
        }
    }

    // =====================================
    // ELIMINAR USUARIO
    // =====================================

    public boolean eliminar(int idUsuario) {

        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

        try (
            Connection conexion = Conexion.obtenerConexion();
            PreparedStatement consulta = conexion.prepareStatement(sql)
        ) {

            consulta.setInt(1, idUsuario);

            return consulta.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println("Error al eliminar usuario: "
                    + e.getMessage());

            return false;
        }
    }
}
