package com.cablush.cablushapp.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 22/10/15.
 */
public class Loja implements Localizavel, Serializable {

    @Expose
    private String uuid;
    @Expose
    private String nome;
    @Expose
    private String descricao;
    @Expose
    private String telefone;
    @Expose
    private String email;
    @Expose
    private String website;
    @Expose
    private String facebook;
    @Expose
    @SerializedName("logo_url")
    private String logo;
    @Expose
    private Boolean fundo;
    @Expose
    private List<Local> locais = new ArrayList<>();
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
    public Loja() {
        setLocal(new Local());
        horario = new Horario();
    }

    /**
     *
     * @param responsavel
     */
    public Loja(Usuario responsavel) {
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Boolean getFundo() {
        return fundo;
    }

    public void setFundo(Boolean fundo) {
        this.fundo = fundo;
    }

    public List<Local> getLocais() {
        return locais;
    }

    public void setLocais(List<Local> locais) {
        this.locais = locais;
    }

    // TODO remove when a 'loja' may have many 'locais'
    @Override
    public Local getLocal() {
        return locais.get(0);
    }

    // TODO remove when a 'loja' may have many 'locais'
    public void setLocal(Local local) {
        if (!locais.isEmpty()) {
            locais.clear();
        }
        locais.add(0, local);
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
        return logo;
    }
}
