package com.sportspot.sportspot.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.sportspot.sportspot.R;
import com.sportspot.sportspot.auth.google.GoogleSignInService;
import com.sportspot.sportspot.main_menu.MainMenuActivity;
import com.sportspot.sportspot.utils.DialogUtils;
import com.sportspot.sportspot.utils.KeyboardUtils;

public class LoginActivity extends AppCompatActivity {

    // Layout element definitions
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private ConnectivityManager connectivityManager;

    // LOG tags
    private static final String FIREBASE_LOG = "FIREBASE_LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get layout elements
        emailEditText = findViewById(R.id.loginEmailEditText);
        passwordEditText = findViewById(R.id.loginPasswordEditText);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setEnabled(false);
        progressBar = findViewById(R.id.loginProgressBar);
        SignInButton googleSingInButton = findViewById(R.id.googleLoginButton);

        // hide keyboard when clicked out from field
        emailEditText.setOnFocusChangeListener(KeyboardUtils.getKeyboardHiderListener(this));
        passwordEditText.setOnFocusChangeListener(KeyboardUtils.getKeyboardHiderListener(this));

        disableLoginBtnWhenEmptyInputs();

        GoogleSignInService googleSignInService = new GoogleSignInService(getApplicationContext(), LoginActivity.this);
        googleSingInButton.setOnClickListener(googleSignInService);

        // setup ConnectivityManager
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // get Firebase instance
        firebaseAuth = FirebaseAuth.getInstance();

        // Navigate to Main menu, if User is logged in already
        navigateIfUserLoggedIn();
    }

    public void onLogin(View view) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseLogin();
    }

    private void firebaseLogin() {

        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {

            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
                            } else {
                                Log.d(FIREBASE_LOG, "loginUserWithEmailAndPass:failure", task.getException());
                                handleFirebaseLoginErrors(task);
                            }
                        }
                    }
            );
        } else {
            DialogUtils.createAlertDialog(getString(R.string.no_interner_error_title), getString(R.string.no_interner_error_message), LoginActivity.this).show();
        }
    }

    private void handleFirebaseLoginErrors(Task<AuthResult> task) {

        try {
            throw task.getException();
        } catch (FirebaseAuthInvalidCredentialsException e) {
            DialogUtils.createAlertDialog(getString(R.string.login_error), getString(R.string.login_invalid_credentials_msg), LoginActivity.this).show();
        } catch (FirebaseAuthInvalidUserException e) {
            DialogUtils.createAlertDialog(getString(R.string.login_error), getString(R.string.login_invalid_credentials_msg), LoginActivity.this).show();
        }

        catch (Exception e) {
            DialogUtils.createAlertDialog(getString(R.string.login_error),e.getMessage(), LoginActivity.this).show();
        } finally {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GoogleSignInService.GOOGLE__SIGN_IN_RC) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoolgeSignInResult(task);
        }
    }

    private void handleGoolgeSignInResult(Task<GoogleSignInAccount> task) {

        try{
            GoogleSignInAccount account = task.getResult(ApiException.class);
            startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
        } catch (ApiException e) {
            Log.d("Google error", e.getMessage());
            DialogUtils.createAlertDialog(getString(R.string.google_signin_error), task.getException().getMessage(), LoginActivity.this);
        }
    }

    public void navigateToRegisterActivity(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        finish();
    }

    private void navigateIfUserLoggedIn() {
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
            finish();
        } else if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
            finish();
        }
    }

    private void disableLoginBtnWhenEmptyInputs() {

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    loginButton.setEnabled(false);
                } else if (passwordEditText.getText().toString().length() > 0) {
                    loginButton.setEnabled(true);
                }
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    loginButton.setEnabled(false);
                } else if (emailEditText.getText().toString().length() > 0) {
                    loginButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}