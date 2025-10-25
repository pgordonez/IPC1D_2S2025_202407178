package com.sancarlista.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistroBitacora implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Date fecha;
    private String tipoUsuario;
    private String codigoUsuario;
    private String operacion;
    private String estado;
    private String descripcion;
    
    public RegistroBitacora(Date fecha, String tipoUsuario, String codigoUsuario,
                           String operacion, String estado, String descripcion) {
        this.fecha = fecha;
        this.tipoUsuario = tipoUsuario;
        this.codigoUsuario = codigoUsuario;
        this.operacion = operacion;
        this.estado = estado;
        this.descripcion = descripcion;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public String getTipoUsuario() {
        return tipoUsuario;
    }
    
    public String getCodigoUsuario() {
        return codigoUsuario;
    }
    
    public String getOperacion() {
        return operacion;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return String.format("[%s] | [%s] | [%s] | [%s] | [%s] | [%s]",
            sdf.format(fecha), tipoUsuario, codigoUsuario, operacion, estado, descripcion);
    }
}