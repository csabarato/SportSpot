package com.sportspot.sportspot.service.tasks;

import com.sportspot.sportspot.dto.ActivityResponseDto;
import com.sportspot.sportspot.response_model.ResponseModel;
import com.sportspot.sportspot.service.ActivityService;

import java.util.List;
import java.util.concurrent.Callable;

public class GetActivitiesTask extends BaseAsyncTask<List<ActivityResponseDto>> {

    public GetActivitiesTask(String googleIdToken) {
        super(googleIdToken);
    }

    @Override
    public ResponseModel<List<ActivityResponseDto>> call() {
        return ActivityService.getActivities(googleIdToken);
    }
}
