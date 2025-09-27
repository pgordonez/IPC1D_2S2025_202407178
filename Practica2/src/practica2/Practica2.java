/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package practica2;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author pablo
 */
public class Practica2 extends JFrame{
    private GestorPersonajes gestor;
    private JTextArea textArea;
    
    public Practica2(){
        gestor = new GestorPersonajes(100);
        configurarVentana();
        crearComponentes();
        cargarDatosIniciales();
    }
    
    private void configurarVentana(){
        setTitle("ArenaUSAC - Sistema de Batallas");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void crearComponentes(){
        setLayout(new BorderLayout());
        
        //Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(2, 5, 5, 5));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] botones = {
            "Agregar", "Modificar", "Eliminar", "Ver Todos", "Buscar",
            "Batalla", "Historial", "Guardar", "Cargar", "Salir"
        };
        
        for(String texto : botones){
            JButton boton = new JButton(texto);
            boton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    manejarBoton(texto);
                }
            });
            panelBotones.add(boton);
        }
        
        //Área de texto
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        add(panelBotones, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void manejarBoton(String opcion){
        switch (opcion){
            case "Agregar":
                agregarPersonaje();
                break;
            case "Modificar":
                modificarPersonaje();
                break;
            case "Eliminar":
                eliminarPersonaje();
                break;
            case "Ver Todos":
                verPersonajes();
                break;
            case "Buscar":
                buscarPersonaje();
                break;
            case "Batalla":
                simularBatalla();
                break;
            case "Historial":
                verHistorial();
                break;
            case "Guardar":
                guardarDatos();
                break;
            case "Cargar":
                cargarDatos();
                break;
            case "Salir":
                System.exit(0);
                break;
        }
    }

    private void agregarPersonaje(){
        String nombre = JOptionPane.showInputDialog(this, "Nombre del personaje:");
        if (nombre == null || nombre.trim().isEmpty()) return;
        
        String arma = JOptionPane.showInputDialog(this, "Arma:");
        if (arma == null || arma.trim().isEmpty()) return;
        
        try{
            int hp = Integer.parseInt(JOptionPane.showInputDialog(this, "HP (100-500):"));
            int ataque = Integer.parseInt(JOptionPane.showInputDialog(this, "Ataque (10-100):"));
            int velocidad = Integer.parseInt(JOptionPane.showInputDialog(this, "Velocidad (1-10):"));
            int agilidad = Integer.parseInt(JOptionPane.showInputDialog(this, "Agilidad (1-10):"));
            int defensa = Integer.parseInt(JOptionPane.showInputDialog(this, "Defensa (1-50):"));
            
            if (gestor.agregarPersonaje(nombre, arma, hp, ataque, velocidad, agilidad, defensa)) {
                textArea.append("Personaje '" + nombre + "' agregado exitosamente\n");
            }else{
                JOptionPane.showMessageDialog(this, "Error: Datos inválidos o nombre duplicado");
            }
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Error: Ingrese valores numéricos válidos");
        }
    }
    private void modificarPersonaje(){
        String nombre = JOptionPane.showInputDialog(this, "Nombre del personaje a modificar:");
        if (nombre == null) return;
        
        Personaje p = gestor.buscarPorNombre(nombre);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Personaje no encontrado");
            return;
        }
        
        String arma = JOptionPane.showInputDialog(this, "Nueva arma:", p.getArma());
        if(arma == null) return;
        
        try {
            int hp = Integer.parseInt(JOptionPane.showInputDialog(this, "Nuevo HP (100-500):", p.getHp()));
            int ataque = Integer.parseInt(JOptionPane.showInputDialog(this, "Nuevo ataque (10-100):", p.getAtaque()));
            int velocidad = Integer.parseInt(JOptionPane.showInputDialog(this, "Nueva velocidad (1-10):", p.getVelocidad()));
            int agilidad = Integer.parseInt(JOptionPane.showInputDialog(this, "Nueva agilidad (1-10):", p.getAgilidad()));
            int defensa = Integer.parseInt(JOptionPane.showInputDialog(this, "Nueva defensa (1-50):", p.getDefensa()));
            
            if (gestor.modificarPersonaje(nombre, arma, hp, ataque, velocidad, agilidad, defensa)) {
                textArea.append("✏️ Personaje '" + nombre + "' modificado exitosamente\n");
            } else{
                JOptionPane.showMessageDialog(this, "Error: Datos inválidos");
            }
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this, "❌ Error: Ingrese valores numéricos válidos");
        }
    }
    
    private void eliminarPersonaje(){
        String nombre = JOptionPane.showInputDialog(this, "Nombre del personaje a eliminar:");
        if (nombre == null) return;
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar a " + nombre + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (gestor.eliminarPersonaje(nombre)) {
                textArea.append("Personaje '" + nombre + "' eliminado\n");
            }else{
                JOptionPane.showMessageDialog(this, "Personaje no encontrado");
            }
        }
    }
    
    private void verPersonajes(){
        textArea.setText("=== PERSONAJES REGISTRADOS ===\n\n");
        Personaje[] personajes = gestor.getPersonajes();
        int cantidad = gestor.getCantidad();
        
        if(cantidad == 0){
            textArea.append("No hay personajes registrados\n");
            return;
        }
        
        for (int i = 0; i < cantidad; i++) {
            Personaje p = personajes[i];
            textArea.append((i+1) + ". " + p.toString() + " | G: " + p.getBatallasGanadas() + " | P: " + p.getBatallasPerdidas() + "\n");
        }
    }
    
    private void buscarPersonaje(){
        String nombre = JOptionPane.showInputDialog(this, "Nombre del personaje a buscar:");
        if (nombre == null) return;
        
        Personaje p = gestor.buscarPorNombre(nombre);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "❌ Personaje no encontrado");
            return;
        }
        
        textArea.setText("=== INFORMACIÓN DE " + p.getNombre().toUpperCase() + " ===\n\n");
        textArea.append("ID: " + p.getId() + "\n");
        textArea.append("Nombre: " + p.getNombre() + "\n");
        textArea.append("Arma: " + p.getArma() + "\n");
        textArea.append("HP: " + p.getHp() + "\n");
        textArea.append("Ataque: " + p.getAtaque() + "\n");
        textArea.append("Velocidad: " + p.getVelocidad() + "\n");
        textArea.append("Agilidad: " + p.getAgilidad() + "\n");
        textArea.append("Defensa: " + p.getDefensa() + "\n");
        textArea.append("Batallas Ganadas: " + p.getBatallasGanadas() + "\n");
        textArea.append("Batallas Perdidas: " + p.getBatallasPerdidas() + "\n");
        textArea.append("Total Batallas: " + (p.getBatallasGanadas() + p.getBatallasPerdidas()) + "\n");
    }
    
    private void simularBatalla(){
        if (gestor.getCantidad() < 2) {
            JOptionPane.showMessageDialog(this, "Se necesitan al menos 2 personajes para una batalla");
            return;
        }
        
        String nombre1 = JOptionPane.showInputDialog(this, "Nombre del primer personaje:");
        if (nombre1 == null) return;
        
        String nombre2 = JOptionPane.showInputDialog(this, "Nombre del segundo personaje:");
        if (nombre2 == null) return;
        
        Personaje personaje1 = gestor.buscarPorNombre(nombre1);
        Personaje personaje2 = gestor.buscarPorNombre(nombre2);
        
        if (personaje1 == null || personaje2 == null) {
            JOptionPane.showMessageDialog(this, "Uno o ambos personajes no encontrados");
            return;
        }
        
        if (personaje1.getNombre().equalsIgnoreCase(personaje2.getNombre())) {
            JOptionPane.showMessageDialog(this, "Seleccione personajes diferentes");
            return;
        }
        
        textArea.setText(""); //Limpiar área
        
        //Ejecutar batalla en hilo separado
        new Thread(() -> {
            Batalla batalla = new Batalla(personaje1, personaje2, textArea);
            batalla.iniciar();
        }).start();
    }
    
    private void verHistorial(){
        textArea.setText("---HISTORIAL GENERAL---\n\n");
        Personaje[] personajes = gestor.getPersonajes();
        int cantidad = gestor.getCantidad();
        
        if(cantidad == 0){
            textArea.append("No hay personajes registrados");
            return;
        }
        
        int totalBatallas = 0;
        for (int i = 0; i < cantidad; i++) {
            Personaje p = personajes[i];
            int batallas = p.getBatallasGanadas() + p.getBatallasPerdidas();
            totalBatallas += batallas;
            
            textArea.append(p.getNombre() + ": " + p.getBatallasGanadas() + " ganadas, " + 
                          p.getBatallasPerdidas() + " perdidas (" + batallas + " total)\n");
        }
        
        textArea.append("\n=== TOTAL DE BATALLAS: " + totalBatallas + " ===\n");
    }
    
    private void guardarDatos(){
        GestorArchivos.guardarPersonajes(gestor, "personajes.dat");
        textArea.append("Datos guardados exitosamente\n");
    }

    private void cargarDatos() {
        gestor = new GestorPersonajes(100); // Resetear gestor
        GestorArchivos.cargarPersonajes(gestor, "personajes.dat");
        textArea.append("Datos cargados exitosamente\n");
        verPersonajes(); // Mostrar personajes cargados
    }

    private void cargarDatosIniciales() {
        // Cargar datos si el archivo existe
        File archivo = new File("personajes.dat");
        if (archivo.exists()) {
            cargarDatos();
        } else {
            textArea.setText("Bienvenido a ArenaUSAC\n\n");
            textArea.append("Para comenzar, agregue algunos personajes usando el botón 'Agregar'\n");
            textArea.append("Luego puede simular batallas con el botón 'Batalla'\n");
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(() -> {
            new Practica2().setVisible(true);
        });
    }
}
