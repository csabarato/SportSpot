package com.sportspot.sportspot.service.tasks;

import android.content.Context;

import com.sportspot.sportspot.constants.ActivityType;
import com.sportspot.sportspot.constants.SportType;
import com.sportspot.sportspot.model.ActivityModel;
import com.sportspot.sportspot.model.ResponseModel;
import com.sportspot.sportspot.service.ActivityService;

import java.util.List;

public class GetActivitiesTask extends BaseAsyncTask<List<ActivityModel>> {

    private ActivityType activityType;
    private SportType sportType;

    public GetActivitiesTask(Context context, ActivityType activityType, SportType sportType) {
        super(context);
        this.activityType = activityType;
        this.sportType = sportType;
    }

    @Override
    public ResponseModel<List<ActivityModel>> call() {
        return ActivityService.getActivities(authDetails, activityType, sportType);
    }
}
