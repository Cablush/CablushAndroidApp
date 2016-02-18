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

    private final int DELAY = 1000; // 1 sec after login check

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_splashscreen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Check the user login
        LoginPresenter loginPresenter = new LoginPresenter(this, this);
        if (!loginPresenter.checkLogin()) {
            startMainActivity();
        }
    }

    @Override
    public void run() {
        startActivity(MainActivity.makeIntent(this));
        finish();
    }

    @Override
    public void onLoginResponse(LoginPresenter.LoginResponse response) {
        startMainActivity();
        if (LoginPresenter.LoginResponse.SUCCESS.equals(response)) {
            Toast.makeText(this,
                    getString(R.string.success_check_login, Usuario.LOGGED_USER.getNome()),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void startMainActivity() {
        Handler handle = new Handler();
        handle.postDelayed(this, DELAY);
    }

}
