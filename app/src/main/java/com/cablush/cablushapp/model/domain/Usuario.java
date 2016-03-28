package com.cablush.cablushapp.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jonathan on 04/11/15.
 */
public class Usuario implements Serializable {

    @Expose
    private String uuid;

    @Expose
    private String nome;

    @Expose
    private String email;

    // Used only to send to server by json
    @Expose
    private String password;

    @Expose
    private String role;

    @Expose
    @SerializedName("authentication_token")
    private String accessToken;

    @Expose
    private List<Esporte> esportes;

    /**
     * Logged in user.
     */
    public static Usuario LOGGED_USER;

    public Usuario() {
    }

    public Usuario(String nome, String email, String password) {
        this.nome = nome;
        this.email = email;
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<Esporte> getEsportes() {
        return esportes;
    }

    public void setEsportes(List<Esporte> esportes) {
        this.esportes = esportes;
    }
}
