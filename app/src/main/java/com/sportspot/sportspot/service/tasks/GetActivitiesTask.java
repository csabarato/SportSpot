package com.sportspot.sportspot.service.tasks;

import com.sportspot.sportspot.dto.ActivityModel;
import com.sportspot.sportspot.response_model.ResponseModel;
import com.sportspot.sportspot.service.ActivityService;

import java.util.List;

public class GetActivitiesTask extends BaseAsyncTask<List<ActivityModel>> {

    public GetActivitiesTask(String googleIdToken) {
        super(googleIdToken);
    }

    @Override
    public ResponseModel<List<ActivityModel>> call() {
        return ActivityService.getActivities(googleIdToken);
    }
}
