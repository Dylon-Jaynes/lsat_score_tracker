package com.example.lsattracker.view_models;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.InputFilter;
import android.widget.EditText;

import androidx.lifecycle.ViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoresActivityViewModel extends ViewModel {

    private final Context context;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;
    private final String MY_PREFS = "myPrefs";
    private Date currentDate;
    private Date endDate;
    private final int MIN = 120;
    private final int MAX = 180;

    public ScoresActivityViewModel(Context context) {
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

    public Boolean isValidDate(Date[] dates){
        Date currentDate = dates[0];
        Date endDate = dates[1];
        assert endDate != null;
        assert currentDate != null;
        if (currentDate.before(endDate)) {
            return false;
        }else{
            return true;
        }
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

    public int assureValidScoreGoal(int scoreGoal){

        if(scoreGoal < MIN){
            return MIN;
        }else if(scoreGoal > MAX){
            return MAX;
        }

        return scoreGoal;
    }

    public void setInputFilters(EditText lsatScore){
        lsatScore.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
    }
}
