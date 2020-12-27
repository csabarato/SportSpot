package com.sportspot.sportspot.menus.activity_map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.sportspot.sportspot.R;
import com.sportspot.sportspot.auth.google.GoogleSignInService;
import com.sportspot.sportspot.dto.ActivityResponseDto;
import com.sportspot.sportspot.service.tasks.GetActivitiesTask;
import com.sportspot.sportspot.shared.ActivityInfoWindow;
import com.sportspot.sportspot.shared.AsyncTaskRunner;
import com.sportspot.sportspot.shared.LocationProvider;
import com.sportspot.sportspot.utils.DialogUtils;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;
public class ActivitiesMapActivity extends AppCompatActivity {

    private MapView map = null;
    private AsyncTaskRunner asyncTaskRunner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_map);

        asyncTaskRunner = new AsyncTaskRunner();

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

        loadActivities();
    }

    private void loadActivities() {
        asyncTaskRunner.executeAsync(new GetActivitiesTask(GoogleSignInService.getLastUserToken(this)),
                (data) -> {

                    if (data.getErrors().isEmpty() && data.getData() != null) {
                        showActivityMarkers(data.getData());
                    } else {
                        DialogUtils.createAlertDialog(
                                getString(R.string.activities_load_error), String.join(";", data.getErrors()), ActivitiesMapActivity.this).show();
                    }
        });

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

    private void showActivityMarkers(List<ActivityResponseDto> activityDtos) {

        for (ActivityResponseDto dto : activityDtos) {
            Marker activityMarker = new Marker(map);
            activityMarker.setId(dto.get_id());

            activityMarker.setPosition(new GeoPoint(dto.getLocationLatitude(), dto.getLocationLongitude()));
            activityMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            activityMarker.setTitle(activityMarker.getId());

            InfoWindow actIW=new ActivityInfoWindow(R.layout.activity_info_window, map, dto);
            activityMarker.setInfoWindow(actIW);

            Drawable activityLocationIcon;
            if (GoogleSignInService.isIdEqualsCurrentUserId(getApplicationContext(), dto.getOwner().get_id())) {
                activityLocationIcon = ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.ic_my_activity_location);
            } else {
                activityLocationIcon = ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.ic_activity_location);
            }
            activityMarker.setIcon(activityLocationIcon);

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
    }
}