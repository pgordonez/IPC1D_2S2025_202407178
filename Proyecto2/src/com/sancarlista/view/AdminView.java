package com.sancarlista.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Vista del módulo de Administrador
 */
public class AdminView extends JFrame {
    
    // Componentes para Vendedores
    private JTextField txtCodigoVendedor, txtNombreVendedor;
    private JPasswordField txtContrasenaVendedor;
    private JComboBox<String> cmbGeneroVendedor;
    private JButton btnCrearVendedor, btnActualizarVendedor, btnEliminarVendedor, btnCargarVendedoresCSV;
    private JTable tablaVendedores;
    
    // Componentes para Productos
    private JTextField txtCodigoProducto, txtNombreProducto, txtPrecioProducto, txtAtributoProducto;
    private JComboBox<String> cmbCategoriaProducto;
    private JButton btnCrearProducto, btnActualizarProducto, btnEliminarProducto, btnCargarProductosCSV, btnVerDetalle;
    private JTable tablaProductos;
    
    // Componentes generales
    private JButton btnCerrarSesion, btnRefrescar;
    private JTabbedPane tabbedPane;
    
    public AdminView() {
        configurarVentana();
        inicializarComponentes();
    }
    
    private void configurarVentana() {
        setTitle("Sancarlista Shop - Administrador");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void inicializarComponentes() {
        tabbedPane = new JTabbedPane();
        
        // Panel de Vendedores
        JPanel panelVendedores = crearPanelVendedores();
        tabbedPane.addTab("Gestión de Vendedores", panelVendedores);
        
        // Panel de Productos
        JPanel panelProductos = crearPanelProductos();
        tabbedPane.addTab("Gestión de Productos", panelProductos);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        // Header
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelHeader.setBackground(new Color(0, 51, 153));
        
        JLabel lblUsuario = new JLabel("Administrador");
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        panelHeader.add(lblUsuario);
        
        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setBackground(new Color(40, 167, 69));
        btnRefrescar.setForeground(Color.WHITE);
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
    
    private JPanel crearPanelVendedores() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de formulario
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Vendedor"));
        
        panelForm.add(new JLabel("Código:"));
        txtCodigoVendedor = new JTextField();
        panelForm.add(txtCodigoVendedor);
        
        panelForm.add(new JLabel("Nombre:"));
        txtNombreVendedor = new JTextField();
        panelForm.add(txtNombreVendedor);
        
        panelForm.add(new JLabel("Género:"));
        cmbGeneroVendedor = new JComboBox<>(new String[]{"M", "F"});
        panelForm.add(cmbGeneroVendedor);
        
        panelForm.add(new JLabel("Contraseña:"));
        txtContrasenaVendedor = new JPasswordField();
        panelForm.add(txtContrasenaVendedor);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnCrearVendedor = new JButton("Crear");
        btnCrearVendedor.setBackground(new Color(0, 123, 255));
        btnCrearVendedor.setForeground(Color.WHITE);
        panelBotones.add(btnCrearVendedor);
        
        btnActualizarVendedor = new JButton("Actualizar");
        btnActualizarVendedor.setBackground(new Color(255, 193, 7));
        btnActualizarVendedor.setForeground(Color.BLACK);
        panelBotones.add(btnActualizarVendedor);
        
        btnEliminarVendedor = new JButton("Eliminar");
        btnEliminarVendedor.setBackground(new Color(220, 53, 69));
        btnEliminarVendedor.setForeground(Color.WHITE);
        panelBotones.add(btnEliminarVendedor);
        
        btnCargarVendedoresCSV = new JButton("Cargar CSV");
        btnCargarVendedoresCSV.setBackground(new Color(40, 167, 69));
        btnCargarVendedoresCSV.setForeground(Color.WHITE);
        panelBotones.add(btnCargarVendedoresCSV);
        
        panelForm.add(panelBotones);
        
        // Tabla
        String[] columnas = {"Código", "Nombre", "Género", "Ventas Confirmadas"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaVendedores = new JTable(modelo);
        JScrollPane scrollTabla = new JScrollPane(tablaVendedores);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Lista de Vendedores"));
        
        panel.add(panelForm, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de formulario
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));
        
        panelForm.add(new JLabel("Código:"));
        txtCodigoProducto = new JTextField();
        panelForm.add(txtCodigoProducto);
        
        panelForm.add(new JLabel("Nombre:"));
        txtNombreProducto = new JTextField();
        panelForm.add(txtNombreProducto);
        
        panelForm.add(new JLabel("Categoría:"));
        cmbCategoriaProducto = new JComboBox<>(new String[]{"Tecnologia", "Alimento", "General"});
        panelForm.add(cmbCategoriaProducto);
        
        panelForm.add(new JLabel("Precio:"));
        txtPrecioProducto = new JTextField();
        panelForm.add(txtPrecioProducto);
        
        panelForm.add(new JLabel("Atributo Específico:"));
        txtAtributoProducto = new JTextField();
        panelForm.add(txtAtributoProducto);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnCrearProducto = new JButton("Crear");
        btnCrearProducto.setBackground(new Color(0, 123, 255));
        btnCrearProducto.setForeground(Color.WHITE);
        panelBotones.add(btnCrearProducto);
        
        btnActualizarProducto = new JButton("Actualizar");
        btnActualizarProducto.setBackground(new Color(255, 193, 7));
        btnActualizarProducto.setForeground(Color.BLACK);
        panelBotones.add(btnActualizarProducto);
        
        btnEliminarProducto = new JButton("Eliminar");
        btnEliminarProducto.setBackground(new Color(220, 53, 69));
        btnEliminarProducto.setForeground(Color.WHITE);
        panelBotones.add(btnEliminarProducto);
        
        btnCargarProductosCSV = new JButton("Cargar CSV");
        btnCargarProductosCSV.setBackground(new Color(40, 167, 69));
        btnCargarProductosCSV.setForeground(Color.WHITE);
        panelBotones.add(btnCargarProductosCSV);
        
        btnVerDetalle = new JButton("Ver Detalle");
        btnVerDetalle.setBackground(new Color(23, 162, 184));
        btnVerDetalle.setForeground(Color.WHITE);
        panelBotones.add(btnVerDetalle);
        
        panelForm.add(panelBotones);
        
        // Tabla
        String[] columnas = {"Código", "Nombre", "Categoría", "Stock", "Precio"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaProductos = new JTable(modelo);
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Lista de Productos"));
        
        panel.add(panelForm, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Getters
    public JTextField getTxtCodigoVendedor() { return txtCodigoVendedor; }
    public JTextField getTxtNombreVendedor() { return txtNombreVendedor; }
    public JPasswordField getTxtContrasenaVendedor() { return txtContrasenaVendedor; }
    public JComboBox<String> getCmbGeneroVendedor() { return cmbGeneroVendedor; }
    public JButton getBtnCrearVendedor() { return btnCrearVendedor; }
    public JButton getBtnActualizarVendedor() { return btnActualizarVendedor; }
    public JButton getBtnEliminarVendedor() { return btnEliminarVendedor; }
    public JButton getBtnCargarVendedoresCSV() { return btnCargarVendedoresCSV; }
    public JTable getTablaVendedores() { return tablaVendedores; }
    
    public JTextField getTxtCodigoProducto() { return txtCodigoProducto; }
    public JTextField getTxtNombreProducto() { return txtNombreProducto; }
    public JTextField getTxtPrecioProducto() { return txtPrecioProducto; }
    public JTextField getTxtAtributoProducto() { return txtAtributoProducto; }
    public JComboBox<String> getCmbCategoriaProducto() { return cmbCategoriaProducto; }
    public JButton getBtnCrearProducto() { return btnCrearProducto; }
    public JButton getBtnActualizarProducto() { return btnActualizarProducto; }
    public JButton getBtnEliminarProducto() { return btnEliminarProducto; }
    public JButton getBtnCargarProductosCSV() { return btnCargarProductosCSV; }
    public JButton getBtnVerDetalle() { return btnVerDetalle; }
    public JTable getTablaProductos() { return tablaProductos; }
    
    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
    public JButton getBtnRefrescar() { return btnRefrescar; }
}