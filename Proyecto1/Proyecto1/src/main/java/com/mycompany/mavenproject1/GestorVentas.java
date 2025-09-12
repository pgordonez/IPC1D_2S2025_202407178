/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

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
        
        int cantidad = 0;
        try{
            System.out.print("Cantidad a vender: ");
            cantidad = Integer.parseInt(entrada.nextLine());
        }catch(NumberFormatException e){
            System.out.println("Error: La cantidad debe ser un número válido");
            return;
        }
    }
}
