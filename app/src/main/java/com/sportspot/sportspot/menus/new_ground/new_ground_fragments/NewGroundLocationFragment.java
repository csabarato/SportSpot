package com.sportspot.sportspot.menus.new_ground.new_ground_fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sportspot.sportspot.R;
import com.sportspot.sportspot.shared.AlertDialogFragment;
import com.sportspot.sportspot.shared.LocationProvider;
import com.sportspot.sportspot.view_model.NewGroundViewModel;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


public class NewGroundLocationFragment extends Fragment implements MapEventsReceiver, View.OnClickListener {

    private View view;
    private MapView map;
    private GeoPoint currentLocation;

    private NewGroundViewModel newGroundViewModel;
    private Marker groundLocationMarker;
    private Button submitGroundBtn;

    public NewGroundLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_ground_location, container, false);

        newGroundViewModel = ViewModelProviders.of(getActivity()).get(NewGroundViewModel.class);

        submitGroundBtn = view.findViewById(R.id.submit_new_ground);
        submitGroundBtn.setOnClickListener(this);

        submitGroundBtn.setClickable(false);
        submitGroundBtn.setAlpha(.5f);

        configureMapView();

        // Check if necessary permissions is granted.
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationProvider locationProvider = LocationProvider.getInstance(getActivity().getApplicationContext());
            if (locationProvider.getCurrentLocation() != null) {
                currentLocation = new GeoPoint(locationProvider.getCurrentLocation());
                map.getController().setCenter(currentLocation);
            } else {
                showAlertDialog(getString(R.string.location_unavailable_title), getString(R.string.location_unavailable_message));
            }
        }

        return view;
    }

    private void configureMapView() {

        // Load and initialize the osmdroid configuration
        Context ctx = getActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        // Begin fragment Transaction - necessary for DialogFragments

        map = view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setMultiTouchControls(true);

        addLocationOverlay();

        IMapController mapController = map.getController();
        mapController.setZoom(17d);

        MapEventsOverlay eventsOverlay = new MapEventsOverlay(this);
        map.getOverlays().add(eventsOverlay);

    }

    private void addLocationOverlay() {
        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getActivity().getApplicationContext()), map);
        mLocationOverlay.enableMyLocation();
        map.getOverlays().add(mLocationOverlay);
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        setGroundLocationMarker(p);
        return true;
    }

    private void setGroundLocationMarker(GeoPoint position) {

        if (groundLocationMarker != null) {
            map.getOverlays().remove(groundLocationMarker);
        }

        groundLocationMarker = new Marker(map);
        groundLocationMarker.setPosition(position);
        groundLocationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        groundLocationMarker.setInfoWindow(null);
        groundLocationMarker.setDraggable(true);
        groundLocationMarker.setOnMarkerDragListener(onMarkerDragListener());

        Drawable newLocationIcon = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_new_location);
        groundLocationMarker.setIcon(newLocationIcon);

        newGroundViewModel.setLocationLat(position.getLatitude());
        newGroundViewModel.setLocationLon(position.getLongitude());
        map.getOverlays().add(groundLocationMarker);

        submitGroundBtn.setClickable(true);
        submitGroundBtn.setAlpha(1);

        map.getController().animateTo(groundLocationMarker.getPosition());
    }

    private Marker.OnMarkerDragListener onMarkerDragListener() {
        return new Marker.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

                newGroundViewModel.setLocationLat(marker.getPosition().getLatitude());
                newGroundViewModel.setLocationLon(marker.getPosition().getLongitude());
                map.getController().animateTo(groundLocationMarker.getPosition());
            }

            @Override
            public void onMarkerDragStart(Marker marker) {

            }
        };
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_new_ground) {
            newGroundViewModel.submitNewGround();
        }
    }

    private void showAlertDialog(String title, String message) {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        new AlertDialogFragment(title, message).show(ft, "alert_dialog");
    }
}