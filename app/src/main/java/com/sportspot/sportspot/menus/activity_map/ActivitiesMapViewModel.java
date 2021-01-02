package com.sportspot.sportspot.menus.activity_map;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sportspot.sportspot.R;
import com.sportspot.sportspot.dto.ActivityModel;
import com.sportspot.sportspot.service.tasks.ActivitySignUpTask;
import com.sportspot.sportspot.service.tasks.GetActivitiesTask;
import com.sportspot.sportspot.shared.AsyncTaskRunner;
import com.sportspot.sportspot.shared.AlertDialogDetails;

import java.util.List;
import java.util.Optional;

public class ActivitiesMapViewModel extends AndroidViewModel {

    private MutableLiveData<List<ActivityModel>> activities = new MutableLiveData<>();
    private MutableLiveData<AlertDialogDetails> alertDetailsLiveData = new MutableLiveData<>();
    private AsyncTaskRunner asyncTaskRunner = AsyncTaskRunner.getInstance();

    public ActivitiesMapViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<ActivityModel>> getActivities() {
        return activities;
    }

    public MutableLiveData<AlertDialogDetails> getAlertDetailsLiveData() {
        return alertDetailsLiveData;
    }

    public void loadActivities(String googleIdToken) {
        asyncTaskRunner.executeAsync(new GetActivitiesTask(googleIdToken),
                (data) -> {

                    if (data.getErrors().isEmpty() && data.getData() != null) {
                        activities.setValue(data.getData());
                    } else {
                        AlertDialogDetails alertDetails = new AlertDialogDetails(
                                getApplication().getResources().getString(R.string.activities_load_error),
                                String.join(";", data.getErrors()));

                        alertDetailsLiveData.setValue(alertDetails);
                    }
                });
    }

    public void signUpToActivity(String googleIdToken, String activityId) {
        AlertDialogDetails alertDialogDetails = new AlertDialogDetails();

        AsyncTaskRunner.getInstance()
                .executeAsync(
                        new ActivitySignUpTask(googleIdToken, activityId),
                        data -> {
                            if (data.getErrors().isEmpty()) {
                                this.updateActivity(data.getData());

                                alertDialogDetails.setTitle( "Signup succesful");
                                alertDialogDetails.setMessage("You've signed up for this activity.");
                                alertDetailsLiveData.setValue(alertDialogDetails);
                            } else {
                                alertDialogDetails.setTitle( "Signup error");
                                alertDialogDetails.setMessage(String.join(";", data.getErrors()));
                                alertDetailsLiveData.setValue(alertDialogDetails);
                            }
                        });
    }

    private void updateActivity(ActivityModel updatedActivity) {

        Optional<ActivityModel> oldActivity = activities.getValue()
                .stream()
                .filter(act -> act.get_id().equals(updatedActivity.get_id()))
                .findAny();

        if (oldActivity.isPresent()) {
            List<ActivityModel> activityModels = activities.getValue();
            activityModels.set(activityModels.indexOf(oldActivity.get()), updatedActivity);
            activities.setValue(activityModels);
        }
    }
}
