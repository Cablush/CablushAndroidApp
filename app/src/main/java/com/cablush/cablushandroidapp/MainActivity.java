package com.cablush.cablushandroidapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import java.util.zip.Inflater;


public class MainActivity extends Activity {
    private Menu menu;
    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_main);
         getActionBar().show();
        // getBundle();
        createMapView();
        // setMarker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        createMapView();
    }

/*
    private void setMarker() {
        for (Model.Location loc : locationList) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(loc.getLat(), loc.getLng()))
                    .title(loc.getFormatedAddress()).snippet(loc.toString()));
        }
    }
*/
    private void createMapView() {

        try {
            if (null == googleMap) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();

                if (null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.error_create_map), Toast.LENGTH_SHORT).show();
                }

                 LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                 Criteria criteria = new Criteria();

            }
        } catch (NullPointerException e){
            Log.e("ERROR!! -- ", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_search:
                showBuscarDialog();
            break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void showBuscarDialog(){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_search);
        dialog.show();
    }

}
