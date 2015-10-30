package com.cablush.cablushandroidapp.services;

import com.cablush.cablushandroidapp.DAO.EventoDAO;
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

    public SyncEventos() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SyncLocalizavel.ROOT).build();

        apiEventos = restAdapter.create(ApiEventos.class);
    }

    public void getEventos(String name,String estado, String esporte) {
        apiEventos.getEventos(name, estado, esporte, new Callback<Evento>() {
            @Override
            public void success(Evento evento, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }


}
