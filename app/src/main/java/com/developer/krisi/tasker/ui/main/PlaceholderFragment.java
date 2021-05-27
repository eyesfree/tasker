package com.developer.krisi.tasker.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.developer.krisi.tasker.AddEditTaskActivity;
import com.developer.krisi.tasker.R;
import com.developer.krisi.tasker.effects.CustomTouchCallback;
import com.developer.krisi.tasker.model.DateConverter;
import com.developer.krisi.tasker.model.Status;
import com.developer.krisi.tasker.model.Task;
import com.developer.krisi.tasker.model.TaskListAdapter;
import com.developer.krisi.tasker.model.TaskViewModel;
import com.developer.krisi.tasker.model.TaskViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String TAG = "TabFragment";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String SELECTED_PROJECT_ID = "selected_project";
    public static final int NEW_TASK_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_TASK_ACTIVITY_REQUEST_CODE = 2;

    protected TaskListAdapter taskListAdapter;
    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeContainer;

    public static PlaceholderFragment newInstance(int index, String projectId) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        bundle.putString(SELECTED_PROJECT_ID, projectId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String selectedProjectId = this.getArguments().getString(SELECTED_PROJECT_ID);
        taskViewModel = ViewModelProviders.of(this, new TaskViewModelFactory(getActivity().getApplication(), selectedProjectId)).get(TaskViewModel.class);
        taskViewModel.setProjectId(selectedProjectId);
        getAllTasks();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getAllTasks() {

        String selectedProjectId = this.getArguments().getString(SELECTED_PROJECT_ID);
        taskViewModel.getAllTasks().observe(this, tasks -> {
            int sectionNumber = this.getArguments().getInt(ARG_SECTION_NUMBER);

            if(sectionNumber == 1) {
                List<Status> desiredStatusesToDoTab = new ArrayList<>();
                desiredStatusesToDoTab.add(Status.NEW);
                desiredStatusesToDoTab.add(Status.IN_PROGRESS);
                taskListAdapter.setTasks(tasks, desiredStatusesToDoTab);
                swipeContainer.setRefreshing(false);
            } else if(sectionNumber == 2) {
                List<Status> desiredStatusesDoneTab = new ArrayList<>();
                desiredStatusesDoneTab.add(Status.DONE);
                taskListAdapter.setTasks(tasks, desiredStatusesDoneTab);
                swipeContainer.setRefreshing(false);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            final long dueDateTimestamp = data.getLongExtra(AddEditTaskActivity.DUE_DATE, 0);
            Date dueDate = DateConverter.fromTimestamp(dueDateTimestamp);
            String projectId = this.getArguments().getString(SELECTED_PROJECT_ID);

            Task task = new Task(
                    data.getStringExtra(AddEditTaskActivity.NAME),
                    data.getStringExtra(AddEditTaskActivity.DESCRIPTION),
                    Status.valueOf(data.getStringExtra(AddEditTaskActivity.STATUS)),
                    data.getIntExtra(AddEditTaskActivity.PRIORITY, 0),
                    dueDate, projectId);

            if (data.getStringExtra(AddEditTaskActivity.TASK_ID) != null) {
                task.setId(data.getStringExtra(AddEditTaskActivity.TASK_ID));
            }

            if (requestCode == EDIT_TASK_ACTIVITY_REQUEST_CODE && resultCode == AddEditTaskActivity.RESULT_DELETE) {
                taskViewModel.delete(task);
            } else if (requestCode == NEW_TASK_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                taskViewModel.insert(task);
                Toast.makeText(getContext(), "Task added", Toast.LENGTH_LONG).show();
            } else if (requestCode == EDIT_TASK_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

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

        swipeContainer = (SwipeRefreshLayout) root.findViewById(R.id.swipeContainer);

        taskListAdapter.setOnItemClickListener(new TaskListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
                intent.putExtra(AddEditTaskActivity.NAME, task.getName());
                intent.putExtra(AddEditTaskActivity.DESCRIPTION, task.getDescription());
                intent.putExtra(AddEditTaskActivity.PRIORITY, task.getPriority());
                intent.putExtra(AddEditTaskActivity.TASK_ID, task.getId());
                intent.putExtra(AddEditTaskActivity.STATUS, task.getStatus().getStatusName());
                if(task.getDueDate() != null) {
                    intent.putExtra(AddEditTaskActivity.DUE_DATE, task.getDueDate().getTime());
                }
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

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onRefresh() {
                taskViewModel.refresh();
                getAllTasks();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return root;
    }
}