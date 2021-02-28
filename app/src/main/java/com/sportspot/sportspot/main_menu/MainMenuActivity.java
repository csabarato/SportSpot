package com.sportspot.sportspot.main_menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.sportspot.sportspot.R;
import com.sportspot.sportspot.auth.LoginActivity;
import com.sportspot.sportspot.auth.google.GoogleSignInService;
import com.sportspot.sportspot.menus.activity_map.ActivitiesMapActivity;
import com.sportspot.sportspot.menus.new_activity.NewActivityAcitvity;
import com.sportspot.sportspot.menus.profile.UserProfileActivity;
import com.sportspot.sportspot.utils.DialogUtils;
import com.sportspot.sportspot.utils.SideNavDrawer;
import com.squareup.picasso.Picasso;

public class MainMenuActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;

    // Layout element definitions
    private GoogleSignInClient googleSignInClient;
    private DrawerLayout drawerLayout;

    private TextView navHeaderText;
    private ImageView navHeaderImage;
    private RecyclerView dashboardRecyclerView;

    private static final int ACTIVITIES_MAP_REQUEST_PERMISSION_CODE = 1;
    private static final int NEW_ACTIVITY_MAP_REQUEST_PERMISSION_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        firebaseAuth = FirebaseAuth.getInstance();

        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInService.getGsoInstance(getApplicationContext()));

        // Inflate NavHeader into NavView
        NavigationView navView = findViewById(R.id.nav_view_main_menu);
        View headerView = navView.inflateHeaderView(R.layout.nav_header_main_menu);

        // Get layout elements
        Toolbar toolbar = findViewById(R.id.app_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navHeaderText = headerView.findViewById(R.id.nav_header_menu_text);
        navHeaderImage = headerView.findViewById(R.id.nav_header_menu_image);

        // Get Dashboard recycler view
        dashboardRecyclerView = findViewById(R.id.dashboard_sport_recycler_view);
        setupDashboardRecyclerView();

        // setup Main menu nav drawer
        new SideNavDrawer(this, toolbar, drawerLayout, this.createOnNavItemSelectedListener());

        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

            if (account != null) {
                setNavHeaderImageAndTextByGoogleAccount(account);
            }
        }
    }

    private void setupDashboardRecyclerView() {
        dashboardRecyclerView.setLayoutManager(new LinearLayoutManager(MainMenuActivity.this));
        SportCardAdapter sportCardAdapter = new SportCardAdapter(this);
        dashboardRecyclerView.setAdapter(sportCardAdapter);
    }

    private void setNavHeaderImageAndTextByGoogleAccount(GoogleSignInAccount account) {

        if (account.getPhotoUrl() != null) {
            account.getPhotoUrl();

            // Load image to ImageView by URL
            Picasso.get().load(account.getPhotoUrl())
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .error(android.R.drawable.sym_def_app_icon)
                    .resize(200,200)
                    .into(navHeaderImage);
        }
        if (account.getGivenName() != null) {
            navHeaderText.setText(String.format(getString(R.string.welcome_message),account.getGivenName()));
        }
    }

    public NavigationView.OnNavigationItemSelectedListener createOnNavItemSelectedListener() {

        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                if (itemId == R.id.profile) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                    return true;
                } else if (itemId == R.id.activities_map) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivitesMapActivity();
                    return true;
                } else if (itemId == R.id.new_activity) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startAddNewActivityActivity();
                    return true;
                } else if (itemId == R.id.pitches) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (itemId == R.id.logout) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    logout();
                    return true;
                }
                return false;
            }
        };
    }

    public void logout() {

        if (firebaseAuth.getCurrentUser() != null) {
            firebaseAuth.signOut();
        } else if (GoogleSignIn.getLastSignedInAccount(this) !=  null) {
            googleSignInClient.signOut();
        }

        firebaseAuth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    private void startActivitesMapActivity() {

        // Check if necessary permissions is granted.
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(getApplicationContext(), ActivitiesMapActivity.class));
        // Request for needed permissions
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    ACTIVITIES_MAP_REQUEST_PERMISSION_CODE);
        }
    }

    private void startAddNewActivityActivity() {
        // Check if necessary permissions is granted.
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(getApplicationContext(), NewActivityAcitvity.class));
            // Request for needed permissions
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    NEW_ACTIVITY_MAP_REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ACTIVITIES_MAP_REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(getApplicationContext(), ActivitiesMapActivity.class));
            } else {
                DialogUtils.createAlertDialog(getString(R.string.location_denied_aware_title),
                        getString(R.string.location_denied_aware_message),
                        MainMenuActivity.this).show();
            }
        }

        if (requestCode == NEW_ACTIVITY_MAP_REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(getApplicationContext(), NewActivityAcitvity.class));
            } else {

                DialogUtils.getDefaultAlertDialogBuilder(getString(R.string.location_denied_aware_title),
                        getString(R.string.location_disallowed_warning_message), MainMenuActivity.this)
                .setPositiveButton("Continue", (dialog, which) -> startActivity(new Intent(getApplicationContext(), NewActivityAcitvity.class))).create().show();
            }
        }
    }
}