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
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        Inventario inventario = new Inventario();
        GestorVentas gestorVentas = new GestorVentas(inventario);
        
        int opcion;
        do{
            MostrarMenu();
            
            try{
                opcion = Integer.parseInt(entrada.nextLine());
            }catch(NumberFormatException e){
                System.out.println("Error: Debe ingresar un número válido");
                opcion = 0;
            }
            
            switch(opcion){
                case 1:
                    inventario.agregarProducto(entrada);
                    Bitacora.registarAccion("Agregar Producto", "Éxito", "Usuario");
                    break;
                case 2:
                
            }
           
        }
        
        private static void MostrarMenu(){
            System.out.println("-------- MENÚ PRINCIPAL --------");
            System.out.println("Seleccione una opcion: ");
            System.out.println("1. Agregar Producto");
            System.out.println("2. Buscar Producto");
            System.out.println("3. Eliminar Producto");
            System.out.println("4. Regitrar Venta");
            System.out.println("5. Generar Reportes");
            System.out.println("6. Ver datos del estudiante");
            System.out.println("7. Bitacora");
            System.out.println("8. Salir");
            System.out.println("Seleccione una opcion: ");
        }
        
        
        
    }
}
