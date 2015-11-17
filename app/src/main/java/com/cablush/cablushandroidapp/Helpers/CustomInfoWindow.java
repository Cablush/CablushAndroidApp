package com.cablush.cablushandroidapp.Helpers;

import android.view.View;

import com.cablush.cablushandroidapp.model.Localizavel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by jonathan on 16/11/15.
 */
public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private Localizavel localizavel;

    public CustomInfoWindow(Localizavel localizavel) {
        this.localizavel = localizavel;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
