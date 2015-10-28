package com.cablush.cablushandroidapp.services;

import com.cablush.cablushandroidapp.model.Loja;
import com.cablush.cablushandroidapp.model.Pista;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by jonathan on 26/10/15.
 */
public interface ApiLojas {
    @GET("/{name}")
    void getLojaByName(@Path("name") String username, Callback<Loja> cb);


    @GET("/esporte")
    void getLojaByEsporte(@Path("esporte") String esporte, Callback<List<Loja>> cb);

    @GET("/estado")
    void getLojaByEstado(@Path("estado") String estado,  Callback<List<Loja>> cb);

}