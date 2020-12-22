package com.sportspot.sportspot.service.tasks;

import com.sportspot.sportspot.dto.UserDataDto;
import com.sportspot.sportspot.response_model.ResponseModel;
import com.sportspot.sportspot.service.UserDataService;


public class SyncUserDataTask extends BaseAsyncTask<Void> {

    private final UserDataDto userDataDto;

    public SyncUserDataTask(UserDataDto userDataDto, String googleIdToken) {
        super(googleIdToken);
        this.userDataDto = userDataDto;
    }

    @Override
    public ResponseModel<Void> call() {
        return UserDataService.syncUserData(userDataDto, googleIdToken);
    }
}
