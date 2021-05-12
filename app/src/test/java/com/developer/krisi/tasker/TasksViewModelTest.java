package com.developer.krisi.tasker;


import android.os.Build;

import androidx.lifecycle.LiveData;

import com.developer.krisi.tasker.model.Task;
import com.developer.krisi.tasker.model.TaskViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

// Medium: integration test
@RunWith(MockitoJUnitRunner.class)
@Config(maxSdk = Build.VERSION_CODES.P)
public class TasksViewModelTest {
    @Mock
    TaskViewModel taskModel;

    @Test
    public void addTaskToModel() throws Exception{

        taskModel.insert(new Task());
        LiveData<List<Task>> allTasks = taskModel.getAllTasks();

        if(allTasks != null) {
            int size = allTasks.getValue().size();
            Task task = new Task();
            taskModel.insert(task);
            int newSize = allTasks.getValue().size();

            assertEquals(size + 1, newSize);
        }
    }
}
