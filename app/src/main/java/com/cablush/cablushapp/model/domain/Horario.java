package com.cablush.cablushapp.model.domain;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jonathan on 09/11/15.
 */
public class Horario implements Serializable {

    public static final DateFormat FORMAT_TIME = new SimpleDateFormat("HH:mm");
    public static final DateFormat FORMAT_DATE = new SimpleDateFormat("dd/MM/yyyy");

    private String uuidLocalizavel;
    @Expose
    private Date inicio;
    @Expose
    private Date fim;
    @Expose
    private Boolean seg = false;
    @Expose
    private Boolean ter = false;
    @Expose
    private Boolean qua = false;
    @Expose
    private Boolean qui = false;
    @Expose
    private Boolean sex = false;
    @Expose
    private Boolean sab = false;
    @Expose
    private Boolean dom = false;
    @Expose
    private String detalhes;

    public String getUuidLocalizavel() {
        return uuidLocalizavel;
    }

    public void setUuidLocalizavel(String uuidLocalizavel) {
        this.uuidLocalizavel = uuidLocalizavel;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFim() {
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
    }

    public Boolean getSeg() {
        return seg;
    }

    public void setSeg(Boolean seg) {
        this.seg = seg;
    }

    public Boolean getTer() {
        return ter;
    }

    public void setTer(Boolean ter) {
        this.ter = ter;
    }

    public Boolean getQua() {
        return qua;
    }

    public void setQua(Boolean qua) {
        this.qua = qua;
    }

    public Boolean getQui() {
        return qui;
    }

    public void setQui(Boolean qui) {
        this.qui = qui;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Boolean getSab() {
        return sab;
    }

    public void setSab(Boolean sab) {
        this.sab = sab;
    }

    public Boolean getDom() {
        return dom;
    }

    public void setDom(Boolean dom) {
        this.dom = dom;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public boolean isOpenOneDay(){
        return seg || ter || qua || qui || sex || sab || dom;
    }
}
