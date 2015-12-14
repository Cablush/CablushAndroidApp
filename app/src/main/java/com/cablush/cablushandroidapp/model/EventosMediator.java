package com.cablush.cablushandroidapp.model;

import android.content.Context;
import android.util.Log;

import com.cablush.cablushandroidapp.model.persistence.EventoDAO;
import com.cablush.cablushandroidapp.model.domain.Evento;
import com.cablush.cablushandroidapp.model.rest.ApiEventos;
import com.cablush.cablushandroidapp.model.rest.RestServiceBuilder;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jonathan on 26/10/15.
 */
public class EventosMediator {

    private static final String TAG = EventosMediator.class.getSimpleName();

    private Context context;
    private ApiEventos apiEventos;
    private EventoDAO eventoDAO;

    public EventosMediator(Context context) {
        this.context = context;
        this.apiEventos = RestServiceBuilder.createService(ApiEventos.class);
        this.eventoDAO = new EventoDAO(context);
    }

    public List<Evento> getEventos(String nome, String estado, String esporte) {
        apiEventos.getEventos(nome, estado, esporte, new Callback<List<Evento>>() {
            @Override
            public void success(List<Evento> eventos, Response response) {
                if (!eventos.isEmpty()) {
                    eventoDAO.saveEventos(eventos);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting eventos. " + error.getMessage());
            }
        });
        return eventoDAO.getEventos(); // TODO filter eventos search
    }
}
