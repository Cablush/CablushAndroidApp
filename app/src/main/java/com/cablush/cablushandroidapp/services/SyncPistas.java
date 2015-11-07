package com.cablush.cablushandroidapp.services;

import android.util.Log;

import com.cablush.cablushandroidapp.MainActivity;
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
        apiPistas.getPistas(name,estado,esporte, new Callback<List<Pista>>() {
            @Override
            public void success(List<Pista> pistas, Response response) {
                for(Pista pista: pistas) {
                    MainActivity.setMarker(pista.getNome(), pista.getDescricao(), pista.getLocal().getLatitude(), pista.getLocal().getLongitude());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Pista", "Falha ao carregar as pistas");
            }
        });
    }


    public void postPistas(Pista pista) {
        apiPistas.postPistas("",pista, new Callback<Pista>() {
            @Override
            public void success(Pista pista, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
