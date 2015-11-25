package com.cablush.cablushandroidapp.model;

import com.google.gson.annotations.Expose;

/**
 * Created by jonathan on 04/11/15.
 */
public class Usuario {
//access-token": "wwwww", "token-type": "Bearer", "client": "xxxxx", "expiry": "yyyyy", "uid": "zzzzz"
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
    @Expose
    private String uid;
    @Expose
    private String access_token;
    @Expose
    private String token_type;
    @Expose
    private String client;
    @Expose
    private double expiry;

    public static Usuario LOGGED_USER;

    public Usuario() {
    }

    public Usuario(String idSocial, String nome, String email, int role, String uuid, String uid, String access_token, String token_type, String client, double expiry) {
        this.idSocial = idSocial;
        this.nome = nome;
        this.email = email;
        this.role = role;
        this.uuid = uuid;
        this.uid = uid;
        this.access_token = access_token;
        this.token_type = token_type;
        this.client = client;
        this.expiry = expiry;
    }

    public Usuario(int id, String idSocial, String nome, String email, int role, String uuid, String uid, String access_token, String token_type, String client, double expiry) {
        this.id = id;
        this.idSocial = idSocial;
        this.nome = nome;
        this.email = email;
        this.role = role;
        this.uuid = uuid;
        this.uid = uid;
        this.access_token = access_token;
        this.token_type = token_type;
        this.client = client;
        this.expiry = expiry;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public double getExpiry() {
        return expiry;
    }

    public void setExpiry(double expiry) {
        this.expiry = expiry;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
