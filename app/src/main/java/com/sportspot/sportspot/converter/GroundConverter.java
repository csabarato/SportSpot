package com.sportspot.sportspot.converter;

import com.sportspot.sportspot.model.GroundRequestModel;
import com.sportspot.sportspot.view_model.NewGroundViewModel;

import java.util.stream.Collectors;

public class GroundConverter {

    public static GroundRequestModel convertToRequestModel(NewGroundViewModel newGroundViewModel) {

        GroundRequestModel groundRequestModel = new GroundRequestModel();

        groundRequestModel.setSportTypes(newGroundViewModel.getSportTypes().stream().map(Enum::toString).collect(Collectors.toList()));
        groundRequestModel.setCoverage(newGroundViewModel.getGroundCoverage());
        groundRequestModel.setCostFree(newGroundViewModel.getCostFree());
        groundRequestModel.setCostPerHour(newGroundViewModel.getCostPerHour());
        groundRequestModel.setLocationLat(newGroundViewModel.getLocationLat());
        groundRequestModel.setLocationLon(newGroundViewModel.getLocationLon());

        if (newGroundViewModel.getDescription() != null && !newGroundViewModel.getDescription().isEmpty()) {
            groundRequestModel.setDescription(newGroundViewModel.getDescription());
        }

        return groundRequestModel;
    }

}
