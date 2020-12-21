package com.sportspot.sportspot.dto;

import java.util.List;

public class ActivityResponseDto {

    private String _id;
    private String owner;
    private SportTypeDto sportType;
    private Double locationLatitude;
    private Double locationLongitude;
    private Long startDate;
    private Integer numOfPersons;
    private String description;
    private List<String> signedUpUsers;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public SportTypeDto getSportType() {
        return sportType;
    }

    public void setSportType(SportTypeDto sportType) {
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
}
