package com.cablush.cablushandroidapp.model;

import com.google.gson.annotations.Expose;

import org.json.JSONObject;

/**
 * Created by jonathan on 22/10/15.
 */
public class Pista extends Localizavel {
    @Expose
    private Horarios horarios;

    public Pista() {
    }

    public Pista(String uuid, String nome, String descricao, String site, String facebook, String logo, Local local, boolean fundo, Horarios horarios) {
        super(uuid, nome, descricao, site, facebook, logo, local, fundo);
        this.horarios = horarios;
    }

    public Horarios getHorarios() {
        return horarios;
    }

    public void setHorarios(Horarios horarios) {
        this.horarios = horarios;
    }
}
