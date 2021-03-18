package com.sportspot.sportspot.menus.new_ground.new_ground_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.sportspot.sportspot.R;
import com.sportspot.sportspot.constants.SportType;
import com.sportspot.sportspot.view_model.NewGroundViewModel;


public class NewGroundDetailsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener,
        View.OnClickListener {

    private View view;
    private AutoCompleteTextView coverageTypeDropdown;
    private CheckBox basketballCheckBox, footballCheckBox, tableTennisCheckBox,
    tennisCheckBox, teqballCheckBox, isCostFreeCheckBox;

    private EditText costPerHourEditText, descEditText;

    private Button nextToGroundLocationBtn;

    private NewGroundViewModel newGroundViewModel;


    public NewGroundDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_ground_details, container, false);

        coverageTypeDropdown = view.findViewById(R.id.coverage_type_dropdown);

        // init NewGroundViewModel
        newGroundViewModel = ViewModelProviders.of(getActivity()).get(NewGroundViewModel.class);

        costPerHourEditText = view.findViewById(R.id.cost_per_hour);
        descEditText = view.findViewById(R.id.ground_desc);

        // init Next button
        nextToGroundLocationBtn = view.findViewById(R.id.next_to_ground_location_btn);
        nextToGroundLocationBtn.setOnClickListener(this);

        setupCoverageTypeDropdown();
        initCheckBoxes();


        return view;
    }

    private void setupCoverageTypeDropdown() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.coverage_types_array, R.layout.sport_type_dropdown_item);
        coverageTypeDropdown.setAdapter(adapter);
        coverageTypeDropdown.setOnItemClickListener(this);
    }

    private void initCheckBoxes() {
        // get sportType CheckBoxes
        basketballCheckBox = view.findViewById(R.id.basketball_checkBox);
        footballCheckBox = view.findViewById(R.id.football_checkBox);
        tableTennisCheckBox = view.findViewById(R.id.table_tennis_checkBox);
        tennisCheckBox = view.findViewById(R.id.tennis_checkBox);
        teqballCheckBox = view.findViewById(R.id.teqball_checkBox);

        // init onCheckedChangedListener
        basketballCheckBox.setOnCheckedChangeListener(this);
        footballCheckBox.setOnCheckedChangeListener(this);
        tableTennisCheckBox.setOnCheckedChangeListener(this);
        tennisCheckBox.setOnCheckedChangeListener(this);
        teqballCheckBox.setOnCheckedChangeListener(this);

        // init isCost-Free checkBox
        isCostFreeCheckBox = view.findViewById(R.id.cost_free_checkBox);
        isCostFreeCheckBox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            if (buttonView.getId() == basketballCheckBox.getId()) {
                newGroundViewModel.addSportType(SportType.BASKETBALL);
            } else if (buttonView.getId() == footballCheckBox.getId()) {
                newGroundViewModel.addSportType(SportType.FOOTBALL);
            } else if (buttonView.getId() == tableTennisCheckBox.getId()) {
                newGroundViewModel.addSportType(SportType.TABLE_TENNIS);
            } else if (buttonView.getId() == tennisCheckBox.getId()) {
                newGroundViewModel.addSportType(SportType.TENNIS);
            } else if (buttonView.getId() == teqballCheckBox.getId()) {
                newGroundViewModel.addSportType(SportType.TEQBALL);
            } else if (buttonView.getId() == isCostFreeCheckBox.getId()) {
                newGroundViewModel.setCostFree(true);
            }
        } else {
            if (buttonView.getId() == basketballCheckBox.getId()) {
                newGroundViewModel.removeSportType(SportType.BASKETBALL);
            } else if (buttonView.getId() == footballCheckBox.getId()) {
                newGroundViewModel.removeSportType(SportType.FOOTBALL);
            } else if (buttonView.getId() == tableTennisCheckBox.getId()) {
                newGroundViewModel.removeSportType(SportType.TABLE_TENNIS);
            } else if (buttonView.getId() == tennisCheckBox.getId()) {
                newGroundViewModel.removeSportType(SportType.TENNIS);
            } else if (buttonView.getId() == teqballCheckBox.getId()) {
                newGroundViewModel.removeSportType(SportType.TEQBALL);
            } else if (buttonView.getId() == isCostFreeCheckBox.getId()) {
                newGroundViewModel.setCostFree(false);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        newGroundViewModel.setGroundCoverage(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.next_to_ground_location_btn) {

            saveData();
            NavHostFragment.findNavController(NewGroundDetailsFragment.this)
                        .navigate(R.id.action_Ground_Details_to_Location);
            }
        }

    private void saveData() {
        newGroundViewModel.setCostFree(isCostFreeCheckBox.isChecked());

        if (costPerHourEditText.getText() != null && !costPerHourEditText.getText().toString().isEmpty()) {
            newGroundViewModel.setCostPerHour(Integer.parseInt(costPerHourEditText.getText().toString()));
        }

        newGroundViewModel.setDescription(descEditText.getText().toString());
    }

}