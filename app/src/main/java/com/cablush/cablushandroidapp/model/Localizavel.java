package com.cablush.cablushandroidapp.model;

import com.google.gson.annotations.Expose;

/**
 * Created by jonathan on 09/11/15.
 */
public class Localizavel {

    @Expose
    private String uuid;
    @Expose
    private String nome;
    @Expose
    private String descricao;
    @Expose
    private String site;
    @Expose
    private String facebook;
    @Expose
    private String logo;

    @Expose
    private Horarios horario;
    @Expose
    private boolean fundo;
    @Expose
    private Local local;

    public Localizavel() {
    }

    public Localizavel(String nome, String descricao, String site, String facebook, String logo, Horarios horario, boolean fundo) {
        this.nome = nome;
        this.descricao = descricao;
        this.site = site;
        this.facebook = facebook;
        this.logo = logo;
        this.horario = horario;
        this.fundo = fundo;
    }

    public Localizavel(String nome, String descricao, String site, String facebook, String logo, Horarios horario, boolean fundo, Local local) {
        this.nome = nome;
        this.descricao = descricao;
        this.site = site;
        this.facebook = facebook;
        this.logo = logo;
        this.horario = horario;
        this.fundo = fundo;
        this.local = local;
    }

    public Localizavel(String uuid, String nome, String descricao, String site, String facebook, String logo, Horarios horario, boolean fundo, Local local) {
        this.uuid = uuid;
        this.nome = nome;
        this.descricao = descricao;
        this.site = site;
        this.facebook = facebook;
        this.logo = logo;
        this.horario = horario;
        this.fundo = fundo;
        this.local = local;
    }

    public Localizavel(String uuid, String nome, String descricao, String site, String facebook, String logo, Horarios horario, boolean fundo) {
        this.uuid = uuid;
        this.nome = nome;
        this.descricao = descricao;
        this.site = site;
        this.facebook = facebook;
        this.logo = logo;
        this.horario = horario;
        this.fundo = fundo;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public Horarios getHorario() {
        return horario;
    }

    public void setHorario(Horarios horario) {
        this.horario = horario;
    }

    public boolean isFundo() {
        return fundo;
    }

    public void setFundo(boolean fundo) {
        this.fundo = fundo;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }
}
