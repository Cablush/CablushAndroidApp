package com.cablush.cablushapp.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.model.persistence.PistaDAO;
import com.cablush.cablushapp.model.rest.ApiPistas;
import com.cablush.cablushapp.model.rest.RestServiceBuilder;
import com.cablush.cablushapp.model.domain.Pista;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jonathan on 26/10/15.
 */
public class PistasMediator extends CablushMediator {

    /**
     *
     */
    public interface PistasMediatorListener {
        void onGetPistasResult(OperationResult result, List<Pista> pistas);
        void onSavePistasResult(OperationResult result, Pista pista);
    }

    private WeakReference<PistasMediatorListener> mListener;
    private ApiPistas apiPistas;
    private PistaDAO pistaDAO;

    /**
     *
     * @param listener
     * @param context
     */
    public PistasMediator(@NonNull PistasMediatorListener listener, @NonNull Context context) {
        super(context);
        this.mListener = new WeakReference<>(listener);
        this.apiPistas = RestServiceBuilder.createService(ApiPistas.class);
        this.pistaDAO = new PistaDAO(context);
    }

    /**
     *
     * @param pista
     */
    public void savePista(Pista pista) {
        pista = pistaDAO.save(pista);
        if (isOnline()) {
            if (pista.isRemote()) {
                updatePistaOnline(pista);
            } else {
                createPistaOnline(pista);
            }
        } else {
            sendPistaResult(pista, OperationResult.OFF_LINE);
        }
    }

    private void createPistaOnline(final Pista pista) {
        apiPistas.createPista(pista, new Callback<Pista>() {
            @Override
            public void success(Pista pistaRemote, Response response) {
                Pista pistaResult = pistaDAO.merge(pista, pistaRemote);
                sendPistaResult(pistaResult, OperationResult.ON_LINE);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error creating pista online: " + error.getMessage());
                sendPistaResult(pista, OperationResult.ERROR);
            }
        });
    }

    private void updatePistaOnline(final Pista pista) {
        apiPistas.updatePista(pista.getUuid(), pista, new Callback<Pista>() {
            @Override
            public void success(Pista pistaRemote, Response response) {
                Pista pistaResult = pistaDAO.merge(pista, pistaRemote);
                sendPistaResult(pistaResult, OperationResult.ON_LINE);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error updating pista online: " + error.getMessage());
                sendPistaResult(pista, OperationResult.ERROR);
            }
        });
    }

    private void sendPistaResult(final Pista pista, OperationResult result) {
        PistasMediatorListener listener = mListener.get();
        if (listener != null) {
            listener.onSavePistasResult(result, pista);
        }
    }

    /**
     *
     * @param name
     * @param estado
     * @param esporte
     */
    public void getPistas(final String name, final String estado, final String esporte) {
        if (isOnline()) {
            getPistasOnline(name, estado, esporte);
        } else {
            sendPistasResult(name, estado, esporte, OperationResult.OFF_LINE);
        }
    }

    private void getPistasOnline(final String name, final String estado, final String esporte) {
        apiPistas.getPistas(name, estado, esporte, new Callback<List<Pista>>() {
            @Override
            public void success(List<Pista> pistas, Response response) {
                if (!pistas.isEmpty()) {
                    pistaDAO.bulkSave(pistas);
                }
                sendPistasResult(name, estado, esporte, OperationResult.ON_LINE);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting pistas online: " + error.getMessage());
                sendPistasResult(name, estado, esporte, OperationResult.ERROR);
            }
        });
    }

    private void sendPistasResult(final String name, final String estado, final String esporte,
                                  OperationResult result) {
        PistasMediatorListener listener = mListener.get();
        if (listener != null) {
            listener.onGetPistasResult(result, pistaDAO.getPistas(name, estado, esporte));
        }
    }

    /**
     *
     */
    public void getMyPistas() {
        if (isOnline()) {
            getMyPistasOnline();
        } else {
            sendPistasResult(OperationResult.OFF_LINE);
        }
    }

    private void getMyPistasOnline() {
        apiPistas.getPistas(new Callback<List<Pista>>() {
            @Override
            public void success(List<Pista> pistas, Response response) {
                if (!pistas.isEmpty()) {
                    pistaDAO.bulkSave(pistas);
                }
                sendPistasResult(OperationResult.ON_LINE);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting my pistas online: " + error.getMessage());
                sendPistasResult(OperationResult.ERROR);
            }
        });
    }

    private void sendPistasResult(OperationResult result) {
        PistasMediatorListener listener = mListener.get();
        if (listener != null) {
            listener.onGetPistasResult(result, pistaDAO.getPistas(Usuario.LOGGED_USER.getUuid()));
        }
    }
}
