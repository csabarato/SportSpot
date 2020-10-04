package com.sportspot.sportspot.menus.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sportspot.sportspot.R;
import com.sportspot.sportspot.utils.DialogUtils;

public class UserProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;

    // Layout element definitions
    private MaterialCardView emailCardView;
    private TextView emailCardText;

    private MaterialCardView nameCardView;
    private TextView nameCardText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Get layout elements
        emailCardView = findViewById(R.id.profile_email_card);
        emailCardText = findViewById(R.id.email_card_text);

        nameCardView = findViewById(R.id.profile_name_card);
        nameCardText = findViewById(R.id.name_card_text);
        toolbar = findViewById(R.id.app_toolbar);

        if (toolbar != null) {
            setToolbar();
        }

        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            setupProfileDetails(GoogleSignIn.getLastSignedInAccount(this));
        } else if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            setupProfileDetails(FirebaseAuth.getInstance().getCurrentUser());
        } else {
            DialogUtils.buildAlertDialog("Authentication error", "No current user logged in", UserProfileActivity.this).show();
        }
    }

    private void setToolbar() {
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupProfileDetails(GoogleSignInAccount googleUser) {

        if (googleUser.getEmail() != null) {
            emailCardText.setText(googleUser.getEmail());
            emailCardView.setVisibility(View.VISIBLE);
        }

        if (googleUser.getDisplayName() != null) {
            nameCardText.setText(googleUser.getDisplayName());
            nameCardView.setVisibility(View.VISIBLE);
        }
    }

    private void setupProfileDetails(FirebaseUser fbaseUser) {

    }
}