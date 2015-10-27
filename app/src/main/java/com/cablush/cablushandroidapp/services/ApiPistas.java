package com.cablush.cablushandroidapp.services;

import com.cablush.cablushandroidapp.model.Pista;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by jonathan on 26/10/15.
 */
public interface ApiPistas {
    @GET("/pistas")
    void getWeather(@Query("nome") String cityName,
                    Callback<Pista> pista);
}