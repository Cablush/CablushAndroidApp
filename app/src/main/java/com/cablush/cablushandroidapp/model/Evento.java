package com.cablush.cablushandroidapp.model;

import com.google.gson.annotations.Expose;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by jonathan on 22/10/15.
 */
public class Evento {
    @Expose
    private String nome;
    @Expose
    private String descricao;
    @Expose
    private Time time;
    @Expose
    private Date date;
    @Expose
    private String cartaz;
    @Expose
    private Local local;
    @Expose
    private boolean fundo;

    public Evento() {
    }

    public Evento(String nome, String descricao, Time time, Date date, String cartaz,Local local,boolean fundo) {
        this.nome       = nome;
        this.descricao  = descricao;
        this.time       = time;
        this.date       = date;
        this.cartaz     = cartaz;
        this.local      = local;
        this.fundo      = fundo;
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

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCartaz() {
        return cartaz;
    }

    public void setCartaz(String cartaz) {
        this.cartaz = cartaz;
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
