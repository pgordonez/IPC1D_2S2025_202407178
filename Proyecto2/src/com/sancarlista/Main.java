package com.sancarlista;

import com.sancarlista.view.LoginView;
import com.sancarlista.controller.SistemaController;
import com.sancarlista.model.Sistema;
import javax.swing.SwingUtilities;

/**
 * Clase principal del sistema Sancarlista Shop
 * Inicializa el sistema y lanza la interfaz gráfica
 */
public class Main {
    public static void main(String[] args) {
        // Cargar datos serializados al iniciar
        Sistema sistema = Sistema.getInstancia();
        sistema.cargarDatos();
        
        // Iniciar hilos de monitoreo
        sistema.iniciarHilos();
        
        // Lanzar interfaz gráfica en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            SistemaController controller = new SistemaController(loginView);
            loginView.setVisible(true);
        });
        
        // Hook para guardar datos al cerrar
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            sistema.detenerHilos();
            sistema.guardarDatos();
            System.out.println("Sistema cerrado correctamente. Datos guardados.");
        }));
    }
}