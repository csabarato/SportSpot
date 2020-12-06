package com.sportspot.sportspot.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.sportspot.sportspot.R;
import com.sportspot.sportspot.utils.DialogUtils;
import com.sportspot.sportspot.utils.KeyboardUtils;
import com.sportspot.sportspot.utils.TextValidator;

public class RegisterActivity extends AppCompatActivity {

    // Layout element definitions
    private EditText emailEditText, passwordEditText;
    private Button signupBtn;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private ConnectivityManager connectivityManager;

    // LOG tags
    private static final String FIREBASE_LOG = "FIREBASE_LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Get layout elements
        emailEditText = findViewById(R.id.regEmailEditText);
        passwordEditText = findViewById(R.id.regPasswordEditText);
        signupBtn = findViewById(R.id.signupButton);
        signupBtn.setEnabled(false);
        progressBar = findViewById(R.id.regProgressBar);

        // hide keyboard when clicked out from field
        emailEditText.setOnFocusChangeListener(KeyboardUtils.getKeyboardHiderListener(this));
        passwordEditText.setOnFocusChangeListener(KeyboardUtils.getKeyboardHiderListener(this));

        emailEditText.addTextChangedListener(registerInputValidator(emailEditText));
        passwordEditText.addTextChangedListener(registerInputValidator(passwordEditText));

        // Get Firebase instance
        firebaseAuth = FirebaseAuth.getInstance();

        // setup ConnectivityManager
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public void onFirebaseSignup(View view) {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                    task -> {

                        if (task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        } else {
                            Log.d(FIREBASE_LOG, "createUserWithEmail:failure", task.getException());
                            handleFirebaseSignupErrors(task);
                        }
                    }
            );
        } else {
            DialogUtils.createAlertDialog(getString(R.string.no_interner_error_title), getString(R.string.no_interner_error_message), RegisterActivity.this).show();
        }
    }

    public void navigateToLoginActivity(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    private void handleFirebaseSignupErrors(Task<AuthResult> task) {

        try {
            throw task.getException();
        } catch(FirebaseAuthWeakPasswordException e) {
            DialogUtils.createAlertDialog(getString(R.string.signup_error), getString(R.string.password_weak_msg), RegisterActivity.this).show();
        } catch(FirebaseAuthInvalidCredentialsException e) {
            DialogUtils.createAlertDialog(getString(R.string.signup_error), getString(R.string.invalid_email_format_msg), RegisterActivity.this).show();
        } catch(FirebaseAuthUserCollisionException e) {
            DialogUtils.createAlertDialog(getString(R.string.signup_error), getString(R.string.email_collision_msg), RegisterActivity.this).show();
        } catch(Exception e) {
            Log.e(FIREBASE_LOG, e.getMessage());
            DialogUtils.createAlertDialog(getString(R.string.signup_error), e.getMessage(), RegisterActivity.this).show();
        } finally {
            progressBar.setVisibility(View.GONE);
        }
    }


    private TextWatcher registerInputValidator(EditText editText) {

        return new TextValidator(editText) {
            @Override
            public void validate(String text) {
                validateRegisterInputs();
            }
        };
    }

    private void validateRegisterInputs() {

        if (emailEditText.getText() == null || emailEditText.getText().toString().isEmpty()
                || passwordEditText == null || passwordEditText.getText().toString().isEmpty()) {
            signupBtn.setEnabled(false);
        } else {
            signupBtn.setEnabled(true);
        }
    }
}