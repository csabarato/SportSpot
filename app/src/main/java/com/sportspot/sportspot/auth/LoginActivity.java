package com.sportspot.sportspot.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sportspot.sportspot.R;
import com.sportspot.sportspot.utils.KeyboardUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.loginEmailEditText);
        passwordEditText = findViewById(R.id.loginPasswordEditText);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setEnabled(false);

        emailEditText.setOnFocusChangeListener(KeyboardUtils.getKeyboardHiderListener(this));
        passwordEditText.setOnFocusChangeListener(KeyboardUtils.getKeyboardHiderListener(this));

        setupLoginBtnClickability();
    }

    public void navigateToRegisterActivity(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        finish();
    }

    private void setupLoginBtnClickability() {

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


    public void onLogin(View view) {

        switch (view.getId()) {
            case R.id.googleSignIn:

                break;

            case R.id.loginButton:

                break;
        }

    }

}