package com.sportspot.sportspot.service.tasks;

import android.content.Context;

import com.sportspot.sportspot.model.ActivityRequestModel;
import com.sportspot.sportspot.model.ResponseModel;
import com.sportspot.sportspot.service.ActivityService;



public class PostNewActivityTask extends BaseAsyncTask<String> {

    private final ActivityRequestModel activityReqModel;

    public PostNewActivityTask(ActivityRequestModel activityReqModel, Context context) {
        super(context);
        this.activityReqModel = activityReqModel;
    }

    @Override
    public ResponseModel<String> call() {
        return ActivityService.addNewActivity(activityReqModel, authDetails);
    }
}
