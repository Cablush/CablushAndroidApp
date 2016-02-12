package com.cablush.cablushapp.model;

import android.content.Context;

import com.cablush.cablushapp.model.services.ConnectivityChangeReceiver;

import java.lang.ref.WeakReference;

/**
 * Created by oscar on 11/02/16.
 */
public abstract class CablushMediator {

    protected final String TAG = getClass().getSimpleName();

    private WeakReference<Context> mContext;

    /**
     *
     * @param context
     */
    public CablushMediator(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    protected boolean isOnline() {
        Context context = mContext.get();
        return context != null && ConnectivityChangeReceiver.isNetworkAvailable(context);
    }

}
