package com.sportspot.sportspot.service;

import android.net.Uri;


import com.sportspot.sportspot.response_model.ResponseModel;
import com.sportspot.sportspot.view_model.ActivityDetailsViewModel;

import java.io.IOException;
import java.util.Collections;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActivityService {

    private static OkHttpClient client = new OkHttpClient();

    private static String base_url = "https://oauth2.googleapis.com/tokeninfo";
    private static String local_url = "ngrok_url";

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static ResponseModel<String> addNewActivity(ActivityDetailsViewModel activityDetails, String googleIdToken) {

        ResponseModel<String> helloResponse = new ResponseModel<>();

        Uri builtUri = Uri.parse(local_url).buildUpon()
                .appendPath("activity")
                .build();

        String json = activityDetails.toJson();
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(builtUri.toString())
                .addHeader("Authorization", "Bearer "+ googleIdToken)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            helloResponse.setData(response.body().string());
            return helloResponse;
        } catch (IOException e) {
            e.printStackTrace();
            helloResponse.setErrors(Collections.singletonList(e.getMessage()));
            return helloResponse;
        }
    }
}
