package com.cablush.cablushapp.model.domain;

import com.cablush.cablushapp.utils.DateTimeUtils;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jonathan on 09/11/15.
 */
public class Horario implements Serializable {

    private String uuidLocalizavel;
    @Expose
    private Date inicio;
    @Expose
    private Date fim;
    @Expose
    private Boolean seg;
    @Expose
    private Boolean ter;
    @Expose
    private Boolean qua;
    @Expose
    private Boolean qui;
    @Expose
    private Boolean sex;
    @Expose
    private Boolean sab;
    @Expose
    private Boolean dom;
    @Expose
    private String detalhes;

    public String getUuidLocalizavel() {
        return uuidLocalizavel;
    }

    public void setUuidLocalizavel(String uuidLocalizavel) {
        this.uuidLocalizavel = uuidLocalizavel;
    }

    public Date getInicio() {
        if (inicio == null || inicio.getTime() == 0) {
            inicio = DateTimeUtils.parseTime("09:00");
        }
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFim() {
        if (fim == null || fim.getTime() == 0) {
            fim = DateTimeUtils.parseTime("18:00");
        }
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
    }

    public Boolean getSeg() {
        if (seg == null) {
            seg = Boolean.FALSE;
        }
        return seg;
    }

    public void setSeg(Boolean seg) {
        this.seg = seg;
    }

    public Boolean getTer() {
        if (ter == null) {
            ter = Boolean.FALSE;
        }
        return ter;
    }

    public void setTer(Boolean ter) {
        this.ter = ter;
    }

    public Boolean getQua() {
        if (qua == null) {
            qua = Boolean.FALSE;
        }
        return qua;
    }

    public void setQua(Boolean qua) {
        this.qua = qua;
    }

    public Boolean getQui() {
        if (qui == null) {
            qui = Boolean.FALSE;
        }
        return qui;
    }

    public void setQui(Boolean qui) {
        this.qui = qui;
    }

    public Boolean getSex() {
        if (sex == null) {
            sex = Boolean.FALSE;
        }
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Boolean getSab() {
        if (sab == null) {
            sab = Boolean.FALSE;
        }
        return sab;
    }

    public void setSab(Boolean sab) {
        this.sab = sab;
    }

    public Boolean getDom() {
        if (dom == null) {
            dom = Boolean.FALSE;
        }
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
        return getSeg() || getTer() || getQua() || getQui() || getSex() || getSab() || getDom();
    }
}
