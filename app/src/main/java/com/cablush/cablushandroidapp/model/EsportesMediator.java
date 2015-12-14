package com.cablush.cablushandroidapp.model;

import android.content.Context;
import android.util.Log;

import com.cablush.cablushandroidapp.model.domain.Esporte;
import com.cablush.cablushandroidapp.model.persistence.EsporteDAO;
import com.cablush.cablushandroidapp.model.rest.ApiEsportes;
import com.cablush.cablushandroidapp.model.rest.RestServiceBuilder;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jonathan on 11/11/2015.
 */
public class EsportesMediator {

    private static final String TAG = EsportesMediator.class.getSimpleName();

    private Context context;
    private static ApiEsportes apiEsportes;
    private EsporteDAO esporteDAO;

    public EsportesMediator(Context context) {
        this.context = context;
        this.apiEsportes = RestServiceBuilder.createService(ApiEsportes.class);
        this.esporteDAO = new EsporteDAO(context);
    }

    public void getEsportes() {
        apiEsportes.getEsportes(new Callback<List<Esporte>>() {
            @Override
            public void success(List<Esporte> esportes, Response response) {
                if (!esportes.isEmpty()) {
                    esporteDAO.deleteEsportes();
                    esporteDAO.saveEsportes(esportes);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting esportes. " + error.getMessage());
            }
        });

    }
}
