package com.cablush.cablushandroidapp.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cablush.cablushandroidapp.DAO.PistaDAO;
import com.cablush.cablushandroidapp.Helpers.CustomInfoWindow;
import com.cablush.cablushandroidapp.MainActivity;
import com.cablush.cablushandroidapp.R;
import com.cablush.cablushandroidapp.model.Localizavel;
import com.cablush.cablushandroidapp.model.Pista;
import com.cablush.cablushandroidapp.model.Usuario;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jonathan on 26/10/15.
 */
public class SyncPistas {
    private ApiPistas apiPistas;
    private Context context;

    public SyncPistas(Context context) {
        RequestInterceptor requestInterceptor = null;
        RestAdapter restAdapter;
        if(Usuario.LOGGED_USER != null){
            requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader(SyncLogin.ACCESS_TOKEN, Usuario.LOGGED_USER.getAccess_token());
                    request.addHeader(SyncLogin.CLIENT      , Usuario.LOGGED_USER.getClient());
                    request.addHeader(SyncLogin.EXPIRY      , ""+Usuario.LOGGED_USER.getExpiry());
                    request.addHeader(SyncLogin.TOKEN_TYPE  , Usuario.LOGGED_USER.getToken_type());
                    request.addHeader(SyncLogin.UID         , Usuario.LOGGED_USER.getUid());
                }
            };
        }
        if(requestInterceptor == null) {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(SyncLocalizavel.ROOT).build();
        }else{
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(SyncLocalizavel.ROOT)
                    .setRequestInterceptor(requestInterceptor)
                    .build();
        }

        apiPistas = restAdapter.create(ApiPistas.class);
        this.context= context;
    }


    public void getPistas(String name,String estado, String esporte) {
        apiPistas.getPistas(name,estado,esporte, new Callback<List<Pista>>() {
            @Override
            public void success(List<Pista> pistas, Response response) {
                PistaDAO pistaDAO = new PistaDAO(context);
                for(Pista pista: pistas) {
                    MainActivity.setMarker(pista,pista.getLocal(),context);
                    pistaDAO.insert(pista);
                }
            }

            @Override//
            public void failure(RetrofitError error) {
                Log.e("Pista", "Falha ao carregar as pistas");
            }
        });
    }


    public void postPistas(Pista pista) {
        apiPistas.postPistas(pista, new Callback<Pista>() {
            @Override
            public void success(Pista pista, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
