package com.sportspot.sportspot.main_menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.sportspot.sportspot.R;
import com.sportspot.sportspot.auth.LoginActivity;
import com.sportspot.sportspot.auth.google.GoogleSignInService;

public class MainMenuActivity extends AppCompatActivity {

    Button logout;

    FirebaseAuth firebaseAuth;

    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        logout = findViewById(R.id.logoutBtn);

        firebaseAuth = FirebaseAuth.getInstance();

        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInService.getGsoInstance());
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