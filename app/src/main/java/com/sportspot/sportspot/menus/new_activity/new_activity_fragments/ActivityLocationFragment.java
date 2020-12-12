package com.sportspot.sportspot.menus.new_activity.new_activity_fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.sportspot.sportspot.R;
import com.sportspot.sportspot.auth.google.GoogleSignInService;
import com.sportspot.sportspot.main_menu.MainMenuActivity;
import com.sportspot.sportspot.menus.new_activity.PostNewActivityTask;
import com.sportspot.sportspot.shared.LocationProvider;
import com.sportspot.sportspot.shared.AlertDialogFragment;
import com.sportspot.sportspot.shared.AsyncTaskRunner;
import com.sportspot.sportspot.utils.ShowcaseViewUtils;
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
    private AsyncTaskRunner asyncTaskRunner;
    private View view;
    private ProgressDialog progressDialog;
    private Button submitDetailsButton;
    private ImageButton myLocationButton;

    private Marker activityLocationMarker = null;

    private ShowcaseView activityLocationShowcaseView;
    private ShowcaseView activityLocationDragShowcaseView;
    private ShowcaseView myLocationShowcaseView;

    public ActivityLocationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.activity_location_fragment, container, false);
        this.asyncTaskRunner = new AsyncTaskRunner();

        configureMapView();

        Button backToDetailsButton = view.findViewById(R.id.back_to_details_button);
        backToDetailsButton.setOnClickListener(this);

        submitDetailsButton = view.findViewById(R.id.submit_new_activity);
        submitDetailsButton.setOnClickListener(this);

        submitDetailsButton.setClickable(false);
        submitDetailsButton.setAlpha(.5f);

        myLocationButton = view.findViewById(R.id.my_location_button);
        myLocationButton.setOnClickListener(this);

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

        activityDetailsViewModel = ViewModelProviders.of(getActivity()).get(ActivityDetailsViewModel.class);
        restoreDataFromViewModel();

        progressDialog = new ProgressDialog(getActivity());

        if (currentLocation != null) {
            displayActivityLocationShowcaseView();
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
    public boolean longPressHelper(GeoPoint position) {
        setActivityLocationMarker(position);
        return true;
    }

    private void setActivityLocationMarker(GeoPoint position) {

        if (activityLocationMarker != null) {
            map.getOverlays().remove(activityLocationMarker);
        }

        activityLocationMarker = new Marker(map);
        activityLocationMarker.setPosition(position);
        activityLocationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        activityLocationMarker.setInfoWindow(null);
        activityLocationMarker.setDraggable(true);
        activityLocationMarker.setOnMarkerDragListener(onMarkerDragListener());

        Drawable newLocationIcon = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_new_location);
        activityLocationMarker.setIcon(newLocationIcon);

        activityDetailsViewModel.setLocationLat(position.getLatitude());
        activityDetailsViewModel.setLocationLon(position.getLongitude());
        map.getOverlays().add(activityLocationMarker);

        submitDetailsButton.setClickable(true);
        submitDetailsButton.setAlpha(1);

        if (activityLocationShowcaseView != null && activityLocationShowcaseView.isShowing()) {
            activityLocationShowcaseView.hide();
        }

        map.getController().animateTo(activityLocationMarker.getPosition());
    }

    private Marker.OnMarkerDragListener onMarkerDragListener() {
        return new Marker.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                if(activityLocationDragShowcaseView != null && activityLocationDragShowcaseView.isShowing()) {
                    activityLocationDragShowcaseView.hide();
                }
            }

            @Override
            public void onMarkerDragStart(Marker marker) {

            }
        };
    }

    private void restoreDataFromViewModel() {
        if (activityDetailsViewModel.getLocationLat() != null && activityDetailsViewModel.getLocationLon() != null) {

            GeoPoint position = new GeoPoint(activityDetailsViewModel.getLocationLat(), activityDetailsViewModel.getLocationLon());
            setActivityLocationMarker(position);

            submitDetailsButton.setClickable(true);
            submitDetailsButton.setAlpha(1);
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.back_to_details_button) {
            NavHostFragment.findNavController(ActivityLocationFragment.this).navigate(R.id.action_Location_to_Details);
        }

        else if (v.getId() == R.id.submit_new_activity) {
            submitNewActivity();

        } else if (v.getId() == R.id.my_location_button) {
            centerMapOnMyLocation();
        }
    }

    private void submitNewActivity() {

        showProgressDialog();
        asyncTaskRunner.executeAsync(new PostNewActivityTask(
                activityDetailsViewModel,
                GoogleSignInService.getLastUserToken(getActivity().getApplicationContext())), (data) -> {

                    hideProgressDialog();
                    if (!data.getErrors().isEmpty()) {
                        showAlertDialog("Http Request Error", String.join("; ", data.getErrors()));
                    } else {
                        showAlertDialog("Activity saved successfully!", "");
                        waitAndNavigateToMainMenu();
                    }
        });
    }

    private void centerMapOnMyLocation() {

        if(myLocationShowcaseView != null && myLocationShowcaseView.isShowing()) {
            myLocationShowcaseView.hide();
        }

        if (currentLocation != null) {
            map.getController().animateTo(currentLocation);
        } else {
            showAlertDialog(getString(R.string.location_unavailable_title), getString(R.string.location_unavailable_message));
        }
    }

    private void waitAndNavigateToMainMenu() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getActivity(), MainMenuActivity.class));
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        }, 1500);
    }

    private void showAlertDialog(String title, String message) {
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        new AlertDialogFragment(title, message).show(ft, "alert_dialog");
    }

    private void showProgressDialog() {

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Saving your activity...");
        progressDialog.show();
    }

    private void hideProgressDialog() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.dismiss();
    }

    private OnShowcaseEventListener onShowcaseEventListener() {

        return new OnShowcaseEventListener() {
            @Override
            public void onShowcaseViewHide(ShowcaseView showcaseView) {

                if(showcaseView.equals(activityLocationShowcaseView)) {
                    displayActivityLocationDragShowcaseView();
                } else if (showcaseView.equals(activityLocationDragShowcaseView)) {
                    displayMyLocationShowcaseView();
                }
            }

            @Override
            public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

            }

            @Override
            public void onShowcaseViewShow(ShowcaseView showcaseView) {

            }

            @Override
            public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

            }
        };
    }

    private void displayActivityLocationShowcaseView() {

        GeoPoint locationTipGeoPoint = new GeoPoint(currentLocation);
        locationTipGeoPoint.setLatitude(currentLocation.getLatitude() + 0.002);
        locationTipGeoPoint.setLongitude(currentLocation.getLongitude() + 0.002);

        map.getController().setCenter(locationTipGeoPoint);

        activityLocationShowcaseView = ShowcaseViewUtils.getDefaultShowcaseBuilder(
                getActivity(),view,getString(R.string.activity_location_showcase_title),getString(R.string.activity_location_showcase_text))
                .setShowcaseEventListener(onShowcaseEventListener())
                .build();
    }

    private void displayActivityLocationDragShowcaseView() {

        activityLocationDragShowcaseView = ShowcaseViewUtils.getDefaultShowcaseBuilder(
                getActivity(),view,getString(R.string.activity_location_drag_showcase_title),getString(R.string.activity_location_drag_showcase_text))
                .setShowcaseEventListener(onShowcaseEventListener())
                .build();

    }

    private void displayMyLocationShowcaseView() {

        myLocationShowcaseView = ShowcaseViewUtils
                .getDefaultShowcaseBuilder(getActivity(), myLocationButton, getString(R.string.my_location_showcase_text), null)
                .setShowcaseEventListener(onShowcaseEventListener())
                .build();
    }
}
