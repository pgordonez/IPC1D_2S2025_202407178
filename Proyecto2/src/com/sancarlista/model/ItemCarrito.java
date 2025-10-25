package com.sancarlista.model;

import java.io.Serializable;

public class ItemCarrito implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String codigoProducto;
    private String nombreProducto;
    private int cantidad;
    private double precioUnitario;
    
    public ItemCarrito(String codigoProducto, String nombreProducto, 
                       int cantidad, double precioUnitario) {
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
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
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    public double getPrecioUnitario() {
        return precioUnitario;
    }
    
    public double getSubtotal() {
        return cantidad * precioUnitario;
    }
}