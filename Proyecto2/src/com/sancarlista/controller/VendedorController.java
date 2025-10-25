package com.sancarlista.controller;

import com.sancarlista.model.*;
import com.sancarlista.view.*;
import com.sancarlista.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.text.SimpleDateFormat;


/**
 * Controlador del módulo de Vendedor
 */
public class VendedorController {
    private Sistema sistema;
    private VendedorView vista;
    
    public VendedorController(VendedorView vista) {
        this.sistema = Sistema.getInstancia();
        this.vista = vista;
        
        // Validar que el usuario actual sea un vendedor
        if (!validarUsuarioVendedor()) {
            JOptionPane.showMessageDialog(vista, 
                "Acceso denegado: Usuario no es vendedor", 
                "Error", JOptionPane.ERROR_MESSAGE);
            vista.dispose();
            return;
        }
        
        inicializarEventos();
        cargarDatos();
    }
    
    /**
     * Valida que el usuario actual sea un vendedor
     */
    private boolean validarUsuarioVendedor() {
        Usuario usuario = sistema.getUsuarioActual();
        return usuario != null && usuario instanceof Vendedor;
    }
    
    private void inicializarEventos() {
        // Eventos de stock
        vista.getBtnAgregarStock().addActionListener(e -> agregarStock());
        vista.getBtnCargarStockCSV().addActionListener(e -> cargarStockCSV());
        vista.getBtnHistorialStock().addActionListener(e -> verHistorialStock());
        
        // Eventos de clientes
        vista.getBtnCrearCliente().addActionListener(e -> crearCliente());
        vista.getBtnActualizarCliente().addActionListener(e -> actualizarCliente());
        vista.getBtnEliminarCliente().addActionListener(e -> eliminarCliente());
        vista.getBtnCargarClientesCSV().addActionListener(e -> cargarClientesCSV());
        
        // Eventos de pedidos
        vista.getBtnConfirmarPedido().addActionListener(e -> confirmarPedido());
        
        // Eventos generales
        vista.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
        vista.getBtnRefrescar().addActionListener(e -> cargarDatos());
    }
    
    // ===== GESTIÓN DE STOCK =====
    
    private void agregarStock() {
        String codigoProducto = vista.getTxtCodigoProducto().getText().trim();
        String cantidadStr = vista.getTxtCantidadStock().getText().trim();
        
        if (codigoProducto.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(vista, 
                "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int cantidad = Integer.parseInt(cantidadStr);
            
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(vista, 
                    "La cantidad debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Usuario vendedor = sistema.getUsuarioActual();
            
            if (vendedor == null || vendedor.getCodigo() == null) {
                JOptionPane.showMessageDialog(vista, 
                    "Error: Usuario no válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (sistema.agregarStock(codigoProducto, cantidad, vendedor.getCodigo())) {
                JOptionPane.showMessageDialog(vista, 
                    "Stock agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposStock();
                cargarTablaProductos();
            } else {
                JOptionPane.showMessageDialog(vista, 
                    "Producto no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, 
                "Cantidad inválida. Ingrese un número entero", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarStockCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo CSV de stock");
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
            Usuario vendedor = sistema.getUsuarioActual();
            
            if (vendedor == null || vendedor.getCodigo() == null) {
                JOptionPane.showMessageDialog(vista, 
                    "Error: Usuario no válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Saltar el header (índice 0)
            for (int i = 1; i < datos.length; i++) {
                if (datos[i] == null || datos[i].length < 2) {
                    errores++;
                    continue;
                }
                
                try {
                    String codigo = datos[i][0].trim();
                    String cantidadStr = datos[i][1].trim();
                    
                    if (codigo.isEmpty() || cantidadStr.isEmpty()) {
                        errores++;
                        continue;
                    }
                    
                    int cantidad = Integer.parseInt(cantidadStr);
                    
                    if (cantidad > 0 && sistema.agregarStock(codigo, cantidad, vendedor.getCodigo())) {
                        cargados++;
                    } else {
                        errores++;
                    }
                } catch (NumberFormatException e) {
                    errores++;
                }
            }
            
            String mensaje = String.format("%d registros cargados exitosamente", cargados);
            if (errores > 0) {
                mensaje += String.format("\n%d registros con errores", errores);
            }
            
            JOptionPane.showMessageDialog(vista, 
                mensaje, 
                "Resultado", JOptionPane.INFORMATION_MESSAGE);
            cargarTablaProductos();
        }
    }
    
    private void verHistorialStock() {
        MovimientoStock[] movimientos = sistema.obtenerHistorialStock();
        
        if (movimientos == null || movimientos.length == 0) {
            JOptionPane.showMessageDialog(vista, 
                "No hay movimientos registrados", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Crear archivo CSV con historial
        String nombreArchivo = "historial_stock_" + System.currentTimeMillis() + ".csv";
        String[][] datos = new String[movimientos.length + 1][];
        
        // Header
        datos[0] = new String[]{"Fecha", "Hora", "Usuario", "Producto", "Cantidad"};
        
        // Datos
        for (int i = 0; i < movimientos.length; i++) {
            if (movimientos[i] != null) {
                String fechaFormateada = movimientos[i].getFechaFormateada();
                String fecha = "";
                String hora = "";
                
                // Parsear fecha y hora de forma segura
                if (fechaFormateada != null && fechaFormateada.contains(" ")) {
                    String[] partes = fechaFormateada.split(" ", 2);
                    fecha = partes[0];
                    hora = partes.length > 1 ? partes[1] : "";
                }
                
                datos[i + 1] = new String[]{
                    fecha,
                    hora,
                    movimientos[i].getCodigoUsuario() != null ? movimientos[i].getCodigoUsuario() : "N/A",
                    movimientos[i].getNombreProducto() != null ? movimientos[i].getNombreProducto() : "N/A",
                    String.valueOf(movimientos[i].getCantidad())
                };
            }
        }
        
        if (CSVUtil.escribirCSV(nombreArchivo, datos)) {
            JOptionPane.showMessageDialog(vista, 
                "Historial exportado: " + nombreArchivo, 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(vista, 
                "Error al exportar historial", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ===== GESTIÓN DE CLIENTES =====
    
    private void crearCliente() {
        String codigo = vista.getTxtCodigoCliente().getText().trim();
        String nombre = vista.getTxtNombreCliente().getText().trim();
        String genero = (String) vista.getCmbGeneroCliente().getSelectedItem();
        String cumpleanos = vista.getTxtCumpleanosCliente().getText().trim();
        String contrasena = new String(vista.getTxtContrasenaCliente().getPassword());
        
        if (codigo.isEmpty() || nombre.isEmpty() || cumpleanos.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(vista, 
                "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar formato de fecha
        if (!validarFormatoFecha(cumpleanos)) {
            JOptionPane.showMessageDialog(vista, 
                "Formato de fecha inválido. Use DD/MM/YYYY", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar longitud de contraseña
        if (contrasena.length() < 4) {
            JOptionPane.showMessageDialog(vista, 
                "La contraseña debe tener al menos 4 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Usuario vendedor = sistema.getUsuarioActual();
        
        if (vendedor == null || vendedor.getCodigo() == null) {
            JOptionPane.showMessageDialog(vista, 
                "Error: Usuario no válido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Usuario cliente = new Cliente(codigo, nombre, genero, cumpleanos, 
                                     contrasena, vendedor.getCodigo());
        
        if (sistema.agregarUsuario(cliente)) {
            JOptionPane.showMessageDialog(vista, 
                "Cliente creado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposCliente();
            cargarTablaClientes();
        } else {
            JOptionPane.showMessageDialog(vista, 
                "El código ya existe", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarCliente() {
        String codigo = vista.getTxtCodigoCliente().getText().trim();
        String nuevoNombre = vista.getTxtNombreCliente().getText().trim();
        String nuevaContrasena = new String(vista.getTxtContrasenaCliente().getPassword());
        
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
                "Cliente actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposCliente();
            cargarTablaClientes();
        } else {
            JOptionPane.showMessageDialog(vista, 
                "Cliente no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarCliente() {
        String codigo = vista.getTxtCodigoCliente().getText().trim();
        
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(vista, 
                "Ingrese el código del cliente", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(vista, 
            "¿Está seguro de eliminar este cliente?\nEsta acción no se puede deshacer.", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (sistema.eliminarUsuario(codigo)) {
                JOptionPane.showMessageDialog(vista, 
                    "Cliente eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposCliente();
                cargarTablaClientes();
            } else {
                JOptionPane.showMessageDialog(vista, 
                    "Cliente no encontrado o no se puede eliminar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cargarClientesCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo CSV de clientes");
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
            Usuario vendedor = sistema.getUsuarioActual();
            
            if (vendedor == null || vendedor.getCodigo() == null) {
                JOptionPane.showMessageDialog(vista, 
                    "Error: Usuario no válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Saltar el header (índice 0)
            for (int i = 1; i < datos.length; i++) {
                if (datos[i] == null || datos[i].length < 5) {
                    errores++;
                    continue;
                }
                
                try {
                    String codigo = datos[i][0].trim();
                    String nombre = datos[i][1].trim();
                    String genero = datos[i][2].trim();
                    String cumpleanos = datos[i][3].trim();
                    String contrasena = datos[i][4].trim();
                    
                    if (codigo.isEmpty() || nombre.isEmpty() || contrasena.isEmpty()) {
                        errores++;
                        continue;
                    }
                    
                    // Validar formato de fecha
                    if (!validarFormatoFecha(cumpleanos)) {
                        errores++;
                        continue;
                    }
                    
                    Usuario cliente = new Cliente(codigo, nombre, genero, cumpleanos, 
                                                 contrasena, vendedor.getCodigo());
                    if (sistema.agregarUsuario(cliente)) {
                        cargados++;
                    } else {
                        errores++;
                    }
                } catch (Exception e) {
                    errores++;
                }
            }
            
            String mensaje = String.format("%d clientes cargados exitosamente", cargados);
            if (errores > 0) {
                mensaje += String.format("\n%d registros con errores", errores);
            }
            
            JOptionPane.showMessageDialog(vista, 
                mensaje, 
                "Resultado", JOptionPane.INFORMATION_MESSAGE);
            cargarTablaClientes();
        }
    }
    
    // ===== GESTIÓN DE PEDIDOS =====
    
    private void confirmarPedido() {
        int fila = vista.getTablaPedidos().getSelectedRow();
        
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista, 
                "Seleccione un pedido de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Object valorId = vista.getTablaPedidos().getValueAt(fila, 0);
            
            if (valorId == null) {
                JOptionPane.showMessageDialog(vista, 
                    "ID de pedido inválido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int idPedido = Integer.parseInt(valorId.toString());
            Usuario vendedor = sistema.getUsuarioActual();
            
            if (vendedor == null || vendedor.getCodigo() == null) {
                JOptionPane.showMessageDialog(vista, 
                    "Error: Usuario no válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirmacion = JOptionPane.showConfirmDialog(vista, 
                "¿Confirmar el pedido #" + idPedido + "?", 
                "Confirmar acción", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (sistema.confirmarPedido(idPedido, vendedor.getCodigo())) {
                    JOptionPane.showMessageDialog(vista, 
                        "Pedido confirmado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarTablaPedidos();
                    
                    // Actualizar contador de ventas del vendedor
                    if (vendedor instanceof Vendedor) {
                        ((Vendedor) vendedor).incrementarVentas();
                    }
                } else {
                    JOptionPane.showMessageDialog(vista, 
                        "Error al confirmar pedido. Verifique el stock disponible", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, 
                "ID de pedido inválido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, 
                "Error al confirmar pedido: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ===== CARGA DE DATOS EN TABLAS =====
    
    private void cargarDatos() {
        cargarTablaProductos();
        cargarTablaClientes();
        cargarTablaPedidos();
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
                        producto.getStock()
                    });
                }
            }
        }
    }
    
    private void cargarTablaClientes() {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaClientes().getModel();
        modelo.setRowCount(0);
        
        Usuario[] clientes = sistema.obtenerClientes();
        Usuario vendedor = sistema.getUsuarioActual();
        
        if (clientes == null || vendedor == null || vendedor.getCodigo() == null) {
            return;
        }
        
        for (Usuario cliente : clientes) {
            if (cliente instanceof Cliente) {
                Cliente c = (Cliente) cliente;
                
                // Solo mostrar clientes del vendedor actual
                if (c.getCodigoVendedor() != null && 
                    c.getCodigoVendedor().equals(vendedor.getCodigo())) {
                    modelo.addRow(new Object[]{
                        c.getCodigo() != null ? c.getCodigo() : "N/A",
                        c.getNombre() != null ? c.getNombre() : "N/A",
                        c.getGenero() != null ? c.getGenero() : "N/A",
                        c.getCumpleanos() != null ? c.getCumpleanos() : "N/A"
                    });
                }
            }
        }
    }
    
    private void cargarTablaPedidos() {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaPedidos().getModel();
        modelo.setRowCount(0);
        
        Pedido[] pedidos = sistema.obtenerPedidosPendientes();
        
        if (pedidos != null) {
            for (Pedido pedido : pedidos) {
                if (pedido != null) {
                    modelo.addRow(new Object[]{
                        pedido.getId(),
                        pedido.getCodigoCliente() != null ? pedido.getCodigoCliente() : "N/A",
                        String.format("Q%.2f", pedido.getTotal()),
                        pedido.getFechaFormateada() != null ? pedido.getFechaFormateada() : "N/A",
                        pedido.getEstado() != null ? pedido.getEstado() : "N/A"
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
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            sdf.parse(fecha);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // ===== LIMPIEZA DE CAMPOS =====
    
    private void limpiarCamposStock() {
        vista.getTxtCodigoProducto().setText("");
        vista.getTxtCantidadStock().setText("");
    }
    
    private void limpiarCamposCliente() {
        vista.getTxtCodigoCliente().setText("");
        vista.getTxtNombreCliente().setText("");
        vista.getTxtCumpleanosCliente().setText("");
        vista.getTxtContrasenaCliente().setText("");
        vista.getCmbGeneroCliente().setSelectedIndex(0);
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