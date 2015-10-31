package com.cablush.cablushandroidapp.services;

import com.cablush.cablushandroidapp.model.Loja;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by jonathan on 26/10/15.
 */
public interface ApiLojas {
    @GET("/lojas")
    void getLojas(@Query("nome") String nome,@Query("estado") String estado,@Query("esporte") String esporte, Callback<List<Loja>> cb);


}