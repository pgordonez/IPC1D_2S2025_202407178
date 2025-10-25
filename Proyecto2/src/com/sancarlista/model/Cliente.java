package com.sancarlista.model;

import java.io.Serializable;

/**
 * Clase Cliente - Hereda de Usuario
 */
public class Cliente extends Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private String cumpleanos;
    private String codigoVendedor; // Vendedor que lo registró
    
    public Cliente(String codigo, String nombre, String genero, String cumpleanos, 
                   String contrasena, String codigoVendedor) {
        super(codigo, nombre, genero, contrasena);
        this.cumpleanos = cumpleanos;
        this.codigoVendedor = codigoVendedor;
    }
    
    @Override
    public String getTipoUsuario() {
        return "CLIENTE";
    }
    
    public String getCumpleanos() {
        return cumpleanos;
    }
    
    public void setCumpleanos(String cumpleanos) {
        this.cumpleanos = cumpleanos;
    }
    
    public String getCodigoVendedor() {
        return codigoVendedor;
    }
    
    public void setCodigoVendedor(String codigoVendedor) {
        this.codigoVendedor = codigoVendedor;
    }
}