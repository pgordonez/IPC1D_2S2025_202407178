package com.sancarlista.model;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase Sistema - Patrón Singleton
 * Gestiona todos los datos del sistema usando vectores
 */
public class Sistema implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Sistema instancia;
    
    // Vectores para almacenar datos (NO usar ArrayList)
    private Usuario[] usuarios;
    private Producto[] productos;
    private Pedido[] pedidos;
    private MovimientoStock[] historialStock;
    private RegistroBitacora[] bitacora;
    
    // Contadores para vectores
    private int cantUsuarios;
    private int cantProductos;
    private int cantPedidos;
    private int cantMovimientos;
    private int cantRegistros;
    
    // Capacidad inicial
    private static final int CAPACIDAD_INICIAL = 100;
    
    // Usuario actual en sesión
    private Usuario usuarioActual;
    
    // Hilos de monitoreo
    private Thread hiloSesiones;
    private Thread hiloPedidos;
    private Thread hiloEstadisticas;
    private boolean hilosActivos;
    
    private Sistema() {
        usuarios = new Usuario[CAPACIDAD_INICIAL];
        productos = new Producto[CAPACIDAD_INICIAL];
        pedidos = new Pedido[CAPACIDAD_INICIAL];
        historialStock = new MovimientoStock[CAPACIDAD_INICIAL];
        bitacora = new RegistroBitacora[CAPACIDAD_INICIAL];
        
        cantUsuarios = 0;
        cantProductos = 0;
        cantPedidos = 0;
        cantMovimientos = 0;
        cantRegistros = 0;
        
        usuarioActual = null;
        hilosActivos = false;
        
        // Crear administrador por defecto
        crearAdministradorDefecto();
    }
    
    public static Sistema getInstancia() {
        if (instancia == null) {
            instancia = new Sistema();
        }
        return instancia;
    }
    
    private void crearAdministradorDefecto() {
        // Administrador: codigo="admin", password="IPC1<SECCION>"
        Usuario admin = new Administrador("admin", "Administrador", "M", "IPC1D");
        agregarUsuario(admin);
    }
    
    // ===== GESTIÓN DE USUARIOS =====
    
    public boolean agregarUsuario(Usuario usuario) {
        if (buscarUsuarioPorCodigo(usuario.getCodigo()) != null) {
            registrarBitacora("CREAR_USUARIO", "FALLIDA", 
                "Código duplicado: " + usuario.getCodigo());
            return false;
        }
        
        if (cantUsuarios >= usuarios.length) {
            expandirVectorUsuarios();
        }
        
        usuarios[cantUsuarios++] = usuario;
        registrarBitacora("CREAR_USUARIO", "EXITOSA", 
            "Usuario creado: " + usuario.getCodigo());
        return true;
    }
    
    public Usuario buscarUsuarioPorCodigo(String codigo) {
        for (int i = 0; i < cantUsuarios; i++) {
            if (usuarios[i].getCodigo().equals(codigo)) {
                return usuarios[i];
            }
        }
        return null;
    }
    
    public boolean actualizarUsuario(String codigo, String nuevoNombre, String nuevaContrasena) {
        Usuario usuario = buscarUsuarioPorCodigo(codigo);
        if (usuario != null && !(usuario instanceof Administrador)) {
            usuario.setNombre(nuevoNombre);
            usuario.setContrasena(nuevaContrasena);
            registrarBitacora("ACTUALIZAR_USUARIO", "EXITOSA", 
                "Usuario actualizado: " + codigo);
            return true;
        }
        return false;
    }
    
    public boolean eliminarUsuario(String codigo) {
        for (int i = 0; i < cantUsuarios; i++) {
            if (usuarios[i].getCodigo().equals(codigo) && 
                !(usuarios[i] instanceof Administrador)) {
                // Mover elementos hacia la izquierda
                for (int j = i; j < cantUsuarios - 1; j++) {
                    usuarios[j] = usuarios[j + 1];
                }
                usuarios[--cantUsuarios] = null;
                registrarBitacora("ELIMINAR_USUARIO", "EXITOSA", 
                    "Usuario eliminado: " + codigo);
                return true;
            }
        }
        return false;
    }
    
    public Usuario[] obtenerVendedores() {
        int contador = 0;
        for (int i = 0; i < cantUsuarios; i++) {
            if (usuarios[i] instanceof Vendedor) {
                contador++;
            }
        }
        
        Usuario[] vendedores = new Usuario[contador];
        int indice = 0;
        for (int i = 0; i < cantUsuarios; i++) {
            if (usuarios[i] instanceof Vendedor) {
                vendedores[indice++] = usuarios[i];
            }
        }
        return vendedores;
    }
    
    public Usuario[] obtenerClientes() {
        int contador = 0;
        for (int i = 0; i < cantUsuarios; i++) {
            if (usuarios[i] instanceof Cliente) {
                contador++;
            }
        }
        
        Usuario[] clientes = new Usuario[contador];
        int indice = 0;
        for (int i = 0; i < cantUsuarios; i++) {
            if (usuarios[i] instanceof Cliente) {
                clientes[indice++] = usuarios[i];
            }
        }
        return clientes;
    }
    
    // ===== GESTIÓN DE PRODUCTOS =====
    
    public boolean agregarProducto(Producto producto) {
        if (buscarProductoPorCodigo(producto.getCodigo()) != null) {
            registrarBitacora("CREAR_PRODUCTO", "FALLIDA", 
                "Código duplicado: " + producto.getCodigo());
            return false;
        }
        
        if (cantProductos >= productos.length) {
            expandirVectorProductos();
        }
        
        productos[cantProductos++] = producto;
        registrarBitacora("CREAR_PRODUCTO", "EXITOSA", 
            "Producto creado: " + producto.getCodigo());
        return true;
    }
    
    public Producto buscarProductoPorCodigo(String codigo) {
        for (int i = 0; i < cantProductos; i++) {
            if (productos[i].getCodigo().equals(codigo)) {
                return productos[i];
            }
        }
        return null;
    }
    
    public boolean actualizarProducto(String codigo, String nuevoNombre, String atributo) {
        Producto producto = buscarProductoPorCodigo(codigo);
        if (producto != null) {
            producto.setNombre(nuevoNombre);
            producto.setAtributoEspecifico(atributo);
            registrarBitacora("ACTUALIZAR_PRODUCTO", "EXITOSA", 
                "Producto actualizado: " + codigo);
            return true;
        }
        return false;
    }
    
    public boolean eliminarProducto(String codigo) {
        for (int i = 0; i < cantProductos; i++) {
            if (productos[i].getCodigo().equals(codigo)) {
                for (int j = i; j < cantProductos - 1; j++) {
                    productos[j] = productos[j + 1];
                }
                productos[--cantProductos] = null;
                registrarBitacora("ELIMINAR_PRODUCTO", "EXITOSA", 
                    "Producto eliminado: " + codigo);
                return true;
            }
        }
        return false;
    }
    
    public Producto[] obtenerProductos() {
        Producto[] resultado = new Producto[cantProductos];
        for (int i = 0; i < cantProductos; i++) {
            resultado[i] = productos[i];
        }
        return resultado;
    }
    
    public Producto[] obtenerProductosConStock() {
        int contador = 0;
        for (int i = 0; i < cantProductos; i++) {
            if (productos[i].getStock() > 0) {
                contador++;
            }
        }
        
        Producto[] resultado = new Producto[contador];
        int indice = 0;
        for (int i = 0; i < cantProductos; i++) {
            if (productos[i].getStock() > 0) {
                resultado[indice++] = productos[i];
            }
        }
        return resultado;
    }
    
    // ===== GESTIÓN DE STOCK =====
    
    public boolean agregarStock(String codigoProducto, int cantidad, String codigoUsuario) {
        Producto producto = buscarProductoPorCodigo(codigoProducto);
        if (producto != null && cantidad > 0) {
            producto.agregarStock(cantidad);
            
            // Registrar movimiento
            MovimientoStock movimiento = new MovimientoStock(
                codigoProducto, producto.getNombre(), cantidad, 
                codigoUsuario, new Date()
            );
            
            if (cantMovimientos >= historialStock.length) {
                expandirVectorMovimientos();
            }
            historialStock[cantMovimientos++] = movimiento;
            
            registrarBitacora("AGREGAR_STOCK", "EXITOSA", 
                "Stock agregado: " + codigoProducto + ", Cantidad: " + cantidad);
            return true;
        }
        return false;
    }
    
    public MovimientoStock[] obtenerHistorialStock() {
        MovimientoStock[] resultado = new MovimientoStock[cantMovimientos];
        for (int i = 0; i < cantMovimientos; i++) {
            resultado[i] = historialStock[i];
        }
        return resultado;
    }
    
    // ===== GESTIÓN DE PEDIDOS =====
    
    public boolean crearPedido(Pedido pedido) {
        if (cantPedidos >= pedidos.length) {
            expandirVectorPedidos();
        }
        
        pedidos[cantPedidos++] = pedido;
        registrarBitacora("CREAR_PEDIDO", "EXITOSA", 
            "Pedido creado por cliente: " + pedido.getCodigoCliente());
        return true;
    }
    
    public Pedido[] obtenerPedidosPendientes() {
        int contador = 0;
        for (int i = 0; i < cantPedidos; i++) {
            if (pedidos[i].getEstado().equals("PENDIENTE")) {
                contador++;
            }
        }
        
        Pedido[] resultado = new Pedido[contador];
        int indice = 0;
        for (int i = 0; i < cantPedidos; i++) {
            if (pedidos[i].getEstado().equals("PENDIENTE")) {
                resultado[indice++] = pedidos[i];
            }
        }
        return resultado;
    }
    
    public boolean confirmarPedido(int idPedido, String codigoVendedor) {
        for (int i = 0; i < cantPedidos; i++) {
            if (pedidos[i].getId() == idPedido && 
                pedidos[i].getEstado().equals("PENDIENTE")) {
                pedidos[i].setEstado("CONFIRMADO");
                
                // Incrementar ventas del vendedor
                Usuario vendedor = buscarUsuarioPorCodigo(codigoVendedor);
                if (vendedor instanceof Vendedor) {
                    ((Vendedor) vendedor).incrementarVentas();
                }
                
                registrarBitacora("CONFIRMAR_PEDIDO", "EXITOSA", 
                    "Pedido confirmado: " + idPedido);
                return true;
            }
        }
        return false;
    }
    
    /**
 * Obtiene todos los pedidos de un cliente específico
 */
public Pedido[] obtenerPedidosCliente(String codigoCliente) {
    if (codigoCliente == null) {
        return new Pedido[0];
    }
    
    // Contar pedidos del cliente
    int contador = 0;
    for (int i = 0; i < cantPedidos; i++) {
        if (pedidos[i] != null && 
            pedidos[i].getCodigoCliente() != null &&
            pedidos[i].getCodigoCliente().equals(codigoCliente)) {
            contador++;
        }
    }
    
    // Crear array con los pedidos del cliente
    Pedido[] resultado = new Pedido[contador];
    int indice = 0;
    for (int i = 0; i < cantPedidos; i++) {
        if (pedidos[i] != null && 
            pedidos[i].getCodigoCliente() != null &&
            pedidos[i].getCodigoCliente().equals(codigoCliente)) {
            resultado[indice++] = pedidos[i];
        }
    }
    
    return resultado;
}
    
    // ===== AUTENTICACIÓN =====
    
    public Usuario iniciarSesion(String codigo, String contrasena) {
        Usuario usuario = buscarUsuarioPorCodigo(codigo);
        if (usuario != null && usuario.autenticar(codigo, contrasena)) {
            usuarioActual = usuario;
            registrarBitacora("LOGIN", "EXITOSA", "Usuario: " + codigo);
            return usuario;
        }
        registrarBitacora("LOGIN", "FALLIDA", "Intento fallido: " + codigo);
        return null;
    }
    
    public void cerrarSesion() {
        if (usuarioActual != null) {
            registrarBitacora("LOGOUT", "EXITOSA", "Usuario: " + usuarioActual.getCodigo());
            usuarioActual = null;
        }
    }
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    // ===== BITÁCORA =====
    
    public void registrarBitacora(String operacion, String estado, String descripcion) {
        if (cantRegistros >= bitacora.length) {
            expandirVectorBitacora();
        }
        
        String tipoUsuario = usuarioActual != null ? usuarioActual.getTipoUsuario() : "SISTEMA";
        String codigoUsuario = usuarioActual != null ? usuarioActual.getCodigo() : "SISTEMA";
        
        RegistroBitacora registro = new RegistroBitacora(
            new Date(), tipoUsuario, codigoUsuario, operacion, estado, descripcion
        );
        
        bitacora[cantRegistros++] = registro;
        
        // Imprimir en consola
        System.out.println(registro.toString());
    }
    
    public RegistroBitacora[] obtenerBitacora() {
        RegistroBitacora[] resultado = new RegistroBitacora[cantRegistros];
        for (int i = 0; i < cantRegistros; i++) {
            resultado[i] = bitacora[i];
        }
        return resultado;
    }
    
    // ===== HILOS DE MONITOREO =====
    
    public void iniciarHilos() {
        if (hilosActivos) return;
        
        hilosActivos = true;
        
        // Hilo 1: Monitor de sesiones activas
        hiloSesiones = new Thread(() -> {
            while (hilosActivos) {
                try {
                    int usuariosActivos = (usuarioActual != null) ? 1 : 0;
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    System.out.println("Usuarios Activos: " + usuariosActivos + 
                        " - Última actividad: " + sdf.format(new Date()));
                    Thread.sleep(10000); // 10 segundos
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        
        // Hilo 2: Simulador de pedidos pendientes
        hiloPedidos = new Thread(() -> {
            while (hilosActivos) {
                try {
                    int pedidosPendientes = obtenerPedidosPendientes().length;
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    System.out.println("Pedidos Pendientes: " + pedidosPendientes + 
                        " - Procesando... " + sdf.format(new Date()));
                    Thread.sleep(8000); // 8 segundos
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        
        // Hilo 3: Generador de estadísticas
        hiloEstadisticas = new Thread(() -> {
            while (hilosActivos) {
                try {
                    int ventasDelDia = contarVentasConfirmadas();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    System.out.println("Ventas del día: " + ventasDelDia + 
                        " | Productos registrados: " + cantProductos + 
                        " | " + sdf.format(new Date()));
                    Thread.sleep(15000); // 15 segundos
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        
        hiloSesiones.start();
        hiloPedidos.start();
        hiloEstadisticas.start();
    }
    
    public void detenerHilos() {
        hilosActivos = false;
        if (hiloSesiones != null) hiloSesiones.interrupt();
        if (hiloPedidos != null) hiloPedidos.interrupt();
        if (hiloEstadisticas != null) hiloEstadisticas.interrupt();
    }
    
    private int contarVentasConfirmadas() {
        int contador = 0;
        for (int i = 0; i < cantPedidos; i++) {
            if (pedidos[i].getEstado().equals("CONFIRMADO")) {
                contador++;
            }
        }
        return contador;
    }
    
    // ===== SERIALIZACIÓN =====
    
    public void guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("sistema.ser"))) {
            oos.writeObject(this);
            System.out.println("Datos guardados exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al guardar datos: " + e.getMessage());
        }
    }
    
    public void cargarDatos() {
        File archivo = new File("sistema.ser");
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(archivo))) {
                Sistema sistemaGuardado = (Sistema) ois.readObject();
                
                // Copiar datos del sistema guardado
                this.usuarios = sistemaGuardado.usuarios;
                this.productos = sistemaGuardado.productos;
                this.pedidos = sistemaGuardado.pedidos;
                this.historialStock = sistemaGuardado.historialStock;
                this.bitacora = sistemaGuardado.bitacora;
                
                this.cantUsuarios = sistemaGuardado.cantUsuarios;
                this.cantProductos = sistemaGuardado.cantProductos;
                this.cantPedidos = sistemaGuardado.cantPedidos;
                this.cantMovimientos = sistemaGuardado.cantMovimientos;
                this.cantRegistros = sistemaGuardado.cantRegistros;
                
                System.out.println("Datos cargados exitosamente.");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar datos: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró archivo de datos. Iniciando sistema nuevo.");
        }
    }
    
    // ===== EXPANSIÓN DE VECTORES =====
    
    private void expandirVectorUsuarios() {
        Usuario[] nuevoVector = new Usuario[usuarios.length * 2];
        for (int i = 0; i < cantUsuarios; i++) {
            nuevoVector[i] = usuarios[i];
        }
        usuarios = nuevoVector;
    }
    
    private void expandirVectorProductos() {
        Producto[] nuevoVector = new Producto[productos.length * 2];
        for (int i = 0; i < cantProductos; i++) {
            nuevoVector[i] = productos[i];
        }
        productos = nuevoVector;
    }
    
    private void expandirVectorPedidos() {
        Pedido[] nuevoVector = new Pedido[pedidos.length * 2];
        for (int i = 0; i < cantPedidos; i++) {
            nuevoVector[i] = pedidos[i];
        }
        pedidos = nuevoVector;
    }
    
    private void expandirVectorMovimientos() {
        MovimientoStock[] nuevoVector = new MovimientoStock[historialStock.length * 2];
        for (int i = 0; i < cantMovimientos; i++) {
            nuevoVector[i] = historialStock[i];
        }
        historialStock = nuevoVector;
    }
    
    private void expandirVectorBitacora() {
        RegistroBitacora[] nuevoVector = new RegistroBitacora[bitacora.length * 2];
        for (int i = 0; i < cantRegistros; i++) {
            nuevoVector[i] = bitacora[i];
        }
        bitacora = nuevoVector;
    }
    
    // ===== GETTERS =====
    
    public int getCantUsuarios() {
        return cantUsuarios;
    }
    
    public int getCantProductos() {
        return cantProductos;
    }
    
    public int getCantPedidos() {
        return cantPedidos;
    }
    
    public Pedido[] obtenerPedidos() {
        return pedidos;
    }
}