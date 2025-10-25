package com.sancarlista.controller;

import com.sancarlista.model.*;
import com.sancarlista.view.*;
import com.sancarlista.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;

/**
 * Controlador del módulo de Administrador
 */
public class AdminController {
    private Sistema sistema;
    private AdminView vista;
    
    public AdminController(AdminView vista) {
        this.sistema = Sistema.getInstancia();
        this.vista = vista;
        
        // Validar que el usuario actual sea un administrador
        if (!validarUsuarioAdmin()) {
            JOptionPane.showMessageDialog(vista, 
                "Acceso denegado: Usuario no es administrador", 
                "Error", JOptionPane.ERROR_MESSAGE);
            vista.dispose();
            return;
        }
        
        inicializarEventos();
        cargarDatos();
    }
    
    /**
     * Valida que el usuario actual sea un administrador
     */
    private boolean validarUsuarioAdmin() {
        Usuario usuario = sistema.getUsuarioActual();
        return usuario != null && usuario instanceof Administrador;
    }
    
    private void inicializarEventos() {
        // Eventos de vendedores
        vista.getBtnCrearVendedor().addActionListener(e -> crearVendedor());
        vista.getBtnActualizarVendedor().addActionListener(e -> actualizarVendedor());
        vista.getBtnEliminarVendedor().addActionListener(e -> eliminarVendedor());
        vista.getBtnCargarVendedoresCSV().addActionListener(e -> cargarVendedoresCSV());
        
        // Eventos de productos
        vista.getBtnCrearProducto().addActionListener(e -> crearProducto());
        vista.getBtnActualizarProducto().addActionListener(e -> actualizarProducto());
        vista.getBtnEliminarProducto().addActionListener(e -> eliminarProducto());
        vista.getBtnCargarProductosCSV().addActionListener(e -> cargarProductosCSV());
        vista.getBtnVerDetalle().addActionListener(e -> verDetalleProducto());
        
        // Eventos generales
        vista.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
        vista.getBtnRefrescar().addActionListener(e -> cargarDatos());
    }
    
    // ===== GESTIÓN DE VENDEDORES =====
    
    private void crearVendedor() {
        String codigo = vista.getTxtCodigoVendedor().getText().trim();
        String nombre = vista.getTxtNombreVendedor().getText().trim();
        String genero = (String) vista.getCmbGeneroVendedor().getSelectedItem();
        String contrasena = new String(vista.getTxtContrasenaVendedor().getPassword());
        
        if (codigo.isEmpty() || nombre.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(vista, 
                "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar longitud de contraseña
        if (contrasena.length() < 4) {
            JOptionPane.showMessageDialog(vista, 
                "La contraseña debe tener al menos 4 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Usuario vendedor = new Vendedor(codigo, nombre, genero, contrasena);
        
        if (sistema.agregarUsuario(vendedor)) {
            JOptionPane.showMessageDialog(vista, 
                "Vendedor creado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposVendedor();
            cargarTablaVendedores();
        } else {
            JOptionPane.showMessageDialog(vista, 
                "El código ya existe", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarVendedor() {
        String codigo = vista.getTxtCodigoVendedor().getText().trim();
        String nuevoNombre = vista.getTxtNombreVendedor().getText().trim();
        String nuevaContrasena = new String(vista.getTxtContrasenaVendedor().getPassword());
        
        if (codigo.isEmpty() || nuevoNombre.isEmpty() || nuevaContrasena.isEmpty()) {
            JOptionPane.showMessageDialog(vista, 
                "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar longitud de contraseña
        if (nuevaContrasena.length() < 4) {
            JOptionPane.showMessageDialog(vista, 
                "La contraseña debe tener al menos 4 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (sistema.actualizarUsuario(codigo, nuevoNombre, nuevaContrasena)) {
            JOptionPane.showMessageDialog(vista, 
                "Vendedor actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposVendedor();
            cargarTablaVendedores();
        } else {
            JOptionPane.showMessageDialog(vista, 
                "Vendedor no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarVendedor() {
        String codigo = vista.getTxtCodigoVendedor().getText().trim();
        
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(vista, 
                "Ingrese el código del vendedor", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(vista, 
            "¿Está seguro de eliminar este vendedor?\nEsta acción no se puede deshacer.", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (sistema.eliminarUsuario(codigo)) {
                JOptionPane.showMessageDialog(vista, 
                    "Vendedor eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposVendedor();
                cargarTablaVendedores();
            } else {
                JOptionPane.showMessageDialog(vista, 
                    "Vendedor no encontrado o no se puede eliminar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cargarVendedoresCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo CSV de vendedores");
        int resultado = fileChooser.showOpenDialog(vista);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            
            if (!archivo.exists()) {
                JOptionPane.showMessageDialog(vista, 
                    "El archivo no existe", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String[][] datos = CSVUtil.leerCSV(archivo.getAbsolutePath());
            
            if (datos == null || datos.length <= 1) {
                JOptionPane.showMessageDialog(vista, 
                    "Archivo vacío o inválido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int cargados = 0;
            int errores = 0;
            
            // Saltar header (índice 0)
            for (int i = 1; i < datos.length; i++) {
                if (datos[i] == null || datos[i].length < 4) {
                    errores++;
                    continue;
                }
                
                try {
                    String codigo = datos[i][0].trim();
                    String nombre = datos[i][1].trim();
                    String genero = datos[i][2].trim();
                    String contrasena = datos[i][3].trim();
                    
                    if (codigo.isEmpty() || nombre.isEmpty() || contrasena.isEmpty()) {
                        errores++;
                        continue;
                    }
                    
                    if (contrasena.length() < 4) {
                        errores++;
                        continue;
                    }
                    
                    Usuario vendedor = new Vendedor(codigo, nombre, genero, contrasena);
                    if (sistema.agregarUsuario(vendedor)) {
                        cargados++;
                    } else {
                        errores++;
                    }
                } catch (Exception e) {
                    errores++;
                }
            }
            
            String mensaje = String.format("%d vendedores cargados exitosamente", cargados);
            if (errores > 0) {
                mensaje += String.format("\n%d registros con errores", errores);
            }
            
            JOptionPane.showMessageDialog(vista, 
                mensaje, 
                "Resultado", JOptionPane.INFORMATION_MESSAGE);
            cargarTablaVendedores();
        }
    }
    
    // ===== GESTIÓN DE PRODUCTOS =====
    
    private void crearProducto() {
        String codigo = vista.getTxtCodigoProducto().getText().trim();
        String nombre = vista.getTxtNombreProducto().getText().trim();
        String categoria = (String) vista.getCmbCategoriaProducto().getSelectedItem();
        String atributo = vista.getTxtAtributoProducto().getText().trim();
        String precioStr = vista.getTxtPrecioProducto().getText().trim();
        
        if (codigo.isEmpty() || nombre.isEmpty() || atributo.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(vista, 
                "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            double precio = Double.parseDouble(precioStr);
            
            if (precio <= 0) {
                JOptionPane.showMessageDialog(vista, 
                    "El precio debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Producto producto = null;
            
            switch (categoria) {
                case "Tecnologia":
                    try {
                        int meses = Integer.parseInt(atributo);
                        if (meses <= 0) {
                            JOptionPane.showMessageDialog(vista, 
                                "Los meses de garantía deben ser mayores a 0", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        producto = new ProductoTecnologia(codigo, nombre, precio, meses);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(vista, 
                            "Meses de garantía inválidos", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;
                case "Alimento":
                    // Validar formato de fecha
                    if (!validarFormatoFecha(atributo)) {
                        JOptionPane.showMessageDialog(vista, 
                            "Formato de fecha inválido. Use DD/MM/YYYY", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    producto = new ProductoAlimento(codigo, nombre, precio, atributo);
                    break;
                case "General":
                    producto = new ProductoGeneral(codigo, nombre, precio, atributo);
                    break;
                default:
                    JOptionPane.showMessageDialog(vista, 
                        "Categoría inválida", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }
            
            if (producto != null && sistema.agregarProducto(producto)) {
                JOptionPane.showMessageDialog(vista, 
                    "Producto creado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposProducto();
                cargarTablaProductos();
            } else {
                JOptionPane.showMessageDialog(vista, 
                    "El código ya existe o error al crear producto", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, 
                "Precio inválido. Ingrese un número decimal", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, 
                "Error al crear producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarProducto() {
        String codigo = vista.getTxtCodigoProducto().getText().trim();
        String nuevoNombre = vista.getTxtNombreProducto().getText().trim();
        String atributo = vista.getTxtAtributoProducto().getText().trim();
        
        if (codigo.isEmpty() || nuevoNombre.isEmpty() || atributo.isEmpty()) {
            JOptionPane.showMessageDialog(vista, 
                "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar el atributo según el tipo de producto
        Producto productoExistente = sistema.buscarProductoPorCodigo(codigo);
        if (productoExistente != null) {
            if (productoExistente instanceof ProductoTecnologia) {
                try {
                    int meses = Integer.parseInt(atributo);
                    if (meses <= 0) {
                        JOptionPane.showMessageDialog(vista, 
                            "Los meses de garantía deben ser mayores a 0", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(vista, 
                        "Meses de garantía inválidos", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (productoExistente instanceof ProductoAlimento) {
                if (!validarFormatoFecha(atributo)) {
                    JOptionPane.showMessageDialog(vista, 
                        "Formato de fecha inválido. Use DD/MM/YYYY", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        
        if (sistema.actualizarProducto(codigo, nuevoNombre, atributo)) {
            JOptionPane.showMessageDialog(vista, 
                "Producto actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposProducto();
            cargarTablaProductos();
        } else {
            JOptionPane.showMessageDialog(vista, 
                "Producto no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarProducto() {
        String codigo = vista.getTxtCodigoProducto().getText().trim();
        
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(vista, 
                "Ingrese el código del producto", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(vista, 
            "¿Está seguro de eliminar este producto?\nEsta acción no se puede deshacer.", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (sistema.eliminarProducto(codigo)) {
                JOptionPane.showMessageDialog(vista, 
                    "Producto eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposProducto();
                cargarTablaProductos();
            } else {
                JOptionPane.showMessageDialog(vista, 
                    "Producto no encontrado o no se puede eliminar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cargarProductosCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo CSV de productos");
        int resultado = fileChooser.showOpenDialog(vista);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            
            if (!archivo.exists()) {
                JOptionPane.showMessageDialog(vista, 
                    "El archivo no existe", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String[][] datos = CSVUtil.leerCSV(archivo.getAbsolutePath());
            
            if (datos == null || datos.length <= 1) {
                JOptionPane.showMessageDialog(vista, 
                    "Archivo vacío o inválido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int cargados = 0;
            int errores = 0;
            
            // Saltar header (índice 0)
            for (int i = 1; i < datos.length; i++) {
                if (datos[i] == null || datos[i].length < 5) {
                    errores++;
                    continue;
                }
                
                try {
                    String codigo = datos[i][0].trim();
                    String nombre = datos[i][1].trim();
                    String categoria = datos[i][2].trim();
                    String precioStr = datos[i][3].trim();
                    String atributo = datos[i][4].trim();
                    
                    if (codigo.isEmpty() || nombre.isEmpty() || precioStr.isEmpty() || atributo.isEmpty()) {
                        errores++;
                        continue;
                    }
                    
                    double precio = Double.parseDouble(precioStr);
                    
                    if (precio <= 0) {
                        errores++;
                        continue;
                    }
                    
                    Producto producto = null;
                    
                    switch (categoria) {
                        case "Tecnologia":
                            int meses = Integer.parseInt(atributo);
                            if (meses > 0) {
                                producto = new ProductoTecnologia(codigo, nombre, precio, meses);
                            }
                            break;
                        case "Alimento":
                            if (validarFormatoFecha(atributo)) {
                                producto = new ProductoAlimento(codigo, nombre, precio, atributo);
                            }
                            break;
                        case "General":
                            producto = new ProductoGeneral(codigo, nombre, precio, atributo);
                            break;
                    }
                    
                    if (producto != null && sistema.agregarProducto(producto)) {
                        cargados++;
                    } else {
                        errores++;
                    }
                } catch (NumberFormatException e) {
                    errores++;
                } catch (Exception e) {
                    errores++;
                }
            }
            
            String mensaje = String.format("%d productos cargados exitosamente", cargados);
            if (errores > 0) {
                mensaje += String.format("\n%d registros con errores", errores);
            }
            
            JOptionPane.showMessageDialog(vista, 
                mensaje, 
                "Resultado", JOptionPane.INFORMATION_MESSAGE);
            cargarTablaProductos();
        }
    }
    
    private void verDetalleProducto() {
        int fila = vista.getTablaProductos().getSelectedRow();
        
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista, 
                "Seleccione un producto de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Object valorCodigo = vista.getTablaProductos().getValueAt(fila, 0);
            
            if (valorCodigo == null) {
                JOptionPane.showMessageDialog(vista, 
                    "Código de producto inválido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String codigo = valorCodigo.toString();
            Producto producto = sistema.buscarProductoPorCodigo(codigo);
            
            if (producto != null) {
                String detalle = producto.getDetalleEspecifico();
                
                if (detalle != null && !detalle.isEmpty()) {
                    JOptionPane.showMessageDialog(vista, 
                        detalle, 
                        "Detalle del Producto", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(vista, 
                        "No hay detalle disponible para este producto", 
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(vista, 
                    "Producto no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, 
                "Error al obtener detalle del producto: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ===== CARGA DE DATOS EN TABLAS =====
    
    private void cargarDatos() {
        cargarTablaVendedores();
        cargarTablaProductos();
    }
    
    private void cargarTablaVendedores() {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaVendedores().getModel();
        modelo.setRowCount(0);
        
        Usuario[] vendedores = sistema.obtenerVendedores();
        
        if (vendedores != null) {
            for (Usuario vendedor : vendedores) {
                if (vendedor instanceof Vendedor) {
                    modelo.addRow(new Object[]{
                        vendedor.getCodigo() != null ? vendedor.getCodigo() : "N/A",
                        vendedor.getNombre() != null ? vendedor.getNombre() : "N/A",
                        vendedor.getGenero() != null ? vendedor.getGenero() : "N/A",
                        ((Vendedor) vendedor).getVentasConfirmadas()
                    });
                }
            }
        }
    }
    
    private void cargarTablaProductos() {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaProductos().getModel();
        modelo.setRowCount(0);
        
        Producto[] productos = sistema.obtenerProductos();
        
        if (productos != null) {
            for (Producto producto : productos) {
                if (producto != null) {
                    modelo.addRow(new Object[]{
                        producto.getCodigo() != null ? producto.getCodigo() : "N/A",
                        producto.getNombre() != null ? producto.getNombre() : "N/A",
                        producto.getCategoria() != null ? producto.getCategoria() : "N/A",
                        producto.getStock(),
                        String.format("Q%.2f", producto.getPrecio())
                    });
                }
            }
        }
    }
    
    // ===== MÉTODOS AUXILIARES =====
    
    /**
     * Valida que una fecha tenga el formato DD/MM/YYYY
     */
    private boolean validarFormatoFecha(String fecha) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            sdf.parse(fecha);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // ===== LIMPIEZA DE CAMPOS =====
    
    private void limpiarCamposVendedor() {
        vista.getTxtCodigoVendedor().setText("");
        vista.getTxtNombreVendedor().setText("");
        vista.getTxtContrasenaVendedor().setText("");
        vista.getCmbGeneroVendedor().setSelectedIndex(0);
    }
    
    private void limpiarCamposProducto() {
        vista.getTxtCodigoProducto().setText("");
        vista.getTxtNombreProducto().setText("");
        vista.getTxtPrecioProducto().setText("");
        vista.getTxtAtributoProducto().setText("");
        vista.getCmbCategoriaProducto().setSelectedIndex(0);
    }
    
    // ===== CERRAR SESIÓN =====
    
    private void cerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(vista, 
            "¿Desea cerrar sesión?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            sistema.cerrarSesion();
            vista.dispose();
            
            LoginView loginView = new LoginView();
            new SistemaController(loginView);
            loginView.setVisible(true);
        }
    }
}