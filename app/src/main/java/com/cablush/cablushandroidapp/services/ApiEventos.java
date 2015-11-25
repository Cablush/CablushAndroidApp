package com.cablush.cablushandroidapp.services;

import com.cablush.cablushandroidapp.model.Evento;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by jonathan on 26/10/15.
 */
public interface ApiEventos {
    @GET("/eventos")
    void getEventos(@Query("name") String name,@Query("estado") String estado,@Query("esporte") String esporte, Callback<List<Evento>> eventoCallback);

    @POST("/eventos")
    void postEventos(@Body Evento eventos, Callback<Evento> savedPista);

}