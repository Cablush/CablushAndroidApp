package com.cablush.cablushapp.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Usuario;
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
public class EventosMediator extends CablushMediator {

    /**
     *
     */
    public interface EventosMediatorListener {
        void onGetEventosResult(SearchResult result, List<Evento> eventos);
    }

    private WeakReference<EventosMediatorListener> mListener;
    private ApiEventos apiEventos;
    private EventoDAO eventoDAO;

    /**
     *
     * @param listener
     * @param context
     */
    public EventosMediator(@NonNull EventosMediatorListener listener, @NonNull Context context) {
        super(context);
        this.mListener = new WeakReference<>(listener);
        this.apiEventos = RestServiceBuilder.createService(ApiEventos.class);
        this.eventoDAO = new EventoDAO(context);
    }

    /**
     *
     * @param evento
     */
    public void saveEvento(Evento evento) {
        evento = eventoDAO.save(evento);
        if (isOnline()) {
            if (evento.isRemote()) {
                updateEventoOnline(evento);
            } else {
                createEventoOnline(evento);
            }
        }
    }

    private void createEventoOnline(final Evento evento) {
        apiEventos.createEvento(evento, new Callback<Evento>() {
            @Override
            public void success(Evento eventoRemote, Response response) {
                eventoDAO.merge(evento, eventoRemote);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error creating evento online: " + error.getMessage());
            }
        });
    }

    private void updateEventoOnline(final Evento evento) {
        apiEventos.updateEvento(evento.getUuid(), evento, new Callback<Evento>() {
            @Override
            public void success(Evento eventoRemote, Response response) {
                eventoDAO.merge(evento, eventoRemote);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error updating evento online: " + error.getMessage());
            }
        });
    }

    /**
     *
     * @param name
     * @param estado
     * @param esporte
     */
    public void getEventos(final String name, final String estado, final String esporte) {
        if (isOnline()) {
            getEventosOnline(name, estado, esporte);
        } else {
            sendEventosResult(name, estado, esporte, SearchResult.SEARCH_OFF_LINE);
        }
    }

    private void getEventosOnline(final String name, final String estado, final String esporte) {
        apiEventos.getEventos(name, estado, esporte, new Callback<List<Evento>>() {
            @Override
            public void success(List<Evento> eventos, Response response) {
                if (!eventos.isEmpty()) {
                    eventoDAO.bulkSave(eventos);
                }
                sendEventosResult(name, estado, esporte, SearchResult.SEARCH_ON_LINE);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting eventos online: " + error.getMessage());
                sendEventosResult(name, estado, esporte, SearchResult.SEARCH_ERROR);
            }
        });
    }

    private void sendEventosResult(final String name, final String estado, final String esporte,
                                   SearchResult result) {
        EventosMediatorListener listener = mListener.get();
        if (listener != null) {
            listener.onGetEventosResult(result, eventoDAO.getEventos(name, estado, esporte));
        }
    }

    /**
     *
     */
    public void getMyEventos() {
        if (isOnline()) {
            getMyEventosOnline();
        } else {
            sendEventosResult(SearchResult.SEARCH_OFF_LINE);
        }
    }

    private void getMyEventosOnline() {
        apiEventos.getEventos(new Callback<List<Evento>>() {
            @Override
            public void success(List<Evento> eventos, Response response) {
                if (!eventos.isEmpty()) {
                    eventoDAO.bulkSave(eventos);
                }
                sendEventosResult(SearchResult.SEARCH_ON_LINE);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting my eventos online: " + error.getMessage());
                sendEventosResult(SearchResult.SEARCH_ERROR);
            }
        });
    }

    private void sendEventosResult(SearchResult result) {
        EventosMediatorListener listener = mListener.get();
        if (listener != null) {
            listener.onGetEventosResult(result, eventoDAO.getEventos(Usuario.LOGGED_USER.getUuid()));
        }
    }
}
