/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

/**
 *
 * @author pablo
 */
public class Producto {
    //Atributos
    String nombre;
    String categoria;
    double precio;
    int CantStock;
    String codigo;
    
    //Constructor
    public Producto(String nombre, String categoria, double precio, int CantStock, String codigo){
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.CantStock = CantStock;
        this.codigo = codigo;
    }
    
    //Mostrar informacion del producto
    public void mostrarInfo(){
        System.out.println("Codigo: "+ codigo);
        System.out.println("Nombre: "+ nombre);
        System.out.println("Categoria: "+ categoria);
        System.out.println("Precio: "+ precio);
        System.out.println("Stock: "+ CantStock);
        System.out.println("---------------------");
        
    }
}
