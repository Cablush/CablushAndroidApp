package com.cablush.cablushandroidapp.services;

import android.content.Context;
import android.util.Log;

import com.cablush.cablushandroidapp.DAO.LojaDAO;
import com.cablush.cablushandroidapp.Helpers.CustomInfoWindow;
import com.cablush.cablushandroidapp.MainActivity;
import com.cablush.cablushandroidapp.R;
import com.cablush.cablushandroidapp.model.Local;
import com.cablush.cablushandroidapp.model.Localizavel;
import com.cablush.cablushandroidapp.model.Loja;
import com.cablush.cablushandroidapp.model.Pista;
import com.cablush.cablushandroidapp.model.Usuario;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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
public class SyncLojas {
    private ApiLojas apiLojas;
    private Context context;
    public SyncLojas(Context context) {
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

        apiLojas = restAdapter.create(ApiLojas.class);
        this.context = context;
    }

    public void getLojas(String name,String estado, String esporte){
        apiLojas.getLojas(name, estado, esporte, new Callback<List<Loja>>() {
            @Override
            public void success(List<Loja> lojas, Response response) {
                for(Loja loja: lojas) {
                    LojaDAO lojaDAO = new LojaDAO(context);
                    for(Local local : loja.getLocais()) {
                        MainActivity.setMarker(loja, local,context);
                        lojaDAO.insert(loja);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Lojas","Falha ao buscar Lojas");
            }
        });
    }
}
