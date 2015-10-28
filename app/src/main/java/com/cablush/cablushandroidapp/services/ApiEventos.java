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
    @GET("/evento/{name}")
    void getEventoByName(@Path("name") String username, Callback<Evento> cb);


    @GET("/evento/esporte")
    void getEventoByEsporte(@Path("esporte") String esporte, @Query("sort") String sort, Callback<List<Evento>> cb);

    @GET("/evento/estado")
    void getEventoByEstado(@Path("estado") String estado, @Query("sort") String sort, Callback<List<Evento>> cb);

}