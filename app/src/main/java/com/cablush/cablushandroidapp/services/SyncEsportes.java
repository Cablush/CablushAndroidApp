package com.cablush.cablushandroidapp.services;

import android.util.Log;

import com.cablush.cablushandroidapp.MainActivity;
import com.cablush.cablushandroidapp.model.Evento;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jonathan on 11/11/2015.
 */
public class SyncEsportes {
    private static ApiEsportes apiEsportes;

    public SyncEsportes() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SyncLocalizavel.ROOT).build();

        apiEsportes = restAdapter.create(ApiEsportes.class);
    }

    public void getEsportes() {
        apiEsportes.getEsportes(new Callback<List<String>>() {
            @Override
            public void success(List<String> esportes, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Evento", "Falha ao carregar os eventos");
            }
        });

    }
}
