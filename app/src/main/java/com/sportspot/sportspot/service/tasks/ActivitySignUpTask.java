package com.sportspot.sportspot.service.tasks;

import com.sportspot.sportspot.response_model.ResponseModel;
import com.sportspot.sportspot.service.ActivityService;

public class ActivitySignUpTask extends BaseAsyncTask<Void> {

    private String activityId;

    public ActivitySignUpTask(String googleIdToken, String activityId) {
        super(googleIdToken);
        this.activityId = activityId;
    }

    @Override
    public ResponseModel<Void> call() {
        return ActivityService.activitySignUp(googleIdToken, activityId);
    }
}
