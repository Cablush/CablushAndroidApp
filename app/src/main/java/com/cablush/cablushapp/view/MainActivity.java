package com.cablush.cablushapp.view;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.OperationResult;
import com.cablush.cablushapp.model.domain.Evento;
import com.cablush.cablushapp.model.domain.Localizavel;
import com.cablush.cablushapp.model.domain.Loja;
import com.cablush.cablushapp.model.domain.Pista;
import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.presenter.LoginPresenter;
import com.cablush.cablushapp.presenter.RegisterPresenter;
import com.cablush.cablushapp.presenter.SearchPresenter;
import com.cablush.cablushapp.utils.MapUtils;
import com.cablush.cablushapp.view.dialogs.LocalInfoDialog;
import com.cablush.cablushapp.view.dialogs.RegisterDialog;
import com.cablush.cablushapp.view.drawer.DrawerActivityConfiguration;
import com.cablush.cablushapp.view.dialogs.LoginDialog;
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

    public static final int REQUEST_CADASTRO_LOJA = 1;
    public static final int REQUEST_CADASTRO_EVENTO = 2;
    public static final int REQUEST_CADASTRO_PISTA = 3;

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
    public static Intent makeIntent(@NonNull Context context) {
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
                        if (checkUserLoggedIn()) {
                            startActivityForResult(CadastroLojaActivity
                                    .makeIntent(MainActivity.this, new Loja(Usuario.LOGGED_USER)),
                                    REQUEST_CADASTRO_LOJA);
                        }
                        break;
                    case R.id.cadastro_evento:
                        if (checkUserLoggedIn()) {
                            startActivityForResult(CadastroEventoActivity
                                    .makeIntent(MainActivity.this, new Evento(Usuario.LOGGED_USER)),
                                    REQUEST_CADASTRO_EVENTO);
                        }
                        break;
                    case R.id.cadastro_pista:
                        if (checkUserLoggedIn()) {
                            startActivityForResult(CadastroPistaActivity
                                    .makeIntent(MainActivity.this, new Pista(Usuario.LOGGED_USER)),
                                    REQUEST_CADASTRO_PISTA);
                        }
                        break;
                }
                return super.onMenuItemSelected(menuItem);
            }
        });

        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        searchPresenter = new SearchPresenter(this, this);

        configNavigationHead();
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
                //SearchDialog.showDialog(getFragmentManager(), SearchDialog.TYPE.LOJA);
                searchPresenter.getLojas(null, null, null);
                return true;
            case R.id.drawer_search_eventos:
                //SearchDialog.showDialog(getFragmentManager(), SearchDialog.TYPE.EVENTO);
                searchPresenter.getEventos(null, null, null);
                return true;
            case R.id.drawer_search_pistas:
                //SearchDialog.showDialog(getFragmentManager(), SearchDialog.TYPE.PISTA);
                searchPresenter.getPistas(null, null, null);
                return true;
            case R.id.drawer_my_lojas:
                return getMyLojas();
            case R.id.drawer_my_eventos:
                return getMyEventos();
            case R.id.drawer_my_pistas:
                return getMyPistas();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CADASTRO_LOJA:
                    getMyLojas();
                    break;
                case REQUEST_CADASTRO_EVENTO:
                    getMyEventos();
                    break;
                case REQUEST_CADASTRO_PISTA:
                    getMyPistas();
                    break;
            }
        }
    }

    private boolean getMyLojas() {
        if (checkUserLoggedIn()) {
            searchPresenter.getMyLojas();
            spinner.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    private boolean getMyEventos() {
        if (checkUserLoggedIn()) {
            searchPresenter.getMyEventos();
            spinner.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    private boolean getMyPistas() {
        if (checkUserLoggedIn()) {
            searchPresenter.getMyPistas();
            spinner.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    private void configNavigationHead() {
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

    /**
     * Check if the user is logged in, showing a toast if not.
     */
    private boolean checkUserLoggedIn() {
        if (Usuario.LOGGED_USER == null) {
            Toast.makeText(this, R.string.msg_login_required, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
            configNavigationHead();
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
    public void onSearchResult(OperationResult result, List<? extends Localizavel> localizaveis) {
        clearMap();
        switch (result) {
            case OFF_LINE:
                Toast.makeText(this, R.string.msg_search_off_line, Toast.LENGTH_SHORT).show();
                break;
            case ERROR:
                Toast.makeText(this, R.string.msg_search_error, Toast.LENGTH_SHORT).show();
                break;
        }
        if (localizaveis.isEmpty()) {
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
                .icon(BitmapDescriptorFactory.fromResource(
                        (localizavel.isRemote() && !localizavel.isChanged())
                        ? R.drawable.ic_mark_cablush_orange
                        : R.drawable.ic_mark_cablush_blue)));

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
