package com.cablush.cablushandroidapp.services;

import android.util.Log;

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
        apiLojas.getLojas(name, estado, esporte, new Callback<List<Loja>>() {
            @Override
            public void success(List<Loja> lojas, Response response) {
                for(Loja loja: lojas) {
                    MainActivity.setMarker(loja.getNome(), loja.getDescricao(), loja.getLocal().getLatitude(), loja.getLocal().getLongitude());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Lojas","Falha ao buscar Lojas");
            }
        });
    }
}
