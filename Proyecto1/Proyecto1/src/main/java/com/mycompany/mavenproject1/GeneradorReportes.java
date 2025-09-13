/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author pablo
 */
public class GeneradorReportes {
    public static void generarReportesStock(Inventario inventario){
        String fecha = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
        String nombreArchivo = fecha + "_Stock.txt";
        
        try (PrintWriter writer = new PrintWriter(nombreArchivo)) {
            writer.println("=== REPORTE DE STOCK ===");
            writer.println("Fecha: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            writer.println("==========================================");
            writer.println();
            
            for (int i = 0; i < inventario.obtenerCantidadProductos(); i++) {
                Producto p = inventario.obtenerProducto(i);
                if(p != null){
                    writer.println("Producto: " + p.nombre);
                    writer.println("Código: " + p.codigo);
                    writer.println("Categoría: " + p.categoria);
                    writer.println("Precio: Q" + p.precio);
                    writer.println("Stock: " + p.CantStock);
                    writer.println("------------------------------------------");
                }
            }
            System.out.println("Reporte de stock generado: " + nombreArchivo);
        }catch(IOException e){
            System.out.println("Error al generar reporte: " + e.getMessage());
        }
    }
    
    public static void generarReporteVentas(GestorVentas gestorVentas) {
        String fecha = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
        String nombreArchivo = fecha + "_Venta.txt";
        
        try(PrintWriter writer = new PrintWriter(nombreArchivo)){
            writer.println("=== REPORTE DE VENTAS ===");
            writer.println("Fecha: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            writer.println("==========================================");
            writer.println();
            
            double totalGeneral = 0;
            for (int i = 0; i < gestorVentas.obtenerCantidadVentas(); i++) {
                Venta v = gestorVentas.obtenerVenta(i);
                if (v != null) {
                    writer.println("Producto: " + v.codigoProducto);
                    writer.println("Cantidad: " + v.cantidadVendida);
                    writer.println("Total: Q" + v.total);
                    writer.println("Fecha: " + v.fechaHora);
                    writer.println("------------------------------------------");
                    
                    totalGeneral += v.total;
                }
            }
            writer.println();
            writer.println("TOTAL GENERAL: Q" + totalGeneral);
            
            System.out.println("Reporte de ventas generado: " + nombreArchivo);
        }catch (IOException e){
            System.out.println("Error al generar reporte: " + e.getMessage());
        }
    }
}
