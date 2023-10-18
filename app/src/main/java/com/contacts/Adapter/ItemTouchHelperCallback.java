package com.contacts.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final FavListAdapter favListAdapter;

    public ItemTouchHelperCallback(FavListAdapter favListAdapter) {
        this.favListAdapter = favListAdapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        favListAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
//        favListAdapter.saveListToSharedPreferences(recyclerView.getContext());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // Handle swipe (if needed)
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }
}