package com.cablush.cablushapp.model;

import android.content.Context;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Esporte;
import com.cablush.cablushapp.model.persistence.EsporteDAO;
import com.cablush.cablushapp.model.rest.ApiEsportes;
import com.cablush.cablushapp.model.rest.RestServiceBuilder;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jonathan on 11/11/2015.
 */
public class EsportesMediator {

    private static final String TAG = EsportesMediator.class.getSimpleName();

    private WeakReference<Context> mContext;
    private ApiEsportes apiEsportes;
    private EsporteDAO esporteDAO;

    public EsportesMediator(Context context) {
        this.mContext = new WeakReference<>(context);
        this.apiEsportes = RestServiceBuilder.createService(ApiEsportes.class);
        this.esporteDAO = new EsporteDAO(context);
    }

    /**
     * Load the Esportes from Webserver.
     */
    public void loadEsportes() {
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

    /**
     * Get the local stored Esportes.
     *
     * @return
     */
    public List<Esporte> getEsportes() {
        return esporteDAO.getEsportes();
    }
}
