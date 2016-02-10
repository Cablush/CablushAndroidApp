package com.cablush.cablushapp.model;

import android.content.Context;
import android.util.Log;

import com.cablush.cablushapp.R;
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
public class LojasMediator {

    private static final String TAG = LojasMediator.class.getSimpleName();

    public interface LojasMediatorListener {
        void onGetLojasSucess(List<Loja> lojas);
        void onGetLojasFail(String message);
    }

    private WeakReference<LojasMediatorListener> mListener;
    private WeakReference<Context> mContext;
    private ApiLojas apiLojas;
    private LojaDAO lojaDAO;

    public LojasMediator(LojasMediatorListener listener, Context context) {
        this.mListener = new WeakReference<>(listener);
        this.mContext = new WeakReference<>(context);
        this.apiLojas = RestServiceBuilder.createService(ApiLojas.class);
        this.lojaDAO = new LojaDAO(context);
    }

    public void getLojas(final String name,final String estado, final String esporte) {
        apiLojas.getLojas(name, estado, esporte, new Callback<List<Loja>>() {
            @Override
            public void success(List<Loja> lojas, Response response) {
                if (!lojas.isEmpty()) {
                    lojaDAO.saveLojas(lojas);
                }
                LojasMediatorListener listener = mListener.get();
                if (listener != null) {
                   listener.onGetLojasSucess(lojaDAO.getLojas(name, estado, esporte));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting lojas. " + error.getMessage());
                LojasMediatorListener listener = mListener.get();
                Context context = mContext.get();
                if (listener != null && context != null) {
                    listener.onGetLojasFail(context.getString(R.string.error_no_connection));
                }
            }
        });
    }

    public void getMyLojas() {
        apiLojas.getLojas(new Callback<List<Loja>>() {
            @Override
            public void success(List<Loja> lojas, Response response) {
                if (!lojas.isEmpty()) {
                    lojaDAO.saveLojas(lojas);
                }
                LojasMediatorListener listener = mListener.get();
                if (listener != null) {
                    listener.onGetLojasSucess(lojaDAO.getLojas(Usuario.LOGGED_USER.getUuid()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting my lojas. " + error.getMessage());
                LojasMediatorListener listener = mListener.get();
                Context context = mContext.get();
                if (listener != null && context != null) {
                    listener.onGetLojasFail(context.getString(R.string.error_no_connection));
                }
            }
        });
    }
}
