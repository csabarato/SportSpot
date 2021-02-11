package com.sportspot.sportspot.service.tasks;

import android.content.Context;

import com.sportspot.sportspot.model.AuthDetails;
import com.sportspot.sportspot.model.ResponseModel;
import com.sportspot.sportspot.utils.AuthUtils;

import java.util.concurrent.Callable;

public abstract class BaseAsyncTask<T> implements Callable<ResponseModel<T>> {

    protected AuthDetails authDetails;

    public BaseAsyncTask(Context context) {
        this.authDetails = AuthUtils.getActiveUserAuthDetails(context);
    }


}
