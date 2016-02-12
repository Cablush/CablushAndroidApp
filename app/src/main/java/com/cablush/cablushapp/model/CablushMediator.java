package com.cablush.cablushapp.model;

import android.content.Context;

import com.cablush.cablushapp.model.services.ConnectivityChangeReceiver;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by oscar on 11/02/16.
 */
public abstract class CablushMediator implements Observer {

    protected final String TAG = getClass().getSimpleName();

    protected boolean isOnline;

    /**
     *
     * @param context
     */
    public CablushMediator(Context context) {
        // Get current connection status and register this class as observer to connections changes
        this.isOnline = ConnectivityChangeReceiver.isNetworkAvailable(context);
        ConnectivityChangeReceiver.getObservable().addObserver(this);
        // TODO call deleteObserver (?!)
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof ConnectivityChangeReceiver.NetworkObservable) {
            isOnline = (Boolean) data;
        }
    }

}
