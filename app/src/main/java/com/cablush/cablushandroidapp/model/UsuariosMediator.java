package com.cablush.cablushandroidapp.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cablush.cablushandroidapp.model.persistence.UsuarioDAO;
import com.cablush.cablushandroidapp.R;
import com.cablush.cablushandroidapp.model.domain.Usuario;
import com.cablush.cablushandroidapp.model.rest.ApiUsuario;
import com.cablush.cablushandroidapp.model.rest.RestServiceBuilder;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

/**
 * Created by jonathan on 04/11/15.
 */
public class UsuariosMediator {

    private static final String TAG = UsuariosMediator.class.getSimpleName();

    private Context context;
    private ApiUsuario apiUsuario;
    private UsuarioDAO usuarioDAO;

    public UsuariosMediator(Context context) {
        this.context = context;
        this.apiUsuario = RestServiceBuilder.createService(ApiUsuario.class);
        this.usuarioDAO = new UsuarioDAO(context);
    }

    public boolean doLogin(String email, String senha) {
        apiUsuario.doLogin(email, senha, new Callback<Usuario>() {
            @Override
            public void success(Usuario usuario, Response response) {
                usuario = updateAuthData(usuario, response);
                usuarioDAO.saveUsuario(usuario);
                Usuario.LOGGED_USER = usuario;
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(context, R.string.msg_nao_login,Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error ao realizar login. " + error.getMessage());
            }
        });
        return Usuario.LOGGED_USER != null;
    }

    public boolean verificaLogin() {
        Usuario usuario = usuarioDAO.getUsuario();
        if (usuario != null) {
            apiUsuario.doValidateToken(usuario.getUid(), usuario.getAccessToken(), usuario.getClient(), new Callback<Usuario>() {
                @Override
                public void success(Usuario usuario, Response response) {
                    usuario = updateAuthData(usuario, response);
                    usuarioDAO.saveUsuario(usuario);
                    Usuario.LOGGED_USER = usuario;
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "Erro ao verificar login. " + error.getMessage());
                }
            });
        }
        return Usuario.LOGGED_USER != null;
    }

    private Usuario updateAuthData(Usuario usuario, Response response) {
        List<Header> headerList = response.getHeaders();
        for (Header header : headerList) {
            switch (header.getName()){
                case RestServiceBuilder.ACCESS_TOKEN:
                    usuario.setAccessToken(header.getValue());
                    break;
                case RestServiceBuilder.TOKEN_TYPE:
                    usuario.setTokenType(header.getValue());
                    break;
                case RestServiceBuilder.CLIENT:
                    usuario.setClient(header.getValue());
                    break;
                case RestServiceBuilder.EXPIRY:
                    usuario.setExpiry(Long.parseLong(header.getValue()));
                    break;
                case RestServiceBuilder.UID:
                    usuario.setUid(header.getValue());
                    break;
            }
        }
        return usuario;
    }
}
