package com.sportspot.sportspot.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ActivityRequestModel {

    private String sportType;
    private Double locationLat;
    private Double locationLon;
    private Long startDate;
    private Integer numOfPersons;
    private String description;

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

    public String toJson() {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
