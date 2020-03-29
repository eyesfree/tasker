package com.developer.krisi.tasker;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditTaskActivity extends AppCompatActivity {

    public static final String NAME = "com.developer.krisi.tasker.NAME";
    public static final String DESCRIPTION = "com.developer.krisi.tasker.DESCRIPTION";
    public static final String PRIORITY = "com.developer.krisi.tasker.PRIORITY";
    public static final String TASK_ID = "com.developer.krisi.tasker.TASK_ID";
    private EditText newName;
    private EditText newDescription;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        newName = findViewById(R.id.newTaskText);
        newDescription = findViewById(R.id.newTaskDescription);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        numberPickerPriority.setMinValue(0);
        numberPickerPriority.setMaxValue(10);

        Intent intent = getIntent();
        boolean editNote = intent.hasExtra(TASK_ID);
        if (editNote) {
            setTitle("Edit Task");
            newName.setText(intent.getStringExtra(NAME));
            newDescription.setText(intent.getStringExtra(DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(PRIORITY, 0));
        } else {
            setTitle("Add Task");
        }

        final Button button = findViewById(R.id.create_task_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendResult();
            }
        });
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
                sendResult();
            default:
                super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendResult() {
        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(newName.getText())) {
            Toast.makeText(getApplicationContext(), "Please add task name", Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            String name = newName.getText().toString();
            replyIntent.putExtra(NAME, name);
            String description = newDescription.getText().toString();
            replyIntent.putExtra(DESCRIPTION, description);
            int priority = numberPickerPriority.getValue();
            replyIntent.putExtra(PRIORITY, priority);

            Intent intent = getIntent();
            String id = intent.getStringExtra(TASK_ID);
            if(id != null) {
                replyIntent.putExtra(TASK_ID, id);
            }
            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }
}
