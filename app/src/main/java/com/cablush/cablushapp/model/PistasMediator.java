package com.cablush.cablushapp.model;

import android.content.Context;
import android.util.Log;

import com.cablush.cablushapp.model.persistence.PistaDAO;
import com.cablush.cablushapp.model.rest.ApiPistas;
import com.cablush.cablushapp.model.rest.RestServiceBuilder;
import com.cablush.cablushapp.model.domain.Pista;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jonathan on 26/10/15.
 */
public class PistasMediator {

    private static final String TAG = PistasMediator.class.getSimpleName();

    private Context context;
    private ApiPistas apiPistas;
    private PistaDAO pistaDAO;

    public PistasMediator(Context context) {
        this.context= context;
        this.apiPistas = RestServiceBuilder.createService(ApiPistas.class);
        this.pistaDAO = new PistaDAO(context);
    }

    public List<Pista> getPistas(String name, String estado, String esporte) {
        apiPistas.getPistas(name, estado, esporte, new Callback<List<Pista>>() {
            @Override
            public void success(List<Pista> pistas, Response response) {
                if (!pistas.isEmpty()) {
                    pistaDAO.savePistas(pistas);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting pistas. " + error.getMessage());
            }
        });
        return pistaDAO.getPistas(name, estado, esporte);
    }
}
