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
        for (int i = 0; i < cantidadProductos; i++) {   //El ciclo se repetira hasta que el iterador recorra la cantidad de productos existentes
            if(productos[i].codigo.equals(codigo)){     //Verifica que el producto sea igual al del codigo ingresado por el usuario
                return productos[i];                    //Si si es econtrado devuelve el producto
            }
        }
        return null;                                    //Si el producto no es encontrado, devuelve null
    }
    public void buscarProductos(Scanner entrada){
        System.out.println("Buscar por: 1.Codigo, 2.Nombre, 3.Categoria");  //Muestra las opciones de busqueda
        int opcion = Integer.parseInt(entrada.nextLine());          //Lee la opcion del usuario
        
        System.out.println("Termino de busqueda: ");
        String termino = entrada.nextLine();            //Lee lo que el usuario quiere buscar                                
        
        boolean encontrado = false;
        
        for (int i = 0; i < cantidadProductos; i++) {
            boolean coincide = false;
            
            switch(opcion){
                case 1: coincide = productos[i].codigo.equalsIgnoreCase(termino); break;
                case 2: coincide = productos[i].nombre.toLowerCase().contains(termino.toLowerCase()); break;
                case 3: coincide = productos[i].categoria.equalsIgnoreCase(termino); break;
            }
            
            if(coincide){
                productos[i].mostrarInfo();
                encontrado = true;
            }
        }
        if(!encontrado){
            System.out.println("No se encontraron productos");
        }
    }
}
