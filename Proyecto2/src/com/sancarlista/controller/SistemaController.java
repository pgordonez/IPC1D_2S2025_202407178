package com.sancarlista.controller;

import com.sancarlista.model.*;
import com.sancarlista.view.*;
import javax.swing.JOptionPane;

/**
 * Controlador principal del sistema
 * Maneja la autenticación y navegación entre módulos
 */
public class SistemaController {
    private Sistema sistema;
    private LoginView loginView;
    
    public SistemaController(LoginView loginView) {
        this.sistema = Sistema.getInstancia();
        this.loginView = loginView;
        inicializarEventos();
    }
    
    private void inicializarEventos() {
        loginView.getBtnLogin().addActionListener(e -> iniciarSesion());
        loginView.getBtnSalir().addActionListener(e -> salirSistema());
    }
    
    private void iniciarSesion() {
        String codigo = loginView.getTxtCodigo().getText().trim();
        String contrasena = new String(loginView.getTxtContrasena().getPassword());
        
        if (codigo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(loginView, 
                "Por favor complete todos los campos", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Usuario usuario = sistema.iniciarSesion(codigo, contrasena);
        
        if (usuario != null) {
            loginView.dispose();
            abrirModuloSegunRol(usuario);
        } else {
            JOptionPane.showMessageDialog(loginView, 
                "Credenciales incorrectas", 
                "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void abrirModuloSegunRol(Usuario usuario) {
        String tipoUsuario = usuario.getTipoUsuario();
        
        switch (tipoUsuario) {
            case "ADMINISTRADOR":
                AdminView adminView = new AdminView();
                new AdminController(adminView);
                adminView.setVisible(true);
                break;
                
            case "VENDEDOR":
                VendedorView vendedorView = new VendedorView();
                new VendedorController(vendedorView);
                vendedorView.setVisible(true);
                break;
                
            case "CLIENTE":
                ClienteView clienteView = new ClienteView();
                new ClienteController(clienteView);
                clienteView.setVisible(true);
                break;
        }
    }
    
    private void salirSistema() {
        int opcion = JOptionPane.showConfirmDialog(loginView, 
            "¿Está seguro que desea salir?", 
            "Confirmar Salida", 
            JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            sistema.detenerHilos();
            sistema.guardarDatos();
            System.exit(0);
        }
    }
}