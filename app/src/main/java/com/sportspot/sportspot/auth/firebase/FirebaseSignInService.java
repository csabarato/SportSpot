package com.sportspot.sportspot.auth.firebase;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.sportspot.sportspot.constants.SharedPrefConst;

public class FirebaseSignInService {

    public static void getCurrentUserIdToken(OnCompleteListener<GetTokenResult> onCompleteListener) {

        FirebaseUser fbaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fbaseUser.getIdToken(true)
                .addOnCompleteListener(onCompleteListener);
    }

    public static void saveAccessToken(Context context, String fbaseIdToken) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(SharedPrefConst.FIREBASE_ID_TOKEN, fbaseIdToken).apply();
    }

    public static String getAccessToken(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(SharedPrefConst.FIREBASE_ID_TOKEN, null);
    }
}
