package com.sportspot.sportspot.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.sportspot.sportspot.constants.SportType;
import com.sportspot.sportspot.converter.GroundConverter;
import com.sportspot.sportspot.service.tasks.PostNewGroundTask;
import com.sportspot.sportspot.shared.AlertDialogDetails;
import com.sportspot.sportspot.shared.AsyncTaskRunner;

import java.util.ArrayList;
import java.util.List;

public class NewGroundViewModel extends AndroidViewModel {

    private List<SportType> sportTypes = new ArrayList<>();
    private String groundCoverage;
    private Boolean isCostFree;
    private Integer costPerHour;
    private String description;
    private Double locationLat;
    private Double locationLon;

    public NewGroundViewModel(@NonNull Application application) {
        super(application);
    }

    public List<SportType> getSportTypes() {
        return sportTypes;
    }

    public void setSportTypes(List<SportType> sportTypes) {
        this.sportTypes = sportTypes;
    }

    public String getGroundCoverage() {
        return groundCoverage;
    }

    public void setGroundCoverage(String groundCoverage) {
        this.groundCoverage = groundCoverage;
    }

    public Boolean getCostFree() {
        return isCostFree;
    }

    public void setCostFree(Boolean costFree) {
        isCostFree = costFree;
    }

    public Integer getCostPerHour() {
        return costPerHour;
    }

    public void setCostPerHour(Integer costPerHour) {
        this.costPerHour = costPerHour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void addSportType(SportType sportType) {
        this.sportTypes.add(sportType);
    }

    public void removeSportType(SportType sportType) {
        this.sportTypes.remove(sportType);
    }

    public void submitNewGround() {

        AlertDialogDetails alertDialogDetails = new AlertDialogDetails();
        AsyncTaskRunner.getInstance().executeAsync(new PostNewGroundTask(
                getApplication().getApplicationContext(),
                GroundConverter.convertToRequestModel(this)), (data) -> {

            if (!data.getErrors().isEmpty()) {
                alertDialogDetails.setTitle("Submit new ground failed");
                alertDialogDetails.setMessage(String.join("; ", data.getErrors()));
            } else {
                alertDialogDetails.setTitle("Ground saved successfully!");
                alertDialogDetails.setMessage(null);
            }
        });
    }
}
