package com.sportspot.sportspot.service.tasks;

import android.content.Context;

import com.sportspot.sportspot.model.ActivityModel;
import com.sportspot.sportspot.model.ResponseModel;
import com.sportspot.sportspot.service.ActivityService;

public class RemoveActivitySignUpTask extends BaseAsyncTask<ActivityModel> {

    private String activityId;

    public RemoveActivitySignUpTask(Context context, String activityId) {
        super(context);
        this.activityId = activityId;
    }

    @Override
    public ResponseModel<ActivityModel> call() throws Exception {
        return ActivityService.removeActivitySignup(authDetails, activityId);
    }
}
