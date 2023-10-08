package com.droidamsel.doApp;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.droidamsel.doApp.Adapter.doAdapter;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class itemSlide extends ItemTouchHelper.SimpleCallback {
    private final doAdapter adapter;

    public itemSlide(doAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

@Override
public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
    final int position = viewHolder.getAdapterPosition();
    if (direction == ItemTouchHelper.RIGHT){
        AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
        builder.setTitle("Delete Task");
        builder.setMessage("Are You Sure ?");
        builder.setPositiveButton("Yes", (dialog, which) -> adapter.deleteTask(position));
        builder.setNegativeButton("Cancel", (dialog, which) -> adapter.notifyItemChanged(position));
        AlertDialog dialog = builder.create();
        dialog.show();
    }else{
        adapter.editItem(position);
    }
}

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(adapter.getContext() , R.color.base_color))
                .addSwipeLeftActionIcon(R.drawable.baseline_edit)
                .addSwipeRightBackgroundColor(Color.RED)
                .addSwipeRightActionIcon(R.drawable.baseline_delete_)
                .create()
                .decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}

