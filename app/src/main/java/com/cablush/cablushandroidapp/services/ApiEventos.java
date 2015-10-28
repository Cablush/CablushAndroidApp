package com.cablush.cablushandroidapp.services;

import com.cablush.cablushandroidapp.model.Evento;
import com.cablush.cablushandroidapp.model.Loja;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by jonathan on 26/10/15.
 */
public interface ApiEventos {
    @GET("/{name}")
    void getEventoByName(@Path("name") String name, Callback<Evento> eventoCallback);


    @GET("/esporte")
    void getEventoByEsporte(@Path("esporte") String esporte, Callback<List<Evento>> eveListCallback);

    @GET("/estado")
    void getEventoByEstado(@Path("estado") String estado, Callback<List<Evento>> eveListCallback);

}