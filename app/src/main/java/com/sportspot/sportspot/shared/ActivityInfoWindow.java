package com.sportspot.sportspot.shared;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sportspot.sportspot.auth.google.GoogleSignInService;
import com.sportspot.sportspot.dto.ActivityResponseDto;
import com.sportspot.sportspot.utils.DateUtils;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.Date;

import static com.sportspot.sportspot.R.*;

public class ActivityInfoWindow extends InfoWindow {

    private ActivityResponseDto activityDto;

    public ActivityInfoWindow(int layoutResId, MapView mapView, ActivityResponseDto activityDto) {
        super(layoutResId, mapView);
        this.activityDto = activityDto;
    }

    @Override
    public void onOpen(Object item) {

        TextView activityInfoOwner = mView.findViewById(id.activity_info_owner);
        TextView activityInfoSportType = mView.findViewById(id.activity_info_sport);
        TextView activityInfoNoP = mView.findViewById(id.activity_info_nop);
        TextView activityInfoStartDate = mView.findViewById(id.activity_info_start_date);
        ImageButton infoCloseButton = mView.findViewById(id.info_close_button);
        Button activitySignUpButton = mView.findViewById(id.activity_signup_button);

        TextView activityInfoDescLabel = mView.findViewById(id.activity_info_desc_label);
        TextView activityInfoDesc = mView.findViewById(id.activity_info_desc);

        if (GoogleSignInService.isIdEqualsCurrentUserId(mView.getContext(), activityDto.getOwner().get_id())) {
            activityInfoOwner.setText(string.info_window_owner_me);
        } else {
            activitySignUpButton.setVisibility(View.VISIBLE);
            activityInfoOwner.setText(activityDto.getOwner().getDisplayName());
        }

        activityInfoSportType.setText(activityDto.getSportType().getName());
        activityInfoNoP.setText(Integer.toString(activityDto.getNumOfPersons() - activityDto.getSignedUpUsers().size()));
        activityInfoStartDate.setText(DateUtils.toDateTimeString(new Date(activityDto.getStartDate())));

        if (activityDto.getDescription() != null && !activityDto.getDescription().isEmpty()) {
            activityInfoDescLabel.setVisibility(View.VISIBLE);
            activityInfoDesc.setText(activityDto.getDescription());
        }

        infoCloseButton.setOnClickListener(onClick -> this.close());
    }

    @Override
    public void onClose() {

    }
}
