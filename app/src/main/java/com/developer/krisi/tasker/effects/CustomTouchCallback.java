package com.developer.krisi.tasker.effects;

import android.util.Log;

import com.developer.krisi.tasker.model.Task;
import com.developer.krisi.tasker.model.TaskListAdapter;
import com.developer.krisi.tasker.model.TaskViewModel;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class CustomTouchCallback extends ItemTouchHelper.SimpleCallback {

    private TaskViewModel viewModel;
    private TaskListAdapter adapter;

    public CustomTouchCallback(TaskViewModel model, TaskListAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START | ItemTouchHelper.END);
        this.viewModel = model;
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if(direction == ItemTouchHelper.START) {
            Log.i("TouchHelper","Swiped right, removing item");
            viewModel.delete(adapter.getTaskAt(viewHolder.getAdapterPosition()));
        } else if(direction == ItemTouchHelper.END) {
            Log.i("TouchHelper", "Swiped left, setting in progress");
            Task currentTask = adapter.getTaskAt(viewHolder.getAdapterPosition());
            currentTask.setStatus("DOING");
            viewModel.update(currentTask);
        } else {
            Log.i("TouchHelper", "Swiped to " + direction);
        }
    }
}
