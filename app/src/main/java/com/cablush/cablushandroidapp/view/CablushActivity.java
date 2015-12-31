package com.cablush.cablushandroidapp.view;

import android.app.Activity;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;


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

    /**
     * Check if Google Play Service is available, and display an error dialog if not.
     *
     * @return
     */
    boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            Log.d(TAG, "Google Play Services is available.");
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    /**
     * Get the current user location based on "best provider".
     *
     * @return
     */
    LatLng getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        LatLng latLng = null;
        if (location != null) {
            Log.d(TAG, "Current location successfully obtained.");
             latLng = new LatLng(location.getLatitude(), location.getLongitude());

        }
        return latLng;
    }
}
