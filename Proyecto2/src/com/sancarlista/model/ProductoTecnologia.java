package com.sancarlista.model;

import java.io.Serializable;

/**
 * Producto Tecnológico - Tiene meses de garantía
 */
public class ProductoTecnologia extends Producto implements Serializable {
    private static final long serialVersionUID = 1L;
    private int mesesGarantia;
    
    public ProductoTecnologia(String codigo, String nombre, double precio, int mesesGarantia) {
        super(codigo, nombre, "Tecnologia", precio);
        this.mesesGarantia = mesesGarantia;
    }
    
    @Override
    public String getDetalleEspecifico() {
        return "Meses de Garantía: " + mesesGarantia;
    }
    
    @Override
    public void setAtributoEspecifico(String valor) {
        this.mesesGarantia = Integer.parseInt(valor);
    }
    
    public int getMesesGarantia() {
        return mesesGarantia;
    }
    
    public void setMesesGarantia(int mesesGarantia) {
        this.mesesGarantia = mesesGarantia;
    }
}