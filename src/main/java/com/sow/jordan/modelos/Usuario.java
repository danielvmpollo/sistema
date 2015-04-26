/*
 * Paquete que contiene los modelos del sistema Jordan
 */
package com.sow.jordan.modelos;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Clase que almacena la información de los usuarios del sistema.
 * @author GARCÍA CASTRO HÉCTOR JAVIER
 * @author LARA RAMÍREZ JOSÉ JAVIER
 * @author OLIVOS NAVARRO CESAR JONATHAN
 * @author VILLEGAS MORENO ZEUXIS DANIEL
 */
@Entity
@Table(name = "Usuario")
public class Usuario implements Serializable {

    @Id
    private String usuario;

    @NotNull
    private String contrasena;

    @NotNull
    private String nombre;

    @NotNull
    private String correo;

    @NotNull
    private String privilegio;
    
    @NotNull 
   private Boolean activo;

    @OneToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            mappedBy = "usuario",
            orphanRemoval = true)
    private  List<Comentario> comentarios;
    
    private byte[] contra;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        String llaveSimetrica = "holamundocruel12";
        SecretKeySpec key = new SecretKeySpec(llaveSimetrica.getBytes(), "AES");
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES");

            //Comienzo a encriptar
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] campoCifrado = cipher.doFinal(contrasena.getBytes());
            this.contra=campoCifrado;
            this.contrasena = new String (campoCifrado);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPrivilegio() {
        return privilegio;
    }

    public byte[] getContra() {
        return contra;
    }

    public void setContra(byte[] contra) {
        this.contra = contra;
    }

    public void setPrivilegio(String privilegio) {
        this.privilegio = privilegio;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public  List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios( List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }
    
}
