package com.sportspot.sportspot.service.tasks;

import com.sportspot.sportspot.model.ResponseModel;

import java.util.concurrent.Callable;

public abstract class BaseAsyncTask<T> implements Callable<ResponseModel<T>> {

    protected String googleIdToken;

    public BaseAsyncTask(String googleIdToken) {
        this.googleIdToken = googleIdToken;
    }


}
