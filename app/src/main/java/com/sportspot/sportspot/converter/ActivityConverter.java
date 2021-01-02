package com.sportspot.sportspot.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportspot.sportspot.model.ActivityRequestModel;
import com.sportspot.sportspot.model.ActivityModel;
import com.sportspot.sportspot.view_model.NewActivityViewModel;

import java.util.List;


public class ActivityConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ActivityRequestModel convertToRequestModel(NewActivityViewModel newActivityViewModel) {

        ActivityRequestModel activityReqModel = new ActivityRequestModel();

        activityReqModel.setSportType(newActivityViewModel.getSportType());
        activityReqModel.setLocationLat(newActivityViewModel.getLocationLat());
        activityReqModel.setLocationLon(newActivityViewModel.getLocationLon());
        activityReqModel.setStartDate(newActivityViewModel.getStartDate().getTimeInMillis());
        activityReqModel.setNumOfPersons(newActivityViewModel.getNumOfPersons());

        if (newActivityViewModel.getDescription() != null && !newActivityViewModel.getDescription().isEmpty()) {
            activityReqModel.setDescription(newActivityViewModel.getDescription());
        }

        return activityReqModel;
    }

    public static List<ActivityModel> convertToActivityModelList(String json) {

        try {
            return objectMapper.readValue(json, new TypeReference<List<ActivityModel>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ActivityModel convertToActivityModel(String json) {

        try {
            return objectMapper.readValue(json, ActivityModel.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
