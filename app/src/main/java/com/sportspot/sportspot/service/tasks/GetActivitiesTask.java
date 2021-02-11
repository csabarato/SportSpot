package com.sportspot.sportspot.service.tasks;

import android.content.Context;

import com.sportspot.sportspot.constants.ActivityFilter;
import com.sportspot.sportspot.model.ActivityModel;
import com.sportspot.sportspot.model.ResponseModel;
import com.sportspot.sportspot.service.ActivityService;

import java.util.List;

public class GetActivitiesTask extends BaseAsyncTask<List<ActivityModel>> {

    private ActivityFilter activityFilter;

    public GetActivitiesTask(Context context, ActivityFilter activityFilter) {
        super(context);
        this.activityFilter = activityFilter;
    }

    @Override
    public ResponseModel<List<ActivityModel>> call() {
        return ActivityService.getActivities(authDetails, activityFilter);
    }
}
