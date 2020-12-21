package com.sportspot.sportspot.menus.new_activity;

import com.sportspot.sportspot.response_model.ResponseModel;
import com.sportspot.sportspot.service.ActivityService;
import com.sportspot.sportspot.view_model.ActivityDetailsViewModel;


import java.util.concurrent.Callable;

public class PostNewActivityTask implements Callable<ResponseModel<String>> {

    private String googleIdToken;
    private ActivityDetailsViewModel activityDetails;

    public PostNewActivityTask(ActivityDetailsViewModel activityDetails, String googleIdToken) {
        this.googleIdToken = googleIdToken;
        this.activityDetails = activityDetails;
    }

    @Override
    public ResponseModel<String> call() {
        return ActivityService.addNewActivity(activityDetails, googleIdToken);
    }
}
