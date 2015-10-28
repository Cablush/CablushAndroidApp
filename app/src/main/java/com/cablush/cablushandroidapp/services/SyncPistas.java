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
    private static String ROOT ="http://api.cablush.com/pista";
    private SyncPistas() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ROOT).build();

        apiPistas = restAdapter.create(ApiPistas.class);
    }

    public void getLojaByName(String name){
        apiPistas.getPistaByName(name, new Callback<Pista>() {
            @Override
            public void success(Pista pista, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void getLojaByEsporte(String esporte){

        apiPistas.getPistaByEsporte(esporte, new Callback<List<Pista>>() {
            @Override
            public void success(List<Pista> pistas, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void getLojaByEstado(String estado){

        apiPistas.getPistaByEstado(estado, new Callback<List<Pista>>() {
            @Override
            public void success(List<Pista> pistas, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

}
