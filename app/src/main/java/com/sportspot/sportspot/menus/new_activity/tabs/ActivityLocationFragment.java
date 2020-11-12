package com.sportspot.sportspot.menus.new_activity.tabs;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.sportspot.sportspot.R;
import com.sportspot.sportspot.service.LocationProvider;
import com.sportspot.sportspot.shared.AlertDialogFragment;
import com.sportspot.sportspot.view_model.ActivityDetailsViewModel;

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

public class ActivityLocationFragment extends Fragment implements View.OnClickListener , MapEventsReceiver {

    private ActivityDetailsViewModel activityDetailsViewModel;
    private MapView map;
    private GeoPoint currentLocation = null;
    private FragmentTransaction ft;

    public ActivityLocationFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_location_fragment, container, false);


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
        mapController.setZoom(13d);

        MapEventsOverlay eventsOverlay = new MapEventsOverlay(this);
        map.getOverlays().add(eventsOverlay);


        Button backToDetailsButton = view.findViewById(R.id.back_to_details_button);
        backToDetailsButton.setOnClickListener(this);

        Button submitDetailsButton = view.findViewById(R.id.submit_new_activity);
        submitDetailsButton.setOnClickListener(this);

        ImageButton myLocationButton = view.findViewById(R.id.my_location_button);
        myLocationButton.setOnClickListener(this);

        // Check if necessary permissions is granted.
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationProvider locationProvider = new LocationProvider(getActivity().getApplicationContext());
            if (locationProvider.getCurrentLocation() != null) {
                currentLocation = new GeoPoint(locationProvider.getCurrentLocation());
                map.getController().setCenter(currentLocation);
            } else {
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                new AlertDialogFragment(getString(R.string.location_denied_aware_title), getString(R.string.location_denied_aware_message))
                        .show(ft, "alert_dialog");
            }
        }

        activityDetailsViewModel = ViewModelProviders.of(getActivity()).get(ActivityDetailsViewModel.class);

        return view;
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

        Marker locationMarker = new Marker(map);
        locationMarker.setPosition(p);
        locationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(locationMarker);

        return false;
    }



    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.back_to_details_button) {
            NavHostFragment.findNavController(ActivityLocationFragment.this).navigate(R.id.action_Location_to_Details);
        }

        else if (v.getId() == R.id.submit_new_activity) {

        } else if (v.getId() == R.id.my_location_button) {
            if (currentLocation != null) {
                map.getController().animateTo(currentLocation);
            } else {
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                new AlertDialogFragment(getString(R.string.location_unavailable_title), getString(R.string.location_unavailable_message))
                        .show(ft, "alert_dialog");
            }
        }
    }
}
