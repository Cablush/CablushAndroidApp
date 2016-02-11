package com.cablush.cablushapp.model;

import android.content.Context;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.model.persistence.LojaDAO;
import com.cablush.cablushapp.model.rest.ApiLojas;
import com.cablush.cablushapp.model.rest.RestServiceBuilder;
import com.cablush.cablushapp.model.domain.Loja;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jonathan on 26/10/15.
 */
public class LojasMediator extends CablushMediator {

    public interface LojasMediatorListener {
        void onGetLojasResult(SearchResult result, List<Loja> lojas);
    }

    private WeakReference<LojasMediatorListener> mListener;
    private ApiLojas apiLojas;
    private LojaDAO lojaDAO;

    public LojasMediator(LojasMediatorListener listener, Context context) {
        super(context);
        this.mListener = new WeakReference<>(listener);
        this.apiLojas = RestServiceBuilder.createService(ApiLojas.class);
        this.lojaDAO = new LojaDAO(context);
    }

    public void getLojas(final String name,final String estado, final String esporte) {
        if (isOnline) {
            getLojasOnline(name, estado, esporte);
        } else {
            sendLojasResult(name, estado, esporte, SearchResult.SEARCH_OFF_LINE);
        }
    }

    private void getLojasOnline(final String name,final String estado, final String esporte) {
        apiLojas.getLojas(name, estado, esporte, new Callback<List<Loja>>() {
            @Override
            public void success(List<Loja> lojas, Response response) {
                if (!lojas.isEmpty()) {
                    lojaDAO.saveLojas(lojas);
                }
                sendLojasResult(name, estado, esporte, SearchResult.SEARCH_ON_LINE);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting lojas online: " + error.getMessage());
                sendLojasResult(name, estado, esporte, SearchResult.SEARCH_ERROR);
            }
        });
    }

    private void sendLojasResult(final String name, final String estado, final String esporte,
                                 SearchResult result) {
        LojasMediatorListener listener = mListener.get();
        if (listener != null) {
            listener.onGetLojasResult(result, lojaDAO.getLojas(name, estado, esporte));
        }
    }

    public void getMyLojas() {
        if (isOnline) {
            getMyLojasOnline();
        } else {
            sendLojasResult(SearchResult.SEARCH_OFF_LINE);
        }
    }

    private void getMyLojasOnline() {
        apiLojas.getLojas(new Callback<List<Loja>>() {
            @Override
            public void success(List<Loja> lojas, Response response) {
                if (!lojas.isEmpty()) {
                    lojaDAO.saveLojas(lojas);
                }
                sendLojasResult(SearchResult.SEARCH_ON_LINE);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting my lojas online: " + error.getMessage());
                sendLojasResult(SearchResult.SEARCH_ERROR);
            }
        });
    }

    private void sendLojasResult(SearchResult result) {
        LojasMediatorListener listener = mListener.get();
        if (listener != null) {
            listener.onGetLojasResult(result, lojaDAO.getLojas(Usuario.LOGGED_USER.getUuid()));
        }
    }
}
