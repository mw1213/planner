package com.agh.planner;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private TextView textView; //if we want tou use date picker in other places, we can change given textViews 

    public void setTextView(TextView textView){
        this.textView = textView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        if (textView!=null)
            textView.setText(prepareString(year,month,day));
    }

    public String prepareString(int year, int month, int day){
        StringBuilder result = new StringBuilder();
        if (year<10) result.append("0");
        if (year<100) result.append("0");
        if (year<1000) result.append("0");
        result.append(year);
        result.append("/");
        if(month<9) result.append("0"); // month is given as 0-11, for example may -> 4
        result.append(month+1);
        result.append("/");
        if (day<10) result.append("0");
        result.append(day);
        return result.toString();
    }
}