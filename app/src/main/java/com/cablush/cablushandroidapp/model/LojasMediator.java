package com.cablush.cablushandroidapp.model;

import android.content.Context;
import android.util.Log;

import com.cablush.cablushandroidapp.model.persistence.LojaDAO;
import com.cablush.cablushandroidapp.model.rest.ApiLojas;
import com.cablush.cablushandroidapp.model.rest.RestServiceBuilder;
import com.cablush.cablushandroidapp.model.domain.Loja;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jonathan on 26/10/15.
 */
public class LojasMediator {

    private static final String TAG = LojasMediator.class.getSimpleName();

    private Context context;
    private ApiLojas apiLojas;
    private LojaDAO lojaDAO;

    public LojasMediator(Context context) {
        this.context = context;
        this.apiLojas = RestServiceBuilder.createService(ApiLojas.class);
        this.lojaDAO = new LojaDAO(context);
    }

    public List<Loja> getLojas(String name,String estado, String esporte){
        apiLojas.getLojas(name, estado, esporte, new Callback<List<Loja>>() {
            @Override
            public void success(List<Loja> lojas, Response response) {
                if (!lojas.isEmpty()) {
                    lojaDAO.saveLojas(lojas);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting lojas. " + error.getMessage());
            }
        });
        return lojaDAO.getLojas();  // TODO filter lojas search
    }
}
