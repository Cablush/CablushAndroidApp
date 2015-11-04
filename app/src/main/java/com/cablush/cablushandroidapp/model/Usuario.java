package com.cablush.cablushandroidapp.model;

import com.google.gson.annotations.Expose;

/**
 * Created by jonathan on 04/11/15.
 */
public class Usuario {

    private int id;//Id do banco do celular e não Id real
    @Expose
    private String idSocial;
    @Expose
    private String nome;
    @Expose
    private String email;
    @Expose
    private int role;
    @Expose
    private String uuid;

    public Usuario() {
    }

    public Usuario(int id, String idSocial, String nome, String email, int role, String uuid) {
        this.id = id;
        this.idSocial = idSocial;
        this.nome = nome;
        this.email = email;
        this.role = role;
        this.uuid = uuid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdSocial() {
        return idSocial;
    }

    public void setIdSocial(String idSocial) {
        this.idSocial = idSocial;
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

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
