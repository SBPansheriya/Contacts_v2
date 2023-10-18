package com.contacts.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Activity.ContactDetailActivity;
import com.contacts.Fragment.ContactsFragment;
import com.contacts.Model.Header;
import com.contacts.Model.Users;
import com.contacts.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {

    ContactsFragment contactsFragment;
    ArrayList<Users> usersList;

    public ContactListAdapter(ContactsFragment contactsFragment, ArrayList<Users> usersList) {
        this.contactsFragment = contactsFragment;
        this.usersList = usersList;
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

        holder.personName.setText(usersList.get(position).first + " " + usersList.get(position).last);

        if (usersList.get(position).image == null) {
            holder.personImage.setImageResource(R.drawable.person_placeholder);
        } else {
            Picasso.get().load(usersList.get(position).image).into(holder.personImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contactsFragment.getActivity(), ContactDetailActivity.class);
                intent.putExtra("user", usersList.get(position));
                contactsFragment.startActivity(intent);
            }
        });

        holder.checkBox.setChecked(usersList.get(position).isSelected());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            usersList.get(position).setSelected(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView personName;
        ImageView personImage;
        CheckBox checkBox;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            personName = itemView.findViewById(R.id.personName);
            personImage = itemView.findViewById(R.id.personImage);
            checkBox = itemView.findViewById(R.id.checkBox1);
        }
    }
}
