package com.cablush.cablushandroidapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.res.TypedArray;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import android.location.LocationListener;

import com.cablush.cablushandroidapp.Adapters.NavDrawerListAdapter;
import com.cablush.cablushandroidapp.model.NavDrawerItem;
import com.cablush.cablushandroidapp.services.SyncEventos;
import com.cablush.cablushandroidapp.services.SyncLojas;
import com.cablush.cablushandroidapp.services.SyncPistas;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private Menu menu;
    private static GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    private LocationManager locationManager;
    private AlertDialog alerta;

    private CharSequence mTitle;
    private CharSequence mDrawerTitle;

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItens;
    private NavDrawerListAdapter adapter;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String selectedTipo;
    private static boolean init = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();

        navMenuTitles = getResources().getStringArray(R.array.tipo);
        navMenuIcons = getResources().obtainTypedArray(R.array.icons);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList   = (ListView)findViewById(R.id.list_slidermenu);

        navDrawerItens = new ArrayList<>();

        navDrawerItens.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItens.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItens.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItens);
        mDrawerList.setAdapter(adapter);
        ImageView img = new ImageView(this);
                img.setImageResource(R.drawable.logo_branca);
        mDrawerList.addHeaderView(img);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        int checkGooglePlayServices = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setMessage(String.valueOf(checkGooglePlayServices));
            alerta.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    return;
                }
            });
            alerta.show();
            alerta = null;
        }
        configGPS();

       // customActionBar();

        createMapView();

    }

    private void customActionBar(){
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        ImageButton ibtnSearch = (ImageButton) mCustomView.findViewById(R.id.ibtSearch);

        ibtnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showBuscarDialog();
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createMapView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
            locationManager.removeUpdates(mLocationListener);
            locationManager = null;
    }

    public static void setMarker(String nome,String descricao, double lat, double lng) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title(nome)
                .snippet(descricao)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
    }

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
        } catch (NullPointerException e) {
            Log.e("ERROR!! -- ", e.toString());
        }
        googleMap.setMyLocationEnabled(true);
    }

    private void configGPS() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider = locationManager.getBestProvider(criteria, true);
        // Location location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 60000, // 1min
                    1000, // 1km
                    mLocationListener);

    }


    private void showBuscarDialog(){
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_search, null);

        alerta = getAlertBuilder(this, view).create();
        if(!init) {
            alerta.show();
        }else{
            init = false;
        }

    }

    private AlertDialog.Builder getAlertBuilder(final Context context,  View view) {

        final Spinner spnEstados  = (Spinner)view.findViewById(R.id.spnEstados);
        final Spinner spnEsportes = (Spinner)view.findViewById(R.id.spnEsportes);
        Button btnBuscar    = (Button)view.findViewById(R.id.btnBuscar);
        Button btnCancelar  = (Button)view.findViewById(R.id.btnCancelar);
        final EditText edtName    = (EditText)view.findViewById(R.id.editText);

        final String[] tipo     = getResources().getStringArray(R.array.tipo);
        String[] estados  = getResources().getStringArray(R.array.estados);
        String[] esportes = getResources().getStringArray(R.array.esportes);

        spnEstados.setAdapter(new ArrayAdapter<>(context, R.layout.simple_item, estados));
        spnEsportes.setAdapter(new ArrayAdapter<>(context, R.layout.simple_item, esportes));


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.dismiss();
            }
        });
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String estado = spnEstados.getSelectedItem().equals("Selecione...") ? "" : spnEstados.getSelectedItem().toString();
                String esporte = spnEsportes.getSelectedItem().equals("Selecione...") ? "" : spnEsportes.getSelectedItem().toString();
                String nome = edtName.getText().toString();

                if (selectedTipo.equals(tipo[0])) {//Loja
                    SyncLojas syncLojas = new SyncLojas();
                    syncLojas.getLojas(nome, estado, esporte);
                } else if (selectedTipo.equals(tipo[1])) {//Evento
                    SyncEventos syncEventos = new SyncEventos();
                    syncEventos.getEventos(nome, estado, esporte);
                } else if (selectedTipo.equals(tipo[2])) {//Pista
                    SyncPistas syncPistas = new SyncPistas();
                    syncPistas.getPistas(nome, estado, esporte);
                } else {
                    Toast.makeText(context, "Selecione um tipo para a busca", Toast.LENGTH_SHORT).show();
                }
                alerta.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("   Buscar  "+selectedTipo);
        builder.setView(view);



        return builder;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        /*switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/
        return true;
    }

    private final LocationListener mLocationListener = new LocationListener(){

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position-1);
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        switch (position) {
            case 0:
                selectedTipo = getResources().getStringArray(R.array.tipo)[position];
                showBuscarDialog();
                break;
            case 1:
                selectedTipo = getResources().getStringArray(R.array.tipo)[position];
                showBuscarDialog();
                break;
            case 2:
                selectedTipo = getResources().getStringArray(R.array.tipo)[position];
                showBuscarDialog();
                break;
            default:
                break;
        }

    }
}
