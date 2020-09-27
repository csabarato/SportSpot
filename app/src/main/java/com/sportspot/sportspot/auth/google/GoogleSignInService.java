package com.sportspot.sportspot.auth.google;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.sportspot.sportspot.R;

public class GoogleSignInService implements View.OnClickListener {

    private Activity activity;
    private GoogleSignInClient googleSignInClient;
    private static GoogleSignInOptions gso = null;

    // Request codes
    public static final Integer GOOGLE__SIGN_IN_RC = 1;

    public GoogleSignInService(Context context, Activity activity) {
        googleSignInClient = getGoogleSignInClient(context);
        this.activity = activity;
    }

    public static GoogleSignInOptions getGsoInstance(Context context) {
        if (gso == null) {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .build();
            return gso;
        }
        return gso;
    }

    public GoogleSignInClient getGoogleSignInClient(Context context) {
            googleSignInClient = GoogleSignIn.getClient(context, getGsoInstance(context));
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
