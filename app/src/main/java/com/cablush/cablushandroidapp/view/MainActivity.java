package com.cablush.cablushandroidapp.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cablush.cablushandroidapp.R;
import com.cablush.cablushandroidapp.model.domain.Localizavel;
import com.cablush.cablushandroidapp.view.drawer.DrawerActivityConfiguration;
import com.cablush.cablushandroidapp.view.drawer.DrawerAdapter;
import com.cablush.cablushandroidapp.view.drawer.DrawerItem;
import com.cablush.cablushandroidapp.view.drawer.DrawerMenuItem;
import com.cablush.cablushandroidapp.view.drawer.DrawerMenuSection;
import com.cablush.cablushandroidapp.view.maps.CablushLocation;
import com.cablush.cablushandroidapp.view.maps.CustomInfoWindow;
import com.cablush.cablushandroidapp.view.maps.Locations;
import com.cablush.cablushandroidapp.view.dialogs.LoginDialog;
import com.cablush.cablushandroidapp.view.dialogs.SearchDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MainActivity extends AbstractDrawerActivity
        implements LoginDialog.LoginDialogListener, SearchDialog.SearchDialogListener {

    private static GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    private static LatLngBounds.Builder latBuilder;
    private LocationManager locationManager;
    private CablushLocation mLocationListener = new CablushLocation();

    /**
     * Make the intent of this activity.
     *
     * @param context
     * @return
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        int checkGooglePlayServices = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setMessage(String.valueOf(checkGooglePlayServices)); // TODO show correct message
            alerta.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    return;
                }
            });
            alerta.show();
        }
        configGPS();
        createMapView();
    }

    @Override
    protected DrawerActivityConfiguration getNavDrawerConfiguration() {
        DrawerItem[] menu = new DrawerItem[] {
                DrawerMenuItem.create(101,"Login", "circle_login", false, this),
                DrawerMenuSection.create(200, "Buscar"),
                DrawerMenuItem.create(201,"Lojas", "ic_launcher", false, this),
                DrawerMenuItem.create(202, "Eventos", "ic_launcher", false, this),
                DrawerMenuItem.create(203, "Pistas", "ic_launcher", false, this),
                DrawerMenuSection.create(300, "Cadastros"),
                DrawerMenuItem.create(301, "Lojas", "ic_launcher", false, this),
                DrawerMenuItem.create(302, "Eventos", "ic_launcher", false, this),
                DrawerMenuItem.create(303, "Pistas", "ic_launcher", false, this)};

        DrawerActivityConfiguration navConf = new DrawerActivityConfiguration();
        navConf.setMainLayout(R.layout.activity_main);
        navConf.setDrawerLayoutId(R.id.drawer_layout);
        navConf.setLeftDrawerId(R.id.list_slidermenu);
        navConf.setNavItems(menu);
//        navConf.setDrawerShadow(R.drawable.drawer_shadow);
//        navConf.setDrawerOpenDesc(R.string.drawer_open);
//        navConf.setDrawerCloseDesc(R.string.drawer_close);
        navConf.setBaseAdapter(new DrawerAdapter(this, R.layout.drawer_item, menu));
        return navConf;
    }

    @Override
    protected void onNavItemSelected(int id) {
        switch (id) {
            case 101:
                LoginDialog.showLoginDialog(getFragmentManager());
                break;
            case 201:
                SearchDialog.showSearchDialog(getFragmentManager(), SearchDialog.TYPE.LOJA);
                break;
            case 202:
                SearchDialog.showSearchDialog(getFragmentManager(), SearchDialog.TYPE.EVENTO);
                break;
            case 203:
                SearchDialog.showSearchDialog(getFragmentManager(), SearchDialog.TYPE.PISTA);
                break;
            case 301:
                break;
            case 302:
                break;
            case 303:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        createMapView();
    }

    @Override
    public void onLoginDialogSuccess() {
    }

    @Override
    public void onLoginDialogCancel() {
    }

    @Override
    public <L extends Localizavel> void onSearchDialogSuccess(List<L> localizaveis) {
        for (L localizavel : localizaveis) {
            setMarker(localizavel);
        }
    }

    @Override
    public void onSearchDialogCancel() {
    }

    private <L extends Localizavel> void setMarker(L localizavel) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(localizavel.getLocal().getLatitude(), localizavel.getLocal().getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        googleMap.setInfoWindowAdapter(new CustomInfoWindow(localizavel, this));
        latBuilder = new LatLngBounds.Builder();
        latBuilder.include(new LatLng(localizavel.getLocal().getLatitude(), localizavel.getLocal().getLongitude()));
        int padding = 16; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latBuilder.build(), padding);
        googleMap.animateCamera(cu);
    }

    private void createMapView() {
        try {
            if (null == googleMap) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getMap();

                if (null == googleMap) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_create_map), Toast.LENGTH_SHORT).show();
                }
            }
            googleMap.setMyLocationEnabled(true);
        } catch (NullPointerException e) {
            Log.e(TAG, "ERROR!! -- ", e);
        }
    }

    private void configGPS() {
        String provider = Locations.getProvider(MainActivity.this);
        locationManager.requestLocationUpdates(provider, 60000, // 1min
                1000, // 1km
                mLocationListener);
    }
}
