package com.cablush.cablushandroidapp.services;

import android.util.Log;

import com.cablush.cablushandroidapp.MainActivity;
import com.cablush.cablushandroidapp.model.Evento;
import com.cablush.cablushandroidapp.model.Usuario;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jonathan on 04/11/15.
 */
public class SyncLogin {
    private static ApiLogin apiLogin;

    public SyncLogin() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SyncLocalizavel.ROOT).build();

        apiLogin = restAdapter.create(ApiLogin.class);
    }

    public void doLogin(String usuario,String senha) {
        apiLogin.doLogin(usuario, senha, new Callback<Usuario>() {
            @Override
            public void success(Usuario usuario, Response response) {


            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Usuario", "Falha ao fazer login");
            }
        });

    }
}
