package com.developer.krisi.tasker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.krisi.tasker.model.Status;
import com.developer.krisi.tasker.ui.main.DatePickerFragment;

import java.util.Calendar;
import java.util.Date;

public class AddEditTaskActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    public static final String NAME = "com.developer.krisi.tasker.NAME";
    public static final String DESCRIPTION = "com.developer.krisi.tasker.DESCRIPTION";
    public static final String PRIORITY = "com.developer.krisi.tasker.PRIORITY";
    public static final String TASK_ID = "com.developer.krisi.tasker.TASK_ID";
    public static final String STATUS = "com.developer.krisi.tasker.STATUS";
    public static final String DUE_DATE = "com.developer.krisi.tasker.DUE_DATE";
    public static final int RESULT_DELETE = 666;
    private EditText newName;
    private EditText newDescription;
    private NumberPicker numberPickerPriority;
    private Spinner statusPicker;
    private String selectedStatus;
    private Date dueDate;
    private DatePickerFragment datePickerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        newName = findViewById(R.id.newTaskText);
        newDescription = findViewById(R.id.newTaskDescription);
        numberPickerPriority = findViewById(R.id.number_picker_priority);
        statusPicker = findViewById(R.id.status_picker);

        numberPickerPriority.setMinValue(0);
        numberPickerPriority.setMaxValue(10);

        final Button button = findViewById(R.id.create_task_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendSaveIntent();
            }
        });

        final Button deleteButton = findViewById(R.id.delete_task_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendDeleteIntent();
            }
        });

        final Button dueDatePicker = findViewById(R.id.due_date_picker);
        dueDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        statusPicker.setAdapter(adapter);
        statusPicker.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        boolean editNote = intent.hasExtra(TASK_ID);
        if (editNote) {
            setTitle("Edit Task");
            newName.setText(intent.getStringExtra(NAME));
            newDescription.setText(intent.getStringExtra(DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(PRIORITY, 0));
            final String stringExtra = intent.getStringExtra(STATUS);
            final int position = adapter.getPosition(stringExtra);
            statusPicker.setSelection(position);
            final long dateTime = intent.getLongExtra(DUE_DATE, new Date().getTime());
            TextView dueDate = findViewById(R.id.due_date_value);
            final Calendar c = Calendar.getInstance();
            c.setTimeInMillis(dateTime);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int displayMonth = month + 1;
            int day = c.get(Calendar.DAY_OF_MONTH);
            dueDate.setText(day + "/" + displayMonth + "/" + year);
        } else {
            setTitle("Add Task");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_task:
                sendSaveIntent();
            default:
                super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendSaveIntent() {
        Intent replyIntent = new Intent();
        if (newName == null || TextUtils.isEmpty(newName.getText())) {
            Toast.makeText(getApplicationContext(), "Please add task name", Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            fillIntentProperties(replyIntent);
            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }

    private void fillIntentProperties(Intent replyIntent) {
        String name = newName.getText().toString();
        replyIntent.putExtra(NAME, name);
        String description = newDescription.getText().toString();
        if(description == null || description.isEmpty()) {
            description = "Example task description here.";
        }
        replyIntent.putExtra(DESCRIPTION, description);
        int priority = numberPickerPriority.getValue();
        replyIntent.putExtra(PRIORITY, priority);
        replyIntent.putExtra(STATUS, selectedStatus);
        if(datePickerFragment != null) {
            this.dueDate = datePickerFragment.getSelectedDate();
            replyIntent.putExtra(DUE_DATE, dueDate.getTime());
        } else {
            this.dueDate = Calendar.getInstance().getTime();
            replyIntent.putExtra(DUE_DATE, dueDate.getTime());
        }

        Intent intent = getIntent();
        String id = intent.getStringExtra(TASK_ID);
        if (id != null) {
            replyIntent.putExtra(TASK_ID, id);
        }
    }

    private void sendDeleteIntent() {
        Intent replyIntent = new Intent();
        fillIntentProperties(replyIntent);
        setResult(RESULT_DELETE, replyIntent);
        finish();
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
        String statusName = parent.getItemAtPosition(position).toString();
        selectedStatus = statusName;
        Toast.makeText(getApplicationContext(), "Setting status to: " + statusName,Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        selectedStatus = Status.NEW.getStatusName();
        Toast.makeText(getApplicationContext(), "Setting default status",Toast.LENGTH_LONG).show();
    }

    public void showDatePickerDialog(View v) {
        datePickerFragment = new DatePickerFragment(Calendar.getInstance().getTime());
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void setDueDate(Date inputDate) {
        this.dueDate = inputDate;
        Toast.makeText(getApplicationContext(), "Setting date to: " + inputDate, Toast.LENGTH_LONG).show();
    }
}