package co.elhacker.maquinitas;

import co.elhacker.maquinitas.dao.UsuarioDAO;
import co.elhacker.maquinitas.modelo.Usuario;
import co.elhacker.maquinitas.seguridad.Seguridad;

public class PruebaRegistro {

    public static void main(String[] args) {

        Usuario usuario = new Usuario();

        usuario.setNombre("Usuario de prueba");
        usuario.setCorreo("prueba01@correo.com");
        usuario.setTelefono("3001234567");

        String clave = "Prueba12345";

        String hash = Seguridad.generarHash(clave);

        usuario.setClaveHash(hash);
        usuario.setRol("Cliente");

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        boolean registrado = usuarioDAO.registrar(usuario);

        if (registrado) {
            System.out.println("USUARIO REGISTRADO CORRECTAMENTE");
        } else {
            System.out.println("NO SE PUDO REGISTRAR EL USUARIO");
        }
    }
}