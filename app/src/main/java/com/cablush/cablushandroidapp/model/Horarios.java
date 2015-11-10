package com.cablush.cablushandroidapp.model;

import com.google.gson.annotations.Expose;

import java.sql.Time;

/**
 * Created by jonathan on 09/11/15.
 */
public class Horarios {

    private int id;
    @Expose
    private String dias;
    @Expose
    private String periodo;
    @Expose
    private Time inicio;
    @Expose
    private Time fim;

    public Horarios() {
    }

    public Horarios(String dias, String periodo, Time inicio, Time fim) {
        this.dias = dias;
        this.periodo = periodo;
        this.inicio = inicio;
        this.fim = fim;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Time getInicio() {
        return inicio;
    }

    public void setInicio(Time inicio) {
        this.inicio = inicio;
    }

    public Time getFim() {
        return fim;
    }

    public void setFim(Time fim) {
        this.fim = fim;
    }
}
