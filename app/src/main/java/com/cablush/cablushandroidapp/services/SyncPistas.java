package com.cablush.cablushandroidapp.services;

import com.cablush.cablushandroidapp.model.Loja;
import com.cablush.cablushandroidapp.model.Pista;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jonathan on 26/10/15.
 */
public class SyncPistas {
    private static ApiPistas apiPistas;

    public SyncPistas() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SyncLocalizavel.ROOT).build();

        apiPistas = restAdapter.create(ApiPistas.class);
    }

    public void getPistas(String name,String estado, String esporte) {
        apiPistas.getPistaByName(name,estado,esporte, new Callback<Pista>() {
            @Override
            public void success(Pista pista, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }



}
