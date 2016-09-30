package com.cablush.cablushapp.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.model.persistence.EventoDAO;
import com.cablush.cablushapp.model.domain.Evento;
import com.cablush.cablushapp.model.rest.ApiEventos;
import com.cablush.cablushapp.model.rest.RestServiceBuilder;
import com.cablush.cablushapp.model.rest.dto.ResponseDTO;
import com.cablush.cablushapp.utils.DateTimeUtils;
import com.cablush.cablushapp.utils.PictureUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by jonathan on 26/10/15.
 */
public class EventosMediator extends CablushMediator {

    /**
     * Callback listener to return the result of the operations.
     */
    public interface EventosMediatorListener {
        void onGetEventosResult(OperationResult result, List<Evento> eventos);
        void onSaveEventosResult(OperationResult result, Evento evento);
    }

    private WeakReference<EventosMediatorListener> mListener;
    private ApiEventos apiEventos;
    private EventoDAO eventoDAO;

    /**
     * Constructor.
     */
    public EventosMediator(@NonNull Context context, @NonNull EventosMediatorListener listener) {
        super(context);
        this.mListener = new WeakReference<>(listener);
        this.apiEventos = RestServiceBuilder.createService(ApiEventos.class);
        this.eventoDAO = new EventoDAO(context);
    }

    /**
     * Save the evento.
     */
    public void saveEvento(Evento evento) {
        Log.d(TAG, "saveEvento()");
        evento.setChanged(true);
        evento = eventoDAO.save(evento);    // Save the evento in local database.
        if (isOnline()) {                   // Check if there is connection available,
            if (evento.isRemote()) {        // yes, then check if the evento is on remote server
                updateEventoOnline(evento); // if so, update the evento.
            } else {
                createEventoOnline(evento); // otherwise, create a new one
            }
        } else {                            // if no connections, return the OFF_LINE result
            sendEventoResult(evento, OperationResult.OFF_LINE);
        }
    }

    private void createEventoOnline(final Evento evento) {
        Log.d(TAG, "createEventoOnline()");
        apiEventos.createEvento(evento, new Callback<ResponseDTO<Evento>>() {
            @Override
            public void success(ResponseDTO<Evento> responseDTO, Response response) {
                commitOperation(evento, responseDTO.getData());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error creating evento online: " + error.getMessage());
                sendEventoResult(evento, OperationResult.ERROR);
            }
        });
    }

    private void updateEventoOnline(final Evento evento) {
        Log.d(TAG, "updateEventoOnline()");
        apiEventos.updateEvento(evento.getUuid(), evento, new Callback<ResponseDTO<Evento>>() {
            @Override
            public void success(ResponseDTO<Evento> responseDTO, Response response) {
                commitOperation(evento, responseDTO.getData());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error updating evento online: " + error.getMessage());
                sendEventoResult(evento, OperationResult.ERROR);
            }
        });
    }

    private void commitOperation(final Evento localEvento, final Evento remoteEvento) {
        Log.d(TAG, "commitOperation()");
        if (PictureUtils.fileExist(localEvento.getFlyer())) {   // Is the flyer in local filesystem?
            remoteEvento.setFlyer(localEvento.getFlyer());      // store it reference, and
            Log.d(TAG, "Uploading flyer...");                   // try to upload the flyer
            apiEventos.uploadFlyer(remoteEvento.getUuid(),
                    new TypedFile("image/jpeg", new File(localEvento.getFlyer())),
                    new Callback<ResponseDTO<Evento>>() {
                        @Override
                        public void success(ResponseDTO<Evento> responseDTO, Response response) {
                            Log.d(TAG, "Success uploading flyer");
                            // update the logo reference and save into local database
                            remoteEvento.setFlyer(responseDTO.getData().getFlyer());
                            eventoDAO.save(remoteEvento);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(TAG, "Error uploading flyer: " + error.getMessage());
                        }
                    });
        }
        // Save the remote data into local database, and return the ON_LINE result
        sendEventoResult(eventoDAO.merge(localEvento, remoteEvento), OperationResult.ON_LINE);
    }

    private void sendEventoResult(final Evento evento, OperationResult result) {
        Log.d(TAG, "sendEventoResult() - " + result.toString());
        EventosMediatorListener listener = mListener.get();
        if (listener != null) {
            listener.onSaveEventosResult(result, evento);
        }
    }

    /**
     * Get a the eventos filtering by the parameter values.
     */
    public void getEventos(final String name, final String estado, final String esporte) {
        Log.d(TAG, "getEventos()");
        // Clean the older eventos
        clearOldEventos();

        // get the eventos
        if (isOnline()) {
            getEventosOnline(name, estado, esporte);
        } else {
            sendEventosResult(name, estado, esporte, OperationResult.OFF_LINE);
        }
    }

    private void getEventosOnline(final String name, final String estado, final String esporte) {
        Log.d(TAG, "getEventosOnline()");
        apiEventos.getEventos(name, estado, esporte, new Callback<List<Evento>>() {
            @Override
            public void success(List<Evento> eventos, Response response) {
                if (!eventos.isEmpty()) {
                    Log.d(TAG, "Eventos found: " + eventos.size());
                    eventoDAO.bulkSave(eventos);
                }
                sendEventosResult(name, estado, esporte, OperationResult.ON_LINE);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting eventos online: " + error.getMessage());
                sendEventosResult(name, estado, esporte, OperationResult.ERROR);
            }
        });
    }

    private void sendEventosResult(final String name, final String estado, final String esporte,
                                   OperationResult result) {
        Log.d(TAG, "sendEventosResult() - " + result.toString());
        EventosMediatorListener listener = mListener.get();
        if (listener != null) {
            listener.onGetEventosResult(result, eventoDAO.getEventos(name, estado, esporte));
        }
    }

    /**
     * Get the current user eventos.
     */
    public void getMyEventos() {
        Log.d(TAG, "getMyEventos()");
        if (isOnline()) {
            getMyEventosOnline();
        } else {
            sendEventosResult(OperationResult.OFF_LINE);
        }
    }

    private void getMyEventosOnline() {
        Log.d(TAG, "getMyEventosOnline()");
        apiEventos.getEventos(new Callback<List<Evento>>() {
            @Override
            public void success(List<Evento> eventos, Response response) {
                if (!eventos.isEmpty()) {
                    Log.d(TAG, "Eventos found: " + eventos.size());
                    eventoDAO.bulkSave(eventos);
                }
                sendEventosResult(OperationResult.ON_LINE);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting my eventos online: " + error.getMessage());
                sendEventosResult(OperationResult.ERROR);
            }
        });
    }

    private void sendEventosResult(OperationResult result) {
        Log.d(TAG, "sendEventosResult() - " + result.toString());
        EventosMediatorListener listener = mListener.get();
        if (listener != null) {
            listener.onGetEventosResult(result, eventoDAO.getEventos(Usuario.LOGGED_USER.getUuid()));
        }
    }

    private void clearOldEventos() {
        String uuid = Usuario.LOGGED_USER != null ? Usuario.LOGGED_USER.getUuid() : null;
        eventoDAO.cleanEventos(uuid, DateTimeUtils.clearTime(new Date()));
    }
}
