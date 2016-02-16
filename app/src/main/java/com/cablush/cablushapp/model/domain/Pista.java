package com.cablush.cablushapp.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 22/10/15.
 */
public class Pista implements Localizavel, Serializable {

    @Expose
    private String uuid;

    @Expose
    private String nome;

    @Expose
    private String descricao;

    @Expose
    private String website;

    @Expose
    private String facebook;

    @Expose(serialize = false)
    @SerializedName("foto_url")
    private String foto;

    @Expose
    private Boolean fundo;

    @Expose
    private Local local;

    @Expose
    private List<Esporte> esportes = new ArrayList<>();

    @Expose
    private Horario horario;

    @Expose
    @SerializedName("responsavel_uuid")
    private String responsavel;

    /** Identify that the object is on server **/
    private Boolean remote;

    /**
     * Default constructor;
     */
    public Pista() {
        local = new Local();
        horario = new Horario();
    }

    /**
     * Constructor by responsavel.
     *
     * @param responsavel
     */
    public Pista(Usuario responsavel) {
        this();
        if (responsavel != null) {
            this.responsavel = responsavel.getUuid();
        }
    }

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

    @Override
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    @Override
    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    @Override
    public Boolean isRemote() {
        return remote;
    }

    public void setRemote(Boolean remote) {
        this.remote = remote;
    }

    @Override
    public String getImagemURL() {
        return foto;
    }
}
