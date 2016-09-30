package com.cablush.cablushapp.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.model.persistence.PistaDAO;
import com.cablush.cablushapp.model.rest.ApiPistas;
import com.cablush.cablushapp.model.rest.RestServiceBuilder;
import com.cablush.cablushapp.model.domain.Pista;
import com.cablush.cablushapp.model.rest.dto.ResponseDTO;
import com.cablush.cablushapp.utils.PictureUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by jonathan on 26/10/15.
 */
public class PistasMediator extends CablushMediator {

    /**
     * Callback listener to return the result of the operations.
     */
    public interface PistasMediatorListener {
        void onGetPistasResult(OperationResult result, List<Pista> pistas);
        void onSavePistasResult(OperationResult result, Pista pista);
    }

    private WeakReference<PistasMediatorListener> mListener;
    private ApiPistas apiPistas;
    private PistaDAO pistaDAO;

    /**
     * Constructor.
     */
    public PistasMediator(@NonNull PistasMediatorListener listener) {
        super();
        this.mListener = new WeakReference<>(listener);
        this.apiPistas = RestServiceBuilder.createService(ApiPistas.class);
        this.pistaDAO = new PistaDAO();
    }

    /**
     * Save the pista.
     */
    public void savePista(Pista pista) {
        Log.d(TAG, "savePista()");
        pista.setChanged(true);
        pista = pistaDAO.save(pista);       // Save the pista in local database.
        if (isOnline()) {                   // Check if there is connection available,
            if (pista.isRemote()) {         // yes, then check if the pista is on remote server
                updatePistaOnline(pista);   // if so, update the pista.
            } else {
                createPistaOnline(pista);   // otherwise, create a new one
            }
        } else {                            // if no connections, return the OFF_LINE result
            sendPistaResult(pista, OperationResult.OFF_LINE);
        }
    }

    private void createPistaOnline(final Pista pista) {
        Log.d(TAG, "createPistaOnline()");
        apiPistas.createPista(pista, new Callback<ResponseDTO<Pista>>() {
            @Override
            public void success(ResponseDTO<Pista> responseDTO, Response response) {
                commitOperation(pista, responseDTO.getData());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error creating pista online: " + error.getMessage());
                sendPistaResult(pista, OperationResult.ERROR);
            }
        });
    }

    private void updatePistaOnline(final Pista pista) {
        Log.d(TAG, "updatePistaOnline()");
        apiPistas.updatePista(pista.getUuid(), pista, new Callback<ResponseDTO<Pista>>() {
            @Override
            public void success(ResponseDTO<Pista> responseDTO, Response response) {
                commitOperation(pista, responseDTO.getData());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error updating pista online: " + error.getMessage());
                sendPistaResult(pista, OperationResult.ERROR);
            }
        });
    }

    private void commitOperation(final Pista localPista, final Pista remotePista) {
        Log.d(TAG, "commitOperation()");
        if (PictureUtils.fileExist(localPista.getFoto())) { // Is the foto in local filesystem?
            remotePista.setFoto(localPista.getFoto());      // store it reference, and
            Log.d(TAG, "Uploading foto...");                // try to upload the foto
            apiPistas.uploadFoto(remotePista.getUuid(),
                    new TypedFile("image/jpeg", new File(localPista.getFoto())),
                    new Callback<ResponseDTO<Pista>>() {
                        @Override
                        public void success(ResponseDTO<Pista> responseDTO, Response response) {
                            Log.d(TAG, "Success uploading foto");
                            // update the foto reference and save into local database
                            remotePista.setFoto(responseDTO.getData().getFoto());
                            pistaDAO.save(remotePista);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(TAG, "Error uploading foto: " + error.getMessage());
                        }
                    });
        }
        // Save the remote data into local database, and return the ON_LINE result
        sendPistaResult(pistaDAO.merge(localPista, remotePista), OperationResult.ON_LINE);
    }

    private void sendPistaResult(final Pista pista, OperationResult result) {
        Log.d(TAG, "sendPistaResult() - " + result.toString());
        PistasMediatorListener listener = mListener.get();
        if (listener != null) {
            listener.onSavePistasResult(result, pista);
        }
    }

    /**
     * Get a the pistas filtering by the parameter values.
     */
    public void getPistas(final String name, final String estado, final String esporte) {
        Log.d(TAG, "getPistas()");
        if (isOnline()) {
            getPistasOnline(name, estado, esporte);
        } else {
            sendPistasResult(name, estado, esporte, OperationResult.OFF_LINE);
        }
    }

    private void getPistasOnline(final String name, final String estado, final String esporte) {
        Log.d(TAG, "getPistasOnline()");
        apiPistas.getPistas(name, estado, esporte, new Callback<List<Pista>>() {
            @Override
            public void success(List<Pista> pistas, Response response) {
                if (!pistas.isEmpty()) {
                    Log.d(TAG, "Pistas found: " + pistas.size());
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
        Log.d(TAG, "sendPistasResult() - " + result.toString());
        PistasMediatorListener listener = mListener.get();
        if (listener != null) {
            listener.onGetPistasResult(result, pistaDAO.getPistas(name, estado, esporte));
        }
    }

    /**
     * Get the current user pistas.
     */
    public void getMyPistas() {
        Log.d(TAG, "getMyPistas()");
        if (isOnline()) {
            getMyPistasOnline();
        } else {
            sendPistasResult(OperationResult.OFF_LINE);
        }
    }

    private void getMyPistasOnline() {
        Log.d(TAG, "getMyPistasOnline()");
        apiPistas.getPistas(new Callback<List<Pista>>() {
            @Override
            public void success(List<Pista> pistas, Response response) {
                if (!pistas.isEmpty()) {
                    Log.d(TAG, "Pistas found: " + pistas.size());
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
        Log.d(TAG, "sendPistasResult() - " + result.toString());
        PistasMediatorListener listener = mListener.get();
        if (listener != null) {
            listener.onGetPistasResult(result, pistaDAO.getPistas(Usuario.LOGGED_USER.getUuid()));
        }
    }
}
