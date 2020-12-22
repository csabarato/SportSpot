package com.sportspot.sportspot.service.tasks;

import com.sportspot.sportspot.dto.ActivityRequestDto;
import com.sportspot.sportspot.response_model.ResponseModel;
import com.sportspot.sportspot.service.ActivityService;



public class PostNewActivityTask extends BaseAsyncTask<String> {

    private final ActivityRequestDto activityReqDto;

    public PostNewActivityTask(ActivityRequestDto activityReqDto, String googleIdToken) {
        super(googleIdToken);
        this.activityReqDto = activityReqDto;
    }

    @Override
    public ResponseModel<String> call() {
        return ActivityService.addNewActivity(activityReqDto, googleIdToken);
    }
}
