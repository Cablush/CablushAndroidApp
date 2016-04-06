package com.cablush.cablushapp.model.geonames;


import android.util.Log;

import com.cablush.cablushapp.BuildConfig;
import com.cablush.cablushapp.model.geonames.dto.Geonames;
import com.cablush.cablushapp.model.geonames.dto.GeonamesResult;
import com.cablush.cablushapp.model.geonames.service.GeonamesApi;
import com.cablush.cablushapp.model.geonames.service.GeonamesServiceBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by oscar on 05/04/16.
 */
public class GeonamesLoader {

    private static final String TAG = GeonamesLoader.class.getSimpleName();

    private GeonamesApi geonamesApi;

    private Map<String, List<String>> firstSubdivisionMap = new HashMap<>();

    public GeonamesLoader() {
        this.geonamesApi = GeonamesServiceBuilder.createService(GeonamesApi.class);
    }

    /**
     * Get the first subdivisions of a country.
     */
    public List<String> getFirstSubdivisions(final String country) {
        if (firstSubdivisionMap.get(country) == null) {
            GeonamesResult result = geonamesApi.searchFromCountry(BuildConfig.GEONAME_USERNAME, Locale.getDefault().getLanguage(), country, "ADM1", "SHORT");
            if (result.success()) {
                Log.d(TAG, "Success on getFirstSubdivision.");
                List firstSubdivisionList = new ArrayList<String>();
                for (Geonames geonames : result.getGeonames()) {
                    firstSubdivisionList.add(geonames.getName());
                }
                firstSubdivisionMap.put(country, firstSubdivisionList);

            } else {
                Log.e(TAG, "Error on getFirstSubdivision: " + result.getStatus().getMessage());
            }
        }
        return firstSubdivisionMap.get(country);
    }

}
