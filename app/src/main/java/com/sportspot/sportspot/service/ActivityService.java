package com.sportspot.sportspot.service;

import android.net.Uri;


import com.sportspot.sportspot.constants.ActivityType;
import com.sportspot.sportspot.constants.ConfigConstants;
import com.sportspot.sportspot.constants.MediaTypes;
import com.sportspot.sportspot.constants.SportType;
import com.sportspot.sportspot.converter.ActivityConverter;
import com.sportspot.sportspot.model.ActivityRequestModel;
import com.sportspot.sportspot.model.ActivityModel;
import com.sportspot.sportspot.model.AuthDetails;
import com.sportspot.sportspot.model.ResponseModel;
import com.sportspot.sportspot.utils.ConfigUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActivityService {

    private static final OkHttpClient client = new OkHttpClient();
    private static final String api_url = ConfigUtil.getDevProperties().getProperty(ConfigConstants.API_URL);

    public static ResponseModel<String> addNewActivity(ActivityRequestModel activityReqModel, AuthDetails authDetails) {

        ResponseModel<String> responseModel = new ResponseModel<>();

        Uri builtUri = Uri.parse(api_url).buildUpon()
                .appendPath("activity")
                .build();

        String json = activityReqModel.toJson();
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

    public static ResponseModel<List<ActivityModel>> getActivities(AuthDetails authDetails, ActivityType activityType, SportType sportType) {

        ResponseModel<List<ActivityModel>> responseModel = new ResponseModel<>();

        Uri.Builder uriBuilder = Uri.parse(api_url).buildUpon()
                .appendPath("activities");

        if (activityType != null && activityType != ActivityType.ALL_ACTIVITIES) {
            uriBuilder.appendQueryParameter("q", activityType.getFilterValue());
        }

        if (sportType != null) {
            uriBuilder.appendQueryParameter("sportType", sportType.getName());
        }

        try {

            Request request = new Request.Builder()
                    .url(uriBuilder.build().toString())
                    .addHeader("Authorization", "Bearer "+ authDetails.getUserIdToken())
                    .addHeader("Auth-Type", authDetails.getAuthType().toString())
                    .get()
                    .build();

            Response response = client.newCall(request).execute();

            if (response.code() == HttpURLConnection.HTTP_OK) {

                List<ActivityModel> activities = ActivityConverter.convertToActivityModelList(response.body().string());
                responseModel.setData(activities);
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

    public static ResponseModel<ActivityModel> activitySignUp(AuthDetails authDetails, String activityId) {

        ResponseModel<ActivityModel> responseModel = new ResponseModel<>();

        Uri builtUri = Uri.parse(api_url).buildUpon()
                    .appendPath("activity")
                    .appendPath("sign_up")
                    .build();

        try {

            // Build request body
            JSONObject bodyJSONObj = new JSONObject();
            bodyJSONObj.put("activity", activityId);

            RequestBody body = RequestBody.create(MediaTypes.JSON, bodyJSONObj.toString());

            Request request = new Request.Builder()
                    .url(builtUri.toString())
                    .addHeader("Authorization", "Bearer "+ authDetails.getUserIdToken())
                    .addHeader("Auth-Type", authDetails.getAuthType().toString())
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            if (response.code() == HttpURLConnection.HTTP_OK) {
                responseModel.setData(ActivityConverter.convertToActivityModel(response.body().string()));
            } else {
                responseModel.setErrors(Arrays.asList("Error: "+ (response.code()),response.message(), response.body().string()));
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            responseModel.setErrors(Collections.singletonList(e.getMessage()));
            return responseModel;
        }
        return responseModel;
    }

    public static ResponseModel<ActivityModel> removeActivitySignup(AuthDetails authDetails, String activityId) {

        ResponseModel<ActivityModel> responseModel = new ResponseModel<>();

        Uri builtUri = Uri.parse(api_url).buildUpon()
                .appendPath("activity")
                .appendPath("remove_signup")
                .build();

        try {

            // Build request body
            JSONObject bodyJSONObj = new JSONObject();
            bodyJSONObj.put("activity", activityId);

            RequestBody body = RequestBody.create(MediaTypes.JSON, bodyJSONObj.toString());

            Request request = new Request.Builder()
                    .url(builtUri.toString())
                    .addHeader("Authorization", "Bearer " + authDetails.getUserIdToken())
                    .addHeader("Auth-Type", authDetails.getAuthType().toString())
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            if (response.code() == HttpURLConnection.HTTP_OK) {
                responseModel.setData(ActivityConverter.convertToActivityModel(response.body().string()));
            } else {
                responseModel.setErrors(Arrays.asList("Error: " + (response.code()), response.message(), response.body().string()));
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            responseModel.setErrors(Collections.singletonList(e.getMessage()));
            return responseModel;
        }
        return responseModel;
    }
}
