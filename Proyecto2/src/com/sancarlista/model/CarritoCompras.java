package com.sancarlista.model;

import java.io.Serializable;

/**
 * Clase CarritoCompras - Gestiona los items del carrito de un cliente
 */
public class CarritoCompras implements Serializable {
    private static final long serialVersionUID = 1L;
    private ItemCarrito[] items;
    private int cantItems;
    private static final int CAPACIDAD_INICIAL = 50;
    
    public CarritoCompras() {
        items = new ItemCarrito[CAPACIDAD_INICIAL];
        cantItems = 0;
    }
    
    /**
     * Agrega un producto al carrito
     */
    public boolean agregarItem(Producto producto, int cantidad) {
        if (producto == null || cantidad <= 0) {
            return false;
        }
        
        // Verificar si el producto ya existe en el carrito
        for (int i = 0; i < cantItems; i++) {
            if (items[i] != null && 
                items[i].getCodigoProducto().equals(producto.getCodigo())) {
                // Actualizar cantidad
                items[i].setCantidad(items[i].getCantidad() + cantidad);
                return true;
            }
        }
        
        // Agregar nuevo item
        if (cantItems >= items.length) {
            expandirVector();
        }
        
        ItemCarrito nuevoItem = new ItemCarrito(
            producto.getCodigo(),
            producto.getNombre(),
            cantidad,
            producto.getPrecio()
        );
        
        items[cantItems++] = nuevoItem;
        return true;
    }
    
    /**
     * Actualiza la cantidad de un producto en el carrito
     */
    public boolean actualizarCantidad(String codigoProducto, int nuevaCantidad) {
        if (codigoProducto == null || nuevaCantidad <= 0) {
            return false;
        }
        
        for (int i = 0; i < cantItems; i++) {
            if (items[i] != null && 
                items[i].getCodigoProducto().equals(codigoProducto)) {
                items[i].setCantidad(nuevaCantidad);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Elimina un producto del carrito
     */
    public boolean eliminarItem(String codigoProducto) {
        if (codigoProducto == null) {
            return false;
        }
        
        for (int i = 0; i < cantItems; i++) {
            if (items[i] != null && 
                items[i].getCodigoProducto().equals(codigoProducto)) {
                // Mover items hacia la izquierda
                for (int j = i; j < cantItems - 1; j++) {
                    items[j] = items[j + 1];
                }
                items[--cantItems] = null;
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Obtiene la cantidad de un producto específico en el carrito
     */
    public int obtenerCantidadProducto(String codigoProducto) {
        if (codigoProducto == null) {
            return 0;
        }
        
        for (int i = 0; i < cantItems; i++) {
            if (items[i] != null && 
                items[i].getCodigoProducto() != null &&
                items[i].getCodigoProducto().equals(codigoProducto)) {
                return items[i].getCantidad();
            }
        }
        
        return 0;
    }
    
    /**
     * Vacía el carrito
     */
    public void vaciar() {
        for (int i = 0; i < cantItems; i++) {
            items[i] = null;
        }
        cantItems = 0;
    }
    
    /**
     * Calcula el total del carrito
     */
    public double calcularTotal() {
        double total = 0;
        for (int i = 0; i < cantItems; i++) {
            if (items[i] != null) {
                total += items[i].getSubtotal();
            }
        }
        return total;
    }
    
    /**
     * Obtiene todos los items del carrito
     */
    public ItemCarrito[] getItems() {
        ItemCarrito[] resultado = new ItemCarrito[cantItems];
        for (int i = 0; i < cantItems; i++) {
            resultado[i] = items[i];
        }
        return resultado;
    }
    
    /**
     * Obtiene la cantidad de items en el carrito
     */
    public int getCantItems() {
        return cantItems;
    }
    
    /**
     * Expande el vector cuando se llena
     */
    private void expandirVector() {
        ItemCarrito[] nuevoVector = new ItemCarrito[items.length * 2];
        for (int i = 0; i < cantItems; i++) {
            nuevoVector[i] = items[i];
        }
        items = nuevoVector;
    }
}