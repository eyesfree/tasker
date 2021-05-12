package com.developer.krisi.tasker.model

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(maxSdk = Build.VERSION_CODES.P)
class TaskViewModelTest {

    @Test
    fun getAllTasks() {
    }

    @Test
    fun insert() {
        var taskViewModel = TaskViewModel(ApplicationProvider.getApplicationContext())
        taskViewModel.insert( Task("test", "describe", Status.NEW, 9))
        // var finalSize = taskViewModel.allTasks.value!!.size;
        // assertEquals(1, finalSize);
    }

    @Test
    fun delete() {
    }

    @Test
    fun update() {
    }
}