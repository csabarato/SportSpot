package com.sportspot.sportspot.converter;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.sportspot.sportspot.model.UserDataModel;

public class UserDataConverter {

    public static UserDataModel convertAccountToUserDataModel(GoogleSignInAccount account) {

        UserDataModel userDataModel = new UserDataModel();

        userDataModel.set_id(account.getId());

        if (account.getEmail() != null) {
            userDataModel.setEmail(account.getEmail());
        }

        if (account.getGivenName() != null) {
            userDataModel.setFirstName(account.getGivenName());
        }

        if (account.getFamilyName() != null) {
            userDataModel.setLastName(account.getFamilyName());
        }

        return userDataModel;
    }
}
