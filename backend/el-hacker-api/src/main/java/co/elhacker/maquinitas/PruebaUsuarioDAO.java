package co.elhacker.maquinitas;

import java.util.List;

import co.elhacker.maquinitas.dao.UsuarioDAO;
import co.elhacker.maquinitas.modelo.Usuario;
import co.elhacker.maquinitas.seguridad.Seguridad;

public class PruebaUsuarioDAO {

    public static void main(String[] args) {

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        // 1. INSERTAR USUARIO DE PRUEBA
        Usuario nuevoUsuario = new Usuario();

        nuevoUsuario.setNombre("Usuario Temporal CRUD");
        nuevoUsuario.setCorreo("pruebacrud01@correo.com");
        nuevoUsuario.setTelefono("3000000000");

        nuevoUsuario.setClaveHash(
            Seguridad.generarHash("Temporal12345")
        );

        nuevoUsuario.setRol("Cliente");

        boolean insertado = usuarioDAO.registrar(nuevoUsuario);

        if (!insertado) {
            System.out.println(
                "ERROR: No se pudo insertar el usuario."
            );
            return;
        }

        System.out.println("1. INSERTAR: CORRECTO");

        // 2. CONSULTAR USUARIOS
        System.out.println("\n2. LISTAR USUARIOS:");

        List<Usuario> usuarios = usuarioDAO.listar();

        for (Usuario usuario : usuarios) {

            System.out.println(
                usuario.getIdUsuario() + " - " +
                usuario.getNombre() + " - " +
                usuario.getCorreo()
            );
        }

        // 3. BUSCAR USUARIO POR CORREO
        Usuario usuarioEncontrado =
            usuarioDAO.buscarPorCorreo("pruebacrud01@correo.com");

        if (usuarioEncontrado == null) {
            System.out.println("ERROR: Usuario no encontrado.");
            return;
        }

        int idUsuario = usuarioEncontrado.getIdUsuario();

        System.out.println("\n3. BUSCAR POR CORREO: CORRECTO");

        // 4. ACTUALIZAR USUARIO
        usuarioEncontrado.setNombre("Usuario CRUD Actualizado");
        usuarioEncontrado.setTelefono("3111111111");

        boolean actualizado =
            usuarioDAO.actualizar(usuarioEncontrado);

        System.out.println(
            "\n4. ACTUALIZAR: " +
            (actualizado ? "CORRECTO" : "ERROR")
        );

        // 5. ELIMINAR USUARIO TEMPORAL
        boolean eliminado = usuarioDAO.eliminar(idUsuario);

        System.out.println(
            "\n5. ELIMINAR: " +
            (eliminado ? "CORRECTO" : "ERROR")
        );

        // 6. VERIFICAR ELIMINACIÓN
        Usuario verificacion =
            usuarioDAO.buscarPorCorreo("pruebacrud01@correo.com");

        System.out.println(
            "\n6. VERIFICAR ELIMINACIÓN: " +
            (verificacion == null ? "CORRECTO" : "ERROR")
        );
    }
}
