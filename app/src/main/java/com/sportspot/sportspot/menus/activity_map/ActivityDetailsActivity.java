package com.sportspot.sportspot.menus.activity_map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.sportspot.sportspot.R;
import com.sportspot.sportspot.model.ActivityModel;
import com.sportspot.sportspot.shared.SignedUpUsersListAdapter;
import com.sportspot.sportspot.utils.DateUtils;

public class ActivityDetailsActivity extends AppCompatActivity {

    private TextView sportTypeTextView, startDateTimeTextView, ownerTextView,
    placesTextView, descriptionTextView;

    MaterialCardView descriptionCardView;
    RecyclerView signedUpUsersRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setToolbar();

        ActivityModel activity = (ActivityModel) getIntent().getSerializableExtra("activity");

        ownerTextView = findViewById(R.id.owner_card_text);
        sportTypeTextView = findViewById(R.id.sport_type_card_text);
        placesTextView = findViewById(R.id.places_card_text);
        startDateTimeTextView = findViewById(R.id.start_datetime_card_text);
        descriptionCardView = findViewById(R.id.description_card);
        descriptionTextView = findViewById(R.id.description_card_text);

        ownerTextView.setText(activity.getOwner().getDisplayName());
        sportTypeTextView.setText(activity.getSportType().getName());
        placesTextView.setText(activity.getRemainingPlaces() + " / "+ (activity.getNumOfPersons()));
        startDateTimeTextView.setText(DateUtils.toDateTimeString(activity.getStartDate()));

        if (activity.getDescription() != null && !activity.getDescription().isEmpty()) {
            descriptionCardView.setVisibility(View.VISIBLE);
            descriptionTextView.setText(activity.getDescription());
        }

        signedUpUsersRecyclerView = findViewById(R.id.signed_up_users_recycler_view);


        SignedUpUsersListAdapter adapter = new SignedUpUsersListAdapter(this,activity.getSignedUpUsers());
        signedUpUsersRecyclerView.setAdapter(adapter);
        signedUpUsersRecyclerView.setLayoutManager(new LinearLayoutManager(ActivityDetailsActivity.this));

    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.app_toolbar);
        toolbar.setTitle("Activity details");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}