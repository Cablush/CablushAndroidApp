package com.cablush.cablushapp.model;

import android.content.Context;
import android.util.Log;

import com.cablush.cablushapp.model.persistence.EventoDAO;
import com.cablush.cablushapp.model.domain.Evento;
import com.cablush.cablushapp.model.rest.ApiEventos;
import com.cablush.cablushapp.model.rest.RestServiceBuilder;

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

    public List<Evento> getEventos(String name, String estado, String esporte) {
        apiEventos.getEventos(name, estado, esporte, new Callback<List<Evento>>() {
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
        return eventoDAO.getEventos(name, estado, esporte);
    }
}
