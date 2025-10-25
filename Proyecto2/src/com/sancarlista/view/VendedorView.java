package com.sancarlista.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Vista del módulo de Vendedor
 */
public class VendedorView extends JFrame {
    
    // Componentes para Stock
    private JTextField txtCodigoProducto, txtCantidadStock;
    private JButton btnAgregarStock, btnCargarStockCSV, btnHistorialStock;
    private JTable tablaProductos;
    
    // Componentes para Clientes
    private JTextField txtCodigoCliente, txtNombreCliente, txtCumpleanosCliente;
    private JPasswordField txtContrasenaCliente;
    private JComboBox<String> cmbGeneroCliente;
    private JButton btnCrearCliente, btnActualizarCliente, btnEliminarCliente, btnCargarClientesCSV;
    private JTable tablaClientes;
    
    // Componentes para Pedidos
    private JButton btnConfirmarPedido;
    private JTable tablaPedidos;
    
    // Componentes generales
    private JButton btnCerrarSesion, btnRefrescar;
    private JTabbedPane tabbedPane;
    
    public VendedorView() {
        configurarVentana();
        inicializarComponentes();
    }
    
    private void configurarVentana() {
        setTitle("Sancarlista Shop - Vendedor");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void inicializarComponentes() {
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Productos", crearPanelProductos());
        tabbedPane.addTab("Clientes", crearPanelClientes());
        tabbedPane.addTab("Pedidos", crearPanelPedidos());
        
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        // Header
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelHeader.setBackground(new Color(40, 167, 69));
        
        JLabel lblUsuario = new JLabel("Vendedor");
        lblUsuario.setForeground(Color.WHITE);
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
    
    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de gestión de stock
        JPanel panelStock = new JPanel(new GridLayout(3, 2, 10, 10));
        panelStock.setBorder(BorderFactory.createTitledBorder("Gestión de Stock"));
        
        panelStock.add(new JLabel("Código Producto:"));
        txtCodigoProducto = new JTextField();
        panelStock.add(txtCodigoProducto);
        
        panelStock.add(new JLabel("Cantidad:"));
        txtCantidadStock = new JTextField();
        panelStock.add(txtCantidadStock);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAgregarStock = new JButton("Agregar Stock");
        btnAgregarStock.setBackground(new Color(0, 123, 255));
        btnAgregarStock.setForeground(Color.WHITE);
        panelBotones.add(btnAgregarStock);
        
        btnCargarStockCSV = new JButton("Cargar CSV");
        btnCargarStockCSV.setBackground(new Color(40, 167, 69));
        btnCargarStockCSV.setForeground(Color.WHITE);
        panelBotones.add(btnCargarStockCSV);
        
        btnHistorialStock = new JButton("Ver Historial");
        btnHistorialStock.setBackground(new Color(23, 162, 184));
        btnHistorialStock.setForeground(Color.WHITE);
        panelBotones.add(btnHistorialStock);
        
        panelStock.add(panelBotones);
        
        // Tabla
        String[] columnas = {"Código", "Nombre", "Categoría", "Stock"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaProductos = new JTable(modelo);
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Productos Disponibles"));
        
        panel.add(panelStock, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelClientes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de formulario
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        
        panelForm.add(new JLabel("Código:"));
        txtCodigoCliente = new JTextField();
        panelForm.add(txtCodigoCliente);
        
        panelForm.add(new JLabel("Nombre:"));
        txtNombreCliente = new JTextField();
        panelForm.add(txtNombreCliente);
        
        panelForm.add(new JLabel("Género:"));
        cmbGeneroCliente = new JComboBox<>(new String[]{"M", "F"});
        panelForm.add(cmbGeneroCliente);
        
        panelForm.add(new JLabel("Cumpleaños (DD/MM/YYYY):"));
        txtCumpleanosCliente = new JTextField();
        panelForm.add(txtCumpleanosCliente);
        
        panelForm.add(new JLabel("Contraseña:"));
        txtContrasenaCliente = new JPasswordField();
        panelForm.add(txtContrasenaCliente);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnCrearCliente = new JButton("Crear");
        btnCrearCliente.setBackground(new Color(0, 123, 255));
        btnCrearCliente.setForeground(Color.WHITE);
        panelBotones.add(btnCrearCliente);
        
        btnActualizarCliente = new JButton("Actualizar");
        btnActualizarCliente.setBackground(new Color(255, 193, 7));
        panelBotones.add(btnActualizarCliente);
        
        btnEliminarCliente = new JButton("Eliminar");
        btnEliminarCliente.setBackground(new Color(220, 53, 69));
        btnEliminarCliente.setForeground(Color.WHITE);
        panelBotones.add(btnEliminarCliente);
        
        btnCargarClientesCSV = new JButton("Cargar CSV");
        btnCargarClientesCSV.setBackground(new Color(40, 167, 69));
        btnCargarClientesCSV.setForeground(Color.WHITE);
        panelBotones.add(btnCargarClientesCSV);
        
        panelForm.add(panelBotones);
        
        // Tabla
        String[] columnas = {"Código", "Nombre", "Género", "Cumpleaños"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaClientes = new JTable(modelo);
        JScrollPane scrollTabla = new JScrollPane(tablaClientes);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Mis Clientes"));
        
        panel.add(panelForm, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelPedidos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Botón confirmar
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnConfirmarPedido = new JButton("Confirmar Pedido Seleccionado");
        btnConfirmarPedido.setBackground(new Color(40, 167, 69));
        btnConfirmarPedido.setForeground(Color.WHITE);
        btnConfirmarPedido.setFont(new Font("Arial", Font.BOLD, 14));
        panelTop.add(btnConfirmarPedido);
        
        // Tabla
        String[] columnas = {"ID", "Cliente", "Total", "Fecha", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPedidos = new JTable(modelo);
        JScrollPane scrollTabla = new JScrollPane(tablaPedidos);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Pedidos Pendientes"));
        
        panel.add(panelTop, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Getters
    public JTextField getTxtCodigoProducto() { return txtCodigoProducto; }
    public JTextField getTxtCantidadStock() { return txtCantidadStock; }
    public JButton getBtnAgregarStock() { return btnAgregarStock; }
    public JButton getBtnCargarStockCSV() { return btnCargarStockCSV; }
    public JButton getBtnHistorialStock() { return btnHistorialStock; }
    public JTable getTablaProductos() { return tablaProductos; }
    
    public JTextField getTxtCodigoCliente() { return txtCodigoCliente; }
    public JTextField getTxtNombreCliente() { return txtNombreCliente; }
    public JTextField getTxtCumpleanosCliente() { return txtCumpleanosCliente; }
    public JPasswordField getTxtContrasenaCliente() { return txtContrasenaCliente; }
    public JComboBox<String> getCmbGeneroCliente() { return cmbGeneroCliente; }
    public JButton getBtnCrearCliente() { return btnCrearCliente; }
    public JButton getBtnActualizarCliente() { return btnActualizarCliente; }
    public JButton getBtnEliminarCliente() { return btnEliminarCliente; }
    public JButton getBtnCargarClientesCSV() { return btnCargarClientesCSV; }
    public JTable getTablaClientes() { return tablaClientes; }
    
    public JButton getBtnConfirmarPedido() { return btnConfirmarPedido; }
    public JTable getTablaPedidos() { return tablaPedidos; }
    
    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
    public JButton getBtnRefrescar() { return btnRefrescar; }
}