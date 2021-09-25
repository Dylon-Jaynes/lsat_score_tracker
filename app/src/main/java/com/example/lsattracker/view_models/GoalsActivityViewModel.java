package com.example.lsattracker.view_models;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.lsattracker.R;
import com.example.lsattracker.activities.GoalsActivity;
import com.example.lsattracker.fragments.DateFrag;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GoalsActivityViewModel extends ViewModel {

    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;
    private final String MY_PREFS = "myPrefs";
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SCORE_GOAL = "scoreGoal";
    public static final String STUDY_GOAL = "studyGoal";
    public static final String TEST_DATE = "testDate";
    private Date currentDate;
    private Date endDate;
    private final int MIN = 120;
    private final int MAX = 180;

    public GoalsActivityViewModel(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public String getSharedPreferenceValue(String key){
        return prefs.getString(key, "default");
    }

    public void setPrefString(String key, String value){
        editor.putString(key, value);
        editor.commit();
    }

    public void setInputFilters(EditText lsatScoreGoal, EditText studyHoursGoal){
        lsatScoreGoal.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
        studyHoursGoal.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
    }

    public int assureValidScoreGoal(int scoreGoal){

        if(scoreGoal < MIN){
            return MIN;
        }else if(scoreGoal > MAX){
            return MAX;
        }

        return scoreGoal;
    }

    public boolean assureValidStudyGoal(int studyGoal){

        if(studyGoal <= 0){
            return false;
        }else{
            return true;
        }
    }

    public Boolean isValidDate(Date[] dates){
        Date currentDate = dates[0];
        Date endDate = dates[1];
        assert endDate != null;
        assert currentDate != null;
        if (currentDate.after(endDate) || currentDate.equals(endDate)) {
            return false;
        }else{
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int hoursRequiredForGoal(Date[] dates, int studyHoursInt) {
        long diff = endDate.toInstant().toEpochMilli() - currentDate.toInstant().toEpochMilli();
        long daysDifference = diff / (1000 * 60 * 60 * 24);
        int hoursPerDay = (int) Math.ceil((float) studyHoursInt / daysDifference);
        return hoursPerDay;
    }

    public Boolean isGoalPossible(int hoursPerDay){
        if(hoursPerDay > 24) {
            return false;
        }else{
            return true;
        }
    }

    public Date[] parseDates(String testDate){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String today = sdf.format(new Date());
        try {
            currentDate = sdf.parse(today);
            endDate = sdf.parse(testDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date[] dates = {currentDate, endDate};
        return dates;
    }

    public boolean isParsable(String input){
        boolean parsable = true;
        try{
            Integer.parseInt(input);
        }catch(NumberFormatException e){
            parsable = false;
        }
        return parsable;
    }
}
