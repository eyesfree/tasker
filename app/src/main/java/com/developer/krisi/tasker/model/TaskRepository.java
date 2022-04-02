package com.developer.krisi.tasker.model;

import android.app.Application;
import android.util.Log;
import com.developer.krisi.tasker.web.service.TaskServiceApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;
    Retrofit retrofit;
    TaskServiceApi taskServiceApi;


    public TaskRepository(Application application, String projectId) {
        TaskDatabase db = TaskDatabase.getDatabase(application);
        this.taskDao = db.taskDao();
        this.allTasks = taskDao.getAll(projectId);

        if(this.allTasks != null) {
            if(this.allTasks.getValue() != null) {
                int numberOfTasks = this.allTasks.getValue().size();
                Log.i("TaskRepository", "number of tasks found in local database " + numberOfTasks);
            }
        }

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();

        retrofit = new Retrofit.Builder()
                //.baseUrl("http://172.17.9.33:8080")
                .baseUrl("https://task-service.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        taskServiceApi = retrofit.create(TaskServiceApi.class);
    }

    public LiveData<List<Task>> getAllTasks() {
        boolean needRefresh = true;
        if (needRefresh) {
            refreshTasks();
        }

        return allTasks;
    }

    public LiveData<List<Task>> getAllTasksByProject(String projectId) {
        findTasksByProject(projectId);
        return allTasks;
    }

    public void insertOrUpdate(Task task) {
        if (task.getId() != null) { // this means it comes from the REST Repo
            TaskDatabase.databaseWriteExecutor.execute(() -> {
                Task tryGetTask = this.taskDao.getById(task.getId());
                if (tryGetTask == null) {
                    Log.i("TaskRepository", "inserting task " + task.getName());
                    if(task.projectId == null) {
                        Log.e("TaskRepository", "task has no project! " + task.getName());
                        return;
                    }
                    this.taskDao.insert(task);
                } else {
                    Log.i("TaskRepository", "updating task " + task.getName() + " with status " + task.getStatus() + " with project " + task.getProjectId());
                    this.taskDao.update(task);
                }
            });
        } else {
            createRemoteTask(task);
        }
    }

    public void insertAfterCreateRemote(Task createdTask) {
        String id = createdTask == null ? null : createdTask.getId();
        if (id == null) {
            Log.i("TaskRepository", "Task does not have id, creating one from timestamp.");
            id = Calendar.getInstance().toString();
            createdTask.setId(id);
        }
        insertOrUpdate(createdTask); // instead of writing a separate method, reuse the one above
    }

    public void delete(final Task task) {
        TaskDatabase.databaseWriteExecutor.execute(() -> {
            this.taskDao.delete(task);
        });

        deleteRemoteTask(task);
    }

    public void update(final Task task) {
        TaskDatabase.databaseWriteExecutor.execute(() -> {
            this.taskDao.update(task);
        });

        updateRemoteTask(task);
    }

    private void updateRemoteTask(Task task) {
        Call<Task> update = taskServiceApi.update(task.getId(), task);
        update.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                Log.i("TaskRepository", "updated task in database " + task.getName());
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Log.e("TaskRepository", "error by updating database " + t.getMessage());
            }
        });
    }

    private void createRemoteTask(Task task) {
        Call<Task> create = taskServiceApi.create(task);
        create.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful()) {
                    Task createdTask = response.body();
                    Log.i("TaskRepository", "created task in database " + createdTask.getId());
                    insertAfterCreateRemote(createdTask);
                } else {
                    try {
                        Converter<ResponseBody, ErrorModel> converter = retrofit.responseBodyConverter(ErrorModel.class, new Annotation[0]);
                        ErrorModel errorModel =  converter.convert(response.errorBody());
                        Log.w("TaskRepository", errorModel.toString());

                        Log.w("TaskRepository", "response code for create " + response.code());
                        insertAfterCreateRemote(task);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Log.e("TaskRepository", "error by updating database" + t.getMessage());

                insertAfterCreateRemote(task);
            }
        });
    }

    private void deleteRemoteTask(Task task) {
        Call<ResponseBody> delete = taskServiceApi.delete(task.getId());
        delete.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.i("TaskRepository", "deleted task from database" + task.getId());
                } else {
                    Log.w("TaskRepository", "response code for delete " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("TaskRepository", "error by updating database" + t.getMessage());
            }
        });
    }

    public void refreshTasks() {
        Log.d("TaskRepository", "Calling REST Api for getAll() ");
        Call<List<Task>> getAll = taskServiceApi.getTasks();

        getAll.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful()) {
                    List<Task> tasks = response.body();
                    for (Task task : tasks) {
                        insertOrUpdate(task);
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

    public void findTasksByProject(String projectId) {
        Log.d("TaskRepository", "Calling REST Api for findByProjectId() with " + projectId);
        if(projectId.isEmpty()) {
            Log.i("TaskRepository", "no projectId set yet - returning ");
            return;
        }
        Call<List<Task>> allByProject = taskServiceApi.findByProjectId(projectId);

        allByProject.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful()) {
                    List<Task> tasks = response.body();
                    for (Task task : tasks) {
                        Log.i("TaskRepository", "updating with task " + task.getName());
                        insertOrUpdate(task);
                    }
                } else {
                    Log.w("TaskRepository", "Response code for findByProjectId() " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Log.e("TaskRepository", "Response exception findByProjectId " + t.getMessage());
            }
        });
    }

}
