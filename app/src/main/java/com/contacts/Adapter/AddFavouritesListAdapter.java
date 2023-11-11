package com.contacts.Adapter;

import static com.contacts.Class.Constant.favoriteList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Activity.AddFavouritesActivity;
import com.contacts.Class.Constant;

import com.contacts.Model.Users;
import com.contacts.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddFavouritesListAdapter extends RecyclerView.Adapter<AddFavouritesListAdapter.ViewHolder> {

    AddFavouritesActivity addFavouritesActivity;
    ArrayList<Users> usersArrayList;
    int favorites;

    public AddFavouritesListAdapter(AddFavouritesActivity addFavouritesActivity, ArrayList<Users> usersArrayList) {
        this.addFavouritesActivity = addFavouritesActivity;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public AddFavouritesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addfavourites_item_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddFavouritesListAdapter.ViewHolder holder, int position) {

        if (Constant.favoriteList.size() > 0) {
            boolean isMatch = false;
            for (int i = 0; i < Constant.favoriteList.size(); i++) {
                if (usersArrayList.get(position).contactId.equals(Constant.favoriteList.get(i).contactId)) {
                    isMatch = true;
                    break;
                }
            }
            if (isMatch) {
                holder.fav_add.setVisibility(View.VISIBLE);
                holder.no_fav_add.setVisibility(View.GONE);
            } else {
                holder.no_fav_add.setVisibility(View.VISIBLE);
                holder.fav_add.setVisibility(View.GONE);
            }
        }
        else {
            holder.no_fav_add.setVisibility(View.VISIBLE);
            holder.fav_add.setVisibility(View.GONE);
        }

        holder.personName.setText(usersArrayList.get(position).first + " " + usersArrayList.get(position).last);

        if (TextUtils.isEmpty(usersArrayList.get(position).image)) {
            holder.personImage.setImageResource(R.drawable.person_placeholder);
        } else {
            Picasso.get().load(usersArrayList.get(position).image).into(holder.personImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.fav_add.getVisibility() == View.VISIBLE){
                    holder.fav_add.setVisibility(View.GONE);
                    holder.no_fav_add.setVisibility(View.VISIBLE);
                    favorites = 0;
                    addToFavorites(addFavouritesActivity, usersArrayList.get(position).getContactId(),usersArrayList.get(position).image,usersArrayList.get(position).first,usersArrayList.get(position).last,usersArrayList.get(position).personPhone,favorites);
                }
                else {
                    holder.fav_add.setVisibility(View.VISIBLE);
                    holder.no_fav_add.setVisibility(View.GONE);
                    favorites = 1;
                    addToFavorites(addFavouritesActivity, usersArrayList.get(position).getContactId(),usersArrayList.get(position).image,usersArrayList.get(position).first,usersArrayList.get(position).last,usersArrayList.get(position).personPhone,favorites);
                }
            }
        });

    }

    public static void addToFavorites(Context context, String contactId, String image, String first, String last, String phone, int favorite) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        Uri rawContactUri = Uri.withAppendedPath(ContactsContract.RawContacts.CONTENT_URI, contactId);
        values.put(ContactsContract.CommonDataKinds.Phone.STARRED, favorite); // 1 for favorite, 0 for not favorite
        contentResolver.update(rawContactUri, values, null, null);

        Users users = new Users(contactId,image,first,last,phone,"");
        if (favorite == 1) {
            favoriteList.add(users);
        }
        else {
            for (int i = 0; i < favoriteList.size(); i++) {
                if (favoriteList.get(i).contactId.equalsIgnoreCase(users.contactId)) {
                    favoriteList.remove(i);
                    break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView personImage;
        TextView personName;
        ImageView fav_add,no_fav_add;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            personName = itemView.findViewById(R.id.fav_add_personName);
            personImage = itemView.findViewById(R.id.fav_add_personImage);
            fav_add = itemView.findViewById(R.id.fav_add);
            no_fav_add = itemView.findViewById(R.id.no_fav_add);
        }
    }
}
