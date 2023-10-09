package com.contacts.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Fragment.FavoritesFragment;
import com.contacts.R;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.favouriteviewholder> {

    FavoritesFragment favoritesFragment;

    public FavListAdapter(FavoritesFragment favoritesFragment) {
        this.favoritesFragment = favoritesFragment;
    }

    @NonNull
    @Override
    public favouriteviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_item_list,parent,false);
        return new favouriteviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull favouriteviewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class favouriteviewholder extends RecyclerView.ViewHolder {
        public favouriteviewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
