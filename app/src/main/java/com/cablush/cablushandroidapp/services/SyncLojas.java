package com.cablush.cablushandroidapp.services;

/**
 * Created by jonathan on 26/10/15.
 */
public class SyncLojas {
    private static ApiPistas REST_CLIENT;
    private static String ROOT ="http://api.cablush.com/";

    static {
        setupRestClient();
    }

    private SyncLojas() {}

    public static ApiPistas get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {
//        RestAdapter.Builder builder = new RestAdapter.Builder()
//                .setEndpoint(ROOT)
//                .setClient(new OkClient(new OkHttpClient()))
//                .builder().setLogLevel(RestAdapter.LogLevel.FULL);
//
//        RestAdapter restAdapter = builder.build();
//        REST_CLIENT = restAdapter.create(ApiPistas.class);
    }
}
