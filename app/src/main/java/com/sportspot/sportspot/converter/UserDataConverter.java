package com.sportspot.sportspot.converter;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.sportspot.sportspot.dto.UserDataDto;

public class UserDataConverter {

    public static UserDataDto convertAccountToUserDataDto(GoogleSignInAccount account) {

        UserDataDto userDataDto = new UserDataDto();

        userDataDto.set_id(account.getId());

        if (account.getEmail() != null) {
            userDataDto.setEmail(account.getEmail());
        }

        if (account.getGivenName() != null) {
            userDataDto.setFirstName(account.getGivenName());
        }

        if (account.getFamilyName() != null) {
            userDataDto.setLastName(account.getFamilyName());
        }

        return userDataDto;
    }
}
