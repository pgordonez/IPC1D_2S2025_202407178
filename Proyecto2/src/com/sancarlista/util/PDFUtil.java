package com.sancarlista.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import com.sancarlista.model.*;

/**
 * Utilidad para generación de reportes en PDF usando iText
 * 
 * IMPORTANTE: Requiere librería iText 5.x
 * Descargar de: https://repo1.maven.org/maven2/com/itextpdf/itextpdf/5.5.13/
 * 
 * Genera 7 tipos de reportes:
 * 1. Productos Más Vendidos
 * 2. Productos Menos Vendidos
 * 3. Inventario con Stock Crítico
 * 4. Ventas por Vendedor
 * 5. Clientes Activos
 * 6. Reporte Financiero
 * 7. Productos por Caducar
 */
public class PDFUtil {
    
    private static final Font TITULO = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font SUBTITULO = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
    private static final Font NORMAL = new Font(Font.FontFamily.HELVETICA, 12);
    
    /**
     * Genera el nombre del archivo con formato de fecha
     */
    private static String generarNombreArchivo(String tipoReporte) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        return sdf.format(new Date()) + "_" + tipoReporte + ".pdf";
    }
    
    /**
     * Reporte de Productos Más Vendidos (Top 5)
     */
    public static boolean generarReporteMasVendidos(Producto[] productos) {
        if (productos == null || productos.length == 0) {
            System.err.println("No hay productos para generar el reporte");
            return false;
        }
        
        Document documento = new Document(PageSize.A4);
        
        try {
            String nombreArchivo = generarNombreArchivo("Productos_Mas_Vendidos");
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            
            documento.open();
            
            // Título
            Paragraph titulo = new Paragraph("REPORTE - PRODUCTOS MÁS VENDIDOS", TITULO);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);
            
            documento.add(new Paragraph("\n"));
            
            // Ordenar productos por cantidad vendida (descendente)
            Producto[] productosOrdenados = copiarArray(productos);
            ordenarProductosPorVentas(productosOrdenados, false);
            
            // Crear tabla
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            
            // Headers
            agregarCeldaHeader(tabla, "Producto");
            agregarCeldaHeader(tabla, "Cantidad Vendida");
            agregarCeldaHeader(tabla, "Categoría");
            agregarCeldaHeader(tabla, "Ingresos");
            
            // Top 5 productos
            int limite = Math.min(5, productosOrdenados.length);
            for (int i = 0; i < limite; i++) {
                if (productosOrdenados[i] != null && productosOrdenados[i].getCantidadVendida() > 0) {
                    tabla.addCell(productosOrdenados[i].getNombre() != null ? 
                        productosOrdenados[i].getNombre() : "N/A");
                    tabla.addCell(String.valueOf(productosOrdenados[i].getCantidadVendida()));
                    tabla.addCell(productosOrdenados[i].getCategoria() != null ? 
                        productosOrdenados[i].getCategoria() : "N/A");
                    double ingresos = productosOrdenados[i].getCantidadVendida() * 
                                     productosOrdenados[i].getPrecio();
                    tabla.addCell(String.format("Q%.2f", ingresos));
                }
            }
            
            documento.add(tabla);
            
            documento.add(new Paragraph("\nFecha de generación: " + 
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), NORMAL));
            
            System.out.println("Reporte generado: " + nombreArchivo);
            return true;
            
        } catch (DocumentException | IOException e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            return false;
        } finally {
            if (documento.isOpen()) {
                documento.close();
            }
        }
    }
    
    /**
     * Reporte de Productos Menos Vendidos (Top 5)
     */
    public static boolean generarReporteMenosVendidos(Producto[] productos) {
        if (productos == null || productos.length == 0) {
            System.err.println("No hay productos para generar el reporte");
            return false;
        }
        
        Document documento = new Document(PageSize.A4);
        
        try {
            String nombreArchivo = generarNombreArchivo("Productos_Menos_Vendidos");
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            
            documento.open();
            
            Paragraph titulo = new Paragraph("REPORTE - PRODUCTOS MENOS VENDIDOS", TITULO);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);
            
            documento.add(new Paragraph("\n"));
            
            // Ordenar productos por cantidad vendida (ascendente)
            Producto[] productosOrdenados = copiarArray(productos);
            ordenarProductosPorVentas(productosOrdenados, true);
            
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            
            agregarCeldaHeader(tabla, "Producto");
            agregarCeldaHeader(tabla, "Cantidad Vendida");
            agregarCeldaHeader(tabla, "Stock Actual");
            agregarCeldaHeader(tabla, "Recomendación");
            
            int limite = Math.min(5, productosOrdenados.length);
            for (int i = 0; i < limite; i++) {
                if (productosOrdenados[i] != null) {
                    tabla.addCell(productosOrdenados[i].getNombre() != null ? 
                        productosOrdenados[i].getNombre() : "N/A");
                    tabla.addCell(String.valueOf(productosOrdenados[i].getCantidadVendida()));
                    tabla.addCell(String.valueOf(productosOrdenados[i].getStock()));
                    
                    String recomendacion = productosOrdenados[i].getStock() > 20 ? 
                        "Aplicar descuento" : "Monitorear ventas";
                    tabla.addCell(recomendacion);
                }
            }
            
            documento.add(tabla);
            
            documento.add(new Paragraph("\nFecha de generación: " + 
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), NORMAL));
            
            System.out.println("Reporte generado: " + nombreArchivo);
            return true;
            
        } catch (DocumentException | IOException e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            return false;
        } finally {
            if (documento.isOpen()) {
                documento.close();
            }
        }
    }
    
    /**
     * Reporte de Inventario con Stock Crítico
     */
    public static boolean generarReporteInventario(Producto[] productos) {
        if (productos == null || productos.length == 0) {
            System.err.println("No hay productos para generar el reporte");
            return false;
        }
        
        Document documento = new Document(PageSize.A4);
        
        try {
            String nombreArchivo = generarNombreArchivo("Inventario_Stock_Critico");
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            
            documento.open();
            
            Paragraph titulo = new Paragraph("REPORTE - INVENTARIO (STOCK CRÍTICO)", TITULO);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);
            
            documento.add(new Paragraph("\n"));
            
            PdfPTable tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            
            agregarCeldaHeader(tabla, "Código");
            agregarCeldaHeader(tabla, "Producto");
            agregarCeldaHeader(tabla, "Categoría");
            agregarCeldaHeader(tabla, "Stock");
            agregarCeldaHeader(tabla, "Estado");
            
            int productosEncontrados = 0;
            for (Producto producto : productos) {
                if (producto != null && producto.getStock() < 20) {
                    tabla.addCell(producto.getCodigo() != null ? producto.getCodigo() : "N/A");
                    tabla.addCell(producto.getNombre() != null ? producto.getNombre() : "N/A");
                    tabla.addCell(producto.getCategoria() != null ? producto.getCategoria() : "N/A");
                    tabla.addCell(String.valueOf(producto.getStock()));
                    
                    String estado = producto.getStock() < 10 ? "CRÍTICO" : "BAJO";
                    PdfPCell celda = new PdfPCell(new Phrase(estado));
                    celda.setBackgroundColor(producto.getStock() < 10 ? 
                        BaseColor.RED : BaseColor.YELLOW);
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabla.addCell(celda);
                    productosEncontrados++;
                }
            }
            
            if (productosEncontrados == 0) {
                documento.add(new Paragraph("No hay productos con stock crítico", NORMAL));
            } else {
                documento.add(tabla);
                documento.add(new Paragraph("\nSugerencia: Reabastecer productos en estado CRÍTICO", 
                    NORMAL));
            }
            
            documento.add(new Paragraph("Fecha de generación: " + 
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), NORMAL));
            
            System.out.println("Reporte generado: " + nombreArchivo);
            return true;
            
        } catch (DocumentException | IOException e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            return false;
        } finally {
            if (documento.isOpen()) {
                documento.close();
            }
        }
    }
    
    /**
     * Reporte de Ventas por Vendedor
     */
    public static boolean generarReporteVendedores(Usuario[] vendedores) {
        if (vendedores == null || vendedores.length == 0) {
            System.err.println("No hay vendedores para generar el reporte");
            return false;
        }
        
        Document documento = new Document(PageSize.A4);
        
        try {
            String nombreArchivo = generarNombreArchivo("Ventas_Por_Vendedor");
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            
            documento.open();
            
            Paragraph titulo = new Paragraph("REPORTE - VENTAS POR VENDEDOR", TITULO);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);
            
            documento.add(new Paragraph("\n"));
            
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            
            agregarCeldaHeader(tabla, "Código");
            agregarCeldaHeader(tabla, "Nombre");
            agregarCeldaHeader(tabla, "Pedidos Confirmados");
            agregarCeldaHeader(tabla, "Ranking");
            
            // Ordenar vendedores por ventas
            Usuario[] vendedoresOrdenados = copiarArray(vendedores);
            ordenarVendedoresPorVentas(vendedoresOrdenados);
            
            int ranking = 1;
            for (Usuario usuario : vendedoresOrdenados) {
                if (usuario instanceof Vendedor) {
                    Vendedor v = (Vendedor) usuario;
                    tabla.addCell(v.getCodigo() != null ? v.getCodigo() : "N/A");
                    tabla.addCell(v.getNombre() != null ? v.getNombre() : "N/A");
                    tabla.addCell(String.valueOf(v.getVentasConfirmadas()));
                    tabla.addCell(String.valueOf(ranking++));
                }
            }
            
            documento.add(tabla);
            
            documento.add(new Paragraph("\nFecha de generación: " + 
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), NORMAL));
            
            System.out.println("Reporte generado: " + nombreArchivo);
            return true;
            
        } catch (DocumentException | IOException e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            return false;
        } finally {
            if (documento.isOpen()) {
                documento.close();
            }
        }
    }
    
    /**
     * Reporte Financiero por Categoría
     */
    public static boolean generarReporteFinanciero(Producto[] productos) {
        if (productos == null || productos.length == 0) {
            System.err.println("No hay productos para generar el reporte");
            return false;
        }
        
        Document documento = new Document(PageSize.A4);
        
        try {
            String nombreArchivo = generarNombreArchivo("Reporte_Financiero");
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            
            documento.open();
            
            Paragraph titulo = new Paragraph("REPORTE FINANCIERO - INGRESOS POR CATEGORÍA", TITULO);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);
            
            documento.add(new Paragraph("\n"));
            
            // Calcular ingresos por categoría
            double ingresosTecnologia = 0;
            double ingresosAlimento = 0;
            double ingresosGeneral = 0;
            int cantTecnologia = 0;
            int cantAlimento = 0;
            int cantGeneral = 0;
            
            for (Producto p : productos) {
                if (p != null && p.getCategoria() != null) {
                    double ingresos = p.getCantidadVendida() * p.getPrecio();
                    
                    switch (p.getCategoria()) {
                        case "Tecnologia":
                            ingresosTecnologia += ingresos;
                            cantTecnologia += p.getCantidadVendida();
                            break;
                        case "Alimento":
                            ingresosAlimento += ingresos;
                            cantAlimento += p.getCantidadVendida();
                            break;
                        case "General":
                            ingresosGeneral += ingresos;
                            cantGeneral += p.getCantidadVendida();
                            break;
                    }
                }
            }
            
            double totalIngresos = ingresosTecnologia + ingresosAlimento + ingresosGeneral;
            
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            
            agregarCeldaHeader(tabla, "Categoría");
            agregarCeldaHeader(tabla, "Unidades Vendidas");
            agregarCeldaHeader(tabla, "Ingresos Totales");
            agregarCeldaHeader(tabla, "% Participación");
            
            // Tecnología
            tabla.addCell("Tecnología");
            tabla.addCell(String.valueOf(cantTecnologia));
            tabla.addCell(String.format("Q%.2f", ingresosTecnologia));
            double porcTec = totalIngresos > 0 ? (ingresosTecnologia / totalIngresos) * 100 : 0;
            tabla.addCell(String.format("%.2f%%", porcTec));
            
            // Alimento
            tabla.addCell("Alimento");
            tabla.addCell(String.valueOf(cantAlimento));
            tabla.addCell(String.format("Q%.2f", ingresosAlimento));
            double porcAli = totalIngresos > 0 ? (ingresosAlimento / totalIngresos) * 100 : 0;
            tabla.addCell(String.format("%.2f%%", porcAli));
            
            // General
            tabla.addCell("General");
            tabla.addCell(String.valueOf(cantGeneral));
            tabla.addCell(String.format("Q%.2f", ingresosGeneral));
            double porcGen = totalIngresos > 0 ? (ingresosGeneral / totalIngresos) * 100 : 0;
            tabla.addCell(String.format("%.2f%%", porcGen));
            
            documento.add(tabla);
            
            documento.add(new Paragraph("\n"));
            documento.add(new Paragraph("Total de Ingresos: Q" + 
                String.format("%.2f", totalIngresos), SUBTITULO));
            
            documento.add(new Paragraph("\nFecha de generación: " + 
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), NORMAL));
            
            System.out.println("Reporte generado: " + nombreArchivo);
            return true;
            
        } catch (DocumentException | IOException e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            return false;
        } finally {
            if (documento.isOpen()) {
                documento.close();
            }
        }
    }
    
    /**
     * Reporte de Clientes Activos
     */
    public static boolean generarReporteClientesActivos(Usuario[] clientes, Pedido[] pedidos) {
        if (clientes == null || clientes.length == 0) {
            System.err.println("No hay clientes para generar el reporte");
            return false;
        }
        
        if (pedidos == null) {
            pedidos = new Pedido[0]; // Array vacío si no hay pedidos
        }
        
        Document documento = new Document(PageSize.A4);
        
        try {
            String nombreArchivo = generarNombreArchivo("Clientes_Activos");
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            
            documento.open();
            
            Paragraph titulo = new Paragraph("REPORTE - CLIENTES ACTIVOS", TITULO);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);
            
            documento.add(new Paragraph("\n"));
            
            PdfPTable tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            
            agregarCeldaHeader(tabla, "Código");
            agregarCeldaHeader(tabla, "Nombre");
            agregarCeldaHeader(tabla, "Compras (30 días)");
            agregarCeldaHeader(tabla, "Monto Total");
            agregarCeldaHeader(tabla, "Clasificación");
            
            // Analizar cada cliente
            for (Usuario usuario : clientes) {
                if (usuario instanceof Cliente) {
                    Cliente cliente = (Cliente) usuario;
                    String codigo = cliente.getCodigo();
                    
                    if (codigo != null) {
                        int compras30dias = contarComprasCliente(codigo, pedidos, 30);
                        double montoTotal = calcularMontoCliente(codigo, pedidos);
                        
                        String clasificacion = "Nuevo";
                        if (compras30dias >= 10) {
                            clasificacion = "Frecuente";
                        } else if (compras30dias >= 3) {
                            clasificacion = "Ocasional";
                        }
                        
                        tabla.addCell(codigo);
                        tabla.addCell(cliente.getNombre() != null ? cliente.getNombre() : "N/A");
                        tabla.addCell(String.valueOf(compras30dias));
                        tabla.addCell(String.format("Q%.2f", montoTotal));
                        tabla.addCell(clasificacion);
                    }
                }
            }
            
            documento.add(tabla);
            
            documento.add(new Paragraph("\nFecha de generación: " + 
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), NORMAL));
            
            System.out.println("Reporte generado: " + nombreArchivo);
            return true;
            
        } catch (DocumentException | IOException e) {
            System.err.println("Error generando PDF Clientes Activos: " + e.getMessage());
            return false;
        } finally {
            if (documento.isOpen()) {
                documento.close();
            }
        }
    }
    
    /**
     * Reporte de Productos por Caducar (solo alimentos)
     */
    public static boolean generarReporteProductosPorCaducar(Producto[] productos) {
        if (productos == null || productos.length == 0) {
            System.err.println("No hay productos para generar el reporte");
            return false;
        }
        
        Document documento = new Document(PageSize.A4);
        
        try {
            String nombreArchivo = generarNombreArchivo("Productos_Por_Caducar");
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            
            documento.open();
            
            Paragraph titulo = new Paragraph("REPORTE - PRODUCTOS POR CADUCAR", TITULO);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);
            
            documento.add(new Paragraph("\n"));
            
            PdfPTable tabla = new PdfPTable(6);
            tabla.setWidthPercentage(100);
            
            agregarCeldaHeader(tabla, "Código");
            agregarCeldaHeader(tabla, "Producto");
            agregarCeldaHeader(tabla, "Fecha Caducidad");
            agregarCeldaHeader(tabla, "Días Restantes");
            agregarCeldaHeader(tabla, "Stock");
            agregarCeldaHeader(tabla, "Prioridad");
            
            int productosEncontrados = 0;
            // Filtrar solo productos alimenticios
            for (Producto producto : productos) {
                if (producto instanceof ProductoAlimento) {
                    ProductoAlimento alimento = (ProductoAlimento) producto;
                    String fechaCad = alimento.getFechaCaducidad();
                    
                    if (fechaCad != null) {
                        int diasRestantes = calcularDiasRestantes(fechaCad);
                        
                        // Solo mostrar productos que caducan en menos de 15 días
                        if (diasRestantes <= 15 && diasRestantes >= 0) {
                            tabla.addCell(alimento.getCodigo() != null ? alimento.getCodigo() : "N/A");
                            tabla.addCell(alimento.getNombre() != null ? alimento.getNombre() : "N/A");
                            tabla.addCell(fechaCad);
                            tabla.addCell(String.valueOf(diasRestantes));
                            tabla.addCell(String.valueOf(alimento.getStock()));
                            
                            String prioridad = diasRestantes <= 3 ? "CRÍTICO" : 
                                             diasRestantes <= 7 ? "URGENTE" : "ATENCIÓN";
                            
                            PdfPCell celdaPrioridad = new PdfPCell(new Phrase(prioridad));
                            celdaPrioridad.setHorizontalAlignment(Element.ALIGN_CENTER);
                            if (diasRestantes <= 3) {
                                celdaPrioridad.setBackgroundColor(BaseColor.RED);
                            } else if (diasRestantes <= 7) {
                                celdaPrioridad.setBackgroundColor(BaseColor.ORANGE);
                            } else {
                                celdaPrioridad.setBackgroundColor(BaseColor.YELLOW);
                            }
                            tabla.addCell(celdaPrioridad);
                            productosEncontrados++;
                        }
                    }
                }
            }
            
            if (productosEncontrados == 0) {
                documento.add(new Paragraph("No hay productos próximos a caducar", NORMAL));
            } else {
                documento.add(tabla);
                documento.add(new Paragraph("\nRecomendación: Aplicar descuentos urgentes en productos CRÍTICOS", 
                    new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.RED)));
            }
            
            documento.add(new Paragraph("Fecha de generación: " + 
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), NORMAL));
            
            System.out.println("Reporte generado: " + nombreArchivo);
            return true;
            
        } catch (DocumentException | IOException e) {
            System.err.println("Error generando PDF Productos por Caducar: " + e.getMessage());
            return false;
        } finally {
            if (documento.isOpen()) {
                documento.close();
            }
        }
    }
    
    // ===== MÉTODOS AUXILIARES =====
    
    /**
     * Agrega una celda de header a la tabla con formato
     */
    private static void agregarCeldaHeader(PdfPTable tabla, String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, SUBTITULO));
        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(5);
        tabla.addCell(celda);
    }
    
    /**
     * Copia un array de productos
     */
    private static Producto[] copiarArray(Producto[] original) {
        Producto[] copia = new Producto[original.length];
        System.arraycopy(original, 0, copia, 0, original.length);
        return copia;
    }
    
    /**
     * Copia un array de usuarios
     */
    private static Usuario[] copiarArray(Usuario[] original) {
        Usuario[] copia = new Usuario[original.length];
        System.arraycopy(original, 0, copia, 0, original.length);
        return copia;
    }
    
    /**
     * Algoritmo de ordenamiento burbuja para productos por ventas
     */
    private static void ordenarProductosPorVentas(Producto[] productos, boolean ascendente) {
        for (int i = 0; i < productos.length - 1; i++) {
            for (int j = 0; j < productos.length - i - 1; j++) {
                if (productos[j] != null && productos[j + 1] != null) {
                    boolean condicion = ascendente ? 
                        productos[j].getCantidadVendida() > productos[j + 1].getCantidadVendida() :
                        productos[j].getCantidadVendida() < productos[j + 1].getCantidadVendida();
                    
                    if (condicion) {
                        Producto temp = productos[j];
                        productos[j] = productos[j + 1];
                        productos[j + 1] = temp;
                    }
                }
            }
        }
    }
    
    /**
     * Algoritmo de ordenamiento burbuja para vendedores por ventas
     */
    private static void ordenarVendedoresPorVentas(Usuario[] vendedores) {
        for (int i = 0; i < vendedores.length - 1; i++) {
            for (int j = 0; j < vendedores.length - i - 1; j++) {
                if (vendedores[j] instanceof Vendedor && vendedores[j + 1] instanceof Vendedor) {
                    Vendedor v1 = (Vendedor) vendedores[j];
                    Vendedor v2 = (Vendedor) vendedores[j + 1];
                    
                    if (v1.getVentasConfirmadas() < v2.getVentasConfirmadas()) {
                        Usuario temp = vendedores[j];
                        vendedores[j] = vendedores[j + 1];
                        vendedores[j + 1] = temp;
                    }
                }
            }
        }
    }
    
    /**
     * Cuenta las compras de un cliente en los últimos N días
     */
    private static int contarComprasCliente(String codigoCliente, Pedido[] pedidos, int dias) {
        int contador = 0;
        long tiempoActual = System.currentTimeMillis();
        long diasEnMilisegundos = dias * 24L * 60L * 60L * 1000L;
        
        for (Pedido pedido : pedidos) {
            if (pedido != null && 
                pedido.getCodigoCliente() != null &&
                pedido.getCodigoCliente().equals(codigoCliente) &&
                pedido.getEstado() != null &&
                pedido.getEstado().equals("CONFIRMADO") &&
                pedido.getFecha() != null) {
                
                long tiempoPedido = pedido.getFecha().getTime();
                if ((tiempoActual - tiempoPedido) <= diasEnMilisegundos) {
                    contador++;
                }
            }
        }
        return contador;
    }
    
    /**
     * Calcula el monto total gastado por un cliente
     */
    private static double calcularMontoCliente(String codigoCliente, Pedido[] pedidos) {
        double total = 0;
        for (Pedido pedido : pedidos) {
            if (pedido != null && 
                pedido.getCodigoCliente() != null &&
                pedido.getCodigoCliente().equals(codigoCliente) &&
                pedido.getEstado() != null &&
                pedido.getEstado().equals("CONFIRMADO")) {
                total += pedido.getTotal();
            }
        }
        return total;
    }
    
    /**
     * Calcula días restantes hasta una fecha (formato DD/MM/YYYY)
     */
    private static int calcularDiasRestantes(String fechaCaducidad) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false); // Validación estricta de fechas
            Date fecha = sdf.parse(fechaCaducidad);
            long diferencia = fecha.getTime() - System.currentTimeMillis();
            return (int) (diferencia / (24 * 60 * 60 * 1000));
        } catch (ParseException e) {
            System.err.println("Error parseando fecha: " + fechaCaducidad);
            return 999; // Si hay error, retornar número alto
        }
    }
}