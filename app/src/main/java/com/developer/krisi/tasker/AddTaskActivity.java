package com.developer.krisi.tasker;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

public class AddTaskActivity extends AppCompatActivity {

    public static final String NAME = "com.developer.krisi.tasker.NAME";
    public static final String DESCRIPTION = "com.developer.krisi.tasker.DESCRIPTION";
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

        setTitle("Add Task");

        final Button button = findViewById(R.id.create_task_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(newName.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String name = newName.getText().toString();
                    replyIntent.putExtra(NAME, name);
                    String description = newDescription.getText().toString();
                    replyIntent.putExtra(DESCRIPTION, description);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_task_menu, menu);
        return true;
    }
}
