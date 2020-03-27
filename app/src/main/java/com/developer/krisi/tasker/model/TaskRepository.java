package com.developer.krisi.tasker.model;

import android.app.Application;
import android.util.Log;

import com.developer.krisi.tasker.ui.main.TaskServiceApi;

import java.util.List;

import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;
    Retrofit retrofit;
    TaskServiceApi taskServiceApi;
    Call<List<Task>> getAll;

    public TaskRepository(Application application) {
        TaskDatabase db = TaskDatabase.getDatabase(application);
        this.taskDao = db.taskDao();
        this.allTasks = taskDao.getAll();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://172.17.151.241:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        taskServiceApi = retrofit.create(TaskServiceApi.class);
        getAll = taskServiceApi.getTasks();

    }

    LiveData<List<Task>> getAllTasks() {
        boolean needRefresh = true;
        if(needRefresh) {
            refreshTasks();
        }

        return allTasks;
    }

    private void refreshTasks() {
        getAll.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if(response.isSuccessful()) {
                    List<Task> tasks = response.body();
                    for(Task task : tasks) {
                        Log.i("TaskRepository", "inserting task " + task.getName());
                        insert(task);
                    }
                } else {
                    Log.w("TaskRepository", "Response code for getAll() " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Log.e("TaskRepository", "Response exception " + t.getMessage());
            }
        });
    }

    public void insert(final Task task){
        // createRemoteTask(task);


        TaskDatabase.databaseWriteExecutor.execute(()-> {
            this.taskDao.insert(task);
        });
    }

    public void delete(final Task task){
        TaskDatabase.databaseWriteExecutor.execute(()-> {
            this.taskDao.delete(task);
        });
    }

    public void update(final Task task){
        TaskDatabase.databaseWriteExecutor.execute(()-> {
            this.taskDao.update(task);
        });

        updateRemoteTask(task);
    }

    private void updateRemoteTask(Task task) {
        Call<Task> update = taskServiceApi.update(task.getId(), task);
        update.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                Log.i("TaskRepository", "updated task in database" + task.getName());
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Log.e("TaskRepository", "error by updating database" + t.getMessage());
            }
        });
    }

    private Task createRemoteTask(Task task) {
        Call<Task> create = taskServiceApi.create(task);
        final Task createdTask = new Task();
        create.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                Log.i("TaskRepository", "created task in database" + task.getName());
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Log.e("TaskRepository", "error by updating database" + t.getMessage());
            }
        });
        return createdTask;
    }
}
