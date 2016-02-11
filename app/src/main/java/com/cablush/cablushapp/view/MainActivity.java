package com.cablush.cablushapp.view;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.SearchResult;
import com.cablush.cablushapp.model.domain.Localizavel;
import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.presenter.LoginPresenter;
import com.cablush.cablushapp.presenter.RegisterPresenter;
import com.cablush.cablushapp.presenter.SearchPresenter;
import com.cablush.cablushapp.utils.MapUtils;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class MainActivity extends AbstractDrawerActivity implements OnMapReadyCallback,
        LoginDialog.LoginDialogListener, LoginPresenter.LoginView,
        RegisterPresenter.RegisterView, SearchPresenter.SearchView {

    private ProgressBar spinner;

    private GoogleMap googleMap;

    // Map to store the localizaveis by UUIDs
    private Map<String, Localizavel> localizavelMap = new HashMap<>();

    private SearchPresenter searchPresenter;

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

        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.add_fabDial);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.cadastro_loja:
                        startActivity(CadastroLojaActivity.makeIntent(MainActivity.this, null));
                        break;
                    case R.id.cadastro_evento:
                        startActivity(CadastroEventoActivity.makeIntent(MainActivity.this, null));
                        break;
                    case R.id.cadastro_pista:
                        startActivity(CadastroPistaActivity.makeIntent(MainActivity.this, null));
                        break;
                }
                return super.onMenuItemSelected(menuItem);
            }
        });

        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        searchPresenter = new SearchPresenter(this, this);

        checkLogin();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "GoogleMap loaded.");
        this.googleMap = googleMap;

        // Check the Location Permissions
        checkLocationPermission();
    }

    @Override
    public void onLocationPermissionGranted() {
        // If Location Permissions are granted, set the user location on Map
        if (googleMap != null) {
            MapUtils.setUserLocation(this, googleMap);
            MapUtils.enableUserLocation(this, googleMap);
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
            case R.id.drawer_my_lojas:
                if (ViewUtils.checkUserLoggedIn(this)) {
                    searchPresenter.getMyLojas();
                    spinner.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            case R.id.drawer_my_eventos:
                if (ViewUtils.checkUserLoggedIn(this)) {
                    searchPresenter.getMyEventos();
                    spinner.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            case R.id.drawer_my_pistas:
                if (ViewUtils.checkUserLoggedIn(this)) {
                    searchPresenter.getMyPistas();
                    spinner.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            default:
                Toast.makeText(this, R.string.error_invalid_option, Toast.LENGTH_SHORT).show();
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
    public void onRegisterButtonClicked() {
        RegisterDialog.showDialog(getFragmentManager());
    }

    @Override
    public void onLoginResponse(LoginPresenter.LoginResponse response) {
        if (LoginPresenter.LoginResponse.SUCCESS.equals(response)) {
            Toast.makeText(this,
                    getString(R.string.success_login, Usuario.LOGGED_USER.getNome()),
                    Toast.LENGTH_SHORT).show();
            checkLogin();
        } else {
            Toast.makeText(this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRegisterResponse(RegisterPresenter.RegisterResponse response) {
        if (RegisterPresenter.RegisterResponse.SUCCESS.equals(response)) {
            Toast.makeText(this, R.string.success_register, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.error_register), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public SearchPresenter getSearchPresenter() {
        return searchPresenter;
    }

    @Override
    public void onSearchResult(SearchResult result, List<? extends Localizavel> localizaveis) {
        // TODO warning offline and error searches (?)
        clearMap();
        if (localizaveis == null || localizaveis.isEmpty()) {
            Toast.makeText(this, R.string.msg_local_not_found, Toast.LENGTH_SHORT).show();
        } else {
            for (Localizavel localizavel : localizaveis) {
                setMarker(localizavel);
            }
            centerMap(localizaveis);
        }
        spinner.setVisibility(View.GONE);
    }

    private void clearMap() {
        googleMap.clear();
        localizavelMap.clear();
    }

    private <L extends Localizavel> void setMarker(L localizavel) {
        localizavelMap.put(localizavel.getUuid(), localizavel);
        googleMap.addMarker(new MarkerOptions()
                .position(localizavel.getLocal().getLatLng())
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
            builder.include(localizavel.getLocal().getLatLng());
        }
        LatLngBounds bounds = builder.build();
        int padding = 150; // offset from edges of the map in pixels
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cameraUpdate);
    }
}
