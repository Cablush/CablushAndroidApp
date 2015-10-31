package com.cablush.cablushandroidapp.services;

import com.cablush.cablushandroidapp.model.Pista;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by jonathan on 26/10/15.
 */
public interface ApiPistas {
    @GET("/pistas")
    void getPistas(@Path("name") String name,@Path("estado") String estado,@Path("esporte") String esporte, Callback<Pista> pista);




}