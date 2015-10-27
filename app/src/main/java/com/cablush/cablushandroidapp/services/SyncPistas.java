package com.cablush.cablushandroidapp.services;

import com.google.android.gms.common.api.Api;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by jonathan on 26/10/15.
 */
public class SyncPistas {
    private static ApiPistas REST_CLIENT;
    private static String ROOT =
            "http://api.openweathermap.org/data/2.5";

    static {
        setupRestClient();
    }

    private SyncPistas() {}

    public static ApiPistas get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ROOT)
                .setClient(new OkClient(new OkHttpClient()))
                .builder().setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();
        REST_CLIENT = restAdapter.create(ApiPistas.class);
    }
}
