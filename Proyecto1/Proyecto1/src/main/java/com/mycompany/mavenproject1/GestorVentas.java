/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author pablo
 */
public class GestorVentas {
    Venta[] ventas;         //Almacena los objetos de tipo Venta
    int cantidadVentas;     //Contador del numero actual de ventas
    Inventario inventario;      //Referencia al inventario para verificar stock y precios
    
    public GestorVentas(Inventario inventario){
        ventas = new Venta[100];        //Capacidad inicial de 100 ventas
        cantidadVentas = 0;         //Inicia contador en 0
        this.inventario = inventario;   //Guarda la referencia al inventario
        cargarVentas(); //Llama al metodo para cargar ventas previas desde un archivo
    }
    
    public void registrarVenta(Scanner entrada){
        System.out.print("Codigo del producto: ");
        String codigo = entrada.nextLine();     //Solicita el codigo del producto
        
        Producto producto = inventario.buscarPorCodigo(codigo);     //Busca el producto en el inventario
        if(producto == null){
            System.out.println("Producto no encontrado");       //Si no existe el producto, muestra error y termina
            return;
        }
        
        //Verifica que la cantidad sea un numero valido
        int cantidad = 0;
        try{
            System.out.print("Cantidad a vender: ");
            cantidad = Integer.parseInt(entrada.nextLine());    //Convierte el String a int
        }catch(NumberFormatException e){
            System.out.println("Error: La cantidad debe ser un número válido");
            return;
        }
        
        //Verifica que la cantidad sea un numero positiva
        if(cantidad <= 0){
            System.out.println("La cantidad debe ser positiva");
            return;
        }
        
        //verifica si hay stock disponible
        if(cantidad > producto.CantStock){
            System.out.println("Stock insuficiente. Disponible: " + producto.CantStock);
            return; 
        }
        
        double total = cantidad * producto.precio;      //Calcula el monto total de la venta
        producto.CantStock -= cantidad;     //Reduce el stock disponible del producto
        
        Venta nuevaVenta = new Venta(codigo, cantidad, total);      //Instancia un nuevo objeto de tipo Venta
        
        if(cantidadVentas >= ventas.length){        //Comprueba si el array esta lleno
            Venta[] nuevoArray = new Venta[ventas.length * 2];      //Crea un nuevo array con el doble de capacidad
            for (int i = 0; i < cantidadVentas; i++) {  
                nuevoArray[i] = ventas[i];      //Transfiere todas las ventas existentes al nuevo array
            }
            ventas = nuevoArray;        //El array original ahora apunta al nuevo array expandido
        }
        ventas[cantidadVentas] = nuevaVenta;        //Coloca la nueva venta en la siguiente posición disponible
        cantidadVentas++;       //Incrementa el contador de ventas
        
        guardarVenta(nuevaVenta);       //Invoca el método que guarda la venta en el archivo
        
        try(PrintWriter writer = new PrintWriter("inventario.txt")){
            for (int i = 0; i < inventario.obtenerCantidadProductos(); i++) {   //Recorre todos los productos del inventario
                Producto p = inventario.obtenerProducto(i);     //Devuelve el producto en la posicion i
                writer.println(p.nombre + "|" + p.categoria + "|" + p.precio + "|" + 
              p.CantStock + "|" + p.codigo);
            }
        }catch(IOException e){
            System.out.println("Error al guardar inventario: " + e.getMessage());   //Captura posibles errores de escritura del archivo
        }
    }
    private void guardarVenta(Venta venta){     //Solo accesible dentro de esta clase
        try(PrintWriter writer = new PrintWriter(new FileWriter("ventas.txt", true))){
            writer.println(venta.codigoProducto + "|" + venta.cantidadVendida + "|" + venta.total + "|" + venta.fechaHora);
        }catch(IOException e){      //Captura errores de entrada/salida
            System.out.println("Error al guardar venta: " + e.getMessage());
        }
    }
    private void cargarVentas(){
        try(Scanner fileScanner = new Scanner(new File("ventas.txt"))){
            while(fileScanner.hasNextLine()){       //Verifica si hay más líneas por leer
                String[] datos = fileScanner.nextLine().split("\\|");   //Lee una línea completa del archivo
                if(datos.length == 4){      //Verifica que tenga exactamente 4 campos
                    try{
                        Venta v = new Venta(datos[0], Integer.parseInt(datos[1]), 
                      Double.parseDouble(datos[2]), datos[3]);
                        
                        if(cantidadVentas >= ventas.length){
                            Venta[] nuevoArray = new Venta[ventas.length * 2];
                            for (int i = 0; i < cantidadVentas; i++) {
                                nuevoArray[i] = ventas[i];
                            }
                            ventas = nuevoArray;
                        }
                        ventas[cantidadVentas] = v;
                        cantidadVentas++;
                    }catch(NumberFormatException e){
                        System.out.println("Error en formato de datos de ventas");
                    }
                }
            }
        }catch(FileNotFoundException e){
            System.out.println("Archivo de ventas no encontrado. Se creará uno nuevo.");
        }
        }
    public Venta obtenerVenta(int indice){
            if(indice >= 0 && indice < cantidadVentas){
                return ventas[indice];
            }
            return null;
    }
    public int obtenerCantidadVentas(){
        return cantidadVentas;
    }
}
