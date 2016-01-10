package com.cablush.cablushapp.model;

import android.content.Context;
import android.util.Log;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.persistence.EventoDAO;
import com.cablush.cablushapp.model.domain.Evento;
import com.cablush.cablushapp.model.rest.ApiEventos;
import com.cablush.cablushapp.model.rest.RestServiceBuilder;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jonathan on 26/10/15.
 */
public class EventosMediator {

    private static final String TAG = EventosMediator.class.getSimpleName();

    public interface EventosMediatorListener {
        void onGetEventosSucess(List<Evento> eventos);
        void onGetEventosFail(String message);
    }

    private WeakReference<EventosMediatorListener> listener;
    private WeakReference<Context> context;
    private ApiEventos apiEventos;
    private EventoDAO eventoDAO;

    public EventosMediator(EventosMediatorListener listener, Context context) {
        this.listener = new WeakReference<>(listener);
        this.context = new WeakReference<>(context);
        this.apiEventos = RestServiceBuilder.createService(ApiEventos.class);
        this.eventoDAO = new EventoDAO(context);
    }

    public void getEventos(final String name, final String estado, final String esporte) {
        apiEventos.getEventos(name, estado, esporte, new Callback<List<Evento>>() {
            @Override
            public void success(List<Evento> eventos, Response response) {
                if (!eventos.isEmpty()) {
                    eventoDAO.saveEventos(eventos);
                }
                listener.get().onGetEventosSucess(eventoDAO.getEventos(name, estado, esporte));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting eventos. " + error.getMessage());
                listener.get().onGetEventosFail(context.get().getString(R.string.error_no_connection));
            }
        });
    }
}
