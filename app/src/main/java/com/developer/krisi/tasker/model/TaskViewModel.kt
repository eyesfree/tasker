package com.developer.krisi.tasker.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class TaskViewModel(application: Application, selectedProjectId: String) : AndroidViewModel(application) {
    var projectId: String = selectedProjectId
    private val repository: TaskRepository = TaskRepository(application, selectedProjectId)
    val allTasks: LiveData<List<Task>>

    fun insert(task: Task?) {
        repository.insertOrUpdate(task)
    }

    fun delete(task: Task?) {
        repository.delete(task)
    }

    fun update(task: Task?) {
        repository.update(task)
    }

    fun refresh() {
        repository.refreshTasks()
    }

    private fun getAllTasksByProject(projectId: String): LiveData<List<Task>> {
        return repository.getAllTasksByProject(projectId)
    }

    init {
        allTasks = getAllTasksByProject(projectId)
    }
}