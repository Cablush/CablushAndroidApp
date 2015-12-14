package com.cablush.cablushandroidapp.view;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

import com.cablush.cablushandroidapp.R;
import com.cablush.cablushandroidapp.model.UsuariosMediator;

/**
 * Created by jonathan on 22/10/15.
 */
public class SplashScreenActivity extends CablushActivity implements Runnable {

    private final int DELAY = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        Handler handle = new Handler();
        handle.postDelayed(this, DELAY);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        UsuariosMediator usuariosMediator = new UsuariosMediator(this);
        usuariosMediator.verificaLogin();
    }

    @Override
    public void run() {
        startActivity(MainActivity.makeIntent(this));
        finish();
    }
}
