package com.cablush.cablushapp.model.rest;

import com.cablush.cablushapp.model.domain.Loja;

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
public interface ApiLojas {

    @GET("/lojas")
    void getLojas(@Query("nome") String nome, @Query("estado") String estado, @Query("esporte") String esporte, Callback<List<Loja>> listCallback);

    @POST("/lojas")
    void createLoja(@Body Loja loja, Callback<Loja> savedLoja);

    @PUT("/lojas")
    void updateLoja(@Body Loja loja, Callback<Loja> savedLoja);

}