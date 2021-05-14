package com.developer.krisi.tasker.effects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.developer.krisi.tasker.R;
import com.developer.krisi.tasker.model.Status;
import com.developer.krisi.tasker.model.Task;
import com.developer.krisi.tasker.model.TaskListAdapter;
import com.developer.krisi.tasker.model.TaskViewModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class CustomTouchCallback extends ItemTouchHelper.SimpleCallback {

    private TaskViewModel viewModel;
    private TaskListAdapter adapter;
    private int lastSwipedTaskPosition;
    private Task lastSwipedTask;
    private ColorDrawable swipeBackgroundOnDone = new ColorDrawable(Color.GREEN);
    private Drawable doneIcon;
    private ColorDrawable swipeBackgroundOnDelete = new ColorDrawable(Color.parseColor("#FF0000"));
    private Drawable deleteIcon;
    private static final float buttonWidth = 300;

    public CustomTouchCallback(TaskViewModel model, TaskListAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START | ItemTouchHelper.END);
        this.viewModel = model;
        this.adapter = adapter;
        this.deleteIcon = ContextCompat.getDrawable(viewModel.getApplication().getApplicationContext(), R.drawable.ic_delete_green_36dp);
        this.doneIcon = ContextCompat.getDrawable(viewModel.getApplication().getApplicationContext(), R.drawable.ic_done_red_36dp);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        this.lastSwipedTaskPosition = viewHolder.getAdapterPosition();
        this.lastSwipedTask = adapter.getTaskAt(lastSwipedTaskPosition);

        if(direction == ItemTouchHelper.START) {
            Log.i("TouchHelper","Swiped right, removing item");
            viewModel.delete(this.lastSwipedTask);
            adapter.notifyItemRemoved(this.lastSwipedTaskPosition);
            Snackbar.make(viewHolder.itemView, "Item deleted.", Snackbar.LENGTH_LONG).setAction(R.string.UndoText,v -> {
                viewModel.insert(this.lastSwipedTask);
                adapter.notifyItemInserted(this.lastSwipedTaskPosition);
            }).show();

        } else if(direction == ItemTouchHelper.END) {
            Log.i("TouchHelper", "Swiped left, setting to done");
            Task currentTask = this.lastSwipedTask;
            if (currentTask != null) {
                currentTask.setStatus(Status.DONE);
                viewModel.update(currentTask);
            }
        } else {
            Log.i("TouchHelper", "Swiped to " + direction);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View currentTaskView = viewHolder.itemView;
        int doneIconMargin = (currentTaskView.getHeight() - doneIcon.getIntrinsicHeight()) / 2;
        int deleteIconMargin = (currentTaskView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;

        if(dX > 0) { // swipe right
            swipeBackgroundOnDone.setBounds(currentTaskView.getLeft(), currentTaskView.getTop(), (int)dX, currentTaskView.getBottom());
            doneIcon.setBounds(
                    currentTaskView.getLeft() + doneIconMargin,
                    currentTaskView.getTop() + doneIconMargin,
                    currentTaskView.getLeft() + doneIconMargin + doneIcon.getIntrinsicWidth(),
                    currentTaskView.getBottom() - doneIconMargin);
            swipeBackgroundOnDone.draw(c);
            doneIcon.draw(c);
        } else { // swipe left
            swipeBackgroundOnDelete.setBounds(currentTaskView.getRight() + (int)dX, currentTaskView.getTop(), currentTaskView.getRight(), currentTaskView.getBottom());
            deleteIcon.setBounds(
                    currentTaskView.getRight() - deleteIconMargin - deleteIcon.getIntrinsicWidth(),
                    currentTaskView.getTop() + deleteIconMargin,
                    currentTaskView.getRight() - deleteIconMargin,
                    currentTaskView.getBottom() - deleteIconMargin);
            swipeBackgroundOnDelete.draw(c);
            deleteIcon.draw(c);
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
