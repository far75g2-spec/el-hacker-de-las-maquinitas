package co.elhacker.maquinitas;

import co.elhacker.maquinitas.seguridad.Seguridad;

public class PruebaSeguridad {

    public static void main(String[] args) {

        String clave = "Prueba12345";

        // Generar hash
        String hash = Seguridad.generarHash(clave);

        System.out.println("PRUEBA DE SEGURIDAD");
        System.out.println("--------------------------");

        System.out.println("Contraseña original: " + clave);
        System.out.println("Hash generado: " + hash);

        // Verificar contraseña correcta
        boolean correcta =
                Seguridad.verificarClave(clave, hash);

        // Verificar contraseña incorrecta
        boolean incorrecta =
                Seguridad.verificarClave("OtraClave123", hash);

        System.out.println("Contraseña correcta: " + correcta);
        System.out.println("Contraseña incorrecta: " + incorrecta);
    }
}
