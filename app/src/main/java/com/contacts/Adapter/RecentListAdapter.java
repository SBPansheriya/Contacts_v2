package com.contacts.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecentListAdapter extends RecyclerView.Adapter<RecentListAdapter.recentviewholder> {
    @NonNull
    @Override
    public recentviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull recentviewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class recentviewholder extends RecyclerView.ViewHolder {
        public recentviewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
