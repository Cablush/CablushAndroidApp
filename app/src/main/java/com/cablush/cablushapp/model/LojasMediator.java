package com.cablush.cablushapp.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.model.persistence.LojaDAO;
import com.cablush.cablushapp.model.rest.ApiLojas;
import com.cablush.cablushapp.model.rest.RestServiceBuilder;
import com.cablush.cablushapp.model.domain.Loja;
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
public class LojasMediator extends CablushMediator {

    /**
     * Callback listener to return the result of the operations.
     */
    public interface LojasMediatorListener {
        void onGetLojasResult(OperationResult result, List<Loja> lojas);
        void onSaveLojaResult(OperationResult result, Loja loja);
    }

    private WeakReference<LojasMediatorListener> mListener;
    private ApiLojas apiLojas;
    private LojaDAO lojaDAO;

    /**
     * Constructor.
     */
    public LojasMediator(@NonNull Context context, @NonNull LojasMediatorListener listener) {
        super(context);
        this.mListener = new WeakReference<>(listener);
        this.apiLojas = RestServiceBuilder.createService(ApiLojas.class);
        this.lojaDAO = new LojaDAO(context);
    }

    /**
     * Save the loja.
     */
    public void saveLoja(Loja loja) {
        Log.d(TAG, "saveLoja()");
        loja.setChanged(true);
        loja = lojaDAO.save(loja);      // Save the loja in local database.
        if (isOnline()) {               // Check if there is connection available,
            if (loja.isRemote()) {      // yes, then check if the loja is on remote server
                updateLojaOnline(loja); // if so, update the loja.
            } else {
                createLojaOnline(loja); // otherwise, create a new one
            }
        } else {                        // if no connections, return the OFF_LINE result
            sendLojaResult(loja, OperationResult.OFF_LINE);
        }
    }

    private void createLojaOnline(final Loja loja) {
        Log.d(TAG, "createLojaOnline()");
        apiLojas.createLoja(loja, new Callback<ResponseDTO<Loja>>() {
            @Override
            public void success(ResponseDTO<Loja> responseDTO, Response response) {
                commitOperation(loja, responseDTO.getData());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error creating loja online: " + error.getMessage());
                sendLojaResult(loja, OperationResult.ERROR);
            }
        });
    }

    private void updateLojaOnline(final Loja loja) {
        Log.d(TAG, "updateLojaOnline()");
        apiLojas.updateLoja(loja.getUuid(), loja, new Callback<ResponseDTO<Loja>>() {
            @Override
            public void success(ResponseDTO<Loja> responseDTO, Response response) {
                commitOperation(loja, responseDTO.getData());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error updating loja online: " + error.getMessage());
                sendLojaResult(loja, OperationResult.ERROR);
            }
        });
    }

    private void commitOperation(final Loja localLoja, final Loja remoteLoja) {
        Log.d(TAG, "commitOperation()");
        if (PictureUtils.fileExist(localLoja.getLogo())) {  // Is the logo in local filesystem?
            remoteLoja.setLogo(localLoja.getLogo());        // store it reference, and
            Log.d(TAG, "Uploading logo...");                // try to upload the logo
            apiLojas.uploadLogo(remoteLoja.getUuid(),
                new TypedFile("image/jpeg", new File(localLoja.getLogo())),
                new Callback<ResponseDTO<Loja>>() {
                    @Override
                    public void success(ResponseDTO<Loja> responseDTO, Response response) {
                        Log.d(TAG, "Success uploading logo");
                        // update the logo reference and save into local database
                        remoteLoja.setLogo(responseDTO.getData().getLogo());
                        lojaDAO.save(remoteLoja);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "Error uploading logo: " + error.getMessage());
                    }
                });
        }
        // Save the remote data into local database, and return the ON_LINE result
        sendLojaResult(lojaDAO.merge(localLoja, remoteLoja), OperationResult.ON_LINE);
    }

    private void sendLojaResult(final Loja loja, OperationResult result) {
        Log.d(TAG, "sendLojaResult() - " + result.toString());
        LojasMediatorListener listener = mListener.get();
        if (listener != null) {
            listener.onSaveLojaResult(result, loja);
        }
    }

    /**
     * Get a the lojas filtering by the parameter values.
     */
    public void getLojas(final String name,final String estado, final String esporte) {
        Log.d(TAG, "getLojas()");
        if (isOnline()) {
            getLojasOnline(name, estado, esporte);
        } else {
            sendLojasResult(name, estado, esporte, OperationResult.OFF_LINE);
        }
    }

    private void getLojasOnline(final String name,final String estado, final String esporte) {
        Log.d(TAG, "getLojasOnline()");
        apiLojas.getLojas(name, estado, esporte, new Callback<List<Loja>>() {
            @Override
            public void success(List<Loja> lojas, Response response) {
                if (!lojas.isEmpty()) {
                    Log.d(TAG, "Lojas found: " + lojas.size());
                    lojaDAO.bulkSave(lojas);
                }
                sendLojasResult(name, estado, esporte, OperationResult.ON_LINE);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting lojas online: " + error.getMessage());
                sendLojasResult(name, estado, esporte, OperationResult.ERROR);
            }
        });
    }

    private void sendLojasResult(final String name, final String estado, final String esporte,
                                 OperationResult result) {
        Log.d(TAG, "sendLojasResult() - " + result.toString());
        LojasMediatorListener listener = mListener.get();
        if (listener != null) {
            listener.onGetLojasResult(result, lojaDAO.getLojas(name, estado, esporte));
        }
    }

    /**
     * Get the current user lojas.
     */
    public void getMyLojas() {
        Log.d(TAG, "getMyLojas()");
        if (isOnline()) {
            getMyLojasOnline();
        } else {
            sendLojasResult(OperationResult.OFF_LINE);
        }
    }

    private void getMyLojasOnline() {
        Log.d(TAG, "getMyLojasOnline()");
        apiLojas.getLojas(new Callback<List<Loja>>() {
            @Override
            public void success(List<Loja> lojas, Response response) {
                if (!lojas.isEmpty()) {
                    Log.d(TAG, "Lojas found: " + lojas.size());
                    lojaDAO.bulkSave(lojas);
                }
                sendLojasResult(OperationResult.ON_LINE);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting my lojas online: " + error.getMessage());
                sendLojasResult(OperationResult.ERROR);
            }
        });
    }

    private void sendLojasResult(OperationResult result) {
        Log.d(TAG, "sendLojasResult() - " + result.toString());
        LojasMediatorListener listener = mListener.get();
        if (listener != null) {
            listener.onGetLojasResult(result, lojaDAO.getLojas(Usuario.LOGGED_USER.getUuid()));
        }
    }
}
