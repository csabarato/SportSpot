package com.sportspot.sportspot.menus.new_activity.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.sportspot.sportspot.R;

public class ActivityLocationTab extends Fragment {

    public ActivityLocationTab() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_location_tab, container, false);
    }

}
