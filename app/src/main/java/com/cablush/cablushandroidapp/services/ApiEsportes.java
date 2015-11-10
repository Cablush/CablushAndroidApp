package com.cablush.cablushandroidapp.services;

import com.cablush.cablushandroidapp.model.Evento;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by jonathan on 09/11/15.
 */
public interface ApiEsportes {
    @GET("/esportes")
    void getEsportes(Callback<List<String>> esportesCallback);

}
