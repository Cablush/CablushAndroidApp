package com.cablush.cablushapp.model.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Observable;

/**
 * Created by oscar on 11/02/16.
 */
public class ConnectivityChangeReceiver extends BroadcastReceiver {

    private static final String TAG = ConnectivityChangeReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        getObservable().connectionChanged(isNetworkAvailable(context));
    }

    /**
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     *
     */
    public static class NetworkObservable extends Observable {

        private static NetworkObservable instance = null;

        private NetworkObservable() {
            // Exist to defeat instantiation.
        }

        public void connectionChanged(boolean isOnline) {
            setChanged();
            notifyObservers(isOnline);
        }

        public static NetworkObservable getInstance() {
            if (instance == null) {
                instance = new NetworkObservable();
            }
            return instance;
        }
    }

    /**
     *
     * @return
     */
    public static NetworkObservable getObservable() {
        return NetworkObservable.getInstance();
    }
}
