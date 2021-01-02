package com.sportspot.sportspot.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportspot.sportspot.dto.ActivityRequestDto;
import com.sportspot.sportspot.dto.ActivityModel;
import com.sportspot.sportspot.view_model.NewActivityViewModel;

import java.util.List;


public class ActivityConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ActivityRequestDto convertToRequestDto(NewActivityViewModel newActivityViewModel) {

        ActivityRequestDto requestDto = new ActivityRequestDto();

        requestDto.setSportType(newActivityViewModel.getSportType());
        requestDto.setLocationLat(newActivityViewModel.getLocationLat());
        requestDto.setLocationLon(newActivityViewModel.getLocationLon());
        requestDto.setStartDate(newActivityViewModel.getStartDate().getTimeInMillis());
        requestDto.setNumOfPersons(newActivityViewModel.getNumOfPersons());

        if (newActivityViewModel.getDescription() != null && !newActivityViewModel.getDescription().isEmpty()) {
            requestDto.setDescription(newActivityViewModel.getDescription());
        }

        return requestDto;
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
