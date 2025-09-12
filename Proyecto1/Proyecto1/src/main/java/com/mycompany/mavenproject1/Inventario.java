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
        
        //Inicializa variables numericas con valores por defecto
        double precio = 0;
        int cantidad = 0;
        
        try{
            System.out.println("Precio: ");                     //Solicita al usuario que ingrese el precio
            precio = Double.parseDouble(entrada.nextLine());    //Convierte la cadena de texto en una tipo DOUBLE
            
            System.out.println("Cantidad en Stock: ");          //Solicita al usuario que ingrese la cantidad en stock
            cantidad = Integer.parseInt(entrada.nextLine());    //Convierte la cadena de texto en una tipo INT (entero)
        }catch(NumberFormatException e){
            System.out.println("Error: El precio y la cantidad deben ser números validos");
            return;
        }
        
        System.out.println("Codigo único: ");           //Solicita al usuario que ingrese el codigo del producto
        String codigo = entrada.nextLine();             //Lee el codigo ingresado
        
        //Validaciones
        if(precio<=0 || cantidad <0){       //Rechaza precios negativos o cero || rechaza CANTIDADES negativas (cero esta permitido)
            System.out.println("Error! \nEl precio y cantidad deben ser positivos.");
            return;             //Termina el metodo si hay algun error
        }       
        
        if(buscarPorCodigo(codigo) != null){        //Llama al metodo de busqueda, si retorna un producto (osea no NULL) significa que el codigo ya existe
            System.out.println("Error! El codigo es existente");        
            return;     //Termina el metodo si el codigo ya esta duplicado
        }
        
        if(codigo.trim().isEmpty()){        //Elimina espacios vacios y verifica
            System.out.println("Error! El codigo no puede estar vacio");
        }
        
        //CREACION DEL PRODUCTO
        Producto nuevo = new Producto(nombre, categoria, precio, cantidad, codigo); //Crea una nueva instancia de producto
        if(cantidadProductos >= productos.length){      //Verifica si el arreglo esta lleno
            Producto[] nuevoArray = new Producto[productos.length * 2];     //Crea un nuevo arreglo con el doble de capacidad
            for (int i = 0; i < cantidadProductos; i++) {
                nuevoArray[i] = productos[i];       //Transfiere todos los productos existentes al nuevo arreglo
            }
            productos = nuevoArray;     //Reasigna referencia
        }
        
        //Agregar producto al inventario
        productos[cantidadProductos] = nuevo;       //Agrega en la posicion actual, usa cantidadProductos como indice       
        cantidadProductos++;        //Aumenta en 1 la cantidad de productos
        
        //Guarda y confirma
        guardarEnArchivo();     //Llama al metodo para actualizar el archivo
        System.out.println("producto agregado exitosamente");       //Confirma que la operacion fue exitosa
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
        int opcion = 0;          //Variable para almacenar la eleccion del usuario, inicializada en 0
        
        try{
            opcion = Integer.parseInt(entrada.nextLine());  //Lee la cadena ingresada por el usuario y la convierte en entero INT
        }catch(NumberFormatException e){            //Capturala excepcion si la conversion falla
            System.out.println("Error! debe ingresar un número valido");        //Muestra un mensaje de error
            return;     //Termina el metodo inmediatamente
        }
        
        if(opcion < 1 || opcion > 3){       //Verifica si la opcion esta fuera del rango 1-3
            System.out.println("Opcion no valida"); //Si se cumple la condicion, muestra el mensaje
            return;     //Termina el metodo
        }
        
        System.out.print("Termino de busqueda: ");
        String termino = entrada.nextLine();            //Almacena el texto que el usuario quiere buscar                                
        boolean encontrado = false;         //Inicializa la variable en false, porque asume que no hay resultados
        
        for (int i = 0; i < cantidadProductos; i++) {       //Solo recorre productos existentes para no recorrer los 100 espacios
            boolean coincide = false;   //Inicializa en false, porque asume que no hay coincidencia
            
            switch(opcion){
                case 1: coincide = productos[i].codigo.equalsIgnoreCase(termino); //Busqueda por codigo
                break;    //Compara el codigo ingresado con el codigo a buscar | Ignora mayus/minus | coincidencia exacta
                case 2: coincide = productos[i].nombre.toLowerCase().contains(termino.toLowerCase()); //Busqueda por nombre
                break;    //Convierte ambos textos a minus | Busca si el termino esta contenido en cualquier parte
                case 3: coincide = productos[i].categoria.equalsIgnoreCase(termino); //Busqueda por categoria
                break;    //Compara la categoria ingresado con el codigo a buscar | Ignora mayus/minus | coincidencia exacta
            }
            
            if(coincide){       //Si hay coincidencias, devuelve la informacion del producto
                productos[i].mostrarInfo();     //Llama al metodo para mostrar sus datos
                encontrado = true;      //Indica que almenos hay un resultado
            }
        }
        if(!encontrado){        
            System.out.println("No se encontraron productos");      //Si no hay coincidencias, devuelve el mensaje
        }
    }
    
    //Metodo Eliminar Producto
    
    public void eliminarProducto(Scanner entrada){
        System.out.println("Codigo del producto a eliminar: ");     
        String codigo = entrada.nextLine();         //Solicita el codigo del producto a eliminar
        
        Producto producto = buscarPorCodigo(codigo);        //Llama al metodo que busca por codigo unico | almacena la referencia al producto
        if(producto == null){       //Verifica que el producto si existe (NULL significa que no existe)
            System.out.println("Producto no encontrado");
            return;
        }
        
        //CONFIRMACION DE ELIMINACION
        System.out.println("Seguro que quiere eliminar el producto "+ producto.nombre + "? S/N");   //Muestra el nombre del producto para confirmacion
        String confirmacion = entrada.nextLine();       //Solicita confirmacion
        
        if(confirmacion.equalsIgnoreCase("S")){     //Acepta "S", "s", "Sí", "sí"
            int indice = -1;    //Inicializa con un valor invalido
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
