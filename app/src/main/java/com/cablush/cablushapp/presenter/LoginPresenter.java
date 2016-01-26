package com.cablush.cablushapp.presenter;

import android.content.Context;
import android.util.Log;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.persistence.UsuarioDAO;
import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.model.rest.ApiUsuario;
import com.cablush.cablushapp.model.rest.RestServiceBuilder;

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
     * Interface to be implemented by this Presenter's client.
     */
    public interface LoginView {
        void onLoginSuccess();
        void onLoginError(String message);
    }

    private WeakReference<LoginView> mView;
    private WeakReference<Context> mContext;
    private ApiUsuario apiUsuario;
    private UsuarioDAO usuarioDAO;

    /**
     * Constructor
     *
     * @param view
     * @param context
     */
    public LoginPresenter(LoginView view, Context context) {
        this.mView = new WeakReference<>(view);
        this.mContext = new WeakReference<>(context);
        this.apiUsuario = RestServiceBuilder.createService(ApiUsuario.class);
        this.usuarioDAO = new UsuarioDAO(context);
    }

    public void doLogin(String email, String senha) {
        apiUsuario.doLogin(email, senha, new Callback<Usuario>() {
            @Override
            public void success(Usuario usuario, Response response) {
                usuario = updateAuthData(usuario, response);
                usuarioDAO.saveUsuario(usuario);
                Usuario.LOGGED_USER = usuario;
                LoginView view = mView.get();
                if (view != null) {
                    view.onLoginSuccess();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error on user login. " + error.getMessage());
                LoginView view = mView.get();
                Context context = mContext.get();
                if (view != null && context != null) {
                    view.onLoginError(context.getString(R.string.error_login));
                }
            }
        });
    }

    public void checkLogin() {
        Usuario usuario = usuarioDAO.getUsuario();
        if (usuario != null) {
            apiUsuario.doValidateToken(usuario.getUid(), usuario.getAccessToken(), usuario.getClient(), new Callback<Usuario>() {
                @Override
                public void success(Usuario usuario, Response response) {
                    usuario = updateAuthData(usuario, response);
                    usuarioDAO.saveUsuario(usuario);
                    Usuario.LOGGED_USER = usuario;
                    LoginView view = mView.get();
                    if (view != null) {
                        view.onLoginSuccess();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "Error on user check. " + error.getMessage());
                    LoginView view = mView.get();
                    Context context = mContext.get();
                    if (view != null && context != null) {
                        view.onLoginError(context.getString(R.string.error_login));
                    }
                }
            });
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
