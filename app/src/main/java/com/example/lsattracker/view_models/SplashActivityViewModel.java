package com.example.lsattracker.view_models;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.InputFilter;

import android.widget.EditText;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SplashActivityViewModel extends ViewModel {

    private final Context context;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;
    private final String MY_PREFS = "myPrefs";


    public SplashActivityViewModel(Context context) {
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

    public void setFilter(EditText userName){
        userName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(70)});
    }


}
