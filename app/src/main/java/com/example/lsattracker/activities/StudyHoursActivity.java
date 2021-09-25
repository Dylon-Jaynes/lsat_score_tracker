package com.example.lsattracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lsattracker.R;
import com.example.lsattracker.view_models.GoalsActivityViewModel;
import com.example.lsattracker.view_models.StudyHoursActivityViewModel;

public class StudyHoursActivity extends AppCompatActivity {

    private StudyHoursActivityViewModel studyHoursActivityViewModel;
    public SharedPreferences.OnSharedPreferenceChangeListener listener;
    private String progressString;
    private final String STUDY_GOAL = "studyGoal";
    private final String PROGRESS = "progress";
    private int prog;
    private ProgressBar progBar;
    private TextView progText;
    private TextView studyGoalText;
    private TextView usersStudyGoal;
    private EditText hours;
    private int hoursInt;
    private int studyGoalInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_hours);

        studyHoursActivityViewModel = new StudyHoursActivityViewModel(getApplicationContext());

        String studyGoal = studyHoursActivityViewModel.getSharedPreferenceValue(STUDY_GOAL);
        String progress = studyHoursActivityViewModel.getSharedPreferenceValue(PROGRESS);
        progBar = findViewById(R.id.progressbar_score);
        progText = findViewById(R.id.textview_progress);
        if(studyHoursActivityViewModel.isParsable(progress)){
            progBar.setProgress(Integer.parseInt(progress));
            progText.setText(progress);
            prog = Integer.parseInt(progress);
        }
        studyGoalText = findViewById(R.id.textview_user_study_goal_description);
        usersStudyGoal = findViewById(R.id.textview_user_study_goal);
        if(studyGoal.equals("default")){
            studyHoursActivityViewModel.setPrefString(STUDY_GOAL, "default");
            studyGoalText.setText(R.string.study_goal_set_text);
            prog = 0;
        }else{
            studyGoalText.setText(R.string.study_goal_set_text_2);
            usersStudyGoal.setText(studyGoal);
            usersStudyGoal.setVisibility(View.VISIBLE);
        }
        if(progress.equals("default")){
            progBar.setProgress(0);
            progText.setText("0");
        }else if(!progText.getText().toString().equals(progress)){
            progBar.setProgress(0);
            progText.setText("0");
        }

        if(studyHoursActivityViewModel.isParsable(studyGoal)){
            studyGoalInt = Integer.parseInt(studyGoal);
            //Set the max of the progress bar to users study goal.
            progBar.setMax(studyGoalInt);
        }

        updateProgressBar();

        Button addHours = findViewById(R.id.button_add_study_hours);
        Button removeHours = findViewById(R.id.button_subtract_study_hours);

        addHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hours = (EditText) findViewById(R.id.edittext_hours_studied);
                if(studyHoursActivityViewModel.isParsable(hours.getText().toString())) {
                    hoursInt = Integer.parseInt(hours.getText().toString());
                    if(prog <= studyGoalInt){
                        if((prog + hoursInt) < studyGoalInt) {
                            prog += hoursInt;
                        } else{
                            prog = studyGoalInt;
                        }

                        updateProgressBar();
                        saveProgress();
                    }
                }
            }
        });

        removeHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hours = (EditText) findViewById(R.id.edittext_hours_studied);
                if(studyHoursActivityViewModel.isParsable(hours.getText().toString())) {
                    hoursInt = Integer.parseInt(hours.getText().toString());
                    if (prog > 0){
                        if((prog - hoursInt) > 0){
                            prog -= hoursInt;
                        }else{
                            prog = 0;
                        }

                        updateProgressBar();
                        saveProgress();
                    }
                }
            }
        });
    }

    public void updateProgressBar() {
        progBar.setProgress(prog);
        progText.setText(Integer.toString(prog));

    }

    public void saveProgress() {
        studyHoursActivityViewModel.setPrefString(PROGRESS, Integer.toString(prog));
    }
}