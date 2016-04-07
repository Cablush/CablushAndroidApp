package com.cablush.cablushapp.model.geonames.service;

import com.cablush.cablushapp.model.geonames.dto.GeonamesResult;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by oscar on 05/04/16.
 */
public interface GeonamesApi {

    @GET("/searchJSON")
    GeonamesResult searchFromCountry(@Query("userName") String userName,
                                     @Query("lang") String lang,
                                     @Query("country") String country,
                                     @Query("featureCode") String featureCode,
                                     @Query("style") String style,
                                     Callback<GeonamesResult> resultCallback);

}
