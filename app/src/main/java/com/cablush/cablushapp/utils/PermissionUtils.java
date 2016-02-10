package com.cablush.cablushapp.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * Created by oscar on 09/02/16.
 */
public class PermissionUtils {

    public static final int PERMISSIONS_LOCATION = 901;
    public static final int PERMISSIONS_STORAGE = 902;

    /**
     * Check if the Location Permissions are granted.
     *
     * @return True, if the permissions are granted or the Device is previous than M version, false otherwise.
     */
    public static boolean checkLocationPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

}
