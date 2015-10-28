package com.cablush.cablushandroidapp.model;

import com.google.gson.annotations.Expose;

import org.json.JSONObject;

/**
 * Created by jonathan on 22/10/15.
 */
public class Pista {
    @Expose
    private String nome;
    @Expose
    private String descricao;
    @Expose
    private String foto;
    @Expose
    private Local local;
    @Expose
    private boolean fundo;

    public Pista() {
    }

    public Pista(String nome, String descricao, String foto, Local local, boolean fundo) {
        this.nome = nome;
        this.descricao = descricao;
        this.foto = foto;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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
