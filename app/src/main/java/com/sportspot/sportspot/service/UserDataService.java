package com.sportspot.sportspot.service;

import android.net.Uri;

import com.sportspot.sportspot.constants.ConfigConstants;
import com.sportspot.sportspot.constants.MediaTypes;
import com.sportspot.sportspot.model.AuthDetails;
import com.sportspot.sportspot.model.UserDataModel;
import com.sportspot.sportspot.model.ResponseModel;
import com.sportspot.sportspot.utils.ConfigUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collections;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserDataService {

    private static final OkHttpClient client = new OkHttpClient();
    private static final String api_url = ConfigUtil.getDevProperties().getProperty(ConfigConstants.API_URL);

    public static ResponseModel<Void> syncUserData(UserDataModel userDataModel, AuthDetails authDetails) {

        ResponseModel<Void> responseModel = new ResponseModel<>();

        Uri uri = Uri.parse(api_url).buildUpon()
                .appendPath("sync_user_data")
                .build();

        RequestBody body = RequestBody.create(MediaTypes.JSON, userDataModel.toJSON());

        try {

            Request request = new Request.Builder()
                    .url(uri.toString())
                    .addHeader("Authorization", "Bearer "+ authDetails.getUserIdToken())
                    .addHeader("Auth-Type", authDetails.getAuthType().toString())
                    .patch(body)
                    .build();

            Response response = client.newCall(request).execute();

            if (response.code() == HttpURLConnection.HTTP_OK) {
                return responseModel;
            } else {
                responseModel.setErrors(Arrays.asList("Error: " + response.code(), response.message()));
            }

        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            responseModel.setErrors(Collections.singletonList(e.getMessage()));
        }

        return responseModel;
    }
}
