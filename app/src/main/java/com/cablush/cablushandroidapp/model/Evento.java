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

    public Evento(String nome, String descricao, String site, String facebook, String logo, Horarios horario, boolean fundo, Time hora, Date data, Date dataFim) {
        super(nome, descricao, site, facebook, logo, horario, fundo);
        this.hora = hora;
        this.data = data;
        this.dataFim = dataFim;
    }

    public Evento(String nome, String descricao, String site, String facebook, String logo, Horarios horario, boolean fundo, Local local, Time hora, Date data, Date dataFim) {
        super(nome, descricao, site, facebook, logo, horario, fundo, local);
        this.hora = hora;
        this.data = data;
        this.dataFim = dataFim;
    }

    public Evento(String uuid, String nome, String descricao, String site, String facebook, String logo, Horarios horario, boolean fundo, Local local, Time hora, Date data, Date dataFim) {
        super(uuid, nome, descricao, site, facebook, logo, horario, fundo, local);
        this.hora = hora;
        this.data = data;
        this.dataFim = dataFim;
    }

    public Evento(String uuid, String nome, String descricao, String site, String facebook, String logo, Horarios horario, boolean fundo, Time hora, Date data, Date dataFim) {
        super(uuid, nome, descricao, site, facebook, logo, horario, fundo);
        this.hora = hora;
        this.data = data;
        this.dataFim = dataFim;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
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
