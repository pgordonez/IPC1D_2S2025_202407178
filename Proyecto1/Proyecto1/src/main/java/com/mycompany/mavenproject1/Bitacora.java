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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author pablo
 */
public class Bitacora {
    public static void registarAccion(String tipoAccion, String resultado, String usuario){
        try(PrintWriter writer = new PrintWriter(new FileWriter("bitacora.txt", true))){
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String fechaHora = formatter.format(new Date());
            
            writer.println(fechaHora + "|" + tipoAccion + "|" + resultado + "|" + usuario);
        }catch(IOException e){
            System.out.println("Error al registrar en bitácora: " + e.getMessage());
        }
    }
    
    public static void mostrarBitacora(){
        try (Scanner entrada = new Scanner(new File("bitacora.txt"))) {
            System.out.println("=== BITÁCORA ===");
            while(entrada.hasNextLine()){
                String[] datos = entrada.nextLine().split("\\|");
                if(datos.length == 4){
                    System.out.println("Fecha: " + datos[0]);
                    System.out.println("Acción: " + datos[1]);
                    System.out.println("Resultado: " + datos[2]);
                    System.out.println("Usuario: " + datos[3]);
                    System.out.println("-----------------------------");
                }
            }
        }catch(FileNotFoundException e){
            System.out.println("No hay registros en la bitácora");
        }   
    }
    public static void registarAccionPDF(String tipoReporte, String usuario) {
    try(PrintWriter writer = new PrintWriter(new FileWriter("bitacora.txt", true))){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String fechaHora = formatter.format(new Date());
        
        writer.println(fechaHora + "|Generar PDF|" + tipoReporte + "|" + usuario);
    }catch(IOException e){
        System.out.println("Error al registrar en bitácora: " + e.getMessage());
    }
}
    
}
