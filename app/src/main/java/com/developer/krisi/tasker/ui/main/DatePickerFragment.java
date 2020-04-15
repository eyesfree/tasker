package com.developer.krisi.tasker.ui.main;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.developer.krisi.tasker.AddEditTaskActivity;
import com.developer.krisi.tasker.R;

import java.util.Calendar;
import java.util.Date;

import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private Date selectedDate;

    public DatePickerFragment() {}

    public DatePickerFragment(Date useDate) {
        this.selectedDate = useDate;
    }

    public Date getSelectedDate() {
        return selectedDate;
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
        TextView dueDate = getActivity().findViewById(R.id.due_date_value);
        int displayMonth = month + 1;
        dueDate.setText(day + "/" + displayMonth + "/" + year);

        final Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        this.selectedDate = c.getTime();
    }

}