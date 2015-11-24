package com.cablush.cablushandroidapp.services;

import com.cablush.cablushandroidapp.model.Evento;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jonathan on 30/10/2015.
 */
public class SyncLocalizavel {
    private static ApiLocalizavel apiLocalizavel;
    public static final String ROOT ="http://www.cablush.com/api";
    //public static final String ROOT= "http://192.168.0.72:3000/api";

    public SyncLocalizavel() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ROOT).build();

        apiLocalizavel = restAdapter.create(ApiLocalizavel.class);
    }


    public void getLocalizavel(String tipo,String name,String estado, String esporte) {
        apiLocalizavel.getLocalizavel(tipo, name, estado, esporte, new Callback<Evento>() {
            @Override
            public void success(Evento evento, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }
}
