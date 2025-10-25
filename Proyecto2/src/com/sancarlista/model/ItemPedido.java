package com.sancarlista.model;

import java.io.Serializable;

/**
 * Clase ItemPedido - Representa un item individual dentro de un pedido
 * Esta clase debe estar en un archivo separado para ser accesible
 */
public class ItemPedido implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String codigoProducto;
    private String nombreProducto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    
    /**
     * Constructor de ItemPedido
     * @param codigoProducto Código del producto
     * @param nombreProducto Nombre del producto
     * @param cantidad Cantidad del producto
     * @param precioUnitario Precio unitario del producto
     */
    public ItemPedido(String codigoProducto, String nombreProducto, 
                      int cantidad, double precioUnitario) {
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }
    
    // Getters
    public String getCodigoProducto() {
        return codigoProducto;
    }
    
    public String getNombreProducto() {
        return nombreProducto;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public double getPrecioUnitario() {
        return precioUnitario;
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    // Setters (si necesitas modificar después de crear)
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = cantidad * precioUnitario;
    }
    
    @Override
    public String toString() {
        return "ItemPedido{" +
                "producto='" + nombreProducto + '\'' +
                ", cantidad=" + cantidad +
                ", precioUnit=" + precioUnitario +
                ", subtotal=" + subtotal +
                '}';
    }
}