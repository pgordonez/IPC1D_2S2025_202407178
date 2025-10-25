package com.sancarlista.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovimientoStock implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String codigoProducto;
    private String nombreProducto;
    private int cantidad;
    private String codigoUsuario;
    private Date fecha;
    
    public MovimientoStock(String codigoProducto, String nombreProducto, 
                          int cantidad, String codigoUsuario, Date fecha) {
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.codigoUsuario = codigoUsuario;
        this.fecha = fecha;
    }
    
    public String getCodigoProducto() {
        return codigoProducto;
    }
    
    public String getNombreProducto() {
        return nombreProducto;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public String getCodigoUsuario() {
        return codigoUsuario;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public String getFechaFormateada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(fecha);
    }
}