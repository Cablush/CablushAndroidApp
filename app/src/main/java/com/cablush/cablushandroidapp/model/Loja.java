package com.cablush.cablushandroidapp.model;

import com.google.gson.annotations.Expose;

/**
 * Created by jonathan on 22/10/15.
 */
public class Loja {
    @Expose
    private String nome;
    @Expose
    private String descricao;
    @Expose
    private String telefone;
    @Expose
    private String email;
    @Expose
    private String site;
    @Expose
    private String facebook;
    @Expose
    private String logo;
    @Expose
    private Local local;
    @Expose
    private boolean fundo;

    public Loja() {
    }

    public Loja(String nome, String descricao, String telefone, String email, String site, String facebook, String logo, Local local, boolean fundo) {
        this.nome = nome;
        this.descricao = descricao;
        this.telefone = telefone;
        this.email = email;
        this.site = site;
        this.facebook = facebook;
        this.logo = logo;
        this.local = local;
        this.fundo = fundo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public boolean isFundo() {
        return fundo;
    }

    public void setFundo(boolean fundo) {
        this.fundo = fundo;
    }
}
