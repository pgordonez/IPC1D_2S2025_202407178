package com.sancarlista.model;

import java.io.Serializable;

/**
 * Clase Vendedor - Hereda de Usuario
 */
public class Vendedor extends Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private int ventasConfirmadas;
    
    public Vendedor(String codigo, String nombre, String genero, String contrasena) {
        super(codigo, nombre, genero, contrasena);
        this.ventasConfirmadas = 0;
    }
    
    @Override
    public String getTipoUsuario() {
        return "VENDEDOR";
    }
    
    public int getVentasConfirmadas() {
        return ventasConfirmadas;
    }
    
    public void incrementarVentas() {
        this.ventasConfirmadas++;
    }
    
    public void setVentasConfirmadas(int ventas) {
        this.ventasConfirmadas = ventas;
    }
}
