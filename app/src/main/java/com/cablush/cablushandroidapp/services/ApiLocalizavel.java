package com.cablush.cablushandroidapp.services;

import com.cablush.cablushandroidapp.model.Evento;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Jonathan on 30/10/2015.
 */
public interface ApiLocalizavel {
    @GET("/")
    void getLocalizavel(@Path("tipo") String tipo,@Path("name") String name,@Path("estado") String estado,@Path("esporte") String esporte, Callback<Evento> eventoCallback);
}
