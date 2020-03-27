package com.developer.krisi.tasker.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.developer.krisi.tasker.R;
import com.developer.krisi.tasker.effects.CustomTouchCallback;
import com.developer.krisi.tasker.model.Task;
import com.developer.krisi.tasker.model.TaskListAdapter;
import com.developer.krisi.tasker.model.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String TAG = "TabFragment";
    private static final String ARG_SECTION_NUMBER = "section_number";

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
        return root;
    }
}