package com.sportspot.sportspot.service.tasks;

import com.sportspot.sportspot.constants.ActivityFilter;
import com.sportspot.sportspot.model.ActivityModel;
import com.sportspot.sportspot.model.ResponseModel;
import com.sportspot.sportspot.service.ActivityService;

import java.util.List;

public class GetActivitiesTask extends BaseAsyncTask<List<ActivityModel>> {

    private ActivityFilter activityFilter;

    public GetActivitiesTask(String googleIdToken, ActivityFilter activityFilter) {
        super(googleIdToken);
        this.activityFilter = activityFilter;
    }

    @Override
    public ResponseModel<List<ActivityModel>> call() {
        return ActivityService.getActivities(googleIdToken, activityFilter);
    }
}
