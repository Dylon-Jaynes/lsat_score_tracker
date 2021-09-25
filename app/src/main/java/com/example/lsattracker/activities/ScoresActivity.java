package com.example.lsattracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lsattracker.R;
import com.example.lsattracker.fragments.DateFrag;
import com.example.lsattracker.view_models.ScoresActivityViewModel;

import java.util.Date;

public class ScoresActivity extends AppCompatActivity {

    private ScoresActivityViewModel scoresActivityViewModel;
    private EditText dateTaken;
    private EditText score;
    private Button addLsatScore;
    private String SCORE = "score";
    private int parsableScore;
    private String validScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        scoresActivityViewModel = new ScoresActivityViewModel(getApplicationContext());

        dateTaken = findViewById(R.id.edittext_date_taken);
        score = findViewById(R.id.edittext_lsat_score);

        score.addTextChangedListener(inputTextWatcher);
        score.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if(scoresActivityViewModel.getSharedPreferenceValue(SCORE).equals("default")){
                        scoresActivityViewModel.setPrefString(SCORE, score.getText().toString());
                        score.setText(score.getText().toString());
                    }
                    if (scoresActivityViewModel.isParsable(score.getText().toString())) {
                        parsableScore = Integer.parseInt(score.getText().toString());
                        validScore = Integer.toString(scoresActivityViewModel.assureValidScoreGoal(parsableScore));
                        scoresActivityViewModel.setPrefString(SCORE, validScore);
                        score.setText(validScore);
                    }
                }
            }
        });

        scoresActivityViewModel.setInputFilters(score);

        dateTaken.addTextChangedListener(inputTextWatcher);
        dateTaken.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showDatePicker();
                }
            }
        });
        dateTaken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });



        addLsatScore = findViewById(R.id.button_add_lsat_score);
        addLsatScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date[] dates = scoresActivityViewModel.parseDates(dateTaken.getText().toString());
                if(!scoresActivityViewModel.isValidDate(dates)){
                    dateTaken.setError("You cannot select a date in the future.");
                }
            }
        });
    }

    private TextWatcher inputTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            addLsatScore.setEnabled(!dateTaken.getText().toString().trim().isEmpty() && !score.getText().toString().trim().isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
            dateTaken.setError(null);
        }
    };

    public void showDatePicker() {
        DialogFragment newFrag = new DateFrag();
        newFrag.show(getSupportFragmentManager(), "datePicker");
    }

    public void processDatePickerResult(int year, int month, int day){
        String monthString = Integer.toString(month + 1);
        String dayString = Integer.toString(day);
        String yearString = Integer.toString(year);

        String dateMessage = (monthString+"/"+dayString+"/"+yearString);
        dateTaken.setText(dateMessage);
    }
}