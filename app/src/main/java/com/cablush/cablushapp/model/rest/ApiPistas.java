package com.cablush.cablushapp.model.rest;

import com.cablush.cablushapp.model.domain.Pista;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Query;

/**
 * Created by jonathan on 26/10/15.
 */
public interface ApiPistas {

    @GET("/pistas")
    void getPistas(@Query("name") String name,
                   @Query("estado") String estado,
                   @Query("esporte") String esporte,
                   Callback<List<Pista>> listCallback);

    @GET("/pistas/mine")
    void getPistas(Callback<List<Pista>> listCallback);

    @POST("/pistas")
    void createPista(@Body Pista pista, Callback<Pista> savedPista);

    @PUT("/pistas")
    void updatePista(@Body Pista pista, Callback<Pista> savedPista);

}