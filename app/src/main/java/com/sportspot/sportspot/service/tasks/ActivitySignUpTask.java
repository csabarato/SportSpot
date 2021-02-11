package com.sportspot.sportspot.service.tasks;

import android.content.Context;

import com.sportspot.sportspot.model.ActivityModel;
import com.sportspot.sportspot.model.ResponseModel;
import com.sportspot.sportspot.service.ActivityService;

public class ActivitySignUpTask extends BaseAsyncTask<ActivityModel> {

    private String activityId;

    public ActivitySignUpTask(Context context, String activityId) {
        super(context);
        this.activityId = activityId;
    }

    @Override
    public ResponseModel<ActivityModel> call() {
        return ActivityService.activitySignUp(authDetails, activityId);
    }
}
