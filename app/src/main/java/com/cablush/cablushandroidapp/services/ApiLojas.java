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
    @GET("/lojas")
    void getLojas(@Path("nome") String nome,@Path("estado") String estado,@Path("esporte") String esporte, Callback<Loja> cb);


}