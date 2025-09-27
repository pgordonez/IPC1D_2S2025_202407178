/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package practica2;

import java.util.Scanner;

/**
 *
 * @author pablo
 */
public class Practica2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner entrada = new Scanner(System.in);
        
        
        int opcion = 0;
        do{
            MostrarMenu();
        
        }while(opcion !=10);
        
        
        
        
        
    }
    private static void MostrarMenu(){
            System.out.println("-------MENU PRINCIPAL-------");
            System.out.println("Seleccione una opcion:");
            System.out.println("1. Agregar Personajes");
            System.out.println("2. Modificar Personaje");
            System.out.println("3. Eliminar Personaje");
            System.out.println("4. Visualizar los personajes registrados");
            System.out.println("5. Simulacion de batallas");
            System.out.println("6. Ver historial de batallas");
            System.out.println("7. Buscar personajes por nombre");
            System.out.println("8. Guardar y cargar estado del sistema");
            System.out.println("9. Ver datos del estudiante");
            System.out.println("10. Salir");
        }
    
}
