package com.sportspot.sportspot.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sportspot.sportspot.auth.google.GoogleSignInService;
import com.sportspot.sportspot.converter.ActivityConverter;
import com.sportspot.sportspot.service.tasks.PostNewActivityTask;
import com.sportspot.sportspot.shared.AlertDialogDetails;
import com.sportspot.sportspot.shared.AsyncTaskRunner;

import java.util.Calendar;

public class NewActivityViewModel extends AndroidViewModel {

    private String description;
    private String sportType;
    private Double locationLat;
    private Double locationLon;
    private Calendar startDate;
    private Integer numOfPersons;

    private MutableLiveData<AlertDialogDetails> alertDetailsLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSubmitPending = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isSubmitted = new MutableLiveData<>();

    public NewActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public Double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(Double locationLat) {
        this.locationLat = locationLat;
    }

    public Double getLocationLon() {
        return locationLon;
    }

    public void setLocationLon(Double locationLon) {
        this.locationLon = locationLon;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Integer getNumOfPersons() {
        return numOfPersons;
    }

    public void setNumOfPersons(Integer numOfPersons) {
        this.numOfPersons = numOfPersons;
    }

    public void submitNewActivity() {
        isSubmitPending.setValue(true);
        AlertDialogDetails alertDialogDetails = new AlertDialogDetails();
        AsyncTaskRunner.getInstance().executeAsync(new PostNewActivityTask(
                ActivityConverter.convertToRequestModel(this),
                GoogleSignInService.getLastUserToken(getApplication().getApplicationContext())), (data) -> {

            isSubmitPending.setValue(false);
            if (!data.getErrors().isEmpty()) {
                alertDialogDetails.setTitle("Submit activity failed");
                alertDialogDetails.setMessage(String.join("; ", data.getErrors()));
            } else {
                alertDialogDetails.setTitle("Activity saved successfully!");
                alertDialogDetails.setMessage(null);
                isSubmitted.setValue(true);
            }
            alertDetailsLiveData.setValue(alertDialogDetails);
        });
    }

    public MutableLiveData<AlertDialogDetails> getAlertDetailsLiveData() {
        return alertDetailsLiveData;
    }

    public MutableLiveData<Boolean> isSubmitPending() {
        return isSubmitPending;
    }

    public MutableLiveData<Boolean> isSubmitted() {
        return isSubmitted;
    }
}
