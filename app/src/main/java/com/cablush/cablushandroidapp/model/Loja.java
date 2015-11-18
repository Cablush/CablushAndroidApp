package com.cablush.cablushandroidapp.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by jonathan on 22/10/15.
 */
public class Loja extends Localizavel {
    @Expose
    private String telefone;
    @Expose
    private String email;
    @Expose
    private List<Local> locais;

    public Loja() {
    }

    public Loja(String telefone, String email, List<Local> locais) {
        this.telefone = telefone;
        this.email = email;
        this.locais = locais;
    }

    public Loja(String nome, String descricao, String site, String facebook, String logo, Horarios horario, boolean fundo, String telefone, String email, List<Local> locais) {
        super(nome, descricao, site, facebook, logo, horario, fundo);
        this.telefone = telefone;
        this.email = email;
        this.locais = locais;
    }

    public Loja(String uuid, String nome, String descricao, String site, String facebook, String logo, Horarios horario, boolean fundo, String telefone, String email, List<Local> locais) {
        super(uuid, nome, descricao, site, facebook, logo, horario, fundo);
        this.telefone = telefone;
        this.email = email;
        this.locais = locais;
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

    public List<Local> getLocais() {
        return locais;
    }

    public void setLocais(List<Local> locais) {
        this.locais = locais;
    }
}
