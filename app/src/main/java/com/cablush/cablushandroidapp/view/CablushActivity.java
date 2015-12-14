package com.cablush.cablushandroidapp.view;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;


/**
 * Created by Jonathan on 11/11/2015.
 */
public class CablushActivity extends Activity {

    protected String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PassionOne-Bold.ttf");
    }
}
