package com.cablush.cablushapp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.model.rest.ApiUsuario;
import com.cablush.cablushapp.model.rest.RestServiceBuilder;
import com.cablush.cablushapp.model.rest.dto.RequestUsuarioDTO;

import java.lang.ref.WeakReference;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by oscar on 27/03/16.
 */
public class ResetPasswordPresenter {

    private static final String TAG = ResetPasswordPresenter.class.getSimpleName();

    /**
     * Responses for reset password.
     */
    public enum Response {
        SUCCESS, ERROR
    }

    /**
     * Interface to be implemented by this Presenter's client.
     */
    public interface ResetPasswordView {
        void onResponse(Response response);
    }

    private WeakReference<ResetPasswordView> mView;
    private ApiUsuario apiUsuario;

    /**
     * Constructor.
     */
    public ResetPasswordPresenter(@NonNull ResetPasswordView view) {
        this.mView = new WeakReference<>(view);
        this.apiUsuario = RestServiceBuilder.createService(ApiUsuario.class);
    }

    /**
     * Reset the user password by email.
     */
    public void resetPassword(String email) {
        apiUsuario.resetPassword(new RequestUsuarioDTO(email), new Callback<Usuario>() {
            @Override
            public void success(Usuario usuario, retrofit.client.Response response) {
                Log.d(TAG, "Resetpassword successful.");
                mView.get().onResponse(Response.SUCCESS);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error on reset password. " + error.getMessage());
                mView.get().onResponse(Response.ERROR);
            }
        });
    }

}
