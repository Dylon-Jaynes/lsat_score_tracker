package com.example.lsattracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lsattracker.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goalsButton = findViewById(R.id.button_start_goals_activity);
        goalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startGoalsActivity = new Intent(MainActivity.this, GoalsActivity.class);
                startActivity(startGoalsActivity);
            }
        });

        Button ScoresButton = findViewById(R.id.button_start_lsat_scores_activity);
        ScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startScoresActivity = new Intent(MainActivity.this, ScoresActivity.class);
                startActivity(startScoresActivity);
            }
        });

        Button studyHoursButton = findViewById(R.id.button_start_hours_studied_activity);
        studyHoursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startStudyHoursActivity = new Intent(MainActivity.this, StudyHoursActivity.class);
                startActivity(startStudyHoursActivity);
            }
        });

    }
}
