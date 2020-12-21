package com.sportspot.sportspot.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportspot.sportspot.dto.ActivityRequestDto;
import com.sportspot.sportspot.dto.ActivityResponseDto;
import com.sportspot.sportspot.view_model.ActivityViewModel;

import java.util.List;


public class ActivityConverter {

    public static ActivityRequestDto convertToRequestDto(ActivityViewModel activityViewModel) {

        ActivityRequestDto requestDto = new ActivityRequestDto();

        requestDto.setSportType(activityViewModel.getSportType());
        requestDto.setLocationLat(activityViewModel.getLocationLat());
        requestDto.setLocationLon(activityViewModel.getLocationLon());
        requestDto.setStartDate(activityViewModel.getStartDate().getTimeInMillis());
        requestDto.setNumOfPersons(activityViewModel.getNumOfPersons());

        if (requestDto.getDescription() != null && !requestDto.getDescription().isEmpty()) {
            requestDto.setDescription(activityViewModel.getDescription());
        }

        return requestDto;
    }

    public static List<ActivityResponseDto> convertToResponseDtoList(String json) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<ActivityResponseDto> list = objectMapper.readValue(json, new TypeReference<List<ActivityResponseDto>>() {});
            return list;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
