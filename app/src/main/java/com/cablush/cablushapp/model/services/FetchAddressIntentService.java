package com.cablush.cablushapp.model.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Local;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by oscar on 09/02/16.
 */
public class FetchAddressIntentService extends IntentService {

    public static final String TAG = FetchAddressIntentService.class.getSimpleName();

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;

    public static final String PACKAGE_NAME = "com.cablush.cablushapp.model.services";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String RESULT_LOCAL_KEY = PACKAGE_NAME + ".RESULT_LOCAL_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    protected ResultReceiver mReceiver;

    /**
     *
     * @param context
     * @param receiver
     * @param latLng
     * @return
     */
    public static Intent makeIntent(Context context, ResultReceiver receiver, LatLng latLng) {
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        intent.putExtra(RECEIVER, receiver);
        intent.putExtra(LOCATION_DATA_EXTRA, latLng);
        return intent;
    }

    public FetchAddressIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";

        mReceiver = intent.getParcelableExtra(RECEIVER);
        // Check if receiver was properly registered.
        if (mReceiver == null) {
            Log.e(TAG, "No receiver received. There is nowhere to send the results.");
            return;
        }

        // Get the location and receiver passed to this service through an extra.
        LatLng latLng = intent.getParcelableExtra(LOCATION_DATA_EXTRA);
        // Make sure that the location data was really sent over through an extra. If it wasn't,
        // send an error error message and return.
        if (latLng == null) {
            errorMessage = getString(R.string.error_no_location_data_provided);
            Log.e(TAG, errorMessage);
            deliverResultToReceiver(FAILURE_RESULT, errorMessage, null);
            return;
        }

        // The Geocoder's responses are localized for the given Locale, which represents a specific
        // geographical or linguistic region.
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        // Address found using the Geocoder.
        List<Address> addresses = null;

        try {
            // Using getFromLocation() returns an array of Addresses for the area immediately
            // surrounding the given latitude and longitude.
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.error_service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.error_invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". "
                    + "Latitude = " + latLng.latitude +
                    ", Longitude = " + latLng.longitude,
                    illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.error_no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(FAILURE_RESULT, errorMessage, null);
        } else {
            Address address = addresses.get(0);
            Log.d(TAG, address.toString());

            // Fetch the address lines using getAddressLine, join them, and send them to the thread.
            List<String> addressFragments = new ArrayList<>();
            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }

            // Fetch the address by class fields. // TODO improve address parse
            Local local = new Local();
            local.setCep(address.getPostalCode());
            if (address.getMaxAddressLineIndex() >= 1) {
                String line = address.getAddressLine(0);
                if (line.contains(",")) {
                    line = line.split(",")[0];
                }
                local.setLogradouro(line);
            }
            //local.setNumero();
            //local.setComplemento();
            local.setBairro(address.getSubLocality());
            local.setCidade(address.getLocality());
            local.setEstado(address.getAdminArea());
            local.setPais(address.getCountryCode());
            local.setLatitude(address.getLatitude());
            local.setLongitude(address.getLongitude());

            Log.i(TAG, getString(R.string.msg_address_found));
            deliverResultToReceiver(SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"), addressFragments),
                    local);
        }
    }

    /**
     * Sends a resultCode, message and local to the receiver.
     */
    private void deliverResultToReceiver(int resultCode, String message, Local local) {
        Bundle bundle = new Bundle();
        bundle.putString(RESULT_DATA_KEY, message);
        bundle.putSerializable(RESULT_LOCAL_KEY, local);
        mReceiver.send(resultCode, bundle);
    }
}
