package com.cablush.cablushandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by jonathan on 22/10/15.
 */
public class SplashScreen extends CablushActivity implements  Runnable{

    private final int DELAY = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        Handler handle = new Handler();
        handle.postDelayed(this, DELAY);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    public void run() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
