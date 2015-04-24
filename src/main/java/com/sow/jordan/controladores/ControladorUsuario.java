/*
 * Paquete que contiene los controladores del sistema Jordan
 */
package com.sow.jordan.controladores;

import com.sow.jordan.modelos.Usuario;
import com.sow.jordan.servicios.ServicioUsuario;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * Clase se encarga de conectar las vistas con los modelos del sistema, conecta
 * a los usuarios del sistema con las vistas.
 * @author GARCÍA CASTRO HÉCTOR JAVIER
 * @author LARA RAMÍREZ JOSÉ JAVIER
 * @author OLIVOS NAVARRO CESAR JONATHAN
 * @author VILLEGAS MORENO ZEUXIS DANIEL
 */
@Controller("controladorUsuario")
@Scope("session")
public class ControladorUsuario implements Serializable {
    
    @Autowired
    private ServicioUsuario servicioUsuario;
    
    private Usuario usuario;
    
    private List<Usuario> usuarios;

    @PostConstruct
    public void inicia() {
        usuarios = servicioUsuario.cargarUsuarios();
    }
    
    public void guardarUsuario(){
        this.servicioUsuario.guardarUsuario(usuario);
        usuarios = servicioUsuario.cargarUsuarios();
        this.usuario = new Usuario();
    }
    
    public void eliminarUsuario(Usuario usuario){
        this.servicioUsuario.eliminarUsuario(usuario);
        usuarios = servicioUsuario.cargarUsuarios();
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }    

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
    public void autentifica(){
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/j_spring_security_check");
            dispatcher.forward((ServletRequest) context.getRequest(),
                    (ServletResponse) context.getResponse());
            FacesContext.getCurrentInstance().responseComplete();
        } catch (ServletException | IOException ex) {
            Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
