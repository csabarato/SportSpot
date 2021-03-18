package com.sportspot.sportspot.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class GroundRequestModel {

    private List<String> sportTypes;
    private String coverage;
    private Boolean isCostFree;
    private Integer costPerHour;
    private String description;
    private Double locationLat;
    private Double locationLon;

    public List<String> getSportTypes() {
        return sportTypes;
    }

    public void setSportTypes(List<String> sportTypes) {
        this.sportTypes = sportTypes;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
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
