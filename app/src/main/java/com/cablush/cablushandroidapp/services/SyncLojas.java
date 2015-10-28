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
public class SyncLojas {
    private static ApiLojas apiLojas;
    private static String ROOT ="http://api.cablush.com/loja";

    private SyncLojas() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ROOT).build();

        apiLojas = restAdapter.create(ApiLojas.class);
    }

    public void getLojaByName(String name){
        apiLojas.getLojaByName(name, new Callback<Loja>() {
            @Override
            public void success(Loja loja, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void getLojaByEsporte(String esporte){

        apiLojas.getLojaByEsporte(esporte, new Callback<List<Loja>>() {
            @Override
            public void success(List<Loja> lojas, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void getLojaByEstado(String estado){

        apiLojas.getLojaByEstado(estado, new Callback<List<Loja>>() {
            @Override
            public void success(List<Loja> lojas, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
