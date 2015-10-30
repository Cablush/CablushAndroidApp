package com.cablush.cablushandroidapp.services;

import com.cablush.cablushandroidapp.MainActivity;
import com.cablush.cablushandroidapp.model.Local;
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
public class SyncLojas {
    private static ApiLojas apiLojas;


    public SyncLojas() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SyncLocalizavel.ROOT).build();

        apiLojas = restAdapter.create(ApiLojas.class);
    }

    public void getLojas(String name,String estado, String esporte){
        Local local = new Local(-19.7928431,-43.8830101, "lograoudor", "10", "complemento","bairro","cidade","estado","cep","pais");
        Loja l = new Loja("nome", "descricao", "telefone", "email", "site", "facebook", "logo", local, true);
        MainActivity.setMarker("retorno das lojas","nome",-19.7928431,-43.8830101);

        apiLojas.getLojas(name, estado, esporte, new Callback<Loja>() {
            @Override
            public void success(Loja loja, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
