package com.sportspot.sportspot.service.tasks;

import com.sportspot.sportspot.model.UserDataModel;
import com.sportspot.sportspot.model.ResponseModel;
import com.sportspot.sportspot.service.UserDataService;


public class SyncUserDataTask extends BaseAsyncTask<Void> {

    private final UserDataModel userDataModel;

    public SyncUserDataTask(UserDataModel userDataModel, String googleIdToken) {
        super(googleIdToken);
        this.userDataModel = userDataModel;
    }

    @Override
    public ResponseModel<Void> call() {
        return UserDataService.syncUserData(userDataModel, googleIdToken);
    }
}
