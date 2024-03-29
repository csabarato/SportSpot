package com.sportspot.sportspot.shared;

import android.os.Handler;
import android.os.Looper;


import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncTaskRunner {

    private static AsyncTaskRunner asyncTaskRunner;

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public static AsyncTaskRunner getInstance() {
        if (asyncTaskRunner == null) {
            asyncTaskRunner = new AsyncTaskRunner();
        }
        return asyncTaskRunner;
    }

    public interface Callback<R> {
        void onComplete(R result);
    }

    public <R> void executeAsync(Callable<R> callable, Callback<R> callback) {
        executor.execute(() -> {

            try {
                final R result = callable.call();
                handler.post( () -> callback.onComplete(result));

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }
}
