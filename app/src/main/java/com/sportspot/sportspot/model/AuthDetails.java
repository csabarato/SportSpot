package com.sportspot.sportspot.model;

import com.sportspot.sportspot.constants.AuthType;

public class AuthDetails {

    private AuthType authType;
    private String userIdToken;

    public AuthDetails(AuthType authType, String userIdToken) {
        this.authType = authType;
        this.userIdToken = userIdToken;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    public String getUserIdToken() {
        return userIdToken;
    }

    public void setUserIdToken(String userIdToken) {
        this.userIdToken = userIdToken;
    }
}
