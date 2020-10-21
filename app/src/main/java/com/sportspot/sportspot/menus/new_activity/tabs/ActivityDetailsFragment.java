package com.sportspot.sportspot.menus.new_activity.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.sportspot.sportspot.R;
import com.sportspot.sportspot.view_model.ActivityDetailsViewModel;

public class ActivityDetailsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText activityDescEditText;
    private ActivityDetailsViewModel activityDetailsViewModel;
    private AutoCompleteTextView sportTypeDropdown;
    private String selectedSportType = null;

    public ActivityDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_details_fragment, container, false);

        Button nextToLocationButton = view.findViewById(R.id.next_to_location_button);
        activityDescEditText = view.findViewById(R.id.activity_desc);
        sportTypeDropdown = view.findViewById(R.id.sport_type_dropdown);

        setupSportTypeDropdown();

        activityDetailsViewModel = ViewModelProviders.of(getActivity()).get(ActivityDetailsViewModel.class);
        nextToLocationButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_to_location_button:
                saveData();
                NavHostFragment.findNavController(ActivityDetailsFragment.this)
                        .navigate(R.id.action_Details_to_Location);
                break;
        }
    }

    private void saveData() {

        if (!activityDescEditText.getText().toString().isEmpty()) {
            activityDetailsViewModel.setDescription(activityDescEditText.getText().toString());
        }

        if (selectedSportType == null || !selectedSportType.isEmpty()) {
            activityDetailsViewModel.setSportType(selectedSportType);
        }
    }

    private void setupSportTypeDropdown() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.sport_type_items_array, R.layout.sport_type_dropdown_item);
        sportTypeDropdown.setAdapter(adapter);
        sportTypeDropdown.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedSportType = parent.getItemAtPosition(position).toString();
    }
}
