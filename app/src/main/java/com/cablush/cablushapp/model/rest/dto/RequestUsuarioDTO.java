package com.cablush.cablushapp.model.rest.dto;

import com.cablush.cablushapp.model.domain.Usuario;
import com.google.gson.annotations.Expose;

/**
 * DTO to encapsulates the Usuario's JSON requests.
 */
public class RequestUsuarioDTO {

    @Expose
    private Usuario usuario;

    public RequestUsuarioDTO() {
    }

    /**
     * Constructor to Signin
     *
     * @param email
     * @param password
     */
    public RequestUsuarioDTO(String email, String password) {
        this.usuario = new Usuario(null, email, password);
    }

    /**
     * Constructor to Signup
     *
     * @param name
     * @param email
     * @param password
     */
    public RequestUsuarioDTO(String name, String email, String password) {
        this.usuario = new Usuario(name, email, password);
    }

    /**
     * Constructor to Reset Password
     *
     * @param email
     */
    public RequestUsuarioDTO(String email) {
        this.usuario = new Usuario(null, email, null);
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
