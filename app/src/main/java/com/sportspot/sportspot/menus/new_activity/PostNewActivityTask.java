package com.sportspot.sportspot.menus.new_activity;

import com.sportspot.sportspot.service.ActivityService;

import org.json.JSONException;

import java.util.concurrent.Callable;

public class PostNewActivityTask implements Callable<String> {

    @Override
    public String call() {
        try {
            return ActivityService.getHello();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
