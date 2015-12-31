package com.cablush.cablushandroidapp.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by jonathan on 22/10/15.
 */
public class Evento implements Localizavel {

    @Expose
    private String uuid;
    @Expose
    private String nome;
    @Expose
    private String descricao;
    @Expose
    private Date hora;
    @Expose
    private Date data;
    @Expose
    private Date dataFim;
    @Expose
    private String website;
    @Expose
    private String facebook;
    @Expose
    @SerializedName("flyer_file_name")
    private String flyer;
    @Expose
    private Boolean fundo;
    @Expose
    private Local local;
    @Expose
    private List<Esporte> esportes;

    @Override
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getFlyer() {
        return flyer;
    }

    public void setFlyer(String flyer) {
        this.flyer = flyer;
    }

    public Boolean getFundo() {
        return fundo;
    }

    public void setFundo(Boolean fundo) {
        this.fundo = fundo;
    }

    @Override
    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public List<Esporte> getEsportes() {
        return esportes;
    }

    public void setEsportes(List<Esporte> esportes) {
        this.esportes = esportes;
    }
}
