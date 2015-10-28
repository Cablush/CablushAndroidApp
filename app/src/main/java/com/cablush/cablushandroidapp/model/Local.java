package com.cablush.cablushandroidapp.model;

import com.google.gson.annotations.Expose;

/**
 * Created by jonathan on 22/10/15.
 */
public class Local {
    @Expose
    private int id;
    @Expose
    private double latitude;
    @Expose
    private double longitude;
    @Expose
    private String logradouro;
    @Expose
    private String numero;
    @Expose
    private String complemento;
    @Expose
    private String bairro;
    @Expose
    private String cidade;
    @Expose
    private String estado;
    @Expose
    private String cep;
    @Expose
    private String pais;

    public Local() {
    }

    public Local(int id, double latitude, double longitude, String logradouro, String numero, String complemento, String bairro, String cidade, String estado, String cep, String pais) {
        this.id          = id;
        this.latitude    = latitude;
        this.longitude   = longitude;
        this.logradouro  = logradouro;
        this.numero      = numero;
        this.complemento = complemento;
        this.bairro      = bairro;
        this.cidade      = cidade;
        this.estado      = estado;
        this.cep         = cep;
        this.pais        = pais;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
