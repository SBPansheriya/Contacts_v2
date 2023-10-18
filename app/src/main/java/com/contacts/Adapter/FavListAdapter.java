package com.contacts.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Activity.ContactDetailActivity;
import com.contacts.Fragment.FavoritesFragment;
import com.contacts.Model.Users;
import com.contacts.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.favouriteviewholder>{

    Context context;
    FavoritesFragment favoritesFragment;
    ArrayList<Users> favoriteContacts;

    public FavListAdapter(FavoritesFragment favoritesFragment, ArrayList<Users> favoriteContacts) {
        this.favoritesFragment = favoritesFragment;
        this.favoriteContacts = favoriteContacts;
    }

    @NonNull
    @Override
    public favouriteviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_item_list,parent,false);
        return new favouriteviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull favouriteviewholder holder, int position) {


        if (favoriteContacts.get(position).image == null) {
            holder.fav_person_image.setImageResource(R.drawable.person_placeholder);
        } else {
            Picasso.get().load(favoriteContacts.get(position).image).into(holder.fav_person_image);
        }

        holder.fav_person_name.setText(favoriteContacts.get(position).first + "" + favoriteContacts.get(position).last);

        holder.info_plite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(favoritesFragment.getContext(), ContactDetailActivity.class);
                intent.putExtra("user", favoriteContacts.get(position));
                favoritesFragment.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteContacts.size();
    }

    public class favouriteviewholder extends RecyclerView.ViewHolder {
        ImageView fav_person_image,info_plite,scrollcontact;

        TextView fav_person_name;
        public favouriteviewholder(@NonNull View itemView) {
            super(itemView);
            fav_person_image = itemView.findViewById(R.id.fav_personImage);
            info_plite = itemView.findViewById(R.id.info_icon);
            scrollcontact = itemView.findViewById(R.id.scrollcontact);
            fav_person_name = itemView.findViewById(R.id.fav_personName);
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(favoriteContacts, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }
}
