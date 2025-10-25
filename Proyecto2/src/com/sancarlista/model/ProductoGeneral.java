package com.sancarlista.model;

import java.io.Serializable;

/**
 * Producto General - Tiene material
 */
public class ProductoGeneral extends Producto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String material;
    
    public ProductoGeneral(String codigo, String nombre, double precio, String material) {
        super(codigo, nombre, "General", precio);
        this.material = material;
    }
    
    @Override
    public String getDetalleEspecifico() {
        return "Material: " + material;
    }
    
    @Override
    public void setAtributoEspecifico(String valor) {
        this.material = valor;
    }
    
    public String getMaterial() {
        return material;
    }
    
    public void setMaterial(String material) {
        this.material = material;
    }
}