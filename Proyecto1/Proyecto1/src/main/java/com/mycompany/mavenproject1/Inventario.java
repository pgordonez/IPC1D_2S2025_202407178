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
public class Inventario {
    //Almacenamiento de productos
    Producto[] productos;
    int cantidadProductos;
    
    public Inventario(){
        productos = new Producto[100];  //Tamaño inicial
        cantidadProductos = 0;
        cargarDesdeArchivo();           //Carga datos al iniciar
    }

    //Metodo para agregar productos
    public void agregarProducto(Scanner entrada){
        //Solicitar datos al usuario
        System.out.println("Nombre del producto: ");
        String nombre = entrada.nextLine();
        
        System.out.println("Categoria: ");
        String categoria = entrada.nextLine();
        
        System.out.println("Precio: ");
        int precio = entrada.nextInt();
        
        System.out.println("Cantidad en Stock: ");
        int cantidad = entrada.nextInt();
        
        System.out.println("Codigo único: ");
        String codigo = entrada.nextLine();
        
        //Validaciones
        if(precio<=0 || cantidad <0){
            System.out.println("Error! \nEl precio y cantidad deben ser positivos.");
            return;
        }       
        
        if(buscarPorCodigo(codigo) != null){
            System.out.println("Error! El codigo es existente");
            return;
        }
        
    }
    
    //Metodos de busqueda
    public Producto buscarPorCodigo(String codigo){
        for (int i = 0; i < cantidadProductos; i++) {
            if(productos[i].codigo.equals(codigo)){
                return productos[i];
            }
        }
        return null;
    }
    public void buscarProductos(Scanner entrada){
        System.out.println("Buscar por: 1.Codigo, 2.Nombre, 3.Categoria");
        int opcion = Integer.parseInt(entrada.nextLine());
    }
}
