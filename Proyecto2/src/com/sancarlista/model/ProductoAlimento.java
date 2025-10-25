package com.sancarlista.model;

import java.io.Serializable;

/**
 * Producto Alimenticio - Tiene fecha de caducidad
 */
public class ProductoAlimento extends Producto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String fechaCaducidad;
    
    public ProductoAlimento(String codigo, String nombre, double precio, String fechaCaducidad) {
        super(codigo, nombre, "Alimento", precio);
        this.fechaCaducidad = fechaCaducidad;
    }
    
    @Override
    public String getDetalleEspecifico() {
        return "Fecha de Caducidad: " + fechaCaducidad;
    }
    
    @Override
    public void setAtributoEspecifico(String valor) {
        this.fechaCaducidad = valor;
    }
    
    public String getFechaCaducidad() {
        return fechaCaducidad;
    }
    
    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }
}