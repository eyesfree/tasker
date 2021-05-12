package com.developer.krisi.tasker.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository = TaskRepository(application)
    val allTasks: LiveData<List<Task>>

    fun insert(task: Task?) {
        repository.insert(task)
    }

    fun delete(task: Task?) {
        repository.delete(task)
    }

    fun update(task: Task?) {
        repository.update(task)
    }

    init {
        allTasks = repository.allTasks
    }
}