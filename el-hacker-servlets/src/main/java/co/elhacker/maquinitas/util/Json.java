package co.elhacker.maquinitas.util;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
public final class Json {
 private static final ObjectMapper MAPPER = new ObjectMapper();
 private Json() {}
 public static void enviar(HttpServletResponse r, int status, Object data) throws IOException {
  r.setStatus(status); r.setContentType("application/json"); r.setCharacterEncoding("UTF-8");
  r.setHeader("Cache-Control", "no-store"); MAPPER.writeValue(r.getOutputStream(), data);
 }
 public static Map leerMapa(jakarta.servlet.http.HttpServletRequest req) throws IOException {
  return MAPPER.readValue(req.getInputStream(), Map.class);
 }
}
