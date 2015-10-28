package com.cablush.cablushandroidapp.services;

import com.cablush.cablushandroidapp.model.Evento;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jonathan on 26/10/15.
 */
public class SyncEventos {
    private static ApiEventos apiEventos;
    private static String ROOT ="http://api.cablush.com/evento";

    private SyncEventos() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ROOT).build();

        apiEventos = restAdapter.create(ApiEventos.class);
    }




    public void getEventoByName(String name) {
        apiEventos.getEventoByName(name, new Callback<Evento>() {
            @Override
            public void success(Evento evento, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }


    public void getEventoByEsporte(String esporte) {
        apiEventos.getEventoByEsporte(esporte, new Callback<List<Evento>>() {
            @Override
            public void success(List<Evento> eventos, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    public void getEventoByEstado(String estado) {
        apiEventos.getEventoByEstado(estado, new Callback<List<Evento>>() {
            @Override
            public void success(List<Evento> eventos, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }
}
