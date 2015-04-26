/*
 * Paquete que contiene los controladores del sistema Jordan
 */
package com.sow.jordan.controladores;

import com.sow.jordan.modelos.Usuario;
import com.sow.jordan.servicios.ServicioUsuario;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
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
    
    private JavaMailSenderImpl mailSender;
    
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
    public void enviarMail(){
        String llaveSimetrica = "holamundocruel12";

        SecretKeySpec key = new SecretKeySpec(llaveSimetrica.getBytes(), "AES");
        Cipher cipher;
        
        this.usuarios=servicioUsuario.buscarPassword(usuario.getCorreo());
            
        SimpleMailMessage mail=new SimpleMailMessage();
        if(this.usuarios.isEmpty()==false){
            for (Usuario u : this.usuarios) {
                try {
                    cipher = Cipher.getInstance("AES");
                    cipher.init(Cipher.DECRYPT_MODE, key);
                    byte[] datosDecifrados = cipher.doFinal(u.getContra());
                    String contrasenia = new String(datosDecifrados);
                    mail.setTo(u.getCorreo());
                    mail.setFrom("jordan.dantm@gmail.com");
                    mail.setSubject("JORDAN");
                    mail.setText("RECUPERACION DE CONTRASEÑA\n\n\n" + "Hola " + u.getNombre() + " tu contraseña es: " + contrasenia);
                    try {
                        addMessage("hola");
                        mailSender.send(mail);
                        addMessage("Adios");
                    } catch (MailException ex) {
                        addMessage("No se pudo enviar el mensaje");
                    }
                } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                    addMessage("No se pudo desencriptar");
                }
            }
        } else {
            addMessage("El correo que proporcionaste no existe en el registro");
        }
    }
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
}
