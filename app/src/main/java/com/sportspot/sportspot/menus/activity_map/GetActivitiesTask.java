package com.sportspot.sportspot.menus.activity_map;

import com.sportspot.sportspot.dto.ActivityResponseDto;
import com.sportspot.sportspot.response_model.ResponseModel;
import com.sportspot.sportspot.service.ActivityService;

import java.util.List;
import java.util.concurrent.Callable;

public class GetActivitiesTask implements Callable<ResponseModel<List<ActivityResponseDto>>> {

    private String googleIdToken;

    public GetActivitiesTask(String googleIdToken) {
        this.googleIdToken = googleIdToken;
    }

    @Override
    public ResponseModel<List<ActivityResponseDto>> call() {
        return ActivityService.getActivities(googleIdToken);
    }
}
