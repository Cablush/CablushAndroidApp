package com.cablush.cablushandroidapp.model;

import com.google.gson.annotations.Expose;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by jonathan on 22/10/15.
 */
public class Evento extends Localizavel {
    @Expose
    private Time hora;
    @Expose
    private Date data;
    @Expose
    private Date dataFim;

    public Evento() {
    }

    public Evento(String uuid, String nome, String descricao, String site, String facebook, String logo, Local local, Horarios horario, boolean fundo, Time time, Date data, Date dataFim) {
        super(uuid, nome, descricao, site, facebook, logo, local, horario, fundo);
        this.hora = time;
        this.data = data;
        this.dataFim = dataFim;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time time) {
        this.hora = time;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }
}
