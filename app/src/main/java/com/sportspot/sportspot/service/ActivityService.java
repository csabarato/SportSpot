package com.sportspot.sportspot.service;

import android.net.Uri;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

    public static String addNewActivity(String token) {

        Uri builtUri = Uri.parse(base_url).buildUpon()
                .appendQueryParameter("id_token", token)
                .build();

        Request request = new Request.Builder()
                .url(builtUri.toString())
                .get()
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    return null;
    }

    public static String getHello() throws JSONException {

        Uri builtUri = Uri.parse(local_url);

        JSONObject json = new JSONObject();
        json.put("msg", "posted");

        RequestBody body = RequestBody.create(JSON, json.toString());

        Request request = new Request.Builder()
                .url(builtUri.toString())
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
