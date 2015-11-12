package com.cablush.cablushandroidapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.res.TypedArray;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.cablush.cablushandroidapp.Adapters.NavDrawerListAdapter;
import com.cablush.cablushandroidapp.Helpers.CablushLocation;
import com.cablush.cablushandroidapp.Helpers.SlideMenuClickListener;
import com.cablush.cablushandroidapp.model.NavDrawerItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends CablushActivity {
    private Menu menu;
    private static GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    private LocationManager locationManager;
    private CablushLocation mLocationListener = new CablushLocation();

    private CharSequence mTitle;
    private CharSequence mDrawerTitle;

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItens;
    private NavDrawerListAdapter adapter;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        navDrawerItens.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(2, -1)));
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener(MainActivity.this));
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
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

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
        createMapView();
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
        return true;
    }
}
