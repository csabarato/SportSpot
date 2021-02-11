package com.sportspot.sportspot.service.tasks;

import android.content.Context;

import com.sportspot.sportspot.model.UserDataModel;
import com.sportspot.sportspot.model.ResponseModel;
import com.sportspot.sportspot.service.UserDataService;


public class SyncUserDataTask extends BaseAsyncTask<Void> {

    private final UserDataModel userDataModel;

    public SyncUserDataTask(UserDataModel userDataModel, Context context) {
        super(context);
        this.userDataModel = userDataModel;
    }

    @Override
    public ResponseModel<Void> call() {
        return UserDataService.syncUserData(userDataModel, authDetails);
    }
}
