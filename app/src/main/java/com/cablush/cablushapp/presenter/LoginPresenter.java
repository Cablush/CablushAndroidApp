package com.cablush.cablushapp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cablush.cablushapp.model.EsportesMediator;
import com.cablush.cablushapp.model.persistence.UsuarioDAO;
import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.model.rest.ApiUsuario;
import com.cablush.cablushapp.model.rest.RestServiceBuilder;
import com.cablush.cablushapp.model.rest.dto.ResponseDTO;
import com.cablush.cablushapp.model.services.ConnectivityChangeReceiver;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

/**
 * Created by jonathan on 04/11/15.
 */
public class LoginPresenter {

    private static final String TAG = LoginPresenter.class.getSimpleName();

    /**
     * Responses for login.
     */
    public enum LoginResponse {
        SUCCESS, ERROR
    }

    /**
     * Interface to be implemented by this Presenter's client.
     */
    public interface LoginView {
        void onLoginResponse(LoginResponse response);
    }

    private WeakReference<Context> mContext;
    private WeakReference<LoginView> mView;
    private ApiUsuario apiUsuario;
    private UsuarioDAO usuarioDAO;

    private EsportesMediator esportesMediator;

    /**
     * Constructor.
     */
    public LoginPresenter(@NonNull LoginView view, @NonNull Context context) {
        this.mContext = new WeakReference<>(context);
        this.mView = new WeakReference<>(view);
        this.apiUsuario = RestServiceBuilder.createService(ApiUsuario.class);
        this.usuarioDAO = new UsuarioDAO(context);
        this.esportesMediator  = new EsportesMediator(context);
    }

    public void doLogin(String email, String senha) {
        apiUsuario.doLogin(email, senha, new Callback<ResponseDTO<Usuario>>() {
            @Override
            public void success(ResponseDTO<Usuario> dto, Response response) {
                Usuario usuario = updateAuthData(dto.getData(), response);
                usuarioDAO.save(usuario);
                Usuario.LOGGED_USER = usuario;
                LoginView view = mView.get();
                if (view != null) {
                    view.onLoginResponse(LoginResponse.SUCCESS);
                }
                esportesMediator.loadEsportes();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error on user login. " + error.getMessage());
                LoginView view = mView.get();
                if (view != null) {
                    view.onLoginResponse(LoginResponse.ERROR);
                }
            }
        });
    }

    public void checkLogin() {
        Usuario usuario = usuarioDAO.getUsuario();
        if (usuario != null) {
            Usuario.LOGGED_USER = usuario;
            // Only validate the token if there is connection, allowing offline registries
            if (!ConnectivityChangeReceiver.isNetworkAvailable(mContext.get())) {
                apiUsuario.doValidateToken(new Callback<ResponseDTO<Usuario>>() {
                    @Override
                    public void success(ResponseDTO<Usuario> dto, Response response) {
                        Usuario usuario = updateAuthData(dto.getData(), response);
                        usuarioDAO.save(usuario);
                        Usuario.LOGGED_USER = usuario;
                        LoginView view = mView.get();
                        if (view != null) {
                            view.onLoginResponse(LoginResponse.SUCCESS);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "Error on user check. " + error.getMessage());
                        Usuario.LOGGED_USER = null; // FIXME force (re)login at any http error?
                        LoginView view = mView.get();
                        if (view != null) {
                            view.onLoginResponse(LoginResponse.ERROR);
                        }
                    }
                });
            }
        }
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
