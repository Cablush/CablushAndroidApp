package com.cablush.cablushandroidapp.model;

import com.google.gson.annotations.Expose;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by jonathan on 22/10/15.
 */
public class Evento extends Localizavel {
    @Expose
    private Time time;
    @Expose
    private Date date;

    public Evento() {
    }

    public Evento(String uuid, String nome, String descricao, String site, String facebook, String logo, Local local, boolean fundo, Time time, Date date) {
        super(uuid, nome, descricao, site, facebook, logo, local, fundo);
        this.time = time;
        this.date = date;
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
}
