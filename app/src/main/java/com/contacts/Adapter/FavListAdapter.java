package com.contacts.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Activity.ContactDetailActivity;
import com.contacts.Fragment.FavoritesFragment;
import com.contacts.R;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.favouriteviewholder> {

    FavoritesFragment favoritesFragment;
    String[] shayariname;

    public FavListAdapter(FavoritesFragment favoritesFragment, String[] shayariname) {
        this.favoritesFragment = favoritesFragment;
        this.shayariname = shayariname;
    }

    @NonNull
    @Override
    public favouriteviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_item_list,parent,false);
        return new favouriteviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull favouriteviewholder holder, int position) {

        holder.fav_person_name.setText(shayariname[position]);

        holder.info_plite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(favoritesFragment.getContext(), ContactDetailActivity.class);
                favoritesFragment.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shayariname.length;
    }

    public class favouriteviewholder extends RecyclerView.ViewHolder {
        ImageView fav_person_image,info_plite;
        TextView fav_person_name;
        public favouriteviewholder(@NonNull View itemView) {
            super(itemView);
            fav_person_image = itemView.findViewById(R.id.fav_personImage);
            info_plite = itemView.findViewById(R.id.info_icon);
            fav_person_name = itemView.findViewById(R.id.fav_personName);
        }
    }
}
