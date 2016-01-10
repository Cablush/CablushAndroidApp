package com.cablush.cablushapp.model.rest;

import com.cablush.cablushapp.model.domain.Esporte;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by jonathan on 09/11/15.
 */
public interface ApiEsportes {

    @GET("/esportes")
    void getEsportes(Callback<List<Esporte>> listCallback);

}
