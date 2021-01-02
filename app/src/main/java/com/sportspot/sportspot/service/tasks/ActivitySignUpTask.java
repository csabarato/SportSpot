package com.sportspot.sportspot.service.tasks;

import com.sportspot.sportspot.model.ActivityModel;
import com.sportspot.sportspot.model.ResponseModel;
import com.sportspot.sportspot.service.ActivityService;

public class ActivitySignUpTask extends BaseAsyncTask<ActivityModel> {

    private String activityId;

    public ActivitySignUpTask(String googleIdToken, String activityId) {
        super(googleIdToken);
        this.activityId = activityId;
    }

    @Override
    public ResponseModel<ActivityModel> call() {
        return ActivityService.activitySignUp(googleIdToken, activityId);
    }
}
