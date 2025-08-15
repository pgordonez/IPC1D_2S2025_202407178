/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.practica1;

import java.time.LocalDateTime;
import java.util.Scanner;

/**
 *
 * @author pablo
 */
public class Practica1 {
    static Scanner entrada = new Scanner(System.in);
    
    //Constantes
    static final int max_personajes = 50;
    static final int max_habilidades = 5;
    static final int max_peleas = 100;
       
    //Info de los personajes
    static int[] id = new int[max_personajes];
    static String[] nombres = new String[max_personajes];
    static String[] armas = new String[max_personajes];
    static String[][] habilidades = new String[max_personajes][max_habilidades];
    static int[] nhabilidades = new int[max_habilidades];
    static int[] nivel = new int[max_personajes];
    static int npersonajes = 0;
    static int nextid = 1;
       
    //Datos de las peleas
    static int[] idpeleadorA = new int[max_peleas];
    static int[] idpeleadorB = new int[max_peleas];
    static LocalDateTime[] TiempoPelea = new LocalDateTime[max_personajes];
    static int npeleas = 0;
       
       

    public static void main(String[] args) {
        while(true){
            System.out.println("******* MENU PRINCIPAL *******");
            System.out.println("1. Agregar Personaje");
            System.out.println("2. Modificar Personaje");
            System.out.println("3. Eliminar Personaje");
            System.out.println("4. Ver Datos de un Personaje");
            System.out.println("5. Ver Listado de Personajes");
            System.out.println("6. Realizar Pelea entre Personajes");
            System.out.println("7. Ver Historial de Peleas");
            System.out.println("8. Ver Datos de Estudiante");
            System.out.println("9. Salir");
            
        
        
        
        
        
        }
        
       
       
       


       
       
       
       
    }
}
