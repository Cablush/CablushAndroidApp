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
    @GET("/eventos")
    void getEventos(@Path("name") String name,@Path("estado") String estado,@Path("esporte") String esporte, Callback<Evento> eventoCallback);


}