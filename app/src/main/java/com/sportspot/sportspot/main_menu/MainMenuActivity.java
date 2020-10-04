package com.sportspot.sportspot.main_menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
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
import com.sportspot.sportspot.menus.profile.UserProfileActivity;
import com.sportspot.sportspot.utils.SideNavDrawer;
import com.squareup.picasso.Picasso;

public class MainMenuActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;

    // Layout element definitions
    private GoogleSignInClient googleSignInClient;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    private TextView navHeaderText;
    private ImageView navHeaderImage;

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
        toolbar = findViewById(R.id.app_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navHeaderText = headerView.findViewById(R.id.nav_header_menu_text);
        navHeaderImage = headerView.findViewById(R.id.nav_header_menu_image);

        // setup Main menu nav drawer
        new SideNavDrawer(this, toolbar, drawerLayout, this.createOnNavItemSelectedListener());

        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

            if (account != null) {
                setNavHeaderImageAndTextByGoogleAccount(account);
            }
        }
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

                switch (item.getItemId()) {
                    case R.id.profile:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                        return true;
                    case R.id.activities:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.new_activity:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.pitches:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    default:
                        return false;
                }
            }
        };
    }

    public void logout(View view) {

        if (firebaseAuth.getCurrentUser() != null) {
            firebaseAuth.signOut();
        } else if (GoogleSignIn.getLastSignedInAccount(this) !=  null) {
            googleSignInClient.signOut();
        }

        firebaseAuth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}