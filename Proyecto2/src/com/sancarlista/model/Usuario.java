package com.sancarlista.model;

import java.io.Serializable;

/**
 * Clase abstracta base para todos los usuarios del sistema
 * Implementa herencia para Administrador, Vendedor y Cliente
 */
public abstract class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String codigo;
    protected String nombre;
    protected String genero;
    protected String contrasena;
    protected String tipoUsuario;
    
    public Usuario(String codigo, String nombre, String genero, String contrasena) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.genero = genero;
        this.contrasena = contrasena;
    }
    
    // Método abstracto que implementará cada tipo de usuario
    public abstract String getTipoUsuario();
    
    // Método para autenticación
    public boolean autenticar(String codigo, String contrasena) {
        return this.codigo.equals(codigo) && this.contrasena.equals(contrasena);
    }
    
    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public void setGenero(String genero) {
        this.genero = genero;
    }
    
    public String getContrasena() {
        return contrasena;
    }
    
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", genero='" + genero + '\'' +
                ", tipo='" + getTipoUsuario() + '\'' +
                '}';
    }
}