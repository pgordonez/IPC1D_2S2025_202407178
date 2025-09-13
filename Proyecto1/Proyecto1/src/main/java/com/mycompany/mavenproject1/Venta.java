/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

import java.text.SimpleDateFormat;  //Para formatear fechas en texto legible
import java.util.Date;      //Sirve para obtener la fecha y hora actual del sistema

/**
 *
 * @author pablo
 */
public class Venta {
    //Atributos
    String codigoProducto; //Identificador del producto
    int cantidadVendida;    //Unidades vendidas
    String fechaHora;       //Momento en que se realizó la venta
    double total;           //monto total de la venta
    
    //Constructor para ventas nuevas
    public Venta(String codigoProducto, int cantidadVendida, double total){
        this.codigoProducto = codigoProducto;
        this.cantidadVendida = cantidadVendida;
        this.total = total;
        
        //Generacion de fecha/hora
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");       //Da formato al texto
        this.fechaHora = formatter.format(new Date());      //Obtiene la fecha actual del sistema
    }
    
    //Constructor para ventas existentes
    public Venta(String codigoProducto, int cantidadVendida, double total, String fechaHora){
        this.codigoProducto = codigoProducto;
        this.cantidadVendida = cantidadVendida;
        this.total = total;
        this.fechaHora = fechaHora;
    }
    
    //Metodo Para mostrar informacion
    public void mostrarInfo(){
        System.out.println("Producto: "+ codigoProducto);
        System.out.println("Cantidad: "+ cantidadVendida);
        System.out.println("Total: "+   total);
        System.out.println("Fecha: "+ fechaHora);
        System.out.println("----------------------");
    }
}
