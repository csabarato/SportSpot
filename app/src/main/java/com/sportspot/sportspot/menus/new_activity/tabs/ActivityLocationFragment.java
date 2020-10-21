package com.sportspot.sportspot.menus.new_activity.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.sportspot.sportspot.R;
import com.sportspot.sportspot.view_model.ActivityDetailsViewModel;

public class ActivityLocationFragment extends Fragment implements View.OnClickListener {

    private ActivityDetailsViewModel activityDetailsViewModel;

    public ActivityLocationFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_location_fragment, container, false);
        Button backToDetailsButton = view.findViewById(R.id.back_to_details_button);
        backToDetailsButton.setOnClickListener(this);

        Button submitDetailsButton = view.findViewById(R.id.submit_new_activity);
        submitDetailsButton.setOnClickListener(this);

        activityDetailsViewModel = ViewModelProviders.of(getActivity()).get(ActivityDetailsViewModel.class);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_to_details_button:
                NavHostFragment.findNavController(ActivityLocationFragment.this).navigate(R.id.action_Location_to_Details);
                break;
            case R.id.submit_new_activity:
                break;
        }
    }
}
