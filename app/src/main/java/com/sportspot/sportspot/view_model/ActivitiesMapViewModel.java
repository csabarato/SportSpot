package com.sportspot.sportspot.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sportspot.sportspot.R;
import com.sportspot.sportspot.constants.ActivityType;
import com.sportspot.sportspot.constants.SportType;
import com.sportspot.sportspot.model.ActivityModel;
import com.sportspot.sportspot.service.tasks.ActivitySignUpTask;
import com.sportspot.sportspot.service.tasks.GetActivitiesTask;
import com.sportspot.sportspot.service.tasks.RemoveActivitySignUpTask;
import com.sportspot.sportspot.shared.AsyncTaskRunner;
import com.sportspot.sportspot.shared.AlertDialogDetails;

import java.util.List;
import java.util.Optional;

public class ActivitiesMapViewModel extends AndroidViewModel {

    private MutableLiveData<List<ActivityModel>> activities = new MutableLiveData<>();
    private MutableLiveData<AlertDialogDetails> alertDetailsLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isActivitiesLoading = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isSignupPending = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isRemoveSignupPending = new MutableLiveData<>(false);

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

    public void loadActivities(ActivityType activityType, SportType sportType) {
        isActivitiesLoading.setValue(true);
        asyncTaskRunner.executeAsync(
                new GetActivitiesTask(getApplication().getApplicationContext(),
                        activityType, sportType),
                (data) -> {

                    if (data.getErrors().isEmpty() && data.getData() != null) {
                        activities.setValue(data.getData());
                    } else {
                        AlertDialogDetails alertDetails = new AlertDialogDetails(
                                getApplication().getResources().getString(R.string.activities_load_error),
                                String.join(";", data.getErrors()));

                        alertDetailsLiveData.setValue(alertDetails);
                    }
                    isActivitiesLoading.setValue(false);
                });
    }

    public void signUpToActivity(String activityId) {
        isSignupPending.setValue(true);
        AlertDialogDetails alertDialogDetails = new AlertDialogDetails();
        AsyncTaskRunner.getInstance()
                .executeAsync(
                        new ActivitySignUpTask(getApplication().getApplicationContext(),activityId),
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
                            isSignupPending.setValue(false);
                        });
    }

    public void removeActivitySignup(String activityId) {
        isRemoveSignupPending.setValue(true);
        AlertDialogDetails alertDialogDetails = new AlertDialogDetails();
        AsyncTaskRunner.getInstance()
                .executeAsync(
                        new RemoveActivitySignUpTask(getApplication().getApplicationContext(),activityId),
                        data -> {
                            if (data.getErrors().isEmpty()) {
                                this.updateActivity(data.getData());

                                alertDialogDetails.setTitle( "Signup removed succesfully");
                                alertDialogDetails.setMessage("You've removed your signup from this activity.");
                                alertDetailsLiveData.setValue(alertDialogDetails);
                            } else {
                                alertDialogDetails.setTitle( "Remove signup error");
                                alertDialogDetails.setMessage(String.join(";", data.getErrors()));
                                alertDetailsLiveData.setValue(alertDialogDetails);
                            }
                            isRemoveSignupPending.setValue(false);
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

    public MutableLiveData<Boolean> isActivitiesLoading() {
        return isActivitiesLoading;
    }

    public MutableLiveData<Boolean> isSignupPending() {
        return isSignupPending;
    }

    public MutableLiveData<Boolean> isRemoveSignupPending() {return isRemoveSignupPending;}
}
