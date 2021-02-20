package com.sportspot.sportspot.menus.activity_map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sportspot.sportspot.R;
import com.sportspot.sportspot.constants.ActivityFilter;
import com.sportspot.sportspot.constants.SharedPrefConst;
import com.sportspot.sportspot.model.ActivityModel;
import com.sportspot.sportspot.shared.AlertDialogDetails;
import com.sportspot.sportspot.shared.LocationProvider;
import com.sportspot.sportspot.utils.AuthUtils;
import com.sportspot.sportspot.utils.DateUtils;
import com.sportspot.sportspot.utils.DialogUtils;
import com.sportspot.sportspot.view_model.ActivitiesMapViewModel;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ActivitiesMapActivity extends AppCompatActivity {

    private MapView map = null;
    private ActivitiesMapViewModel activitiesMapViewModel;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_map);

        sharedPreferences = this.getSharedPreferences(SharedPrefConst.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        activitiesMapViewModel = ViewModelProviders.of(this).get(ActivitiesMapViewModel.class);

        // Load and initialize the osmdroid configuration
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setToolbar();

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setMultiTouchControls(true);

        addLocationOverlay();
        addCompassOverlay();

        IMapController mapController = map.getController();
        mapController.setZoom(13d);

        LocationProvider locationProvider = LocationProvider.getInstance(getApplicationContext());

        if (locationProvider.getCurrentLocation() != null) {
            GeoPoint currentLocation = new GeoPoint(locationProvider.getCurrentLocation());
            map.getController().setCenter(currentLocation);
        } else {
            DialogUtils.createAlertDialog(getString(R.string.location_unavailable_title),getString(R.string.location_unavailable_message), ActivitiesMapActivity.this).show();
        }

        String selectedFilterType = sharedPreferences.getString(SharedPrefConst.SELECTED_ACTIVITY_FILTER_TYPE, "all");
        loadActivities(ActivityFilter.getActivityFilterByValue(selectedFilterType));

        activitiesMapViewModel.getActivities().observe(this, this::showActivityMarkers);
        activitiesMapViewModel.getAlertDetailsLiveData().observe(this, this::showAlertDialog);


        ProgressDialog activitiesLoadingPD = createProgressDialog("Loading activities");
        activitiesMapViewModel.isActivitiesLoading().observe(this, (isLoading) -> {
            if (isLoading) activitiesLoadingPD.show();
            else {
                activitiesLoadingPD.dismiss();
            }
        });

        ProgressDialog signupPendingDP = createProgressDialog("Signing up to activity");
        activitiesMapViewModel.isSignupPending().observe(this, (isPending) -> {
            if (isPending) signupPendingDP.show();
            else signupPendingDP.dismiss();
        });

    }

    private void loadActivities(ActivityFilter activityFilter) {
            activitiesMapViewModel.loadActivities(activityFilter);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.app_toolbar);
        toolbar.setTitle(getString(R.string.menu_activities_map));
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activities_map_menu_items, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        String selectedFilterType = sharedPreferences.getString(SharedPrefConst.SELECTED_ACTIVITY_FILTER_TYPE, "all");
        ActivityFilter selectedFilter = ActivityFilter.getActivityFilterByValue(selectedFilterType);

        switch (selectedFilter) {
            case MY_ACTIVITIES: menu.findItem(R.id.my_activities).setChecked(true);
            break;
            case OPEN_ACTIVITIES: menu.findItem(R.id.open_activities).setChecked(true);
            break;
            case SIGNED_UP_ACTIVITIES: menu.findItem(R.id.signed_up_activities).setChecked(true);
            break;
            case ALL_ACTIVITIES: menu.findItem(R.id.all_activities).setChecked(true);
            break;
            }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.all_activities && !item.isChecked()) {
            loadActivities(null);
            saveSelectedFilterToSharedPref(ActivityFilter.ALL_ACTIVITIES);
        } else if (item.getItemId() == R.id.my_activities && !item.isChecked()) {
            loadActivities(ActivityFilter.MY_ACTIVITIES);
            saveSelectedFilterToSharedPref(ActivityFilter.MY_ACTIVITIES);
        } else if (item.getItemId() == R.id.signed_up_activities && !item.isChecked()) {
            loadActivities(ActivityFilter.SIGNED_UP_ACTIVITIES);
            saveSelectedFilterToSharedPref(ActivityFilter.SIGNED_UP_ACTIVITIES);
        } else if (item.getItemId() == R.id.open_activities && !item.isChecked()) {
            loadActivities(ActivityFilter.OPEN_ACTIVITIES);
            saveSelectedFilterToSharedPref(ActivityFilter.OPEN_ACTIVITIES);
        } else if (item.getItemId() == R.id.map_legend) {
            showLegendDialog();
        }
        item.setChecked(true);

        return super.onOptionsItemSelected(item);
    }

    private void saveSelectedFilterToSharedPref(ActivityFilter activityFilter) {
        sharedPreferences.edit().putString(SharedPrefConst.SELECTED_ACTIVITY_FILTER_TYPE, activityFilter.getFilterValue()).apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    private void addLocationOverlay() {
        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), map);
        mLocationOverlay.enableMyLocation();
        map.getOverlays().add(mLocationOverlay);
    }

    private void addCompassOverlay() {
        CompassOverlay mCompassOverlay = new CompassOverlay(getApplication(), new InternalCompassOrientationProvider(getApplicationContext()), map);
        mCompassOverlay.enableCompass();
        map.getOverlays().add(mCompassOverlay);
    }

    private void showActivityMarkers(List<ActivityModel> activities) {

        // Remove not showing markers
        for (Overlay overlay : map.getOverlays()) {
            if (overlay instanceof Marker &&
                activities.stream().noneMatch(activity -> activity.get_id().equals(((Marker) overlay).getId()))) {
                    map.getOverlays().remove(overlay);
                }
        }

        for (ActivityModel activity : activities) {

            Optional<Overlay> activityMarkerOpt = map.getOverlays().stream()
                    .filter(overlay -> overlay instanceof Marker)
                    .filter(overlay -> ((Marker) overlay).getId().equals(activity.get_id()))
                    .findFirst();

            if (activityMarkerOpt.isPresent()) {
                Marker activityMarker = (Marker) activityMarkerOpt.get();
                refreshActivityMarker(activityMarker, activity);
            } else {
                addActivityMarker(activity);
            }
        }
    }

    private void refreshActivityMarker(Marker activityMarker, ActivityModel activity) {

        activityMarker.setPosition(new GeoPoint(activity.getLocationLatitude(), activity.getLocationLongitude()));
        InfoWindow activityInfoWindow = new ActivityInfoWindow(R.layout.activity_info_window, map, activity);
        activityMarker.setInfoWindow(activityInfoWindow);
        setActivityMarkerIcon(activityMarker, activity);
    }

    private void addActivityMarker(ActivityModel activity) {
            Marker activityMarker = new Marker(map);
            activityMarker.setId(activity.get_id());

            activityMarker.setPosition(new GeoPoint(activity.getLocationLatitude(), activity.getLocationLongitude()));
            activityMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            activityMarker.setTitle(activityMarker.getId());

            InfoWindow activityInfoWindow = new ActivityInfoWindow(R.layout.activity_info_window, map, activity);
            activityMarker.setInfoWindow(activityInfoWindow);
            setActivityMarkerIcon(activityMarker, activity);


            activityMarker.setOnMarkerClickListener((marker, mapView) -> {

                // Set center when Info window shows
                Point pixelsPoint = new Point();
                mapView.getProjection().toPixels(marker.getPosition(), pixelsPoint);

                // Set  offset
                pixelsPoint.y -= 500;

                final IGeoPoint centerPoint = mapView.getProjection().fromPixels(pixelsPoint.x, pixelsPoint.y);
                mapView.getController().animateTo(centerPoint);

                if (marker.isInfoWindowOpen()) {
                    marker.closeInfoWindow();
                } else {
                    marker.showInfoWindow();
                }

                return true;
            });
            map.getOverlays().add(activityMarker);
    }

    private void setActivityMarkerIcon(Marker activityMarker, ActivityModel activity) {
        Drawable activityLocationIcon;
        // Set Marker icon
        if (AuthUtils.getActiveUserId(this).equals(activity.getOwner().get_id())) {
            activityLocationIcon = ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.ic_my_activity_location);
        } else if (activity.isUserSignedUp(AuthUtils.getActiveUserId(this))) {
            activityLocationIcon = ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.ic_signed_up_activity_location);
        } else {
            activityLocationIcon = ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.ic_open_activity_location);
        }
        activityMarker.setIcon(activityLocationIcon);

    }

    private void showAlertDialog(AlertDialogDetails alertDialogDetails) {
            DialogUtils.createAlertDialog(
                alertDialogDetails.getTitle(), alertDialogDetails.getMessage(), ActivitiesMapActivity.this).show();
    }

    private ProgressDialog createProgressDialog(String message) {
        ProgressDialog progressDialog  = new ProgressDialog(ActivitiesMapActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    // Nested class for Activity info window
    private class ActivityInfoWindow extends InfoWindow implements View.OnClickListener {

        private final ActivityModel activity;
        private Button activitySignUpButton;
        private Button activityDetailsButton;

        public ActivityInfoWindow(int layoutResId, MapView mapView, ActivityModel activity) {
            super(layoutResId, mapView);
            this.activity = activity;
        }

        @Override
        public void onOpen(Object item) {

            TextView activityInfoOwner = mView.findViewById(R.id.activity_info_owner);
            TextView activityInfoSportType = mView.findViewById(R.id.activity_info_sport);
            TextView activityInfoPlaces = mView.findViewById(R.id.activity_info_places);
            TextView activityInfoStartDate = mView.findViewById(R.id.activity_info_start_date);
            ImageButton infoCloseButton = mView.findViewById(R.id.info_close_button);
            activitySignUpButton = mView.findViewById(R.id.activity_signup_button);
            activityDetailsButton = mView.findViewById(R.id.activity_details_button);

            TextView activityInfoDescLabel = mView.findViewById(R.id.activity_info_desc_label);
            TextView activityInfoDesc = mView.findViewById(R.id.activity_info_desc);

            if (AuthUtils.getActiveUserId(mView.getContext()).equals(activity.getOwner().get_id())) {
                activityInfoOwner.setText(R.string.info_window_owner_me);
            } else if (!activity.isUserSignedUp(AuthUtils.getActiveUserId(ActivitiesMapActivity.this))) {
                activitySignUpButton.setVisibility(View.VISIBLE);
                activityInfoOwner.setText(activity.getOwner().getDisplayName());
            } else {
                activityInfoOwner.setText(activity.getOwner().getDisplayName());
            }

            activityInfoSportType.setText(activity.getSportType().getName());
            activityInfoPlaces.setText((activity.getRemainingPlaces()) + " / "+ (activity.getNumOfPersons()));
            activityInfoStartDate.setText(DateUtils.toDateTimeString(activity.getStartDate()));

            if (activity.getDescription() != null && !activity.getDescription().isEmpty()) {
                activityInfoDescLabel.setVisibility(View.VISIBLE);
                activityInfoDesc.setText(activity.getDescription());
            }

            infoCloseButton.setOnClickListener(onClick -> this.close());
            activitySignUpButton.setOnClickListener(this);
            activityDetailsButton.setOnClickListener(this);
        }

        @Override
        public void onClose() {

        }

        @Override
        public void onClick(View v) {
            if (v.equals(activitySignUpButton)) {
                activitiesMapViewModel.signUpToActivity(activity.get_id());
                close();
            } else if (v.equals(activityDetailsButton)) {
                Intent intent = new Intent(getApplicationContext(), ActivityDetailsActivity.class);
                intent.putExtra("activity", activity);
                startActivity(intent);
            }
        }
    }

    private void showLegendDialog() {

        MaterialAlertDialogBuilder legendDialog = DialogUtils.getDefaultAlertDialogBuilder("Legend", null, ActivitiesMapActivity.this);
        LayoutInflater layoutInflater = LayoutInflater.from(ActivitiesMapActivity.this);
        final View view = layoutInflater.inflate(R.layout.activities_map_legend, null);
        legendDialog.setView(view);
        legendDialog.show();

    }
}