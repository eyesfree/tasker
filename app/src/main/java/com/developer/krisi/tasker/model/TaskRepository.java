package com.developer.krisi.tasker.model;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public TaskRepository(Application application) {
        TaskDatabase db = TaskDatabase.getDatabase(application);
        this.taskDao = db.taskDao();
        this.allTasks = taskDao.getAll();
    }

    LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert(final Task task){
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
    }
}
