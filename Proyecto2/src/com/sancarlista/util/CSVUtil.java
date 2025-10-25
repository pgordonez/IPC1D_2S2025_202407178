package com.sancarlista.util;

import com.sancarlista.model.RegistroBitacora;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilidad para lectura y escritura de archivos CSV
 * SIN usar librerías externas
 */
public class CSVUtil {
    
    /**
     * Lee un archivo CSV y retorna una matriz de Strings
     * @param rutaArchivo Ruta del archivo CSV
     * @return Matriz con los datos del CSV
     */
    public static String[][] leerCSV(String rutaArchivo) {
        try {
            BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo));
            List<String[]> filas = new ArrayList<>();
            String linea;
            
            while ((linea = lector.readLine()) != null) {
                filas.add(parsearLineaCSV(linea));
            }
            
            lector.close();
            
            // Convertir List a array
            String[][] datos = new String[filas.size()][];
            for (int i = 0; i < filas.size(); i++) {
                datos[i] = filas.get(i);
            }
            
            return datos;
            
        } catch (IOException e) {
            System.err.println("Error al leer CSV: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Escribe datos en un archivo CSV
     * @param rutaArchivo Ruta del archivo CSV
     * @param datos Matriz con los datos a escribir
     * @return true si se escribió exitosamente
     */
    public static boolean escribirCSV(String rutaArchivo, String[][] datos) {
        try {
            BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo));
            
            for (int i = 0; i < datos.length; i++) {
                StringBuilder linea = new StringBuilder();
                
                for (int j = 0; j < datos[i].length; j++) {
                    linea.append(escaparCampoCSV(datos[i][j]));
                    if (j < datos[i].length - 1) {
                        linea.append(",");
                    }
                }
                
                escritor.write(linea.toString());
                escritor.newLine();
            }
            
            escritor.close();
            return true;
            
        } catch (IOException e) {
            System.err.println("Error al escribir CSV: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Parsea una línea CSV considerando comas dentro de comillas
     * @param linea Línea a parsear
     * @return Array con los valores separados
     */
    private static String[] parsearLineaCSV(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder campoActual = new StringBuilder();
        boolean dentroComillas = false;
        
        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            
            if (c == '"') {
                // Verificar si es una comilla escapada ""
                if (dentroComillas && i + 1 < linea.length() && linea.charAt(i + 1) == '"') {
                    campoActual.append('"');
                    i++; // Saltar la siguiente comilla
                } else {
                    // Cambiar estado de dentro/fuera de comillas
                    dentroComillas = !dentroComillas;
                }
            } else if (c == ',' && !dentroComillas) {
                // Fin del campo
                campos.add(campoActual.toString().trim());
                campoActual = new StringBuilder();
            } else {
                campoActual.append(c);
            }
        }
        
        // Agregar el último campo
        campos.add(campoActual.toString().trim());
        
        return campos.toArray(new String[0]);
    }
    
    /**
     * Escapa un campo para formato CSV
     * Si contiene comas, comillas o saltos de línea, se envuelve en comillas
     * Las comillas internas se duplican
     * @param campo Campo a escapar
     * @return Campo escapado
     */
    private static String escaparCampoCSV(String campo) {
        if (campo == null) {
            return "";
        }
        
        // Verificar si necesita ser envuelto en comillas
        boolean necesitaComillas = campo.contains(",") || 
                                   campo.contains("\"") || 
                                   campo.contains("\n") ||
                                   campo.contains("\r");
        
        if (necesitaComillas) {
            // Escapar comillas duplicándolas
            String campoEscapado = campo.replace("\"", "\"\"");
            return "\"" + campoEscapado + "\"";
        }
        
        return campo;
    }
    
    /**
     * Exporta la bitácora a CSV
     */
    public static boolean exportarBitacoraCSV(RegistroBitacora[] registros, String nombreArchivo) {
        if (registros == null || registros.length == 0) {
            return false;
        }
        
        String[][] datos = new String[registros.length + 1][];
        
        // Header
        datos[0] = new String[]{"Fecha", "Tipo Usuario", "Código Usuario", 
                                "Operación", "Estado", "Descripción"};
        
        // Datos
        for (int i = 0; i < registros.length; i++) {
            RegistroBitacora reg = registros[i];
            datos[i + 1] = new String[]{
                reg.getFecha() != null ? reg.getFecha().toString() : "",
                reg.getTipoUsuario() != null ? reg.getTipoUsuario() : "",
                reg.getCodigoUsuario() != null ? reg.getCodigoUsuario() : "",
                reg.getOperacion() != null ? reg.getOperacion() : "",
                reg.getEstado() != null ? reg.getEstado() : "",
                reg.getDescripcion() != null ? reg.getDescripcion() : ""
            };
        }
        
        return escribirCSV(nombreArchivo, datos);
    }
}