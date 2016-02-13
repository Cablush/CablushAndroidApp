package com.cablush.cablushapp.model.domain;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by oscar on 12/12/15.
 */
public class Esporte implements Serializable {

    @Expose
    private Integer id;
    @Expose
    private String nome;
    @Expose
    private String categoria;
    @Expose
    private String icone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public String getCategoriaNome() {
        return categoria.equals("outros") ? nome : categoria + " - " + nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
