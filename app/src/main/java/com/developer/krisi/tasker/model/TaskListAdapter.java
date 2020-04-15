package com.developer.krisi.tasker.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.developer.krisi.tasker.R;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder> {

    private static final String ADAPTER = "TaskListAdapter";

    private final LayoutInflater mInflater;
    private List<Task> tasks; // Cached copy of tasks

    private OnItemClickListener listener;

    public TaskListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, status, priority;
        public ImageView statusImage;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            description = (TextView) view.findViewById(R.id.description);
            status = (TextView) view.findViewById(R.id.status);
            statusImage = (ImageView) view.findViewById(R.id.imageStatus);
            priority = (TextView) view.findViewById(R.id.priority);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(tasks.get(position));
                    }
                }
            });
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
            myViewHolder.status.setText(task.getStatus().getStatusName());
            myViewHolder.priority.setText(String.valueOf(task.getPriority()));
            if(task.getStatus().equals(Status.DONE)) {
                myViewHolder.statusImage.setImageResource(R.drawable.ic_lens_green_24dp);
            } else if (task.getStatus().equals(Status.IN_PROGRESS)){
                myViewHolder.statusImage.setImageResource(R.drawable.ic_lens_orange_24dp);
            } else if (task.getStatus().equals(Status.NEW)) {
                myViewHolder.statusImage.setImageResource(R.drawable.ic_lens_blue_24dp);
            }
        } else {
            // Covers the case of data not being ready yet.
            myViewHolder.name.setText("My task");
            myViewHolder.description.setText("none");
            myViewHolder.status.setText("NEW");
            myViewHolder.priority.setText("1");
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
        Log.i(ADAPTER, "Requested task at position " + position);
        if(position != RecyclerView.NO_POSITION) {
            return tasks.get(position);
        } else {
            return null;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Task task);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
