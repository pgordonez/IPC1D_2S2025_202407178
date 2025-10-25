package com.sancarlista.model;

import java.io.Serializable;

/**
 * Clase abstracta base para productos
 * Implementa polimorfismo según categoría
 * 
 * Esta clase es la base para:
 * - ProductoTecnologia (con meses de garantía)
 * - ProductoAlimento (con fecha de caducidad)
 * - ProductoGeneral (con material)
 */
public abstract class Producto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Atributos comunes a todos los productos
    protected String codigo;
    protected String nombre;
    protected String categoria;
    protected int stock;
    protected double precio;
    protected int cantidadVendida;
    
    /**
     * Constructor de la clase Producto
     * @param codigo Código único del producto
     * @param nombre Nombre del producto
     * @param categoria Categoría (Tecnologia, Alimento, General)
     * @param precio Precio del producto
     */
    public Producto(String codigo, String nombre, String categoria, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = 0;
        this.cantidadVendida = 0;
    }
    
    // ===== MÉTODOS ABSTRACTOS (Polimorfismo) =====
    
    /**
     * Método abstracto para obtener detalles específicos del producto
     * Cada tipo de producto implementará su propia versión
     * @return String con el detalle específico
     */
    public abstract String getDetalleEspecifico();
    
    /**
     * Método abstracto para actualizar atributo específico
     * Cada tipo de producto implementará su propia versión
     * @param valor Nuevo valor del atributo específico
     */
    public abstract void setAtributoEspecifico(String valor);
    
    // ===== MÉTODOS DE GESTIÓN DE STOCK =====
    
    /**
     * Agrega stock al producto
     * @param cantidad Cantidad a agregar
     */
    public void agregarStock(int cantidad) {
        if (cantidad > 0) {
            this.stock += cantidad;
        }
    }
    
    /**
     * Reduce el stock del producto
     * @param cantidad Cantidad a reducir
     * @return true si había stock suficiente, false si no
     */
    public boolean reducirStock(int cantidad) {
        if (this.stock >= cantidad && cantidad > 0) {
            this.stock -= cantidad;
            this.cantidadVendida += cantidad;
            return true;
        }
        return false;
    }
    
    /**
     * Verifica si hay stock disponible
     * @param cantidad Cantidad a verificar
     * @return true si hay stock suficiente
     */
    public boolean hayStockDisponible(int cantidad) {
        return this.stock >= cantidad;
    }
    
    // ===== GETTERS Y SETTERS =====
    
    /**
     * Obtiene el código del producto
     * @return Código del producto
     */
    public String getCodigo() {
        return codigo;
    }
    
    /**
     * Establece el código del producto
     * @param codigo Nuevo código
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    /**
     * Obtiene el nombre del producto
     * @return Nombre del producto
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Establece el nombre del producto
     * @param nombre Nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Obtiene la categoría del producto
     * @return Categoría del producto
     */
    public String getCategoria() {
        return categoria;
    }
    
    /**
     * Establece la categoría del producto
     * @param categoria Nueva categoría
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    /**
     * Obtiene el stock actual del producto
     * @return Stock disponible
     */
    public int getStock() {
        return stock;
    }
    
    /**
     * Establece el stock del producto directamente
     * @param stock Nuevo stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    /**
     * Obtiene el precio del producto
     * @return Precio del producto
     */
    public double getPrecio() {
        return precio;
    }
    
    /**
     * Establece el precio del producto
     * @param precio Nuevo precio
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    /**
     * Obtiene la cantidad total vendida del producto
     * @return Cantidad vendida
     */
    public int getCantidadVendida() {
        return cantidadVendida;
    }
    
    /**
     * Establece la cantidad vendida del producto
     * @param cantidadVendida Nueva cantidad vendida
     */
    public void setCantidadVendida(int cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }
    
    /**
     * Calcula los ingresos generados por este producto
     * @return Ingresos totales (cantidad vendida * precio)
     */
    public double calcularIngresos() {
        return cantidadVendida * precio;
    }
    
    /**
     * Verifica si el producto tiene stock crítico (menos de 10 unidades)
     * @return true si el stock es crítico
     */
    public boolean esStockCritico() {
        return stock < 10;
    }
    
    /**
     * Verifica si el producto tiene stock bajo (menos de 20 unidades)
     * @return true si el stock es bajo
     */
    public boolean esStockBajo() {
        return stock < 20;
    }
    
    /**
     * Obtiene el estado del stock
     * @return "CRÍTICO" si stock < 10, "BAJO" si stock < 20, "NORMAL" en otro caso
     */
    public String getEstadoStock() {
        if (esStockCritico()) {
            return "CRÍTICO";
        } else if (esStockBajo()) {
            return "BAJO";
        } else {
            return "NORMAL";
        }
    }
    
    /**
     * Representación en String del producto
     * @return String con información básica del producto
     */
    @Override
    public String toString() {
        return "Producto{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", categoria='" + categoria + '\'' +
                ", stock=" + stock +
                ", precio=" + precio +
                ", vendidos=" + cantidadVendida +
                '}';
    }
}