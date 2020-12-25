package com.sportspot.sportspot.view_model;

import androidx.lifecycle.ViewModel;

import java.util.Calendar;

public class NewActivityViewModel extends ViewModel {

    private String description;
    private String sportType;
    private Double locationLat;
    private Double locationLon;
    private Calendar startDate;
    private Integer numOfPersons;

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
}
