package com.cablush.cablushandroidapp.view.maps;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by Jonathan on 13/11/2015.
 */
public class Locations {

    public static LocationManager locationManager;

    private static void initLocationManager(Context context) {
        String location_context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) context.getSystemService(location_context);
    }


    public static double[] getLocationLatLng(Context context) {
        initLocationManager(context);
        double longitude = 0.0;
        double latitude = 0.0;
        Location location = locationManager.getLastKnownLocation(getProvider(context));
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        return new double[]{latitude,longitude};
    }


    private static Criteria getCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    public static String getProvider(Context context) {
        initLocationManager(context);
        return locationManager.getBestProvider(getCriteria(), true);
    }
}
