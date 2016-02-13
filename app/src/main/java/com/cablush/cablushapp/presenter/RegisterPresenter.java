package com.cablush.cablushapp.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.model.rest.ApiUsuario;
import com.cablush.cablushapp.model.rest.RestServiceBuilder;
import com.cablush.cablushapp.model.rest.dto.ResponseDTO;

import java.lang.ref.WeakReference;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by oscar on 26/12/15.
 */
public class RegisterPresenter {

    private static final String TAG = RegisterPresenter.class.getSimpleName();

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

    /**
     * Constructor
     *
     * @param view
     */
    public RegisterPresenter(@NonNull RegisterView view) {
        this.mView = new WeakReference<>(view);
        this.apiUsuario = RestServiceBuilder.createService(ApiUsuario.class);
    }

    public void doRegister(String name, String email, String password, Boolean shopkeeper) {
        apiUsuario.doRegister(name, email, password, password, shopkeeper, new Callback<ResponseDTO<Usuario>>() {
            @Override
            public void success(ResponseDTO<Usuario> dto, Response response) {
                RegisterView view = mView.get();
                if (view != null) {
                    view.onRegisterResponse(RegisterResponse.SUCCESS);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error on user register. " + error.getMessage());
                RegisterView view = mView.get();
                if (view != null) {
                    view.onRegisterResponse(RegisterResponse.ERROR);
                }
            }
        });
    }
}
