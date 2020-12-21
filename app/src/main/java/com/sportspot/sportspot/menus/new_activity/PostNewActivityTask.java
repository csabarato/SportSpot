package com.sportspot.sportspot.menus.new_activity;

import com.sportspot.sportspot.dto.ActivityRequestDto;
import com.sportspot.sportspot.response_model.ResponseModel;
import com.sportspot.sportspot.service.ActivityService;


import java.util.concurrent.Callable;

public class PostNewActivityTask implements Callable<ResponseModel<String>> {

    private String googleIdToken;
    private ActivityRequestDto activityReqDto;

    public PostNewActivityTask(ActivityRequestDto activityReqDto, String googleIdToken) {
        this.googleIdToken = googleIdToken;
        this.activityReqDto = activityReqDto;
    }

    @Override
    public ResponseModel<String> call() {
        return ActivityService.addNewActivity(activityReqDto, googleIdToken);
    }
}
