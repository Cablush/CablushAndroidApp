package com.cablush.cablushapp.view;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.presenter.LoginPresenter;

/**
 * Created by jonathan on 22/10/15.
 */
public class SplashScreenActivity extends Activity implements Runnable, LoginPresenter.LoginView {

    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    private final int DELAY = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_splashscreen);
        Handler handle = new Handler();
        handle.postDelayed(this, DELAY);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LoginPresenter loginPresenter = new LoginPresenter(this, this);
        loginPresenter.checkLogin();
    }

    @Override
    public void run() {
        startActivity(MainActivity.makeIntent(this));
        finish();
    }

    @Override
    public void onLoginResponse(LoginPresenter.LoginResponse response) {
        if (LoginPresenter.LoginResponse.SUCCESS.equals(response)) {
            Toast.makeText(this,
                    getString(R.string.success_check_login, Usuario.LOGGED_USER.getNome()),
                    Toast.LENGTH_SHORT).show();
        }
    }

}
