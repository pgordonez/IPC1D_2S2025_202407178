package com.mycompany.mavenproject1;

import com.itextpdf.html2pdf.HtmlConverter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneradorReportes {
    
    public static void generarReportesStock(Inventario inventario) {
        String fecha = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
        String nombreArchivo = fecha + "_Stock.pdf";
        
        try {
            // Generar HTML content
            String htmlContent = generarHTMLStock(inventario);
            
            // Convertir HTML a PDF
            HtmlConverter.convertToPdf(htmlContent, new FileOutputStream(nombreArchivo));
            
            System.out.println("Reporte de stock generado: " + nombreArchivo);
            
            Bitacora.registarAccionPDF("Reporte de Stock", "Usuario");
            
        } catch (Exception e) {
            System.out.println("Error al generar reporte PDF: " + e.getMessage());
            Bitacora.registarAccion("Generar PDF", "Error: " + e.getMessage(), "Usuario");
        }
    }
    
    private static String generarHTMLStock(Inventario inventario) {
        StringWriter writer = new StringWriter();
        
        writer.write("<!DOCTYPE html>");
        writer.write("<html>");
        writer.write("<head>");
        writer.write("    <meta charset='UTF-8'>");
        writer.write("    <title>Reporte de Stock</title>");
        writer.write("    <style>");
        writer.write("        body { font-family: Arial, sans-serif; margin: 40px; }");
        writer.write("        h1 { color: #2c3e50; text-align: center; }");
        writer.write("        .fecha { text-align: right; color: #7f8c8d; margin-bottom: 20px; }");
        writer.write("        table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        writer.write("        th { background-color: #34495e; color: white; padding: 10px; text-align: left; }");
        writer.write("        td { padding: 8px; border-bottom: 1px solid #ddd; }");
        writer.write("        tr:nth-child(even) { background-color: #f2f2f2; }");
        writer.write("        .total { font-weight: bold; text-align: right; margin-top: 20px; }");
        writer.write("    </style>");
        writer.write("</head>");
        writer.write("<body>");
        writer.write("    <h1>REPORTE DE STOCK</h1>");
        writer.write("    <div class='fecha'>Fecha: " + 
                    new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "</div>");
        
        writer.write("    <table>");
        writer.write("        <thead>");
        writer.write("            <tr>");
        writer.write("                <th>Producto</th>");
        writer.write("                <th>Código</th>");
        writer.write("                <th>Categoría</th>");
        writer.write("                <th>Precio</th>");
        writer.write("                <th>Stock</th>");
        writer.write("            </tr>");
        writer.write("        </thead>");
        writer.write("        <tbody>");
        
        int totalStock = 0;
        double valorTotal = 0;
        
        for (int i = 0; i < inventario.obtenerCantidadProductos(); i++) {
            Producto p = inventario.obtenerProducto(i);
            if (p != null) {
                writer.write("        <tr>");
                writer.write("            <td>" + escapeHtml(p.nombre) + "</td>");
                writer.write("            <td>" + escapeHtml(p.codigo) + "</td>");
                writer.write("            <td>" + escapeHtml(p.categoria) + "</td>");
                writer.write("            <td>Q" + String.format("%.2f", p.precio) + "</td>");
                writer.write("            <td>" + p.CantStock + "</td>");
                writer.write("        </tr>");
                
                totalStock += p.CantStock;
                valorTotal += (p.precio * p.CantStock);
            }
        }
        
        writer.write("        </tbody>");
        writer.write("    </table>");
        
        writer.write("    <div class='total'>");
        writer.write("        Total de productos: " + inventario.obtenerCantidadProductos() + "<br>");
        writer.write("        Total en stock: " + totalStock + " unidades<br>");
        writer.write("        Valor total del inventario: Q" + String.format("%.2f", valorTotal));
        writer.write("    </div>");
        
        writer.write("</body>");
        writer.write("</html>");
        
        return writer.toString();
    }
    
    public static void generarReporteVentas(GestorVentas gestorVentas) {
        String fecha = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
        String nombreArchivo = fecha + "_Ventas.pdf";
        
        try {
            // Generar HTML content
            String htmlContent = generarHTMLVentas(gestorVentas);
            
            // Convertir HTML a PDF
            HtmlConverter.convertToPdf(htmlContent, new FileOutputStream(nombreArchivo));
            
            System.out.println("Reporte de ventas generado: " + nombreArchivo);
            Bitacora.registarAccionPDF("Reporte de Ventas", "Usuario");
            
        } catch (Exception e) {
            System.out.println("Error al generar reporte PDF: " + e.getMessage());
            Bitacora.registarAccion("Generar PDF", "Error: " + e.getMessage(), "Usuario");
        }
    }
    
    private static String generarHTMLVentas(GestorVentas gestorVentas) {
        StringWriter writer = new StringWriter();
        
        writer.write("<!DOCTYPE html>");
        writer.write("<html>");
        writer.write("<head>");
        writer.write("    <meta charset='UTF-8'>");
        writer.write("    <title>Reporte de Ventas</title>");
        writer.write("    <style>");
        writer.write("        body { font-family: Arial, sans-serif; margin: 40px; }");
        writer.write("        h1 { color: #2c3e50; text-align: center; }");
        writer.write("        .fecha { text-align: right; color: #7f8c8d; margin-bottom: 20px; }");
        writer.write("        table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        writer.write("        th { background-color: #34495e; color: white; padding: 10px; text-align: left; }");
        writer.write("        td { padding: 8px; border-bottom: 1px solid #ddd; }");
        writer.write("        tr:nth-child(even) { background-color: #f2f2f2; }");
        writer.write("        .total { font-weight: bold; text-align: right; margin-top: 20px; font-size: 16px; }");
        writer.write("    </style>");
        writer.write("</head>");
        writer.write("<body>");
        writer.write("    <h1>REPORTE DE VENTAS</h1>");
        writer.write("    <div class='fecha'>Fecha: " + 
                    new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "</div>");
        
        writer.write("    <table>");
        writer.write("        <thead>");
        writer.write("            <tr>");
        writer.write("                <th>Producto</th>");
        writer.write("                <th>Cantidad</th>");
        writer.write("                <th>Total</th>");
        writer.write("                <th>Fecha</th>");
        writer.write("            </tr>");
        writer.write("        </thead>");
        writer.write("        <tbody>");
        
        double totalGeneral = 0;
        int totalVentas = 0;
        
        for (int i = 0; i < gestorVentas.obtenerCantidadVentas(); i++) {
            Venta v = gestorVentas.obtenerVenta(i);
            if (v != null) {
                writer.write("        <tr>");
                writer.write("            <td>" + escapeHtml(v.codigoProducto) + "</td>");
                writer.write("            <td>" + v.cantidadVendida + "</td>");
                writer.write("            <td>Q" + String.format("%.2f", v.total) + "</td>");
                writer.write("            <td>" + escapeHtml(v.fechaHora) + "</td>");
                writer.write("        </tr>");
                
                totalGeneral += v.total;
                totalVentas++;
            }
        }
        
        writer.write("        </tbody>");
        writer.write("    </table>");
        
        writer.write("    <div class='total'>");
        writer.write("        Total de ventas: " + totalVentas + "<br>");
        writer.write("        TOTAL GENERAL: Q" + String.format("%.2f", totalGeneral));
        writer.write("    </div>");
        
        writer.write("</body>");
        writer.write("</html>");
        
        return writer.toString();
    }
    
    // Método para escapar caracteres HTML especiales
    private static String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
}