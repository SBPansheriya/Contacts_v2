package com.contacts.Adapter;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Activity.ContactDetailActivity;
import com.contacts.Class.Constant;
import com.contacts.Fragment.ContactsFragment;
import com.contacts.Model.Users;
import com.contacts.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {

    ContactsFragment contactsFragment;
    ArrayList<Users> usersList;
    int favorites;
    boolean isEdit = false;

    public ContactListAdapter(ContactsFragment contactsFragment, ArrayList<Users> usersList, boolean isEdit) {
        this.contactsFragment = contactsFragment;
        this.usersList = usersList;
        this.isEdit = isEdit;
    }

    public void setFilteredList(ArrayList<Users> filteredList){
        this.usersList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        String button = contactsFragment.getArguments().getString("btn");

        if (Constant.favoriteList.size() > 0) {
            boolean isMatch = false;
            for (int i = 0; i < Constant.favoriteList.size(); i++) {
                if (usersList.get(position).contactId.equals(Constant.favoriteList.get(i).contactId)) {
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
        } else {
            holder.no_fav_add.setVisibility(View.VISIBLE);
            holder.fav_add.setVisibility(View.GONE);
        }

        if (button.equals("fav")){
            holder.no_fav_add.setVisibility(View.VISIBLE);
        }
        if (button.equals("no_fav_found")){
            holder.no_fav_add.setVisibility(View.VISIBLE);
        }
        if (button.equals("contact")){
            holder.no_fav_add.setVisibility(View.GONE);
            holder.fav_add.setVisibility(View.GONE);
        }

        holder.no_fav_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.fav_add.setVisibility(View.VISIBLE);
                holder.no_fav_add.setVisibility(View.GONE);
                favorites = 1;
                addToFavorites(contactsFragment.getContext(), usersList.get(position).getContactId(),favorites);
            }
        });

        holder.fav_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.fav_add.setVisibility(View.GONE);
                holder.no_fav_add.setVisibility(View.VISIBLE);
                favorites = 0;
                addToFavorites(contactsFragment.getContext(), usersList.get(position).getContactId(),favorites);
            }
        });

        holder.personName.setText(usersList.get(position).first + " " + usersList.get(position).last);

        if (TextUtils.isEmpty(usersList.get(position).image)) {
            holder.personImage.setImageResource(R.drawable.person_placeholder);
        } else {
            Picasso.get().load(usersList.get(position).image).into(holder.personImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ContactsFragment)contactsFragment).intentPass(usersList.get(position));
            }
        });

        holder.checkBox.setChecked(usersList.get(position).isSelected());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            usersList.get(position).setSelected(isChecked);
        });

        if (isEdit) {
            holder.personImage.setVisibility(View.GONE);
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.personImage.setVisibility(View.VISIBLE);
            holder.checkBox.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public static void addToFavorites(Context context, String contactId,int favorite) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        Uri rawContactUri = Uri.withAppendedPath(ContactsContract.RawContacts.CONTENT_URI, contactId);
        values.put(ContactsContract.CommonDataKinds.Phone.STARRED, favorite); // 1 for favorite, 0 for not favorite
        contentResolver.update(rawContactUri, values, null, null);
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView personName;
        ImageView personImage,fav_add,no_fav_add;
        CheckBox checkBox;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            personName = itemView.findViewById(R.id.personName);
            personImage = itemView.findViewById(R.id.personImage);
            checkBox = itemView.findViewById(R.id.checkBox1);
            fav_add = itemView.findViewById(R.id.fav_add);
            no_fav_add = itemView.findViewById(R.id.no_fav_add);
        }
    }
}
