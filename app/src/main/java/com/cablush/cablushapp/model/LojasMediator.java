package com.cablush.cablushapp.model;

import android.content.Context;
import android.util.Log;

import com.cablush.cablushapp.R;
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
public class LojasMediator {

    private static final String TAG = LojasMediator.class.getSimpleName();

    public interface LojasMediatorListener {
        void onGetLojasSucess(List<Loja> lojas);
        void onGetLojasFail(String message);
    }

    private WeakReference<LojasMediatorListener> listener;
    private WeakReference<Context> context;
    private ApiLojas apiLojas;
    private LojaDAO lojaDAO;

    public LojasMediator(LojasMediatorListener listener, Context context) {
        this.listener = new WeakReference<>(listener);
        this.context = new WeakReference<>(context);
        this.apiLojas = RestServiceBuilder.createService(ApiLojas.class);
        this.lojaDAO = new LojaDAO(context);
    }

    public void getLojas(final String name,final String estado, final String esporte){
        apiLojas.getLojas(name, estado, esporte, new Callback<List<Loja>>() {
            @Override
            public void success(List<Loja> lojas, Response response) {
                if (!lojas.isEmpty()) {
                    lojaDAO.saveLojas(lojas);
                }
                listener.get().onGetLojasSucess(lojaDAO.getLojas(name, estado, esporte));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting lojas. " + error.getMessage());
                listener.get().onGetLojasFail(context.get().getString(R.string.error_no_connection));
            }
        });
    }
}
