package com.cablush.cablushapp.view.cadastros;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.utils.MapUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.ref.WeakReference;

/**
 * Created by oscar on 09/02/16.
 */
public class MapaFragment extends CablushFragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMapClickListener {

    private static final String LOCATION_BUNDLE_KEY = "LOCATION_BUNDLE_KEY";

    private MapView mapView;
    private GoogleMap googleMap;
    private LatLng latLng;

    public interface SelectLocationListener {
        void onLocationSelected(LatLng latLng);
    }

    private WeakReference<SelectLocationListener> selectLocationListener;

    public MapaFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of this fragment with the necessary data.
     */
    public static MapaFragment newInstance(LatLng latLng,
                                           @NonNull SelectLocationListener selectLocationListener) {
        MapaFragment fragment = new MapaFragment();
        if (latLng != null) {
            Bundle args = new Bundle();
            args.putParcelable(LOCATION_BUNDLE_KEY, latLng);
            fragment.setArguments(args);
        }
        fragment.selectLocationListener = new WeakReference<>(selectLocationListener); // TODO send this via to bundle
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        // Initialize necessary data
        if (getArguments() != null) {
            latLng = getArguments().getParcelable(LOCATION_BUNDLE_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        initializeView(view, savedInstanceState);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "onLowMemory()");
        mapView.onLowMemory();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.d(TAG, "setUserVisibleHint()");
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible() && isVisibleToUser && latLng == null) {
            Toast.makeText(getActivity(), R.string.txt_select_location, Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeView(View view, Bundle savedInstanceState) {
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "GoogleMap loaded.");
        this.googleMap = googleMap;
        this.googleMap.setOnMapClickListener(this);
        checkLocationPermission();
        setMarker();
    }

    @Override
    public void onLocationPermissionGranted() {
        if (googleMap != null) {
            MapUtils.setUserLocation(getActivity(), googleMap);
            MapUtils.enableUserLocation(getActivity(), googleMap);
            googleMap.setOnMyLocationButtonClickListener(this);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        setUserVisibleHint(true);
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        this.latLng = latLng;
        setMarker();
        fethLocation();
    }

    private void setMarker() {
        if (googleMap != null && latLng != null) {
            // Clear Map
            googleMap.clear();
            // Add the mark to LatLng
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mark_cablush_orange)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(MapUtils.DEFAULT_ZOOM));
        }
    }

    private void fethLocation() {
        if (googleMap != null && latLng != null) {
            // Call the SelectLocationListener
            SelectLocationListener listener = selectLocationListener.get();
            if (listener != null) {
                listener.onLocationSelected(latLng);
            }
        }
    }
}
