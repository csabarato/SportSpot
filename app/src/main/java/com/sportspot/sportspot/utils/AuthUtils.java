package com.sportspot.sportspot.utils;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.sportspot.sportspot.auth.firebase.FirebaseSignInService;
import com.sportspot.sportspot.auth.google.GoogleSignInService;
import com.sportspot.sportspot.constants.AuthType;
import com.sportspot.sportspot.model.AuthDetails;

public class AuthUtils {

    public static AuthDetails getActiveUserAuthDetails(Context context) {

        if (GoogleSignInService.getLastUserToken(context) != null) {
            return new AuthDetails(AuthType.GOOGLE_AUTH, GoogleSignInService.getLastUserToken(context));
        } else if (FirebaseSignInService.getAccessToken(context) != null) {
            return new AuthDetails(AuthType.FIREBASE_AUTH, FirebaseSignInService.getAccessToken(context));
        }
        return null;
    }

    public static String getActiveUserId(Context context) {
        if (GoogleSignInService.getLastUserToken(context) != null) {
            return GoogleSignIn.getLastSignedInAccount(context).getId();
        } else if (FirebaseSignInService.getAccessToken(context) != null) {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return null;
    }
}
