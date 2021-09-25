package com.example.lsattracker.view_models;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

public class StudyHoursActivityViewModel extends ViewModel {

    private final Context context;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;
    private final String MY_PREFS = "myPrefs";
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String STUDY_GOAL = "studyGoal";
    public static final String PROGRESS = "progress";

    public StudyHoursActivityViewModel(Context context) {
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

    public SharedPreferences getPref(){
        return prefs;
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
