package com.sportspot.sportspot.menus.activity_map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.sportspot.sportspot.R;
import com.sportspot.sportspot.model.ActivityModel;
import com.sportspot.sportspot.service.tasks.ActivitySignUpTask;
import com.sportspot.sportspot.service.tasks.RemoveActivitySignUpTask;
import com.sportspot.sportspot.shared.AsyncTaskRunner;
import com.sportspot.sportspot.shared.SignedUpUsersListAdapter;
import com.sportspot.sportspot.utils.AuthUtils;
import com.sportspot.sportspot.utils.DateUtils;
import com.sportspot.sportspot.utils.DialogUtils;

public class ActivityDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView sportTypeTextView, startDateTimeTextView, ownerTextView,
    placesTextView, descriptionTextView;

    private MaterialCardView descriptionCardView;
    private RecyclerView signedUpUsersRecyclerView;
    private Button signupButton, removeSignupButton;
    private ActivityModel activity;
    private ProgressDialog signupProgressDialog, removeSignupProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setToolbar();

        activity = (ActivityModel) getIntent().getSerializableExtra("activity");
        ownerTextView = findViewById(R.id.owner_card_text);
        sportTypeTextView = findViewById(R.id.sport_type_card_text);
        placesTextView = findViewById(R.id.places_card_text);
        startDateTimeTextView = findViewById(R.id.start_datetime_card_text);
        descriptionCardView = findViewById(R.id.description_card);
        descriptionTextView = findViewById(R.id.description_card_text);
        signupButton = findViewById(R.id.activity_details_signup_button);
        removeSignupButton = findViewById(R.id.activity_details_remove_signup_button);

        showActivityDetails();
        showSignupButton();
        showRemoveSignupButton();
        signupProgressDialog = createProgressDialog("Signing up to activity");
        removeSignupProgressDialog = createProgressDialog("Remove activity signup");

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

    private void showActivityDetails() {

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

    private void showSignupButton() {
        String activeUserId = AuthUtils.getActiveUserId(getApplicationContext());

        if (!activity.getOwner().get_id().equals(activeUserId) &&
            !activity.isUserSignedUp(activeUserId)) {
            signupButton.setVisibility(View.VISIBLE);
            signupButton.setOnClickListener(this);
        }
    }

    private void showRemoveSignupButton() {
        String activeUserId = AuthUtils.getActiveUserId(getApplicationContext());

        if (!activity.getOwner().get_id().equals(activeUserId) &&
                activity.isUserSignedUp(activeUserId)) {
            removeSignupButton.setVisibility(View.VISIBLE);
            removeSignupButton.setOnClickListener(this);
        }
    }

    private ProgressDialog createProgressDialog(String message) {
            ProgressDialog progressDialog  = new ProgressDialog(ActivityDetailsActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(message);
            return progressDialog;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == signupButton.getId()) {
            signUpToActivity();
        } else if (v.getId() == removeSignupButton.getId()) {
            removeActivitySignup();
        }
    }

    private void signUpToActivity() {

        signupProgressDialog.show();
        AsyncTaskRunner.getInstance()
                .executeAsync(
                        new ActivitySignUpTask(getApplication().getApplicationContext(), activity.get_id()),
                        data -> {
                            signupProgressDialog.dismiss();
                            if (data.getErrors().isEmpty()) {
                                AlertDialog signupInfoDialog = DialogUtils.createAlertDialog("Signup succesful","You've signed up for this activity.",
                                        ActivityDetailsActivity.this);
                                signupInfoDialog.show();

                                ActivityModel updatedActivity = data.getData();
                                Intent refreshIntent = getIntent().putExtra("activity", updatedActivity);

                                new Handler().postDelayed(() -> {
                                    finish();
                                    signupInfoDialog.dismiss();
                                    startActivity(refreshIntent);
                                }, 1500);

                            } else {
                                DialogUtils.createAlertDialog("Signup error",String.join(";", data.getErrors()),
                                        ActivityDetailsActivity.this).show();
                            }
                        });
    }

    private void removeActivitySignup() {

        removeSignupProgressDialog.show();
        AsyncTaskRunner.getInstance()
                .executeAsync(
                        new RemoveActivitySignUpTask(getApplication().getApplicationContext(), activity.get_id()),
                        data -> {
                            removeSignupProgressDialog.dismiss();
                            if (data.getErrors().isEmpty()) {
                                AlertDialog infoDialog = DialogUtils.createAlertDialog("Signup removed succesfully","You've removed your signup from this activity.",
                                        ActivityDetailsActivity.this);
                                infoDialog.show();

                                ActivityModel updatedActivity = data.getData();
                                Intent refreshIntent = getIntent().putExtra("activity", updatedActivity);

                                new Handler().postDelayed(() -> {
                                    finish();
                                    infoDialog.dismiss();
                                    startActivity(refreshIntent);
                                }, 1500);

                            } else {
                                DialogUtils.createAlertDialog("Remove signup error",String.join(";", data.getErrors()),
                                        ActivityDetailsActivity.this).show();
                            }
                        });


    }
}