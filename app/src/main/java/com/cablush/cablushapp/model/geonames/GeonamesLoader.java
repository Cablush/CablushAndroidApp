package com.cablush.cablushapp.model.geonames;


import android.util.Log;

import com.cablush.cablushapp.BuildConfig;
import com.cablush.cablushapp.model.geonames.dto.Geonames;
import com.cablush.cablushapp.model.geonames.dto.GeonamesResult;
import com.cablush.cablushapp.model.geonames.service.GeonamesApi;
import com.cablush.cablushapp.model.geonames.service.GeonamesServiceBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by oscar on 05/04/16.
 */
public class GeonamesLoader {

    private static final String TAG = GeonamesLoader.class.getSimpleName();

    public interface GeonamesCallback {
        void onLoadFirstSubdivisions(List<Geonames> firstSubdivisions);
    }

    private GeonamesApi geonamesApi;

    private Map<String, List<Geonames>> firstSubdivisionMap = new HashMap<>();

    public GeonamesLoader() {
        this.geonamesApi = GeonamesServiceBuilder.createService(GeonamesApi.class);
    }

    /**
     * Get the first subdivisions of a country.
     */
    public void getFirstSubdivisions(final String country, final GeonamesCallback callback) {
        geonamesApi.searchFromCountry(BuildConfig.GEONAME_USERNAME,
                Locale.getDefault().getLanguage(), country, "ADM1", "FULL",
                new Callback<GeonamesResult>() {
            @Override
            public void success(GeonamesResult geonamesResult, Response response) {
                if (geonamesResult.success()) {
                    Log.d(TAG, "Success on getFirstSubdivision.");
                    List<Geonames> firstSubdivisionList = new ArrayList<>();
                    for (Geonames geonames : geonamesResult.getGeonames()) {
                        firstSubdivisionList.add(geonames);
                    }
                    Collections.sort(firstSubdivisionList, new Comparator<Geonames>() {
                        @Override
                        public int compare(Geonames lhs, Geonames rhs) {
                            return lhs.getAdminName1().compareTo(rhs.getAdminName1());
                        }
                    });
                    firstSubdivisionMap.put(country, firstSubdivisionList);
                    callback.onLoadFirstSubdivisions(firstSubdivisionList);
                } else {
                    Log.e(TAG, "Error retrieving Geonames: " + geonamesResult.getStatus().getMessage());
                    callback.onLoadFirstSubdivisions(null);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error on getFirstSubdivision: " + error.getLocalizedMessage());
                callback.onLoadFirstSubdivisions(null);
            }
        });
    }
}
