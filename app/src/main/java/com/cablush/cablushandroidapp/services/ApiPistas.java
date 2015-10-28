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
    @GET("/pista/{name}")
    void getPistaByName(@Path("name") String username, Callback<Pista> cb);


    @GET("/pista/esporte")
    void getPistaByEsporte(@Path("esporte") String esporte, @Query("sort") String sort, Callback<List<Pista>> cb);

    @GET("/pista/estado")
    void getPistaByEstado(@Path("estado") String estado, @Query("sort") String sort, Callback<List<Pista>> cb);

}