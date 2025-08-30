/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mavenproject1;

import java.util.Scanner;

/**
 *
 * @author pablo
 */
public class Proyecto1 {
    static Scanner entrada = new Scanner(System.in);
    
    static int opcion = 0;
    public static void main(String[] args) {
        while(opcion!=8){
            System.out.println("-------- MENÚ PRINCIPAL --------");
            System.out.println("Seleccione una opcion: ");
            System.out.println("1. Agregar Producto");
            System.out.println("2. Buscar Producto");
            System.out.println("3. Eliminar Producto");
            System.out.println("4. Regitrar Venta");
            System.out.println("5. Generar Reportes");
            System.out.println("6. Ver datos del estudiante");
            System.out.println("7. Bitacora");
            System.out.println("9. Salir");
            opcion = entrada.nextInt();
            
        }
        
        
        
        
    }
}
