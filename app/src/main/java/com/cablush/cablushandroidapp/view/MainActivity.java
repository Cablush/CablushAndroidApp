package com.cablush.cablushandroidapp.view;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cablush.cablushandroidapp.R;
import com.cablush.cablushandroidapp.model.domain.Localizavel;
import com.cablush.cablushandroidapp.model.domain.Usuario;
import com.cablush.cablushandroidapp.utils.ViewUtils;
import com.cablush.cablushandroidapp.view.dialogs.LocalDialog;
import com.cablush.cablushandroidapp.view.dialogs.RegisterDialog;
import com.cablush.cablushandroidapp.view.drawer.DrawerActivityConfiguration;
import com.cablush.cablushandroidapp.view.drawer.DrawerAdapter;
import com.cablush.cablushandroidapp.view.drawer.DrawerItem;
import com.cablush.cablushandroidapp.view.drawer.DrawerMenuHeader;
import com.cablush.cablushandroidapp.view.drawer.DrawerMenuItem;
import com.cablush.cablushandroidapp.view.drawer.DrawerMenuSection;
import com.cablush.cablushandroidapp.view.dialogs.LoginDialog;
import com.cablush.cablushandroidapp.view.dialogs.SearchDialog;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AbstractDrawerActivity implements OnMapReadyCallback,
        LoginDialog.LoginDialogListener, RegisterDialog.RegisterDialogListener, SearchDialog.SearchDialogListener {

    private GoogleMap googleMap;

    // Map to store the localizaveis by UUIDs
    private Map<String, Localizavel> localizavelMap = new HashMap<>();

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

        // Check if google play services is available
        isGooglePlayServicesAvailable();

        // Init Map Async
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "GoogleMap loaded.");
        this.googleMap = googleMap;

        // Try to retrieve the current user location, and show it on map
        LatLng latLng = getCurrentLocation();
        if (latLng != null) {
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

    @Override
    protected DrawerActivityConfiguration getNavDrawerConfiguration() {
        DrawerItem[] menu = new DrawerItem[] {
                DrawerMenuHeader.create(101),
                DrawerMenuSection.create(200, getString(R.string.drawer_section_search)),
                DrawerMenuItem.create(201, getString(R.string.drawer_item_stores), "ic_launcher", false, this),
                DrawerMenuItem.create(202, getString(R.string.drawer_item_events), "ic_launcher", false, this),
                DrawerMenuItem.create(203, getString(R.string.drawer_item_spots), "ic_launcher", false, this),
                DrawerMenuSection.create(300, getString(R.string.drawer_section_my_places)),
                DrawerMenuItem.create(301, getString(R.string.drawer_item_stores), "ic_launcher", false, this),
                DrawerMenuItem.create(302, getString(R.string.drawer_item_events), "ic_launcher", false, this),
                DrawerMenuItem.create(303, getString(R.string.drawer_item_spots), "ic_launcher", false, this)};

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
                if (Usuario.LOGGED_USER == null) {
                    LoginDialog.showLoginDialog(getFragmentManager());
                }
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
                if (ViewUtils.checkUserLoggedIn(this)) {

                }
                break;
            case 302:
                if (ViewUtils.checkUserLoggedIn(this)) {

                }
                break;
            case 303:
                if (ViewUtils.checkUserLoggedIn(this)) {

                }
                break;
            default:
                Toast.makeText(getApplicationContext(), R.string.erro_invalid_option, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoginDialogSuccess() {
        Toast.makeText(this,
                getString(R.string.success_login, Usuario.LOGGED_USER.getNome()),
                Toast.LENGTH_SHORT).show();
        updateDrawer();
    }

    @Override
    public void onLoginDialogError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegisterButtonClicked() {
        RegisterDialog.showRegisterDialog(getFragmentManager());
    }

    @Override
    public void onRegisterDialogSuccess() {
        Toast.makeText(this, R.string.success_register, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegisterDialogError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchDialogSuccess(List<? extends Localizavel> localizaveis) {
        if (localizaveis == null || localizaveis.isEmpty()) {
            Toast.makeText(this, R.string.msg_local_not_found, Toast.LENGTH_SHORT).show();
        } else {
            clearMarker();
            for (Localizavel localizavel : localizaveis) {
                setMarker(localizavel);
            }
            centerMap(localizaveis);
        }
    }

    private void clearMarker() {
        googleMap.clear();
        localizavelMap.clear();
    }

    private <L extends Localizavel> void setMarker(L localizavel) {
        localizavelMap.put(localizavel.getUuid(), localizavel);
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(localizavel.getLocal().getLatitude(), localizavel.getLocal().getLongitude()))
                .snippet(localizavel.getUuid())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mark_cablush_orange)));

        //googleMap.setInfoWindowAdapter(new CustomInfoWindow(localizavel, this));
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getSnippet() == null) {
                    googleMap.moveCamera(CameraUpdateFactory.zoomIn());
                    return true;
                }
                LocalDialog.showLocalDialog(getFragmentManager(), localizavelMap.get(marker.getSnippet()));
                return true;
            }
        });
    }

    private void centerMap(List<? extends Localizavel> localizaveis) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Localizavel localizavel : localizaveis) {
            builder.include(new LatLng(localizavel.getLocal().getLatitude(), localizavel.getLocal().getLongitude()));
        }
        LatLngBounds bounds = builder.build();
        int padding = 150; // offset from edges of the map in pixels
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cameraUpdate);
    }
}
