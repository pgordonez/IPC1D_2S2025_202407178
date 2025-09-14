/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mavenproject1;

import java.util.Scanner;
    
/**1
 * 
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
                    inventario.buscarProductos(entrada);
                    Bitacora.registarAccion("Buscar Producto", "Êxito", "Usuario");
                    break;
                case 3:
                    inventario.eliminarProducto(entrada);
                    Bitacora.registarAccion("Eliminar Producto", "Éxito", "Usuario");
                    break;
                case 4:
                    gestorVentas.registrarVenta(entrada);
                    Bitacora.registarAccion("Registrar Venta", "Éxito", "Usuario");
                    break;
                case 5:
                    System.out.println("1. reporte de Stock");
                    System.out.println("2. reporte de ventas");
                    System.out.println("Seleccione una opcion: ");
                    
                    try{
                        int subOpcion = Integer.parseInt(entrada.nextLine());
                        
                        if(subOpcion == 1){
                            GeneradorReportes.generarReportesStock(inventario);
                        } else if(subOpcion == 2){
                            GeneradorReportes.generarReporteVentas(gestorVentas);
                        }else{
                            System.out.println("Ingrese una opcion valida");
                        }
                        Bitacora.registarAccion("Generar reporte", "Éxito", "Usuario");
                    }catch(NumberFormatException e){
                        System.out.println("Error! Debe ingresar un numero valido");
                    }
                    break;
                case 6:
                    System.out.println("=== DATOS DEL ESTUDIANTE ===");
                    System.out.println("Nombre: Pablo Gabriel Ordoñez Escobar");
                    System.out.println("Carné: 202407178");
                    System.out.println("Curso: Introducción a la Programación y Computación 1");
                    System.out.println("Sección: D");
                    System.out.println("Carrera: Ingenieria en Ciencias y Sistemas");
                    System.out.println("Universidad San Carlos de Guatemala");
                    break;
                case 7:
                    Bitacora.mostrarBitacora();
                    Bitacora.registarAccion("Ver Bitacora", "Éxito", "Usuario");
                    break;
                case 8:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    if(opcion != 0){
                        System.out.println("Opción no válida");
                    }
            }
            if(opcion !=8){
                System.out.println("Presione Enter para continuar...");
                entrada.nextLine();
            }
        }while(opcion != 8);
        entrada.close();
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
