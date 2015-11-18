package com.cablush.cablushandroidapp.model;

import com.google.gson.annotations.Expose;

import org.json.JSONObject;

/**
 * Created by jonathan on 22/10/15.
 */
public class Pista extends Localizavel {

    public Pista() {
    }

    public Pista(String nome, String descricao, String site, String facebook, String logo, Horarios horario, boolean fundo) {
        super(nome, descricao, site, facebook, logo, horario, fundo);
    }

    public Pista(String nome, String descricao, String site, String facebook, String logo, Horarios horario, boolean fundo, Local local) {
        super(nome, descricao, site, facebook, logo, horario, fundo, local);
    }

    public Pista(String uuid, String nome, String descricao, String site, String facebook, String logo, Horarios horario, boolean fundo, Local local) {
        super(uuid, nome, descricao, site, facebook, logo, horario, fundo, local);
    }

    public Pista(String uuid, String nome, String descricao, String site, String facebook, String logo, Horarios horario, boolean fundo) {
        super(uuid, nome, descricao, site, facebook, logo, horario, fundo);
    }
}
