package com.developer.krisi.tasker;


import androidx.lifecycle.LiveData;

import com.developer.krisi.tasker.model.Task;
import com.developer.krisi.tasker.model.TaskViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static com.developer.krisi.tasker.LiveDataTestUtil.getOrAwaitValue;

@RunWith(MockitoJUnitRunner.class)
public class TasksViewModelTest {
    @Mock
    TaskViewModel taskModel;

    @Test
    public void addTaskToModel() throws Exception{

        taskModel.insert(new Task());
        LiveData<List<Task>> allTasks = taskModel.getAllTasks();
        getOrAwaitValue(allTasks);

        if(allTasks != null) {
            int size = allTasks.getValue().size();
            Task task = new Task();
            taskModel.insert(task);
            int newSize = allTasks.getValue().size();

            assertEquals(size + 1, newSize);
        }
    }
}
