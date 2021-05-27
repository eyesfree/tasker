package com.developer.krisi.tasker.data;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.developer.krisi.tasker.data.model.ProjectDao;
import com.developer.krisi.tasker.data.model.ProjectDatabase;
import com.developer.krisi.tasker.data.model.TasksProject;
import com.developer.krisi.tasker.web.service.ProjectServiceApi;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class ProjectRepository {

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private TasksProject project;
    private ProjectDao projectDao;
    private ProjectServiceApi projectServiceApi;
    Retrofit retrofit;

    // private constructor : singleton access
    public ProjectRepository() {

        retrofit = new Retrofit.Builder()
                //.baseUrl("http://172.17.9.33:8080")
                .baseUrl("https://task-service.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        projectServiceApi = retrofit.create(ProjectServiceApi.class);
    }

    public boolean isLoggedIn() {
        return project != null;
    }

    private void setSelectedProject(TasksProject selectedProject) {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<TasksProject> selectProject(String projectId) {
        // handle login
        AsyncTask.Status executionStatus = new FindProjectAsyncTask(projectId).execute().getStatus();
        while(executionStatus != AsyncTask.Status.FINISHED) {
            Log.e("ProjectRepository", "task not finished");
        }
        if(project != null) {
            return new Result.Success<TasksProject>(project);
        }

        return new Result.Error(new Exception("project selection failed"));
    }

    public Result<TasksProject> createAndSelectProject(TasksProject project) {
        // handle login
        new CreateProjectAsyncTask(project).execute();
        return new Result.Success<TasksProject>(project);
    }

    private class CreateProjectAsyncTask extends AsyncTask<Context, Void, TasksProject> {

        private String TAG = CreateProjectAsyncTask.class.getSimpleName();
        private Context contx;
        private TasksProject newProject;

        public CreateProjectAsyncTask(TasksProject projectToCreate) {
            this.newProject = projectToCreate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected TasksProject doInBackground(Context... params) {
            Log.e(TAG, "processing http request in async task using retrofit");

            Call<TasksProject> createdProject = projectServiceApi.create(newProject);
            try {
                Response<TasksProject> response = createdProject.execute();
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
                project = result;
            }
        }
    }

    private class FindProjectAsyncTask extends AsyncTask<Context, Void, TasksProject> {

        private String TAG = CreateProjectAsyncTask.class.getSimpleName();
        private Context contx;
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
            Call<TasksProject> createdProject = projectServiceApi.findById(projectId);
            try {
                Response<TasksProject> response = createdProject.execute();
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
                project = result;
            }
        }
    }
}
