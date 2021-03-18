package com.sportspot.sportspot.service;

import android.net.Uri;

import com.sportspot.sportspot.constants.ConfigConstants;
import com.sportspot.sportspot.constants.MediaTypes;
import com.sportspot.sportspot.model.AuthDetails;
import com.sportspot.sportspot.model.GroundRequestModel;
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

public class GroundService {

    private static final OkHttpClient client = new OkHttpClient();
    private static final String api_url = ConfigUtil.getDevProperties().getProperty(ConfigConstants.API_URL);

    public static ResponseModel<String> addNewGround(GroundRequestModel groundRequestModel, AuthDetails authDetails) {

        ResponseModel<String> responseModel = new ResponseModel<>();

        Uri builtUri = Uri.parse(api_url).buildUpon()
                .appendPath("ground")
                .build();

        String json = groundRequestModel.toJson();
        RequestBody requestBody = RequestBody.create(MediaTypes.JSON, json);

        try {

            Request request = new Request.Builder()
                    .url(builtUri.toString())
                    .addHeader("Authorization", "Bearer "+ authDetails.getUserIdToken())
                    .addHeader("Auth-Type", authDetails.getAuthType().toString())
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();

            if (response.code() == HttpURLConnection.HTTP_CREATED) {
                responseModel.setData(response.body().string());
            } else {
                responseModel.setErrors(Arrays.asList("Error: "+ (response.code()),response.message(), response.body().string()));
            }

            return responseModel;
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            responseModel.setErrors(Collections.singletonList(e.getMessage()));
            return responseModel;
        }
    }

}
