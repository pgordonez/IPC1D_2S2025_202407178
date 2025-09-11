/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystemNotFoundException;
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
        cargarDesdeArchivo();           //Carga datos existentes al iniciar
    }

    //Metodo para agregar productos
    public void agregarProducto(Scanner entrada){
        //Solicitar datos al usuario
        System.out.println("Nombre del producto: ");
        String nombre = entrada.nextLine();
        
        System.out.println("Categoria: ");
        String categoria = entrada.nextLine();
        
        double precio = 0;
        int cantidad = 0;
        
        try{
            System.out.println("Precio: ");
            precio = Double.parseDouble(entrada.nextLine());
            
            System.out.println("Cantidad en Stock: ");
            cantidad = Integer.parseInt(entrada.nextLine());
        }catch(NumberFormatException e){
            System.out.println("Error: El precio y la cantidad deben ser números validos");
            return;
        }
        
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
        
        if(codigo.trim().isEmpty()){
            System.out.println("Error! El codigo no puede estar vacio");
        }
        
        Producto nuevo = new Producto(nombre, categoria, precio, cantidad, codigo);
        if(cantidadProductos >= productos.length){
            Producto[] nuevoArray = new Producto[productos.length * 2];
            for (int i = 0; i < cantidadProductos; i++) {
                nuevoArray[i] = productos[i];
            }
            productos = nuevoArray;
        }
        
        productos[cantidadProductos] = nuevo;
        cantidadProductos++;
        
        guardarEnArchivo();
        System.out.println("producto agregado exitosamente");
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
        int opcion = 0;          //Lee la opcion del usuario
        
        try{
            opcion = Integer.parseInt(entrada.nextLine());
        }catch(NumberFormatException e){
            System.out.println("Error! debe ingresar un número valido");
            return;
        }
        
        if(opcion < 1 || opcion > 3){
            System.out.println("Opcion no valida");
            return;
        }
        
        System.out.println("Termino de busqueda: ");
        String termino = entrada.nextLine();            //Lee lo que el usuario quiere buscar                                
        
        boolean encontrado = false;
        
        for (int i = 0; i < cantidadProductos; i++) {
            boolean coincide = false;
            
            switch(opcion){
                case 1: coincide = productos[i].codigo.equalsIgnoreCase(termino); 
                break;    //Compara el codigo ingresado con el codigo a buscar
                case 2: coincide = productos[i].nombre.toLowerCase().contains(termino.toLowerCase()); 
                break;    //Busqueda parcial  
                case 3: coincide = productos[i].categoria.equalsIgnoreCase(termino); 
                break;    //Busqueda exacta
            }
            
            if(coincide){       //Si hay coincidencias, devuelve la informacion del producto
                productos[i].mostrarInfo();
                encontrado = true;
            }
        }
        if(!encontrado){
            System.out.println("No se encontraron productos");      //Si no hay coincidencias, devuelve el mensaje
        }
    }
    
    //Metodo Eliminar Producto
    
    public void eliminarProducto(Scanner entrada){
        System.out.println("Codigo del producto a eliminar: ");
        String codigo = entrada.nextLine();
        
        Producto producto = buscarPorCodigo(codigo);
        if(producto == null){
            System.out.println("Producto no encontrado");
            return;
        }
        System.out.println("Seguro que quiere eliminar el producto "+ producto.nombre + "? S/N");
        String confirmacion = entrada.nextLine();
        
        if(confirmacion.equalsIgnoreCase("S")){
            int indice = -1;
            for (int i = 0; i < cantidadProductos; i++) {
                if(productos[i].codigo.equals(codigo)){
                    indice = 1;
                    break;
                }
            }
            if(indice != -1){
                for (int i = 0; i < cantidadProductos; i++) {
                    productos[i] = productos[i + 1];
                }
                cantidadProductos--;
                productos[cantidadProductos] = null;
                
                guardarEnArchivo();
                System.out.println("Producto eliminado exitosamente");
            }
        }else{
            System.out.println("Eliminacion cancelada");
        }
    }
    
    //Metodos para guardar y cargar desde archivo
    private void guardarEnArchivo(){
        try(PrintWriter writer = new PrintWriter("inventario.txt")){    //Se define una clase para escribir texto en archivos
            for (int i = 0; i < cantidadProductos; i++) {       //Recorre solo los productos existentes
                Producto p = productos[i];                  //Accede al producto en la posicion actual
                writer.println(p.nombre + " | " + p.categoria + " | " + p.precio + " | " + p.CantStock + " | " + p.codigo);
            }
        }catch(IOException e){
            System.out.println("Error al guardar inventario: " + e.getMessage());
        }
    }
    
    private void cargarDesdeArchivo(){      //Solo accesible dentro de la misma clase "inventario", "void" no retorna valor solo carga datos
        try(Scanner fileScanner = new Scanner(new File("inventario.txt"))){         //Crea un objeto file que representa el archivo
            while(fileScanner.hasNextLine()){       //El ciclo se repite hasta que lea todas las lineas en el archivo
                String[] datos = fileScanner.nextLine().split("\\|");   //Lee una linea completa del archivo
                if(datos.length == 5){      //Verifica que la linea tenga exactamente 5 partes
                    try{
                        Producto p = new Producto(datos[0], datos[1], Double.parseDouble(datos[2]), Integer.parseInt(datos[3]), datos[4]);
                        
                        if(cantidadProductos >= productos.length){
                            Producto[] nuevoArray = new Producto[productos.length * 2];
                            for (int i = 0; i < cantidadProductos; i++) {
                                nuevoArray[i] = productos[i];
                            }
                            productos = nuevoArray;
                        }
                        productos[cantidadProductos] = p;
                        cantidadProductos++;
                    }catch(NumberFormatException e){
                        System.out.println("Error en formato de datos del archivo");
                    }
                }
            }
        }catch(FileNotFoundException e ){
        System.out.println("Archivo de Inventario no encontrado. Se creará uno nuevo.");
        }
    }
    //Metodo para acceder a los datos
    public Producto obtenerProducto(int indice){
        if(indice >= 0 && indice < cantidadProductos){
            return productos[indice];
        }
        return null;
    }
    public int obtenerCantidadProductos(){
        return cantidadProductos;
    }
}
