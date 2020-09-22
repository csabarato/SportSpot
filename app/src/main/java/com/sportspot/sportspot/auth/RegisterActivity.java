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

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.regEmailEditText);
        passwordEditText = findViewById(R.id.regPasswordEditText);
        signupBtn = findViewById(R.id.signupButton);
        signupBtn.setEnabled(false);

        emailEditText.setOnFocusChangeListener(KeyboardUtils.getKeyboardHiderListener(this));
        passwordEditText.setOnFocusChangeListener(KeyboardUtils.getKeyboardHiderListener(this));

        setupLoginBtnClickability();
    }

    public void navigateToLoginActivity(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }


    private void setupLoginBtnClickability() {

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    signupBtn.setEnabled(false);
                } else if (passwordEditText.getText().toString().length() > 0) {
                    signupBtn.setEnabled(true);
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
                    signupBtn.setEnabled(false);
                } else if (emailEditText.getText().toString().length() > 0) {
                    signupBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}