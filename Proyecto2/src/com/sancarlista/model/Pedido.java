package com.sancarlista.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase Pedido - Representa un pedido del cliente
 */
public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int contadorId = 1;
    
    private int id;
    private String codigoCliente;
    private ItemPedido[] items;
    private int cantItems;
    private double total;
    private String estado; // PENDIENTE, CONFIRMADO
    private Date fecha;
    
    public Pedido(String codigoCliente) {
        this.id = contadorId++;
        this.codigoCliente = codigoCliente;
        this.items = new ItemPedido[50];
        this.cantItems = 0;
        this.total = 0;
        this.estado = "PENDIENTE";
        this.fecha = new Date();
    }
    
    public void agregarItem(Producto producto, int cantidad) {
        if (cantItems < items.length) {
            ItemPedido item = new ItemPedido(
                producto.getCodigo(),
                producto.getNombre(),
                cantidad,
                producto.getPrecio()
            );
            items[cantItems++] = item;
            total += item.getSubtotal();
        }
    }
    
    public int getId() {
        return id;
    }
    
    public String getCodigoCliente() {
        return codigoCliente;
    }
    
    public ItemPedido[] getItems() {
        ItemPedido[] resultado = new ItemPedido[cantItems];
        for (int i = 0; i < cantItems; i++) {
            resultado[i] = items[i];
        }
        return resultado;
    }
    
    public double getTotal() {
        return total;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public String getFechaFormateada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(fecha);
    }
}