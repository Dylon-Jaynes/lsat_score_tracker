package com.example.lsattracker.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lsattracker.fragments.DateFrag;
import com.example.lsattracker.R;
import com.example.lsattracker.view_models.GoalsActivityViewModel;

import java.util.Date;

public class GoalsActivity extends AppCompatActivity {

    private GoalsActivityViewModel goalsActivityViewModel;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    private final String TEST_DATE = "testDate";
    private final String SCORE_GOAL = "scoreGoal";
    private final String STUDY_GOAL = "studyGoal";
    private final String STUDY_INFO = "studyInfo";
    private final String PROGRESS = "progress";
    private EditText lsatScoreGoal;
    private EditText studyHoursGoal;
    private EditText testDate;
    private TextView studyInfoText;
    private Button enterButton;
    private Button resetButton;
    private int scoreGoal;
    private int studyHoursInt;
    private int hoursPerDay;
    private String validScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        goalsActivityViewModel = new GoalsActivityViewModel(getApplicationContext());

        lsatScoreGoal = (EditText) findViewById(R.id.edittext_score_goal);
        studyHoursGoal = (EditText) findViewById(R.id.edittext_study_hours_goal);
        testDate = (EditText) findViewById(R.id.edittext_test_date);
        studyInfoText = findViewById(R.id.textview_required_study_hours_per_day);

        resetButton = (Button) findViewById(R.id.button_reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lsatScoreGoal.getText().clear();
                studyHoursGoal.getText().clear();
                testDate.getText().clear();
                studyInfoText.setVisibility(View.GONE);
                lsatScoreGoal.setEnabled(true);
                studyHoursGoal.setEnabled(true);
                testDate.setEnabled(true);
                goalsActivityViewModel.setPrefString(SCORE_GOAL, "default");
                goalsActivityViewModel.setPrefString(STUDY_GOAL, "default");
                goalsActivityViewModel.setPrefString(TEST_DATE, "default");
                goalsActivityViewModel.setPrefString(PROGRESS, "0");
                setFieldsWithListeners();
            }
        });

        if(!goalsActivityViewModel.getSharedPreferenceValue(SCORE_GOAL).equals("default")
                && !goalsActivityViewModel.getSharedPreferenceValue(STUDY_GOAL).equals("default")
                && !goalsActivityViewModel.getSharedPreferenceValue(TEST_DATE).equals("default")
                && !goalsActivityViewModel.getSharedPreferenceValue(STUDY_INFO).equals("default")){
            setFieldsFromSharedPrefs();
        }else{
            setFieldsWithListeners();
        }
    }

    public void setFieldsFromSharedPrefs(){
        lsatScoreGoal.setText(goalsActivityViewModel.getSharedPreferenceValue(SCORE_GOAL));
        studyHoursGoal.setText(goalsActivityViewModel.getSharedPreferenceValue(STUDY_GOAL));
        testDate.setText(goalsActivityViewModel.getSharedPreferenceValue(TEST_DATE));
        studyInfoText.setText(goalsActivityViewModel.getSharedPreferenceValue(STUDY_INFO));
        studyInfoText.setVisibility(View.VISIBLE);
        lsatScoreGoal.setEnabled(false);
        studyHoursGoal.setEnabled(false);
        testDate.setEnabled(false);
    }

    public void setFieldsWithListeners(){
        lsatScoreGoal.addTextChangedListener(inputTextWatcher);
        lsatScoreGoal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if(goalsActivityViewModel.getSharedPreferenceValue(SCORE_GOAL).equals("default")){
                        goalsActivityViewModel.setPrefString(SCORE_GOAL, lsatScoreGoal.getText().toString());
                        lsatScoreGoal.setText(lsatScoreGoal.getText().toString());
                    }
                    if (goalsActivityViewModel.isParsable(lsatScoreGoal.getText().toString())) {
                        scoreGoal = Integer.parseInt(lsatScoreGoal.getText().toString());
                        validScore = Integer.toString(goalsActivityViewModel.assureValidScoreGoal(scoreGoal));
                        goalsActivityViewModel.setPrefString(SCORE_GOAL, validScore);
                        lsatScoreGoal.setText(validScore);
                    }
                }
            }
        });

        studyHoursGoal.addTextChangedListener(inputTextWatcher);
        studyHoursGoal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(goalsActivityViewModel.getSharedPreferenceValue(STUDY_GOAL).equals("default")){
                        if(!goalsActivityViewModel.assureValidStudyGoal(studyHoursInt)){
                            studyHoursGoal.setError("Please select a study hours goal of 1 hour or more.");
                        }else{
                            goalsActivityViewModel.setPrefString(STUDY_GOAL, studyHoursGoal.getText().toString());
                            studyHoursGoal.setText(studyHoursGoal.getText().toString());
                        }
                    }
                }
            }
        });

        goalsActivityViewModel.setInputFilters(lsatScoreGoal, studyHoursGoal);

        testDate.addTextChangedListener(inputTextWatcher);
        testDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showDatePicker();
                }
            }
        });
        testDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        enterButton = (Button) findViewById(R.id.button_enter);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                goalsActivityViewModel.setPrefString(SCORE_GOAL, lsatScoreGoal.getText().toString());
                goalsActivityViewModel.setPrefString(STUDY_GOAL, studyHoursGoal.getText().toString());
                Date[] dates = goalsActivityViewModel.parseDates(testDate.getText().toString());
                if(goalsActivityViewModel.isParsable(studyHoursGoal.getText().toString())) {
                    studyHoursInt = Integer.parseInt(studyHoursGoal.getText().toString());
                }

                if(!goalsActivityViewModel.isValidDate(dates)){
                    testDate.setError("Please select a date in the future.");
                }else{
                    hoursPerDay = goalsActivityViewModel.hoursRequiredForGoal(dates, studyHoursInt);
                    if(goalsActivityViewModel.isGoalPossible(hoursPerDay)){
                        String studyInfo = "To reach your study goal of " + studyHoursGoal.getText().toString() + " hours by " + testDate.getText().toString() + ", you will need to study for at least " + hoursPerDay + " hours every day, starting today.";
                        goalsActivityViewModel.setPrefString(STUDY_INFO, studyInfo);
                        studyInfoText = findViewById(R.id.textview_required_study_hours_per_day);
                        studyInfoText.setText(studyInfo);
                        studyInfoText.setVisibility(View.VISIBLE);

                        goalsActivityViewModel.setPrefString(TEST_DATE, testDate.getText().toString());
                        testDate.setText(testDate.getText().toString());
                        enterButton.setEnabled(false);
                        lsatScoreGoal.setEnabled(false);
                        studyHoursGoal.setEnabled(false);
                        testDate.setEnabled(false);
                    }else{
                        testDate.setError("Warning: Reaching your study goal of " + studyHoursGoal.getText().toString() + " hours by " + testDate.getText().toString() + " is impossible. Please pick a later test date or change your study goal.");
                    }
                }
            }
        });
    }

    public void showDatePicker() {
        DialogFragment newFrag = new DateFrag();
        newFrag.show(getSupportFragmentManager(), "datePicker");
    }

    public void processDatePickerResult(int year, int month, int day){
        String monthString = Integer.toString(month + 1);
        String dayString = Integer.toString(day);
        String yearString = Integer.toString(year);

        String dateMessage = (monthString+"/"+dayString+"/"+yearString);
        testDate.setText(dateMessage);
    }

    private TextWatcher inputTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String scoreGoalInput = lsatScoreGoal.getText().toString().trim();
            String hourGoalInput = studyHoursGoal.getText().toString().trim();
            String dateInput = testDate.getText().toString().trim();

            enterButton.setEnabled(!scoreGoalInput.isEmpty() && !hourGoalInput.isEmpty() && !dateInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
            studyHoursGoal.setError(null);
            testDate.setError(null);
        }
    };
}