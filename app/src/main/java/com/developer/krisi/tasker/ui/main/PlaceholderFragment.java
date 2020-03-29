package com.developer.krisi.tasker.ui.main;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.krisi.tasker.AddEditTaskActivity;
import com.developer.krisi.tasker.MainActivity;
import com.developer.krisi.tasker.R;
import com.developer.krisi.tasker.effects.CustomTouchCallback;
import com.developer.krisi.tasker.model.Status;
import com.developer.krisi.tasker.model.Task;
import com.developer.krisi.tasker.model.TaskListAdapter;
import com.developer.krisi.tasker.model.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String TAG = "TabFragment";
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final int NEW_TASK_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_TASK_ACTIVITY_REQUEST_CODE = 2;

    protected TaskListAdapter taskListAdapter;
    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>(){
            @Override
            public void onChanged(@Nullable final List<Task> tasks) {
                taskListAdapter.setTasks(tasks);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_TASK_ACTIVITY_REQUEST_CODE&& resultCode == Activity.RESULT_OK) {
            Task task = new Task(
                    data.getStringExtra(AddEditTaskActivity.NAME),
                    data.getStringExtra(AddEditTaskActivity.DESCRIPTION),
                    Status.NEW,
                    data.getIntExtra(AddEditTaskActivity.PRIORITY, 0));

            taskViewModel.insert(task);
            Toast.makeText(getContext(), "Task added", Toast.LENGTH_LONG).show();
        } else if (requestCode == EDIT_TASK_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Task task = new Task(
                    data.getStringExtra(AddEditTaskActivity.NAME),
                    data.getStringExtra(AddEditTaskActivity.DESCRIPTION),
                    Status.NEW,
                    data.getIntExtra(AddEditTaskActivity.PRIORITY, 0));
            task.setId(data.getStringExtra(AddEditTaskActivity.TASK_ID));

            taskViewModel.update(task);
            Toast.makeText(getContext(), "Task updated", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(
                    getContext(),
                    R.string.nothing_edited,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.i(TAG, "Creating view for PlaceholderFragment");
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.tasksRecyclerView);
        taskListAdapter = new TaskListAdapter(root.getContext());
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(taskListAdapter);

        ItemTouchHelper.SimpleCallback callback = new CustomTouchCallback(taskViewModel, taskListAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        taskListAdapter.setOnItemClickListener(new TaskListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
                intent.putExtra(AddEditTaskActivity.NAME, task.getName());
                intent.putExtra(AddEditTaskActivity.DESCRIPTION, task.getDescription());
                intent.putExtra(AddEditTaskActivity.PRIORITY, task.getPriority());
                intent.putExtra(AddEditTaskActivity.TASK_ID, task.getId());
                startActivityForResult(intent, EDIT_TASK_ACTIVITY_REQUEST_CODE);
            }
        });

        FloatingActionButton fab = root.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
                startActivityForResult(intent, NEW_TASK_ACTIVITY_REQUEST_CODE);
            }
        });

        return root;
    }
}