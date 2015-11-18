package com.cablush.cablushandroidapp.services;


import android.content.Context;
import android.util.Log;

import com.cablush.cablushandroidapp.Helpers.CustomInfoWindow;
import com.cablush.cablushandroidapp.MainActivity;
import com.cablush.cablushandroidapp.R;
import com.cablush.cablushandroidapp.model.Evento;
import com.cablush.cablushandroidapp.model.Localizavel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

    public void getEventos(String name,String estado, String esporte) {
        apiEventos.getEventos(name, estado, esporte, new Callback<List<Evento>>() {
            @Override
            public void success(List<Evento> eventos, Response response) {
                for(Evento evento: eventos) {

                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Evento", "Falha ao carregar os eventos");
            }
        });

    }


}
