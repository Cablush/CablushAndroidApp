package com.cablush.cablushandroidapp.model;

import com.google.gson.annotations.Expose;

/**
 * Created by jonathan on 22/10/15.
 */
public class Loja extends Localizavel {
    @Expose
    private String telefone;
    @Expose
    private String email;
    @Expose
    private Horarios horarios;

    public Loja() {
    }

    public Loja(String uuid, String nome, String descricao, String site, String facebook, String logo, Local local, boolean fundo, String telefone, String email) {
        super(uuid, nome, descricao, site, facebook, logo, local, fundo);
        this.telefone = telefone;
        this.email = email;
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

    public Horarios getHorarios() {
        return horarios;
    }

    public void setHorarios(Horarios horarios) {
        this.horarios = horarios;
    }
}
