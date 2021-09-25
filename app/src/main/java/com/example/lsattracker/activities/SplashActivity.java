package com.example.lsattracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lsattracker.R;
import com.example.lsattracker.view_models.SplashActivityViewModel;

public class SplashActivity extends AppCompatActivity {

    private SplashActivityViewModel splashActivityViewModel;
    private String USER_NAME = "userName";
    private EditText userName;
    private Button continueButton;
    private String isFirstRun = "true";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashActivityViewModel = new SplashActivityViewModel(getApplicationContext());

        if(splashActivityViewModel.getSharedPreferenceValue(USER_NAME).equals("default")){
            userName = (EditText) findViewById(R.id.edittext_name);
            continueButton = (Button) findViewById(R.id.button_continue);

            splashActivityViewModel.setFilter(userName);
            userName.addTextChangedListener(nameTextWatcher);

            continueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    splashActivityViewModel.setPrefString(USER_NAME, userName.getText().toString());

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }else{
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private final TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String userNameInput = userName.getText().toString().trim();

            continueButton.setEnabled(!userNameInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}