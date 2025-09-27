/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practica2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author pablo
 */
public class GestorArchivos {
    public static void guardarPersonajes(GestorPersonajes gestor, String archivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            Personaje[] personajes = gestor.getPersonajes();
            int cantidad = gestor.getCantidad();
            
            for (int i = 0; i < cantidad; i++) {
                Personaje p = personajes[i];
                writer.println(p.getId() + "," + p.getNombre() + "," + p.getArma() + "," +
                             p.getHp() + "," + p.getAtaque() + "," + p.getVelocidad() + "," +
                             p.getAgilidad() + "," + p.getDefensa() + "," +
                             p.getBatallasGanadas() + "," + p.getBatallasPerdidas());
            }
        }catch (IOException e) {
            System.out.println("Error guardando personajes: " + e.getMessage());
        }
    }
    
    public static void cargarPersonajes(GestorPersonajes gestor, String archivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 10) {
                    try {
                        int id = Integer.parseInt(datos[0]);
                        String nombre = datos[1];
                        String arma = datos[2];
                        int hp = Integer.parseInt(datos[3]);
                        int ataque = Integer.parseInt(datos[4]);
                        int velocidad = Integer.parseInt(datos[5]);
                        int agilidad = Integer.parseInt(datos[6]);
                        int defensa = Integer.parseInt(datos[7]);
                        int ganadas = Integer.parseInt(datos[8]);
                        int perdidas = Integer.parseInt(datos[9]);
                        
                        //Agregar Personaje
                        if(gestor.agregarPersonaje(nombre, arma, hp, ataque, velocidad, agilidad, defensa)){
                            Personaje p = gestor.buscarPorNombre(nombre);
                            if(p != null){
                                p.setBatallasGanadas(ganadas);
                                p.setBatallasPerdidas(perdidas);
                            }
                        }
                    }catch(NumberFormatException e){
                        System.out.println("Error parseando línea: " + linea); 
                    }
                }
            }    
        }catch(IOException e){
            System.out.println("Error cargando personajes: " + e.getMessage());
        }
    }
}