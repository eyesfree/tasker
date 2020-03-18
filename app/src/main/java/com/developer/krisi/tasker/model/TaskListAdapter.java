package com.developer.krisi.tasker.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.developer.krisi.tasker.R;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder> {

    private static final String ADAPTER = "TaskListAdapter";

    private final LayoutInflater mInflater;
    private List<Task> tasks; // Cached copy of words

    public TaskListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, status;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            description = (TextView) view.findViewById(R.id.description);
            status = (TextView) view.findViewById(R.id.status);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.task_item_beautiful, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        if(tasks != null) {
            Task task = tasks.get(position);
            myViewHolder.name.setText(task.getName());
            myViewHolder.description.setText(task.getDescription());
            myViewHolder.status.setText(task.getStatus());
        } else {
            // Covers the case of data not being ready yet.
            myViewHolder.name.setText("My task");
            myViewHolder.description.setText("none");
            myViewHolder.status.setText("NEW");
        }
    }

    @Override
    public int getItemCount() {
        if(tasks != null) {
            return tasks.size();
        } else {
            return 0;
        }
    }

    public void setTasks(List<Task> inputTasks) {
        Log.i(ADAPTER, "Setting all tasks");
        tasks = inputTasks;
        notifyDataSetChanged();
    }

    public Task getTaskAt(int position) {
        return tasks.get(position);
    }
}
