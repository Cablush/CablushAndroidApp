package com.cablush.cablushandroidapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import android.location.LocationListener;

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

public class MainActivity extends Activity {
    private Menu menu;
    private static GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    private LocationManager locationManager;
    long minTime = 5 * 1000;
    long minDistance = 100;
    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_main);

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

        customActionBar();

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

    public static void setMarker(String descricao,String nome, double lat, double lng) {
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
        alerta.show();
    }

    private AlertDialog.Builder getAlertBuilder(final Context context,  View view) {
        final Spinner spnTipo  = (Spinner)view.findViewById(R.id.spnTipo);
        final Spinner spnEstados  = (Spinner)view.findViewById(R.id.spnEstados);
        final Spinner spnEsportes = (Spinner)view.findViewById(R.id.spnEsportes);
        Button btnBuscar    = (Button)view.findViewById(R.id.btnBuscar);
        Button btnCancelar  = (Button)view.findViewById(R.id.btnCancelar);
        final EditText edtName    = (EditText)view.findViewById(R.id.editText);

        final String[] tipo     = getResources().getStringArray(R.array.tipo);
        String[] estados  = getResources().getStringArray(R.array.estados);
        String[] esportes = getResources().getStringArray(R.array.esportes);

        spnTipo.setAdapter(new ArrayAdapter<>(context, R.layout.simple_item, tipo));
        spnEstados.setAdapter(new ArrayAdapter<>(context, R.layout.simple_item,estados));
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
                String estado  = !spnEstados.getSelectedItem().equals("Selecione...")? "" : spnEstados.getSelectedItem().toString();
                String esporte = !spnEsportes.getSelectedItem().equals("Selecione...")? "" : spnEsportes.getSelectedItem().toString();
                String nome    =   edtName.getText().toString();

                if(spnTipo.getSelectedItem().equals(tipo[1])){//Loja
                    SyncLojas syncLojas = new SyncLojas();
                    syncLojas.getLojas(nome, estado,esporte);
                }else if(spnTipo.getSelectedItem().equals(tipo[2])){//Evento
                    SyncEventos syncEventos = new SyncEventos();
                    syncEventos.getEventos(nome,estado,esporte);
                }else if(spnTipo.getSelectedItem().equals(tipo[3])){//Pista
                    SyncPistas syncPistas = new SyncPistas();
                    syncPistas.getPistas(nome, estado,esporte);
                }else{
                    Toast.makeText(context,"Selecione um tipo para a busca",Toast.LENGTH_SHORT).show();
                }
                alerta.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("   Buscar  ");
        builder.setView(view);



        return builder;
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
}
