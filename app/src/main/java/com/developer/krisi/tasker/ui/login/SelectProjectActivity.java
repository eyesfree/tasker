package com.developer.krisi.tasker.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.krisi.tasker.MainActivity;
import com.developer.krisi.tasker.R;
import com.developer.krisi.tasker.data.model.TasksProject;

public class SelectProjectActivity extends AppCompatActivity {

    private ProjectViewModel projectViewModel;
    public static final String PROJECT_ID = "com.developer.krisi.tasker.PROJECT_ID";
    public static final String PROJECT_NAME = "com.developer.krisi.tasker.PROJECT_NAME";
    public static final String PROJECT_ID_PREFERENCE = "com.developer.krisi.tasker.SAVED_PROJECT_ID";
    public static final String PROJECT_NAME_PREFERENCE = "com.developer.krisi.tasker.SAVED_PROJECT_NAME";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String selectedProject = sharedPreferences.getString(PROJECT_ID_PREFERENCE, "");
        String projectName = sharedPreferences.getString(PROJECT_NAME_PREFERENCE, "");
        if(selectedProject.isEmpty()){
            setContentView(R.layout.activity_select_project);
            projectViewModel = ViewModelProviders.of(this).get(ProjectViewModel.class);

            //final EditText projectIdText = findViewById(R.id.projectId);
            final EditText newProjectNameText = findViewById(R.id.newProjectName);
            //final Button selectExistingProjectButton = findViewById(R.id.login);
            final Button createNewButton = findViewById(R.id.create_new_project);
            final ProgressBar loadingProgressBar = findViewById(R.id.loading);

            projectViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
                @Override
                public void onChanged(@Nullable LoginFormState loginFormState) {
                    if (loginFormState == null) {
                        return;
                    }
                    //selectExistingProjectButton.setEnabled(loginFormState.isProjectIdValid());
                    createNewButton.setEnabled(loginFormState.isNewProjectNameValid());
                    //if (loginFormState.getExistingProjectError() != null) {
                     //   projectIdText.setError(getString(loginFormState.getExistingProjectError()));
                    //}
                    if (loginFormState.getNewProjectNameError() != null) {
                        newProjectNameText.setError(getString(loginFormState.getNewProjectNameError()));
                    }
                }
            });

            projectViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
                @Override
                public void onChanged(@Nullable LoginResult loginResult) {
                    if (loginResult == null) {
                        return;
                    }
                    loadingProgressBar.setVisibility(View.GONE);
                    if (loginResult.getError() != null) {
                        showLoginFailed(loginResult.getError());
                    }
                    if (loginResult.getSuccess() != null) {
                        updateUiWithUser(loginResult.getSuccess(), true);
                    }
                    setResult(Activity.RESULT_OK);

                    //Complete and destroy login activity once successful
                    finish();
                }
            });

            TextWatcher afterTextChangedListener = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // ignore
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // ignore
                }

                @Override
                public void afterTextChanged(Editable s) {
                    projectViewModel.loginDataChanged(newProjectNameText.getText().toString());
                }
            };
            //projectIdText.addTextChangedListener(afterTextChangedListener);
            newProjectNameText.addTextChangedListener(afterTextChangedListener);
            newProjectNameText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        projectViewModel.createAndSelectProject(new TasksProject(newProjectNameText.getText().toString()));
                    }
                    return false;
                }
            });
/**
            selectExistingProjectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    projectViewModel.selectProject(projectIdText.getText().toString());
                }
            });**/

            createNewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    projectViewModel.createAndSelectProject(new TasksProject(newProjectNameText.getText().toString()));
                }
            });
        } else {
            LoggedInUserView user = new LoggedInUserView(selectedProject, projectName);
            updateUiWithUser(user, false);
        }
    }

    private void updateUiWithUser(LoggedInUserView model, boolean saveProject) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // show project after successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        String projectId = model.getProjectId();
        intent.putExtra(PROJECT_ID, projectId);
        intent.putExtra(PROJECT_NAME, model.getDisplayName());
        if(saveProject) {
            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PROJECT_ID_PREFERENCE, projectId);
            editor.putString(PROJECT_NAME_PREFERENCE, model.getDisplayName());
            editor.commit();
        }
        startActivity(intent);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
