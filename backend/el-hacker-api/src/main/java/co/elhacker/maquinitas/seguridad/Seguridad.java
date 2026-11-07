package co.elhacker.maquinitas.seguridad;

import org.mindrot.jbcrypt.BCrypt;

public class Seguridad {

    // Convertir contraseña en hash seguro
    public static String generarHash(String clave) {

        return BCrypt.hashpw(clave, BCrypt.gensalt(12));
    }

    // Verificar contraseña
    public static boolean verificarClave(
            String clave,
            String hash) {

        if (clave == null || hash == null) {
            return false;
        }

        return BCrypt.checkpw(clave, hash);
    }
}
