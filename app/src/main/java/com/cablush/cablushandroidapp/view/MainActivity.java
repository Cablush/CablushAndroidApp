package com.cablush.cablushandroidapp.view;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cablush.cablushandroidapp.R;
import com.cablush.cablushandroidapp.model.domain.Localizavel;
import com.cablush.cablushandroidapp.model.domain.Usuario;
import com.cablush.cablushandroidapp.utils.ViewUtils;
import com.cablush.cablushandroidapp.view.dialogs.LocalInfoDialog;
import com.cablush.cablushandroidapp.view.dialogs.RegisterDialog;
import com.cablush.cablushandroidapp.view.drawer.DrawerActivityConfiguration;
import com.cablush.cablushandroidapp.view.dialogs.LoginDialog;
import com.cablush.cablushandroidapp.view.dialogs.SearchDialog;
import com.cablush.cablushandroidapp.view.maps.CustomInfoWindow;
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

//        FloatingActionButton addFAB = (FloatingActionButton) findViewById(R.id.add_fab);
//        addFAB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ViewUtils.checkUserLoggedIn(MainActivity.this)) {
//
//                }
//            }
//        });

        checkLogin();
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
        DrawerActivityConfiguration navConf = new DrawerActivityConfiguration();
        navConf.setMainLayout(R.layout.activity_main);
        navConf.setDrawerLayoutId(R.id.drawer_layout);
        navConf.setToolbarId(R.id.toolbar);
        navConf.setNavigationId(R.id.navigation_view);
        navConf.setHeaderId(R.layout.drawer_header);
        return navConf;
    }

    @Override
    protected boolean onNavItemSelected(int id) {
        switch (id) {
            case R.id.header_view:
                if (Usuario.LOGGED_USER == null) {
                    drawerLayout.closeDrawers();
                    LoginDialog.showDialog(getFragmentManager());
                    return true;
                }
                return false;
            case R.id.drawer_search_lojas:
                SearchDialog.showDialog(getFragmentManager(), SearchDialog.TYPE.LOJA);
                return true;
            case R.id.drawer_search_eventos:
                SearchDialog.showDialog(getFragmentManager(), SearchDialog.TYPE.EVENTO);
                return true;
            case R.id.drawer_search_pistas:
                SearchDialog.showDialog(getFragmentManager(), SearchDialog.TYPE.PISTA);
                return true;
//            case R.id.drawer_my_lojas:
//                if (ViewUtils.checkUserLoggedIn(this)) {
//                    return true;
//                }
//                return false;
//            case R.id.drawer_my_eventos:
//                if (ViewUtils.checkUserLoggedIn(this)) {
//                    return true;
//                }
//                return false;
//            case R.id.drawer_my_pistas:
//                if (ViewUtils.checkUserLoggedIn(this)) {
//                    return true;
//                }
//                return false;
            default:
                Toast.makeText(getApplicationContext(), R.string.erro_invalid_option, Toast.LENGTH_SHORT).show();
                return false;
        }
    }

    private void checkLogin() {
        TextView nameTextView = (TextView) navigationView.findViewById(R.id.name);
        TextView emailTextView = (TextView) navigationView.findViewById(R.id.email);

        if (Usuario.LOGGED_USER != null) {
            nameTextView.setText(Usuario.LOGGED_USER.getNome());
            emailTextView.setText(Usuario.LOGGED_USER.getEmail());
            emailTextView.setVisibility(View.VISIBLE);
        } else {
            nameTextView.setText(R.string.drawer_item_login_register);
            emailTextView.setText("");
            emailTextView.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onLoginDialogSuccess() {
        Toast.makeText(this,
                getString(R.string.success_login, Usuario.LOGGED_USER.getNome()),
                Toast.LENGTH_SHORT).show();

        checkLogin();
    }

    @Override
    public void onLoginDialogError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegisterButtonClicked() {
        RegisterDialog.showDialog(getFragmentManager());
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

        googleMap.setInfoWindowAdapter(new CustomInfoWindow(localizavel, this));
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getSnippet() == null) {
                    googleMap.moveCamera(CameraUpdateFactory.zoomIn());
                    return true;
                }
                LocalInfoDialog.showDialog(getFragmentManager(), localizavelMap.get(marker.getSnippet()));
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
