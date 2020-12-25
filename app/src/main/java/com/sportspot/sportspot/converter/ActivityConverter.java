package com.sportspot.sportspot.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportspot.sportspot.dto.ActivityRequestDto;
import com.sportspot.sportspot.dto.ActivityResponseDto;
import com.sportspot.sportspot.view_model.NewActivityViewModel;

import java.util.List;


public class ActivityConverter {

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

    public static List<ActivityResponseDto> convertToResponseDtoList(String json) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(json, new TypeReference<List<ActivityResponseDto>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
