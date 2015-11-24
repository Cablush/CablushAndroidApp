package com.cablush.cablushandroidapp.services;

import android.content.Context;
import android.util.Log;

import com.cablush.cablushandroidapp.DAO.UsuarioDAO;
import com.cablush.cablushandroidapp.MainActivity;
import com.cablush.cablushandroidapp.model.Evento;
import com.cablush.cablushandroidapp.model.Usuario;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

/**
 * Created by jonathan on 04/11/15.
 */
public class SyncLogin {
    private static ApiLogin apiLogin;
    private static final String ACCESS_TOKEN = "Access-Token";
    private static final String TOKEN_TYPE = "Token-Type";
    private static final String CLIENT = "Client";
    private static final String EXPIRY = "expiry";
    private static final String UID = "uid";
    private Context context;
    public SyncLogin(Context context) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SyncLocalizavel.ROOT).build();

        apiLogin = restAdapter.create(ApiLogin.class);
        this.context = context;
    }

    public void doLogin(String usuario,String senha) {
        apiLogin.doLogin(usuario, senha, new Callback<Usuario>() {
            @Override
            public void success(Usuario usuario, Response response) {
                List<Header> headerList = response.getHeaders();
                for(Header header : headerList) {
                    switch (header.getName()){
                        case ACCESS_TOKEN:
                            usuario.setAccess_token(header.getValue());
                            break;
                        case TOKEN_TYPE:
                            usuario.setToken_type(header.getValue());
                            break;
                        case CLIENT:
                            usuario.setClient(header.getValue());
                            break;
                        case EXPIRY:
                            usuario.setExpiry(Double.parseDouble(header.getValue()));
                            break;
                        case UID:
                            usuario.setUid(header.getValue());
                            break;
                    }
                }
                UsuarioDAO usuarioDAO = new UsuarioDAO(context);
                usuarioDAO.insert(usuario);

            }

            @Override
            public void failure(RetrofitError error) {

                Log.e("Usuario", "Falha ao fazer login");
            }
        });

    }
}
