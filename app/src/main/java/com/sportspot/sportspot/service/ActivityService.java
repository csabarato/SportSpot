package com.sportspot.sportspot.service;

import android.net.Uri;


import com.sportspot.sportspot.converter.ActivityConverter;
import com.sportspot.sportspot.dto.ActivityRequestDto;
import com.sportspot.sportspot.dto.ActivityResponseDto;
import com.sportspot.sportspot.response_model.ResponseModel;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    public static ResponseModel<String> addNewActivity(ActivityRequestDto activityReqDto, String googleIdToken) {

        ResponseModel<String> responseModel = new ResponseModel<>();

        Uri builtUri = Uri.parse(local_url).buildUpon()
                .appendPath("activity")
                .build();

        String json = activityReqDto.toJson();
        RequestBody requestBody = RequestBody.create(JSON, json);

        try {

            Request request = new Request.Builder()
                .url(builtUri.toString())
                .addHeader("Authorization", "Bearer "+ googleIdToken)
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

    public static ResponseModel<List<ActivityResponseDto>> getActivities(String googleIdToken) {

        ResponseModel<List<ActivityResponseDto>> responseModel = new ResponseModel<>();

        Uri builtUri = Uri.parse(local_url).buildUpon()
                .appendPath("activities")
                .build();

        try {

            Request request = new Request.Builder()
                    .url(builtUri.toString())
                    .addHeader("Authorization", "Bearer "+ googleIdToken)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();

            if (response.code() == HttpURLConnection.HTTP_OK) {

                List<ActivityResponseDto> activityDtoList = ActivityConverter.convertToResponseDtoList(response.body().string());
                responseModel.setData(activityDtoList);
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
