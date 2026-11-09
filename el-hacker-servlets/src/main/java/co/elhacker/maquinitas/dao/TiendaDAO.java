package co.elhacker.maquinitas.dao;

import co.elhacker.maquinitas.conexion.Conexion;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.*;

/** Operaciones transaccionales de pedidos, billeteras y recargas USDT. */
public class TiendaDAO {
 private static BigDecimal dinero(BigDecimal v) { return v.setScale(8, RoundingMode.HALF_UP); }
 public Map<String,Object> billetera(int usuario) throws SQLException {
  Map<String,Object> r=new LinkedHashMap<>();
  try(Connection c=Conexion.obtenerConexion(); PreparedStatement p=c.prepareStatement("SELECT saldo_usdt FROM billeteras WHERE id_usuario=?")) {
   p.setInt(1,usuario); try(ResultSet rs=p.executeQuery()){ if(rs.next()) r.put("saldo_usdt",rs.getBigDecimal(1)); else r.put("saldo_usdt",BigDecimal.ZERO.setScale(8)); }
  }
  List<Map<String,Object>> mov=new ArrayList<>();
  try(Connection c=Conexion.obtenerConexion(); PreparedStatement p=c.prepareStatement("SELECT id_movimiento,tipo,monto_usdt,saldo_resultante,referencia,descripcion,fecha FROM movimientos_saldo WHERE id_usuario=? ORDER BY id_movimiento DESC LIMIT 30")) {
   p.setInt(1,usuario); try(ResultSet rs=p.executeQuery()){while(rs.next()){Map<String,Object>x=new LinkedHashMap<>();x.put("id_movimiento",rs.getLong(1));x.put("tipo",rs.getString(2));x.put("monto_usdt",rs.getBigDecimal(3));x.put("saldo_resultante",rs.getBigDecimal(4));x.put("referencia",rs.getString(5));x.put("descripcion",rs.getString(6));x.put("fecha",rs.getTimestamp(7));mov.add(x);}}
  }
  r.put("movimientos",mov); return r;
 }
 public long crearPedido(int usuario, List<Map<String,Object>> items) throws Exception {
  if(items==null||items.isEmpty()||items.size()>50) throw new IllegalArgumentException("El carrito está vacío o contiene demasiados artículos.");
  try(Connection c=Conexion.obtenerConexion()) {
   c.setAutoCommit(false);
   try {
    BigDecimal total=BigDecimal.ZERO; List<Map<String,Object>> lineas=new ArrayList<>();
    for(Map<String,Object> it:items){int id=Integer.parseInt(String.valueOf(it.get("id_producto")));int qty=Integer.parseInt(String.valueOf(it.get("cantidad")));if(qty<1||qty>100)throw new IllegalArgumentException("Cantidad no válida.");
     try(PreparedStatement p=c.prepareStatement("SELECT nombre,precio_usdt FROM productos WHERE id_producto=? AND estado='Activo' FOR UPDATE")){p.setInt(1,id);try(ResultSet rs=p.executeQuery()){if(!rs.next())throw new IllegalArgumentException("Un producto del carrito ya no está disponible.");BigDecimal price=rs.getBigDecimal("precio_usdt");if(price==null||price.signum()<=0)throw new IllegalArgumentException("El producto no tiene un precio USDT válido.");BigDecimal sub=dinero(price.multiply(BigDecimal.valueOf(qty)));total=total.add(sub);Map<String,Object> l=new LinkedHashMap<>();l.put("id",id);l.put("nombre",rs.getString("nombre"));l.put("precio",price);l.put("cantidad",qty);l.put("subtotal",sub);lineas.add(l);}}
    }
    total=dinero(total); BigDecimal saldo;
    try(PreparedStatement p=c.prepareStatement("SELECT saldo_usdt FROM billeteras WHERE id_usuario=? FOR UPDATE")){p.setInt(1,usuario);try(ResultSet rs=p.executeQuery()){if(!rs.next())throw new IllegalStateException("No existe la billetera del usuario. Ejecuta la migración SQL.");saldo=rs.getBigDecimal(1);}}
    if(saldo.compareTo(total)<0)throw new IllegalArgumentException("Saldo insuficiente. Recarga tu billetera antes de comprar.");
    long pedido;
    try(PreparedStatement p=c.prepareStatement("INSERT INTO pedidos(id_usuario,total_usdt,estado) VALUES(?,?,'Pendiente de procesamiento')",Statement.RETURN_GENERATED_KEYS)){p.setInt(1,usuario);p.setBigDecimal(2,total);p.executeUpdate();try(ResultSet rs=p.getGeneratedKeys()){rs.next();pedido=rs.getLong(1);}}
    for(Map<String,Object> l:lineas)try(PreparedStatement p=c.prepareStatement("INSERT INTO pedido_detalles(id_pedido,id_producto,nombre_producto,precio_unitario_usdt,cantidad,subtotal_usdt) VALUES(?,?,?,?,?,?)")){p.setLong(1,pedido);p.setInt(2,(Integer)l.get("id"));p.setString(3,(String)l.get("nombre"));p.setBigDecimal(4,(BigDecimal)l.get("precio"));p.setInt(5,(Integer)l.get("cantidad"));p.setBigDecimal(6,(BigDecimal)l.get("subtotal"));p.executeUpdate();}
    BigDecimal nuevo=dinero(saldo.subtract(total));
    try(PreparedStatement p=c.prepareStatement("UPDATE billeteras SET saldo_usdt=? WHERE id_usuario=?")){p.setBigDecimal(1,nuevo);p.setInt(2,usuario);p.executeUpdate();}
    try(PreparedStatement p=c.prepareStatement("INSERT INTO movimientos_saldo(id_usuario,tipo,monto_usdt,saldo_resultante,referencia,descripcion) VALUES(?,'Compra',?,?,?,?)")){p.setInt(1,usuario);p.setBigDecimal(2,total.negate());p.setBigDecimal(3,nuevo);p.setString(4,"PED-"+pedido);p.setString(5,"Pago del pedido #"+pedido);p.executeUpdate();}
    c.commit();return pedido;
   }catch(Exception e){c.rollback();throw e;}finally{c.setAutoCommit(true);}
  }
 }
 public List<Map<String,Object>> pedidosUsuario(int usuario) throws SQLException {return listarPedidos("WHERE p.id_usuario=?",usuario);}
 public List<Map<String,Object>> todosPedidos() throws SQLException {return listarPedidos("",0);}
 private List<Map<String,Object>> listarPedidos(String filtro,int usuario) throws SQLException {
  List<Map<String,Object>> out=new ArrayList<>();String sql="SELECT p.id_pedido,p.id_usuario,u.nombre,u.correo,p.total_usdt,p.estado,p.fecha FROM pedidos p JOIN usuarios u ON u.id_usuario=p.id_usuario "+filtro+" ORDER BY p.id_pedido DESC";
  try(Connection c=Conexion.obtenerConexion();PreparedStatement p=c.prepareStatement(sql)){if(usuario>0)p.setInt(1,usuario);try(ResultSet rs=p.executeQuery()){while(rs.next()){Map<String,Object>x=new LinkedHashMap<>();long id=rs.getLong("id_pedido");x.put("id_pedido",id);x.put("id_usuario",rs.getInt("id_usuario"));x.put("nombre",rs.getString("nombre"));x.put("correo",rs.getString("correo"));x.put("total_usdt",rs.getBigDecimal("total_usdt"));x.put("estado",rs.getString("estado"));x.put("fecha",rs.getTimestamp("fecha"));List<Map<String,Object>> det=new ArrayList<>();try(PreparedStatement q=c.prepareStatement("SELECT nombre_producto,precio_unitario_usdt,cantidad,subtotal_usdt FROM pedido_detalles WHERE id_pedido=?")){q.setLong(1,id);try(ResultSet d=q.executeQuery()){while(d.next()){Map<String,Object>z=new LinkedHashMap<>();z.put("nombre_producto",d.getString(1));z.put("precio_unitario_usdt",d.getBigDecimal(2));z.put("cantidad",d.getInt(3));z.put("subtotal_usdt",d.getBigDecimal(4));det.add(z);}}}x.put("detalles",det);out.add(x);}}}return out;
 }
 /**
  * Cambia el estado de un pedido. Si se cancela un pedido pagado,
  * reintegra el total a la billetera y registra el movimiento en la
  * misma transacción. El bloqueo FOR UPDATE impide dos devoluciones
  * simultáneas del mismo pedido.
  */
 public boolean cambiarEstadoPedido(long id, String estado) throws SQLException {
  Set<String> estadosPermitidos = Set.of(
      "Pendiente de procesamiento", "En proceso", "Completado", "Cancelado");
  if (id <= 0 || estado == null || !estadosPermitidos.contains(estado)) {
   throw new IllegalArgumentException("Identificador o estado del pedido no válido.");
  }

  try (Connection c = Conexion.obtenerConexion()) {
   c.setAutoCommit(false);
   try {
    int usuario;
    BigDecimal total;
    String estadoActual;

    try (PreparedStatement p = c.prepareStatement(
        "SELECT id_usuario, total_usdt, estado FROM pedidos WHERE id_pedido=? FOR UPDATE")) {
     p.setLong(1, id);
     try (ResultSet rs = p.executeQuery()) {
      if (!rs.next()) {
       c.rollback();
       return false;
      }
      usuario = rs.getInt("id_usuario");
      total = rs.getBigDecimal("total_usdt");
      estadoActual = rs.getString("estado");
     }
    }

    if ("Cancelado".equalsIgnoreCase(estadoActual)) {
     throw new IllegalArgumentException("El pedido ya está cancelado; no se puede devolver el saldo otra vez.");
    }
    if ("Completado".equalsIgnoreCase(estadoActual) && "Cancelado".equals(estado)) {
     throw new IllegalArgumentException("No se puede cancelar un pedido que ya fue completado.");
    }

    if ("Cancelado".equals(estado)) {
     if (total == null || total.signum() <= 0) {
      throw new IllegalStateException("El pedido no tiene un total válido para devolver.");
     }

     BigDecimal saldo;
     try (PreparedStatement p = c.prepareStatement(
         "SELECT saldo_usdt FROM billeteras WHERE id_usuario=? FOR UPDATE")) {
      p.setInt(1, usuario);
      try (ResultSet rs = p.executeQuery()) {
       if (!rs.next()) {
        throw new IllegalStateException("No existe la billetera del cliente; no se pudo realizar la devolución.");
       }
       saldo = rs.getBigDecimal("saldo_usdt");
      }
     }

     BigDecimal nuevoSaldo = dinero(saldo.add(total));
     try (PreparedStatement p = c.prepareStatement(
         "UPDATE billeteras SET saldo_usdt=? WHERE id_usuario=?")) {
      p.setBigDecimal(1, nuevoSaldo);
      p.setInt(2, usuario);
      p.executeUpdate();
     }

     try (PreparedStatement p = c.prepareStatement(
         "INSERT INTO movimientos_saldo " +
         "(id_usuario,tipo,monto_usdt,saldo_resultante,referencia,descripcion) " +
         "VALUES(?,'Devolucion',?,?,?,?)")) {
      p.setInt(1, usuario);
      p.setBigDecimal(2, dinero(total));
      p.setBigDecimal(3, nuevoSaldo);
      p.setString(4, "DEV-PED-" + id);
      p.setString(5, "Devolución del saldo por cancelación del pedido #" + id);
      p.executeUpdate();
     }
    }

    try (PreparedStatement p = c.prepareStatement(
        "UPDATE pedidos SET estado=? WHERE id_pedido=?")) {
     p.setString(1, estado);
     p.setLong(2, id);
     p.executeUpdate();
    }

    c.commit();
    return true;
   } catch (Exception e) {
    c.rollback();
    if (e instanceof SQLException sql) throw sql;
    if (e instanceof IllegalArgumentException iae) throw iae;
    if (e instanceof RuntimeException re) throw re;
    throw new SQLException("No se pudo actualizar el pedido ni procesar su devolución.", e);
   } finally {
    c.setAutoCommit(true);
   }
  }
 }
 public long crearRecarga(int usuario,BigDecimal monto,String tx,String archivo) throws SQLException {
  if(monto==null||monto.compareTo(new BigDecimal("1.00000000"))<0||monto.scale()>8)throw new IllegalArgumentException("La recarga mínima es 1 USDT y admite hasta 8 decimales.");
  try(Connection c=Conexion.obtenerConexion();PreparedStatement p=c.prepareStatement("INSERT INTO recargas(id_usuario,monto_usdt,red,tx_hash,archivo_comprobante,estado) VALUES(?,?,'TRC20',?,?,'Pendiente')",Statement.RETURN_GENERATED_KEYS)){p.setInt(1,usuario);p.setBigDecimal(2,dinero(monto));p.setString(3,tx);p.setString(4,archivo);p.executeUpdate();try(ResultSet rs=p.getGeneratedKeys()){rs.next();return rs.getLong(1);}}
 }
 public List<Map<String,Object>> recargasUsuario(int usuario) throws SQLException{return listarRecargas("WHERE r.id_usuario=?",usuario);}
 public List<Map<String,Object>> todasRecargas() throws SQLException{return listarRecargas("",0);}
 private List<Map<String,Object>> listarRecargas(String filtro,int usuario)throws SQLException{List<Map<String,Object>>out=new ArrayList<>();String sql="SELECT r.id_recarga,r.id_usuario,u.nombre,u.correo,r.monto_usdt,r.red,r.tx_hash,r.archivo_comprobante,r.estado,r.notas_admin,r.fecha FROM recargas r JOIN usuarios u ON u.id_usuario=r.id_usuario "+filtro+" ORDER BY r.id_recarga DESC";try(Connection c=Conexion.obtenerConexion();PreparedStatement p=c.prepareStatement(sql)){if(usuario>0)p.setInt(1,usuario);try(ResultSet rs=p.executeQuery()){while(rs.next()){Map<String,Object>x=new LinkedHashMap<>();x.put("id_recarga",rs.getLong(1));x.put("id_usuario",rs.getInt(2));x.put("nombre",rs.getString(3));x.put("correo",rs.getString(4));x.put("monto_usdt",rs.getBigDecimal(5));x.put("red",rs.getString(6));x.put("tx_hash",rs.getString(7));x.put("archivo_comprobante",rs.getString(8));x.put("estado",rs.getString(9));x.put("notas_admin",rs.getString(10));x.put("fecha",rs.getTimestamp(11));out.add(x);}}}return out;}
 public void resolverRecarga(long id,int admin,String estado,String notas)throws Exception{if(!Set.of("Aprobada","Rechazada").contains(estado))throw new IllegalArgumentException("Decisión no válida.");try(Connection c=Conexion.obtenerConexion()){c.setAutoCommit(false);try{long usuario;BigDecimal monto;String actual;try(PreparedStatement p=c.prepareStatement("SELECT id_usuario,monto_usdt,estado FROM recargas WHERE id_recarga=? FOR UPDATE")){p.setLong(1,id);try(ResultSet rs=p.executeQuery()){if(!rs.next())throw new IllegalArgumentException("Recarga no encontrada.");usuario=rs.getLong(1);monto=rs.getBigDecimal(2);actual=rs.getString(3);}}if(!"Pendiente".equals(actual))throw new IllegalArgumentException("Esta recarga ya fue revisada.");try(PreparedStatement p=c.prepareStatement("UPDATE recargas SET estado=?,notas_admin=?,id_admin=?,fecha_revision=NOW() WHERE id_recarga=?")){p.setString(1,estado);p.setString(2,notas);p.setInt(3,admin);p.setLong(4,id);p.executeUpdate();}if("Aprobada".equals(estado)){BigDecimal saldo;try(PreparedStatement p=c.prepareStatement("SELECT saldo_usdt FROM billeteras WHERE id_usuario=? FOR UPDATE")){p.setLong(1,usuario);try(ResultSet rs=p.executeQuery()){if(!rs.next())throw new IllegalStateException("Billetera no encontrada.");saldo=rs.getBigDecimal(1);}}BigDecimal nuevo=dinero(saldo.add(monto));try(PreparedStatement p=c.prepareStatement("UPDATE billeteras SET saldo_usdt=? WHERE id_usuario=?")){p.setBigDecimal(1,nuevo);p.setLong(2,usuario);p.executeUpdate();}try(PreparedStatement p=c.prepareStatement("INSERT INTO movimientos_saldo(id_usuario,tipo,monto_usdt,saldo_resultante,referencia,descripcion) VALUES(?,'Recarga',?,?,?,?)")){p.setLong(1,usuario);p.setBigDecimal(2,monto);p.setBigDecimal(3,nuevo);p.setString(4,"REC-"+id);p.setString(5,"Recarga USDT aprobada #"+id);p.executeUpdate();}}c.commit();}catch(Exception e){c.rollback();throw e;}finally{c.setAutoCommit(true);}}}
}
