package com.developer.krisi.tasker.ui.login;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.developer.krisi.tasker.data.model.TasksProject;
import com.developer.krisi.tasker.R;
import com.developer.krisi.tasker.web.service.ProjectServiceApi;

import java.io.IOException;

public class ProjectViewModel extends AndroidViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    private ProjectServiceApi projectServiceApi;
    Retrofit retrofit;

    public ProjectViewModel(@NonNull Application application) {
        super(application);


        retrofit = new Retrofit.Builder()
                //.baseUrl("http://172.28.192.1:8080")
                .baseUrl("https://task-service.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        projectServiceApi = retrofit.create(ProjectServiceApi.class);
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }
/**
    public void selectProject(String projectId) {
        new FindProjectAsyncTask(projectId).execute();
    } */

    public void createAndSelectProject(TasksProject project) {
        new FindOrCreate(project).execute();
    }

    public void loginDataChanged(String projectId, String newProjectName) {
        if(isProjectIdValid(projectId) || isProjectNameValid(newProjectName)) {
            //if one is correct set both statuses without setting errors
            loginFormState.setValue(new LoginFormState(isProjectIdValid(projectId), isProjectNameValid(newProjectName)));
            return;
        } if (!isProjectIdValid(projectId)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isProjectNameValid(newProjectName)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        }
    }

    public void loginDataChanged(String newProjectName) {
        if(isProjectNameValid(newProjectName)) {
            //if one is correct set both statuses without setting errors
            loginFormState.setValue(new LoginFormState(true, isProjectNameValid(newProjectName)));
            return;
        } else if (!isProjectNameValid(newProjectName)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        }
    }

    // A placeholder username validation check
    private boolean isProjectIdValid(String projectId) {
        if (projectId == null || (projectId!= null && projectId.length() != 24)) {
            return false;
        }

        return true;
    }

    // Anything not empty is a valid name for now
    private boolean isProjectNameValid(String newName) {
        if (newName == null || newName.isEmpty()) {
            return false;
        }

        return true;
    }

    private class FindOrCreate extends AsyncTask<Context, Void, TasksProject> {

        private String TAG = ProjectViewModel.FindOrCreate.class.getSimpleName();
        private TasksProject newProject;

        public FindOrCreate(TasksProject projectToCreate) {
            this.newProject = projectToCreate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected TasksProject doInBackground(Context... params) {
            Log.e(TAG, "processing http request in async task using retrofit");

            //Step one - find if project already exists
            Call<TasksProject> existingProject = projectServiceApi.findByName(newProject.getName());
            try {
                Response<TasksProject> response = existingProject.execute();
                return response.body();
            } catch (IOException e) {
                Log.e(TAG, "error in getting response from service using retrofit.");
            }

            //Step two - create if not found
            Call<TasksProject> createdProject = projectServiceApi.create(newProject);
            try {
                Response<TasksProject> response = createdProject.execute();
                return response.body();
            } catch (IOException e) {
                Log.e(TAG, "error in getting response from service using retrofit. Returning sample empty project.");
            }

            return null;
        }

        @Override
        protected void onPostExecute(TasksProject result) {
            super.onPostExecute(result);

            if (result != null) {
                Log.e(TAG, "use the created project");
                loginResult.setValue(new LoginResult(new LoggedInUserView(result.getId(), result.getName())));
            } else {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        }
    }
/**
    private class FindProjectAsyncTask extends AsyncTask<Context, Void, TasksProject> {

        private String TAG = ProjectViewModel.FindOrCreate.class.getSimpleName();
        private String projectId;

        public FindProjectAsyncTask(String projectToFind) {
            this.projectId = projectToFind;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected TasksProject doInBackground(Context... params) {
            Log.e(TAG, "processing http request in async task using retrofit");
            if(projectId == null) {
                return null;
            }
            Call<TasksProject> foundProject = projectServiceApi.findById(projectId);
            try {
                Response<TasksProject> response = foundProject.execute();
                return response.body();
            } catch (IOException e) {
                Log.e(TAG, "error in getting response from service using retrofit");
            }
            return null;
        }

        @Override
        protected void onPostExecute(TasksProject result) {
            super.onPostExecute(result);

            if (result != null) {
                Log.e(TAG, "use the received project");
                loginResult.setValue(new LoginResult(new LoggedInUserView(result.getId(), result.getName())));
            } else {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        }
    }
        **/
}
