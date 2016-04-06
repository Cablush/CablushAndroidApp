package com.cablush.cablushapp.model.geonames.service;

import android.support.annotation.NonNull;

import com.cablush.cablushapp.BuildConfig;

import retrofit.RestAdapter;

/**
 * Created by oscar on 05/04/16.
 */
public class GeonamesServiceBuilder {

    private static final String TAG = GeonamesServiceBuilder.class.getSimpleName();

    private static final String GEONAMES_API_ROOT = "http://api.geonames.org/";

    public static <S> S createService(@NonNull Class<S> serviceClass) {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(GEONAMES_API_ROOT)
                .setLogLevel(BuildConfig.RETROFIT_LOG_LEVEL);
        return builder.build().create(serviceClass);
    }

}
