package com.cablush.cablushapp.model.domain;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by jonathan on 22/10/15.
 */
public class Local implements Serializable {

    private String uuidLocalizavel;
    @Expose
    private Double latitude;
    @Expose
    private Double longitude;
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

    public String getUuidLocalizavel() {
        return uuidLocalizavel;
    }

    public void setUuidLocalizavel(String uuidLocalizavel) {
        this.uuidLocalizavel = uuidLocalizavel;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
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

    public LatLng getLatLng() {
        if (latitude != null && longitude != null) {
            return new LatLng(latitude, longitude);
        }
        return null;
    }

    public void setLatLng(LatLng latLng) {
        latitude = latLng.latitude;
        longitude = latLng.longitude;
    }

    public String getEndereco() {
        return logradouro
                + (numero != null && !numero.isEmpty() ? ", " + numero : "")
                + (complemento != null && !complemento.isEmpty() ? ", " + complemento : "")
                + (bairro != null && !bairro.isEmpty() ? ", " + bairro : "");
    }

    public String getCidadeEstado() {
        return cidade + " / " + estado;
    }
}
