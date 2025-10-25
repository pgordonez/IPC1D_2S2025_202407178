package com.sancarlista.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Vista del módulo de Cliente
 */
public class ClienteView extends JFrame {
    
    // Componentes del catálogo
    private JButton btnAgregarCarrito;
    private JTable tablaProductos;
    
    // Componentes del carrito
    private JButton btnActualizarCantidad, btnEliminarDelCarrito, btnRealizarPedido;
    private JTable tablaCarrito;
    private JLabel lblTotal;
    
    // Componentes del historial
    private JTable tablaHistorial;
    
    // Componentes generales
    private JButton btnCerrarSesion, btnRefrescar;
    private JTabbedPane tabbedPane;
    
    public ClienteView() {
        configurarVentana();
        inicializarComponentes();
    }
    
    private void configurarVentana() {
        setTitle("Sancarlista Shop - Cliente");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void inicializarComponentes() {
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Catálogo de Productos", crearPanelCatalogo());
        tabbedPane.addTab("Carrito de Compras", crearPanelCarrito());
        tabbedPane.addTab("Historial de Compras", crearPanelHistorial());
        
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        // Header
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelHeader.setBackground(new Color(255, 193, 7));
        
        JLabel lblUsuario = new JLabel("Cliente");
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        panelHeader.add(lblUsuario);
        
        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setFocusPainted(false);
        panelHeader.add(btnRefrescar);
        
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(220, 53, 69));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        panelHeader.add(btnCerrarSesion);
        
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(tabbedPane, BorderLayout.CENTER);
        
        add(panelPrincipal);
    }
    
    private JPanel crearPanelCatalogo() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel superior con botón
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAgregarCarrito = new JButton("Agregar al Carrito");
        btnAgregarCarrito.setBackground(new Color(0, 123, 255));
        btnAgregarCarrito.setForeground(Color.WHITE);
        btnAgregarCarrito.setFont(new Font("Arial", Font.BOLD, 14));
        panelTop.add(btnAgregarCarrito);
        
        // Tabla de productos
        String[] columnas = {"Código", "Nombre", "Categoría", "Stock", "Precio"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaProductos = new JTable(modelo);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Productos Disponibles"));
        
        panel.add(panelTop, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelCarrito() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        btnActualizarCantidad = new JButton("Actualizar Cantidad");
        btnActualizarCantidad.setBackground(new Color(255, 193, 7));
        btnActualizarCantidad.setForeground(Color.BLACK);
        panelBotones.add(btnActualizarCantidad);
        
        btnEliminarDelCarrito = new JButton("Eliminar");
        btnEliminarDelCarrito.setBackground(new Color(220, 53, 69));
        btnEliminarDelCarrito.setForeground(Color.WHITE);
        panelBotones.add(btnEliminarDelCarrito);
        
        btnRealizarPedido = new JButton("Realizar Pedido");
        btnRealizarPedido.setBackground(new Color(40, 167, 69));
        btnRealizarPedido.setForeground(Color.WHITE);
        btnRealizarPedido.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnRealizarPedido);
        
        // Tabla del carrito
        String[] columnas = {"Código", "Producto", "Cantidad", "Precio Unit.", "Subtotal"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCarrito = new JTable(modelo);
        JScrollPane scrollTabla = new JScrollPane(tablaCarrito);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Mi Carrito"));
        
        // Panel inferior con total
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal = new JLabel("Total: Q0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setForeground(new Color(0, 51, 153));
        panelTotal.add(lblTotal);
        
        panel.add(panelBotones, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);
        panel.add(panelTotal, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tabla de historial
        String[] columnas = {"ID Pedido", "Fecha", "Total", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaHistorial = new JTable(modelo);
        JScrollPane scrollTabla = new JScrollPane(tablaHistorial);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Mis Compras Confirmadas"));
        
        panel.add(scrollTabla, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Getters
    public JButton getBtnAgregarCarrito() { return btnAgregarCarrito; }
    public JTable getTablaProductos() { return tablaProductos; }
    
    public JButton getBtnActualizarCantidad() { return btnActualizarCantidad; }
    public JButton getBtnEliminarDelCarrito() { return btnEliminarDelCarrito; }
    public JButton getBtnRealizarPedido() { return btnRealizarPedido; }
    public JTable getTablaCarrito() { return tablaCarrito; }
    public JLabel getLblTotal() { return lblTotal; }
    
    public JTable getTablaHistorial() { return tablaHistorial; }
    
    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
    public JButton getBtnRefrescar() { return btnRefrescar; }
}