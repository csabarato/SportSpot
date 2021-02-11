package com.sportspot.sportspot.auth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.sportspot.sportspot.R;
import com.sportspot.sportspot.auth.firebase.FirebaseSignInService;
import com.sportspot.sportspot.auth.google.GoogleSignInService;
import com.sportspot.sportspot.converter.UserDataConverter;
import com.sportspot.sportspot.main_menu.MainMenuActivity;
import com.sportspot.sportspot.model.ResponseModel;
import com.sportspot.sportspot.service.tasks.SyncUserDataTask;
import com.sportspot.sportspot.shared.AsyncTaskRunner;
import com.sportspot.sportspot.utils.DialogUtils;
import com.sportspot.sportspot.utils.KeyboardUtils;
import com.sportspot.sportspot.utils.TextValidator;

public class LoginActivity extends AppCompatActivity {

    // Layout element definitions
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private GoogleSignInService googleSignInService;
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

        emailEditText.addTextChangedListener(loginInputValidator(emailEditText));
        passwordEditText.addTextChangedListener(loginInputValidator(passwordEditText));

        googleSignInService = new GoogleSignInService(getApplicationContext(), LoginActivity.this);
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
                    signInTask -> {
                        if (signInTask.isSuccessful()) {

                            FirebaseSignInService.getCurrentUserIdToken(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseSignInService.saveAccessToken(this, task.getResult().getToken());
                                    syncUserData(firebaseAuth.getCurrentUser(),
                                            (data) -> {
                                            if (data.getErrors().isEmpty()) {
                                                startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
                                            }
                                            else {
                                                DialogUtils.createAlertDialog(
                                                        getString(R.string.user_data_sync_error_title) , String.join(";",data.getErrors()), LoginActivity.this).show();
                                            }
                                            });
                                }
                            });

                        } else {
                            Log.d(FIREBASE_LOG, "loginUserWithEmailAndPass:failure", signInTask.getException());
                            handleFirebaseLoginErrors(signInTask);
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

        if (requestCode == GoogleSignInService.GOOGLE_SIGN_IN_RC) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoolgeSignInResult(task);
        }
    }

    private void doGoogleSilentSignIn(GoogleSignInService googleSignInService) {
        Task<GoogleSignInAccount> task = googleSignInService.getGoogleSignInClient(getApplicationContext()).silentSignIn();

        progressBar.setVisibility(View.VISIBLE);
        // If there's immediate result already
        if (task.isSuccessful()) {

            syncUserData(task.getResult(),
                    data -> {
                        progressBar.setVisibility(View.GONE);
                        if (data.getErrors().isEmpty()) {
                            startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
                        } else {
                            DialogUtils.createAlertDialog(
                                    getString(R.string.user_data_sync_error_title) , String.join(";",data.getErrors()), LoginActivity.this).show();
                        }
                    });
        } else {
            task.addOnCompleteListener(signInTask -> {

                try {
                    signInTask.getResult(ApiException.class);
                    syncUserData(signInTask.getResult(),
                            data -> {
                                progressBar.setVisibility(View.GONE);
                                if (data.getErrors().isEmpty()) {
                                    startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
                                } else {
                                    DialogUtils.createAlertDialog(
                                            getString(R.string.user_data_sync_error_title) , String.join(";",data.getErrors()), LoginActivity.this).show();
                                }
                            });

                } catch (ApiException apiException) {
                    progressBar.setVisibility(View.GONE);
                    DialogUtils.createAlertDialog("Google Sign In error", apiException.getMessage(), LoginActivity.this);
                }
            });
        }
    }

    private void handleGoolgeSignInResult(Task<GoogleSignInAccount> task) {

        progressBar.setVisibility(View.VISIBLE);
        try{
            GoogleSignInAccount account = task.getResult(ApiException.class);
            // Sync User data with Database
            syncUserData(account,
                    data -> {
                        progressBar.setVisibility(View.GONE);
                        if (data.getErrors().isEmpty()) {
                            startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
                        } else {
                            DialogUtils.createAlertDialog(
                                    getString(R.string.user_data_sync_error_title) , String.join(";",data.getErrors()), LoginActivity.this).show();
                        }
                    });

        } catch (ApiException e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
            Log.d("Google error", e.getMessage());
            DialogUtils.createAlertDialog(getString(R.string.google_signin_error), task.getException().getMessage(), LoginActivity.this).show();
        }
    }

    private void syncUserData(GoogleSignInAccount account, AsyncTaskRunner.Callback<ResponseModel<Void>> callback) {
        // Sync User data with Database
        AsyncTaskRunner.getInstance().executeAsync(

                new SyncUserDataTask(
                        UserDataConverter.convertAccountToUserDataModel(account), getApplicationContext()), callback);
    }

    private void syncUserData(FirebaseUser user, AsyncTaskRunner.Callback<ResponseModel<Void>> callback) {
        AsyncTaskRunner.getInstance().executeAsync(
                new SyncUserDataTask(
                        UserDataConverter.convertAccountToUserDataModel(user),getApplicationContext()), callback);
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
            doGoogleSilentSignIn(this.googleSignInService);
        }
    }

    private TextWatcher loginInputValidator(EditText editText) {

        return new TextValidator(editText) {
            @Override
            public void validate(String text) {
                validateLoginInputs();
            }
        };
    }

    private void validateLoginInputs() {

        if (emailEditText.getText() == null || emailEditText.getText().toString().isEmpty()
                || passwordEditText == null || passwordEditText.getText().toString().isEmpty()) {
            loginButton.setEnabled(false);
        } else {
            loginButton.setEnabled(true);
        }
    }
}