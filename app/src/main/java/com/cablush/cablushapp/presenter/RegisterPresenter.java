package com.cablush.cablushapp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.model.persistence.UsuarioDAO;
import com.cablush.cablushapp.model.rest.ApiUsuario;
import com.cablush.cablushapp.model.rest.RestServiceBuilder;
import com.cablush.cablushapp.model.rest.dto.RequestUsuarioDTO;

import java.lang.ref.WeakReference;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by oscar on 26/12/15.
 */
public class RegisterPresenter {

    private static final String TAG = RegisterPresenter.class.getSimpleName();

    /**
     * Responses for register.
     */
    public enum RegisterResponse {
        SUCCESS, ERROR
    }

    /**
     * Interface to be implemented by this Presenter's client.
     */
    public interface RegisterView {
        void onRegisterResponse(RegisterResponse response);
    }

    private WeakReference<RegisterView> mView;
    private ApiUsuario apiUsuario;
    private UsuarioDAO usuarioDAO;

    /**
     * Constructor.
     */
    public RegisterPresenter(@NonNull RegisterView view, @NonNull Context context) {
        this.mView = new WeakReference<>(view);
        this.apiUsuario = RestServiceBuilder.createService(ApiUsuario.class);
        this.usuarioDAO = new UsuarioDAO(context);
    }

    /**
     * Register a new user on server.
     */
    public void register(String name, String email, String password) {
        apiUsuario.register(new RequestUsuarioDTO(name, email, password), new Callback<Usuario>() {
            @Override
            public void success(Usuario usuario, Response response) {
                Log.d(TAG, "User register successful.");
                onRegisterSuccess();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error on user register. " + error.getMessage());
                onRegisterError();
            }
        });
    }

    /**
     * Edit the user on server
     */
    public void edit(String name, String email, String password) {
        apiUsuario.edit(new RequestUsuarioDTO(name, email, password), new Callback<Usuario>() {
            @Override
            public void success(Usuario usuario, Response response) {
                Log.d(TAG, "User edit successful.");
                Usuario.LOGGED_USER = usuarioDAO.save(usuario);
                onRegisterSuccess();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error on user edition. " + error.getMessage());
                onRegisterError();
            }
        });
    }

    private void onRegisterSuccess() {
        RegisterView view = mView.get();
        if (view != null) {
            view.onRegisterResponse(RegisterResponse.SUCCESS);
        }
    }

    private void onRegisterError() {
        Usuario.LOGGED_USER = null;
        RegisterView view = mView.get();
        if (view != null) {
            view.onRegisterResponse(RegisterResponse.ERROR);
        }
    }
}
