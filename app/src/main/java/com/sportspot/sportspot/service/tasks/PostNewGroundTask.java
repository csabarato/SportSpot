package com.sportspot.sportspot.service.tasks;

import android.content.Context;

import com.sportspot.sportspot.model.GroundRequestModel;
import com.sportspot.sportspot.model.ResponseModel;
import com.sportspot.sportspot.service.GroundService;

public class PostNewGroundTask extends BaseAsyncTask<String> {

    private GroundRequestModel groundRequestModel;

    public PostNewGroundTask(Context context, GroundRequestModel groundRequestModel) {
        super(context);
        this.groundRequestModel = groundRequestModel;
    }

    @Override
    public ResponseModel<String> call() {
        return GroundService.addNewGround(groundRequestModel, authDetails);
    }
}
