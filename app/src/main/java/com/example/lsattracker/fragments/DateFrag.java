package com.example.lsattracker.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.widget.DatePicker;

import com.example.lsattracker.activities.GoalsActivity;
import com.example.lsattracker.activities.ScoresActivity;
import com.example.lsattracker.view_models.GoalsActivityViewModel;
import com.example.lsattracker.view_models.ScoresActivityViewModel;

import java.util.Calendar;

public class DateFrag extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int month, int dayOfMonth, int year) {
        Activity activity = getActivity();
        if(activity instanceof GoalsActivity){
            ((GoalsActivity) activity).processDatePickerResult(month, dayOfMonth, year);
        }else if(activity instanceof ScoresActivity){
            ((ScoresActivity) activity).processDatePickerResult(month, dayOfMonth, year);
        }
    }
}