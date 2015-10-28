package com.cablush.cablushandroidapp.services;

import com.cablush.cablushandroidapp.model.Evento;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by jonathan on 26/10/15.
 */
public class SyncEventos implements ApiEventos {
    private static ApiPistas REST_CLIENT;
    private static String ROOT ="http://api.cablush.com/";

    static {
        setupRestClient();
    }

    private SyncEventos() {}

    public static ApiPistas get() {
        return REST_CLIENT;
    }

    @Override
    public void getEventoByName(@Path("name") String username, Callback<Evento> cb) {
        /*
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        */

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .build();

        ApiEventos service = retrofit.create(ApiEventos.class);
    }

    @Override
    public void getEventoByEsporte(@Path("esporte") String esporte, @Query("sort") String sort, Callback<List<Evento>> cb) {

    }

    @Override
    public void getEventoByEstado(@Path("estado") String estado, @Query("sort") String sort, Callback<List<Evento>> cb) {

    }
}
