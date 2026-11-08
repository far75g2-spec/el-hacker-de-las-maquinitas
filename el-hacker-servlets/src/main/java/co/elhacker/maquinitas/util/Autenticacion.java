package co.elhacker.maquinitas.util;
import jakarta.servlet.http.*;
public final class Autenticacion {
 private Autenticacion(){}
 public static Integer usuario(HttpServletRequest r){HttpSession s=r.getSession(false);Object id=s==null?null:s.getAttribute("usuarioId");return id instanceof Integer?(Integer)id:null;}
 public static boolean admin(HttpServletRequest r){HttpSession s=r.getSession(false);return s!=null&&"Administrador".equalsIgnoreCase(String.valueOf(s.getAttribute("rol")));}
}
