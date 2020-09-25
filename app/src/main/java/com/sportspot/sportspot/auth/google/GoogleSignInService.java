package com.sportspot.sportspot.auth.google;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GoogleSignInService implements View.OnClickListener {

    private Activity activity;
    private GoogleSignInClient googleSignInClient;
    private static GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

    // Request codes
    public static final Integer GOOGLE__SIGN_IN_RC = 1;

    public GoogleSignInService(Context context, Activity activity) {
        googleSignInClient = getGoogleSignInClient(context);
        this.activity = activity;
    }

    public static GoogleSignInOptions getGsoInstance() {
        if (gso == null) {
            return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        }
        return gso;
    }

    public GoogleSignInClient getGoogleSignInClient(Context context) {
            googleSignInClient = GoogleSignIn.getClient(context, getGsoInstance());
            return googleSignInClient;
    }

    @Override
    public void onClick(View v) {
        handleGoogleSignIn();
    }

    private void handleGoogleSignIn() {
        Intent googleSignInIntent = googleSignInClient.getSignInIntent();
        activity.startActivityForResult(googleSignInIntent, GOOGLE__SIGN_IN_RC);
    }
}
