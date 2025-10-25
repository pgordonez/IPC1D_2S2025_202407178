package com.sancarlista.controller;

import com.sancarlista.model.*;
import com.sancarlista.view.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Controlador del módulo de Cliente
 */
public class ClienteController {
    private Sistema sistema;
    private ClienteView vista;
    private CarritoCompras carrito;
    
    public ClienteController(ClienteView vista) {
        this.sistema = Sistema.getInstancia();
        this.vista = vista;
        this.carrito = new CarritoCompras();
        
        // Validar que el usuario actual sea un cliente
        if (!validarUsuarioCliente()) {
            JOptionPane.showMessageDialog(vista, 
                "Acceso denegado: Usuario no es cliente", 
                "Error", JOptionPane.ERROR_MESSAGE);
            vista.dispose();
            return;
        }
        
        inicializarEventos();
        cargarDatos();
    }
    
    /**
     * Valida que el usuario actual sea un cliente
     */
    private boolean validarUsuarioCliente() {
        Usuario usuario = sistema.getUsuarioActual();
        return usuario != null && usuario instanceof Cliente;
    }
    
    private void inicializarEventos() {
        // Eventos del catálogo
        vista.getBtnAgregarCarrito().addActionListener(e -> agregarAlCarrito());
        
        // Eventos del carrito
        vista.getBtnActualizarCantidad().addActionListener(e -> actualizarCantidad());
        vista.getBtnEliminarDelCarrito().addActionListener(e -> eliminarDelCarrito());
        vista.getBtnRealizarPedido().addActionListener(e -> realizarPedido());
        
        // Eventos generales
        vista.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
        vista.getBtnRefrescar().addActionListener(e -> cargarDatos());
    }
    
    // ===== GESTIÓN DEL CATÁLOGO =====
    
    private void agregarAlCarrito() {
        int fila = vista.getTablaProductos().getSelectedRow();
        
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista, 
                "Seleccione un producto de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Object valorCodigo = vista.getTablaProductos().getValueAt(fila, 0);
            if (valorCodigo == null) {
                JOptionPane.showMessageDialog(vista, 
                    "Código de producto inválido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String codigoProducto = valorCodigo.toString();
            String cantidadStr = JOptionPane.showInputDialog(vista, 
                "Ingrese la cantidad:", "Cantidad", JOptionPane.QUESTION_MESSAGE);
            
            if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
                return;
            }
            
            int cantidad = Integer.parseInt(cantidadStr.trim());
            
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(vista, 
                    "La cantidad debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Producto producto = sistema.buscarProductoPorCodigo(codigoProducto);
            
            if (producto == null) {
                JOptionPane.showMessageDialog(vista, 
                    "Producto no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validar stock disponible
            int cantidadEnCarrito = carrito.obtenerCantidadProducto(codigoProducto);
            int cantidadTotal = cantidadEnCarrito + cantidad;
            
            if (producto.getStock() < cantidadTotal) {
                JOptionPane.showMessageDialog(vista, 
                    "Stock insuficiente. Disponible: " + producto.getStock() + 
                    "\nYa tiene en carrito: " + cantidadEnCarrito, 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (carrito.agregarItem(producto, cantidad)) {
                JOptionPane.showMessageDialog(vista, 
                    "Producto agregado al carrito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarTablaCarrito();
            } else {
                JOptionPane.showMessageDialog(vista, 
                    "Error al agregar al carrito", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, 
                "Cantidad inválida. Ingrese un número entero", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, 
                "Error al agregar al carrito: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ===== GESTIÓN DEL CARRITO =====
    
    private void actualizarCantidad() {
        int fila = vista.getTablaCarrito().getSelectedRow();
        
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista, 
                "Seleccione un item del carrito", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Object valorCodigo = vista.getTablaCarrito().getValueAt(fila, 0);
            if (valorCodigo == null) {
                JOptionPane.showMessageDialog(vista, 
                    "Código de producto inválido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String codigoProducto = valorCodigo.toString();
            String cantidadStr = JOptionPane.showInputDialog(vista, 
                "Ingrese la nueva cantidad:", "Actualizar Cantidad", JOptionPane.QUESTION_MESSAGE);
            
            if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
                return;
            }
            
            int nuevaCantidad = Integer.parseInt(cantidadStr.trim());
            
            if (nuevaCantidad <= 0) {
                JOptionPane.showMessageDialog(vista, 
                    "La cantidad debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Producto producto = sistema.buscarProductoPorCodigo(codigoProducto);
            
            if (producto == null) {
                JOptionPane.showMessageDialog(vista, 
                    "Producto no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (producto.getStock() < nuevaCantidad) {
                JOptionPane.showMessageDialog(vista, 
                    "Stock insuficiente. Disponible: " + producto.getStock(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (carrito.actualizarCantidad(codigoProducto, nuevaCantidad)) {
                JOptionPane.showMessageDialog(vista, 
                    "Cantidad actualizada", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarTablaCarrito();
            } else {
                JOptionPane.showMessageDialog(vista, 
                    "Error al actualizar cantidad", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, 
                "Cantidad inválida. Ingrese un número entero", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, 
                "Error al actualizar cantidad: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarDelCarrito() {
        int fila = vista.getTablaCarrito().getSelectedRow();
        
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista, 
                "Seleccione un item del carrito", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Object valorCodigo = vista.getTablaCarrito().getValueAt(fila, 0);
            if (valorCodigo == null) {
                JOptionPane.showMessageDialog(vista, 
                    "Código de producto inválido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String codigoProducto = valorCodigo.toString();
            
            int confirmacion = JOptionPane.showConfirmDialog(vista, 
                "¿Desea eliminar este producto del carrito?", 
                "Confirmar", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (carrito.eliminarItem(codigoProducto)) {
                    JOptionPane.showMessageDialog(vista, 
                        "Producto eliminado del carrito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarTablaCarrito();
                } else {
                    JOptionPane.showMessageDialog(vista, 
                        "Error al eliminar del carrito", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, 
                "Error al eliminar del carrito: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void realizarPedido() {
        if (carrito == null || carrito.getCantItems() == 0) {
            JOptionPane.showMessageDialog(vista, 
                "El carrito está vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double total = carrito.calcularTotal();
        
        int confirmacion = JOptionPane.showConfirmDialog(vista, 
            "¿Confirma realizar el pedido?\nTotal: Q" + String.format("%.2f", total), 
            "Confirmar Pedido", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        
        Usuario cliente = sistema.getUsuarioActual();
        
        if (cliente == null || cliente.getCodigo() == null) {
            JOptionPane.showMessageDialog(vista, 
                "Error: Usuario no válido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Pedido pedido = new Pedido(cliente.getCodigo());
        ItemCarrito[] items = carrito.getItems();
        
        if (items == null || items.length == 0) {
            JOptionPane.showMessageDialog(vista, 
                "El carrito está vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar stock antes de crear el pedido
        boolean stockSuficiente = true;
        StringBuilder mensajeError = new StringBuilder();
        
        for (ItemCarrito item : items) {
            if (item != null && item.getCodigoProducto() != null) {
                Producto producto = sistema.buscarProductoPorCodigo(item.getCodigoProducto());
                
                if (producto == null) {
                    mensajeError.append("Producto no encontrado: ")
                               .append(item.getCodigoProducto()).append("\n");
                    stockSuficiente = false;
                    continue;
                }
                
                if (producto.getStock() < item.getCantidad()) {
                    mensajeError.append("Stock insuficiente para: ")
                               .append(producto.getNombre())
                               .append(" (Disponible: ").append(producto.getStock())
                               .append(", Solicitado: ").append(item.getCantidad())
                               .append(")\n");
                    stockSuficiente = false;
                }
            }
        }
        
        if (!stockSuficiente) {
            JOptionPane.showMessageDialog(vista, 
                mensajeError.toString(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Agregar items al pedido (sin reducir stock aún)
        for (ItemCarrito item : items) {
            if (item != null && item.getCodigoProducto() != null) {
                Producto producto = sistema.buscarProductoPorCodigo(item.getCodigoProducto());
                if (producto != null) {
                    pedido.agregarItem(producto, item.getCantidad());
                }
            }
        }
        
        // Crear pedido en el sistema
        if (sistema.crearPedido(pedido)) {
            JOptionPane.showMessageDialog(vista, 
                "Pedido realizado exitosamente\n" +
                "ID del pedido: " + pedido.getId() + "\n" +
                "Su pedido será confirmado por un vendedor", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            carrito.vaciar();
            cargarDatos();
        } else {
            JOptionPane.showMessageDialog(vista, 
                "Error al crear el pedido. Intente nuevamente", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ===== CARGA DE DATOS EN TABLAS =====
    
    private void cargarDatos() {
        cargarTablaProductos();
        cargarTablaCarrito();
        cargarTablaHistorial();
    }
    
    private void cargarTablaProductos() {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaProductos().getModel();
        modelo.setRowCount(0);
        
        Producto[] productos = sistema.obtenerProductosConStock();
        
        if (productos != null) {
            for (Producto producto : productos) {
                if (producto != null && producto.getStock() > 0) {
                    modelo.addRow(new Object[]{
                        producto.getCodigo() != null ? producto.getCodigo() : "N/A",
                        producto.getNombre() != null ? producto.getNombre() : "N/A",
                        producto.getCategoria() != null ? producto.getCategoria() : "N/A",
                        producto.getStock(),
                        String.format("Q%.2f", producto.getPrecio())
                    });
                }
            }
        }
    }
    
    private void cargarTablaCarrito() {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaCarrito().getModel();
        modelo.setRowCount(0);
        
        if (carrito != null) {
            ItemCarrito[] items = carrito.getItems();
            
            if (items != null) {
                for (ItemCarrito item : items) {
                    if (item != null) {
                        modelo.addRow(new Object[]{
                            item.getCodigoProducto() != null ? item.getCodigoProducto() : "N/A",
                            item.getNombreProducto() != null ? item.getNombreProducto() : "N/A",
                            item.getCantidad(),
                            String.format("Q%.2f", item.getPrecioUnitario()),
                            String.format("Q%.2f", item.getSubtotal())
                        });
                    }
                }
            }
            
            // Actualizar total
            if (vista.getLblTotal() != null) {
                vista.getLblTotal().setText("Total: Q" + 
                    String.format("%.2f", carrito.calcularTotal()));
            }
        }
    }
    
    private void cargarTablaHistorial() {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaHistorial().getModel();
        modelo.setRowCount(0);
        
        Usuario cliente = sistema.getUsuarioActual();
        
        if (cliente == null || cliente.getCodigo() == null) {
            return;
        }
        
        Pedido[] pedidos = sistema.obtenerPedidosCliente(cliente.getCodigo());
        
        if (pedidos != null) {
            for (Pedido pedido : pedidos) {
                if (pedido != null) {
                    modelo.addRow(new Object[]{
                        pedido.getId(),
                        pedido.getFechaFormateada() != null ? pedido.getFechaFormateada() : "N/A",
                        String.format("Q%.2f", pedido.getTotal()),
                        pedido.getEstado() != null ? pedido.getEstado() : "N/A"
                    });
                }
            }
        }
    }
    
    // ===== CERRAR SESIÓN =====
    
    private void cerrarSesion() {
        // Verificar si hay items en el carrito
        if (carrito != null && carrito.getCantItems() > 0) {
            int confirmacion = JOptionPane.showConfirmDialog(vista, 
                "Tiene productos en el carrito que se perderán.\n¿Desea cerrar sesión?", 
                "Confirmar", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }
        } else {
            int confirmacion = JOptionPane.showConfirmDialog(vista, 
                "¿Desea cerrar sesión?", 
                "Confirmar", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        sistema.cerrarSesion();
        vista.dispose();
        
        LoginView loginView = new LoginView();
        new SistemaController(loginView);
        loginView.setVisible(true);
    }
}