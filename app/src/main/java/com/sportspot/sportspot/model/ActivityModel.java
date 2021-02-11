package com.sportspot.sportspot.model;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;

public class ActivityModel {

    private String _id;
    private UserDataModel owner;
    private SportTypeModel sportType;
    private Double locationLatitude;
    private Double locationLongitude;
    private Long startDate;
    private Integer numOfPersons;
    private Integer remainingPlaces;
    private String description;
    private List<String> signedUpUsers;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public UserDataModel getOwner() {
        return owner;
    }

    public void setOwner(UserDataModel owner) {
        this.owner = owner;
    }

    public SportTypeModel getSportType() {
        return sportType;
    }

    public void setSportType(SportTypeModel sportType) {
        this.sportType = sportType;
    }

    public Double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public Double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(Double locationLon) {
        this.locationLongitude = locationLon;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Integer getNumOfPersons() {
        return numOfPersons;
    }

    public void setNumOfPersons(Integer numOfPersons) {
        this.numOfPersons = numOfPersons;
    }

    public Integer getRemainingPlaces() {
        return remainingPlaces;
    }

    public void setRemainingPlaces(Integer remainingPlaces) {
        this.remainingPlaces = remainingPlaces;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSignedUpUsers() {
        return signedUpUsers;
    }

    public void setSignedUpUsers(List<String> signedUpUsers) {
        this.signedUpUsers = signedUpUsers;
    }

    public boolean isUserSignedUp(String userId) {
        return signedUpUsers.contains(userId);
    }
}
