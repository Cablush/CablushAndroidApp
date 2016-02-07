package com.cablush.cablushapp.view;

import android.content.Context;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Localizavel;
import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.presenter.SearchPresenter;
import com.cablush.cablushapp.utils.ViewUtils;
import com.cablush.cablushapp.view.dialogs.LocalInfoDialog;
import com.cablush.cablushapp.view.dialogs.RegisterDialog;
import com.cablush.cablushapp.view.drawer.DrawerActivityConfiguration;
import com.cablush.cablushapp.view.dialogs.LoginDialog;
import com.cablush.cablushapp.view.dialogs.SearchDialog;
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
        LoginDialog.LoginDialogListener, RegisterDialog.RegisterDialogListener, SearchPresenter.SearchView {

    private ProgressBar spinner;

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

        FloatingActionButton addFAB = (FloatingActionButton) findViewById(R.id.add_fab);
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ViewUtils.checkUserLoggedIn(MainActivity.this)) {
                    startActivity(CadastroLojaActivity.makeIntent(MainActivity.this));
                }
            }
        });

        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        checkLogin();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "GoogleMap loaded.");
        this.googleMap = googleMap;

        // Try to retrieve the current user location
        getUserLocation();
    }

    @Override
    protected void onUserLocationReady(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
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
            case R.id.drawer_my_lojas:
                if (ViewUtils.checkUserLoggedIn(this)) {
                    SearchPresenter presenter = new SearchPresenter(this, this);
                    presenter.getMyLojas();
                    return true;
                }
                return false;
            case R.id.drawer_my_eventos:
                if (ViewUtils.checkUserLoggedIn(this)) {
                    SearchPresenter presenter = new SearchPresenter(this, this);
                    presenter.getMyEventos();
                    return true;
                }
                return false;
            case R.id.drawer_my_pistas:
                if (ViewUtils.checkUserLoggedIn(this)) {
                    SearchPresenter presenter = new SearchPresenter(this, this);
                    presenter.getMyPistas();
                    return true;
                }
                return false;
            default:
                Toast.makeText(getApplicationContext(), R.string.erro_invalid_option, Toast.LENGTH_SHORT).show();
                return false;
        }
    }

    private void checkLogin() {
        View header = navigationView.getHeaderView(0);
        TextView nameTextView = (TextView) header.findViewById(R.id.name);
        TextView emailTextView = (TextView) header.findViewById(R.id.email);

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
    public void onSearchSuccess(List<? extends Localizavel> localizaveis) {
        if (localizaveis == null || localizaveis.isEmpty()) {
            Toast.makeText(this, R.string.msg_local_not_found, Toast.LENGTH_SHORT).show();
        } else {
            clearMarker();
            for (Localizavel localizavel : localizaveis) {
                setMarker(localizavel);
            }
            centerMap(localizaveis);
        }
        spinner.setVisibility(View.GONE);
    }

    @Override
    public void onSearchError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        spinner.setVisibility(View.GONE);
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
