package co.elhacker.maquinitas.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Producto {

    private int idProducto;
    private String nombre;
    private String descripcion;
    private String categoria;
    private String tipoProducto;
    private BigDecimal precio;
    private String estado;
    private LocalDateTime fechaCreacion;

    // Constructor vacio
    public Producto() {
    }

    // Constructor para registrar productos
    public Producto(String nombre, String descripcion,
                    String categoria, String tipoProducto,
                    BigDecimal precio) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.tipoProducto = tipoProducto;
        this.precio = precio;
        this.estado = "Activo";
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
