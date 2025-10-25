package com.sancarlista.view;

import javax.swing.*;
import java.awt.*;

/**
 * Vista de inicio de sesión
 */
public class LoginView extends JFrame {
    
    private JTextField txtCodigo;
    private JPasswordField txtContrasena;
    private JButton btnLogin;
    private JButton btnSalir;
    
    public LoginView() {
        configurarVentana();
        inicializarComponentes();
    }
    
    private void configurarVentana() {
        setTitle("Sancarlista Shop - Inicio de Sesión");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridBagLayout());
        panelPrincipal.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Título
        JLabel lblTitulo = new JLabel("SANCARLISTA SHOP");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(0, 51, 153));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelPrincipal.add(lblTitulo, gbc);
        
        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Sistema de Gestión de Tienda");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 1;
        panelPrincipal.add(lblSubtitulo, gbc);
        
        // Código
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setFont(new Font("Arial", Font.PLAIN, 14));
        panelPrincipal.add(lblCodigo, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        txtCodigo = new JTextField(15);
        txtCodigo.setFont(new Font("Arial", Font.PLAIN, 14));
        panelPrincipal.add(txtCodigo, gbc);
        
        // Contraseña
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        panelPrincipal.add(lblContrasena, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        txtContrasena = new JPasswordField(15);
        txtContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        panelPrincipal.add(txtContrasena, gbc);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.setBackground(new Color(240, 240, 240));
        
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(0, 123, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelBotones.add(btnLogin);
        
        btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalir.setBackground(new Color(220, 53, 69));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelBotones.add(btnSalir);
        
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelPrincipal.add(panelBotones, gbc);
        
        // Info
        JLabel lblInfo = new JLabel("Usuario admin: admin / IPC1A");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 10));
        lblInfo.setForeground(Color.GRAY);
        gbc.gridy = 5;
        panelPrincipal.add(lblInfo, gbc);
        
        add(panelPrincipal);
    }
    
    // Getters
    public JTextField getTxtCodigo() {
        return txtCodigo;
    }
    
    public JPasswordField getTxtContrasena() {
        return txtContrasena;
    }
    
    public JButton getBtnLogin() {
        return btnLogin;
    }
    
    public JButton getBtnSalir() {
        return btnSalir;
    }
}