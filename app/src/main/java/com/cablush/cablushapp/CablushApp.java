package com.cablush.cablushapp;

import android.app.Application;

import com.cablush.cablushapp.utils.FontCache;

/**
 * Created by oscar on 28/12/15.
 */
public class CablushApp extends Application {

    private static CablushApp singleton;

    public static CablushApp getInstance(){
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }
}
