package com.cablush.cablushapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by oscar on 09/02/16.
 */
public class MapUtils {

    public static final int DEFAULT_ZOOM = 15;

    /**
     * Set the user location on the provided GoogleMap based on "best provider".
     * @param googleMap
     * @return
     */
    public static Location setUserLocation(Activity activity, GoogleMap googleMap) {
        if (PermissionUtils.checkLocationPermission(activity)) { // Sanity check
            LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
                return location;
            }
        }
        return null;
    }

    /**
     * Enable the user location on the provided GoogleMap.
     *
     * @param googleMap
     */
    public static void enableUserLocation(Activity activity, GoogleMap googleMap) {
        if (PermissionUtils.checkLocationPermission(activity)) { // Sanity check
            googleMap.setMyLocationEnabled(true);
        }
    }

    /**
     *
     * @param activity
     * @param latLng
     */
    public static void openMapsNavigation(Activity activity, LatLng latLng) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latLng.latitude + "," + latLng.longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        activity.startActivity(mapIntent);
    }

}
