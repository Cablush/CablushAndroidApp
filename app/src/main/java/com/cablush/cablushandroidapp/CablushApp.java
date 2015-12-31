package com.cablush.cablushandroidapp;

import android.app.Application;

import com.cablush.cablushandroidapp.utils.FontCache;

/**
 * Created by oscar on 28/12/15.
 */
public class CablushApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FontCache.overrideFont(this, "normal", "fonts/PassionOne-Bold.ttf");
    }
}
