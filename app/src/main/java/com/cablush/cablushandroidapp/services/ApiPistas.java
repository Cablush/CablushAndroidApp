package com.cablush.cablushandroidapp.services;

import com.cablush.cablushandroidapp.model.Pista;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by jonathan on 26/10/15.
 */
public interface ApiPistas {
    @GET("/pistas")
    void getPistas(@Query("name") String name,@Query("estado") String estado,@Query("esporte") String esporte, Callback<List<Pista>> pista);




}