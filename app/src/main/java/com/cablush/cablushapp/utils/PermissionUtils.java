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

    public static final int PERMISSIONS_LOCATION = 101;
    public static final int PERMISSIONS_STORAGE = 102;

    private static boolean checkPermissionDenied(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED;
    }

    /**
     * Check if the Location Permissions are granted.
     *
     * @param context
     * @return True, if the permissions are granted or the Device is previous than M version, false otherwise.
     */
    public static boolean checkLocationPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissionDenied(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    || checkPermissionDenied(context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the Storage Permissions are granted.
     *
     * @param context
     * @return True, if the permission are granted or the Device is previous than M version, false otherwise.
     */
    public static boolean checkStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissionDenied(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return false;
            }
        }
        return true;
    }

}
