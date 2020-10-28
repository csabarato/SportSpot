package com.sportspot.sportspot.menus.new_activity.tabs;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.sportspot.sportspot.R;
import com.sportspot.sportspot.utils.TextValidator;
import com.sportspot.sportspot.view_model.ActivityDetailsViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ActivityDetailsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText activityDescEditText, startDateEditText, startTimeEditText, numOfPersonsEditText;
    private ImageView startDatePickerIcon, startTimePickerIcon;
    private ActivityDetailsViewModel activityDetailsViewModel;
    private AutoCompleteTextView sportTypeDropdown;
    private String selectedSportType = null;
    private boolean isDetailsFormValid;

    private TextInputLayout sportTypeInputLayout, startDateInputLayout,
            startTimeInputLayout, numOfPersonsInputLayout;
    
    private Calendar startDatetimeCalendar = Calendar.getInstance();

    private final SimpleDateFormat startDateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat startTimeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public ActivityDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_details_fragment, container, false);

        Button nextToLocationButton = view.findViewById(R.id.next_to_location_button);

        activityDescEditText = view.findViewById(R.id.activity_desc);

        sportTypeDropdown = view.findViewById(R.id.sport_type_dropdown);
        sportTypeInputLayout = view.findViewById(R.id.sport_type_input_layout);


        startDateEditText = view.findViewById(R.id.start_date);
        startDateEditText.setOnClickListener(this);
        startDateInputLayout = view.findViewById(R.id.start_date_input_layout);

        startDatePickerIcon = view.findViewById(R.id.start_date_picker_icon);
        startDatePickerIcon.setOnClickListener(this);

        startTimeEditText = view.findViewById(R.id.start_time);
        startTimeEditText.setOnClickListener(this);
        startTimeInputLayout = view.findViewById(R.id.start_time_input_layout);

        startTimePickerIcon = view.findViewById(R.id.start_time_picker_icon);
        startTimePickerIcon.setOnClickListener(this);

        numOfPersonsEditText = view.findViewById(R.id.activity_persons_num);
        numOfPersonsEditText.addTextChangedListener(initNumOfPersonsInputValidator(numOfPersonsEditText));

        numOfPersonsInputLayout = view.findViewById(R.id.activity_persons_num_input_layout);

        setupSportTypeDropdown();

        activityDetailsViewModel = ViewModelProviders.of(getActivity()).get(ActivityDetailsViewModel.class);
        nextToLocationButton.setOnClickListener(this);
        return view;
    }

    private void setupSportTypeDropdown() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.sport_type_items_array, R.layout.sport_type_dropdown_item);
        sportTypeDropdown.setAdapter(adapter);
        sportTypeDropdown.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedSportType = parent.getItemAtPosition(position).toString();
        validateSportTypeDropdown();
    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        if (viewId == R.id.next_to_location_button) {
            validateData();
            if (isDetailsFormValid) {
                saveData();
                NavHostFragment.findNavController(ActivityDetailsFragment.this)
                        .navigate(R.id.action_Details_to_Location);
            }
        }
        else if (viewId == R.id.start_date || viewId == R.id.start_date_picker_icon) {
            showDatePickerDialog();
        }
        else if (viewId == R.id.start_time_picker_icon || viewId == R.id.start_time) {
            showTimePickerDialog();
        }
    }

    private void showDatePickerDialog() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Material_Dialog,
                (view, year, month, dayOfMonth) -> {

                startDatetimeCalendar.set(Calendar.YEAR, year);
                startDatetimeCalendar.set(Calendar.MONTH, month);
                startDatetimeCalendar.set(Calendar.DATE, dayOfMonth);

                startDateEditText.setText(startDateFormatter.format(startDatetimeCalendar.getTime()));
                validateStartDateInput();
                }, startDatetimeCalendar.get(Calendar.YEAR), startDatetimeCalendar.get(Calendar.MONTH), startDatetimeCalendar.get(Calendar.DATE));

        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

    private void showTimePickerDialog() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hourOfDay, minute) -> {

                startDatetimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                startDatetimeCalendar.set(Calendar.MINUTE, minute);

                startTimeEditText.setText(startTimeFormatter.format(startDatetimeCalendar.getTime()));
                validateStartTimeInput();
                }, startDatetimeCalendar.get(Calendar.HOUR_OF_DAY), startDatetimeCalendar.get(Calendar.MINUTE), true);

        timePickerDialog.show();
    }

    private void validateData() {
        isDetailsFormValid = true;

        validateSportTypeDropdown();
        validateStartDateInput();
        validateStartTimeInput();
        validateNumOfPersonsInput();
    }

    private void validateSportTypeDropdown() {
        if (selectedSportType == null || selectedSportType.isEmpty()) {
            sportTypeInputLayout.setError(getString(R.string.new_activity_sport_required));
            isDetailsFormValid = false;
        } else {
            sportTypeInputLayout.setError(null);
        }
    }

    private void validateStartDateInput() {
        if (startDateEditText.getText() == null || startDateEditText.getText().toString().isEmpty()) {
            startDateInputLayout.setError(getString(R.string.new_activity_start_date_required));
            isDetailsFormValid = false;
        } else {
            startDateInputLayout.setError(null);
        }
    }

    private void validateStartTimeInput() {
        if (startTimeEditText.getText() == null || startTimeEditText.getText().toString().isEmpty()) {
            startTimeInputLayout.setError(getString(R.string.new_activity_start_time_required));
            isDetailsFormValid = false;
        } else {
            startTimeInputLayout.setError(null);
        }
    }

    private void validateNumOfPersonsInput() {
        if (numOfPersonsEditText.getText() == null || numOfPersonsEditText.getText().toString().isEmpty()) {
            numOfPersonsInputLayout.setError(getString(R.string.new_activity_num_of_persons_required));
            isDetailsFormValid = false;
        } else {
            numOfPersonsInputLayout.setError(null);
        }
    }

    private TextWatcher initNumOfPersonsInputValidator(EditText editText) {

        return new TextValidator(editText) {
            @Override
            public void validate(String text) {
                validateNumOfPersonsInput();
            }
        };
    }

    private void saveData() {

        activityDetailsViewModel.setSportType(selectedSportType);
        activityDetailsViewModel.setStartDate(startDatetimeCalendar);
        activityDetailsViewModel.setNumOfPersons(Integer.parseInt(numOfPersonsEditText.getText().toString()));
        activityDetailsViewModel.setDescription(activityDescEditText.getText().toString());
    }
}
