package com.cablush.cablushapp.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cablush.cablushapp.model.services.ConnectivityChangeReceiver;

import java.lang.ref.WeakReference;

/**
 * Created by oscar on 11/02/16.
 */
public abstract class CablushMediator {

    protected final String TAG = getClass().getSimpleName();

    private WeakReference<Context> mContext;

    /**
     * Constructor.
     */
    public CablushMediator(@NonNull Context context) {
        this.mContext = new WeakReference<>(context);
    }

    /**
     * Check if the device is online.
     */
    protected boolean isOnline() {
        Context context = mContext.get();
        return context != null && ConnectivityChangeReceiver.isNetworkAvailable(context);
    }

}
