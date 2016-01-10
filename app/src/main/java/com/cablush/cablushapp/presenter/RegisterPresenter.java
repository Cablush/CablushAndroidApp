package com.cablush.cablushapp.presenter;

import android.content.Context;
import android.util.Log;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.model.rest.ApiUsuario;
import com.cablush.cablushapp.model.rest.RestServiceBuilder;

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
     * Interface to be implemented by this Presenter's client.
     */
    public interface RegisterView {
        void onRegisterSuccess();
        void onRegisterError(String message);
    }

    private WeakReference<RegisterView> view;
    private WeakReference<Context> context;
    private ApiUsuario apiUsuario;

    /**
     * Constructor
     *
     * @param view
     */
    public RegisterPresenter(RegisterView view, Context context) {
        this.view = new WeakReference<>(view);
        this.context = new WeakReference<>(context);
        this.apiUsuario = RestServiceBuilder.createService(ApiUsuario.class);
    }

    public void doRegister(String name, String email, String password, Boolean shopkeeper) {
        apiUsuario.doRegister(name, email, password, password, shopkeeper, new Callback<Usuario>() {
            @Override
            public void success(Usuario usuario, Response response) {
                view.get().onRegisterSuccess();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error on user register. " + error.getMessage());
                view.get().onRegisterError(context.get().getString(R.string.error_register));
            }
        });
    }
}
