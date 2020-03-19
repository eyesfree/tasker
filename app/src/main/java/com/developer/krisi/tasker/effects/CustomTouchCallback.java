package com.developer.krisi.tasker.effects;

import android.graphics.Canvas;
import android.util.Log;

import com.developer.krisi.tasker.model.Task;
import com.developer.krisi.tasker.model.TaskListAdapter;
import com.developer.krisi.tasker.model.TaskViewModel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class CustomTouchCallback extends ItemTouchHelper.SimpleCallback {

    private TaskViewModel viewModel;
    private TaskListAdapter adapter;

    private static final float buttonWidth = 300;

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
            Log.i("TouchHelper", "Swiped left, setting to done");
            Task currentTask = adapter.getTaskAt(viewHolder.getAdapterPosition());
            if (currentTask != null) {
                currentTask.setStatus("DONE");
                viewModel.update(currentTask);
            }
        } else {
            Log.i("TouchHelper", "Swiped to " + direction);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
