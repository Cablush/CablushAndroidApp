package com.cablush.cablushapp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cablush.cablushapp.model.EsportesMediator;
import com.cablush.cablushapp.model.persistence.UsuarioDAO;
import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.model.rest.ApiUsuario;
import com.cablush.cablushapp.model.rest.RestServiceBuilder;
import com.cablush.cablushapp.model.rest.dto.RequestUsuarioDTO;
import com.cablush.cablushapp.model.services.ConnectivityChangeReceiver;

import java.lang.ref.WeakReference;

import retrofit.Callback;
import retrofit.RetrofitError;
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
     * Omniauth providers.
     */
    public enum OmniauthProvider {
        FACEBOOK("facebook"), GOOGLE("google_oauth2");
        String name;
        OmniauthProvider(String name) {
            this.name = name;
        }
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

    /**
     * Login on server.
     */
    public void login(String email, String senha) {
        apiUsuario.login(new RequestUsuarioDTO(email, senha), new Callback<Usuario>() {
            @Override
            public void success(Usuario usuario, Response response) {
                Log.d(TAG, "Login successful.");
                onLoginSuccess(usuario);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error on user login. " + error.getMessage());
                onLoginError();
            }
        });
    }

    /**
     * Check if the last login is still valid.
     */
    public void checkLogin() {
        Usuario usuario = usuarioDAO.getUsuario();
        if (usuario != null) {
            Usuario.LOGGED_USER = usuario;
            // Only validate the token if there is connection, allowing offline registries
            if (ConnectivityChangeReceiver.isNetworkAvailable(mContext.get())) {
                apiUsuario.validateToken(new Callback<Usuario>() {
                    @Override
                    public void success(Usuario usuario, Response response) {
                        Log.d(TAG, "CheckLogin successful.");
                        onLoginSuccess(usuario);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "Error on user check. " + error.getMessage());
                        onLoginError();
                    }
                });
            }
        } else {
            LoginView view = mView.get();
            if (view != null) {
                view.onLoginResponse(LoginResponse.ERROR);
            }
        }
    }

    /**
     * Server callback for oauth2 login.
     */
    public void omniauthCallback(OmniauthProvider provider, String token) {
        apiUsuario.omniauthCallback(provider.name, token, new Callback<Usuario>() {
            @Override
            public void success(Usuario usuario, Response response) {
                Log.d(TAG, "Omniauth callback successful.");
                onLoginSuccess(usuario);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error on omniauth callback. " + error.getMessage());
                onLoginError();
            }
        });
    }

    private void onLoginSuccess(Usuario usuario) {
        Usuario.LOGGED_USER = usuarioDAO.save(usuario);
        LoginView view = mView.get();
        if (view != null) {
            view.onLoginResponse(LoginResponse.SUCCESS);
        }
        esportesMediator.loadEsportes();
    }

    private void onLoginError() {
        Usuario.LOGGED_USER = null;
        LoginView view = mView.get();
        if (view != null) {
            view.onLoginResponse(LoginResponse.ERROR);
        }
    }
}
