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
import com.contacts.Fragment.ContactsFragment;
import com.contacts.Model.Header;
import com.contacts.Model.Users;
import com.contacts.R;

import java.util.ArrayList;

public class HeaderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ContactsFragment contactsFragment;
    ArrayList<Object> items;

    public HeaderListAdapter(ContactsFragment contactsFragment, ArrayList<Object> items) {
        this.contactsFragment = contactsFragment;
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {

        if (items.get(position) instanceof Header) {
            return 0; // Header item type
        }
        else if(items.get(position) instanceof Users){
            return 1; // Child item type
        }
        else {
            return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 0:
                View v1 = inflater.inflate(R.layout.header_list_item, parent, false);
                viewHolder = new HeaderViewHolder(v1);
                break;

            case 1:
                View v2 = inflater.inflate(R.layout.contact_list_item, parent, false);
                viewHolder = new ItemViewHolder(v2);
                break;
        }
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (items.get(position) instanceof Header) {
            ((HeaderViewHolder) holder).bind(((Header) items.get(position)).getHeader());
        }
        else if (items.get(position) instanceof Users){
            ((ItemViewHolder) holder).bind(((Users) items.get(position)).getFirst());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView contactHead;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            contactHead = itemView.findViewById(R.id.header);
        }
        public void bind(String headerText) {
            contactHead.setText(headerText);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView personName;
        ImageView personImage;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            personName = itemView.findViewById(R.id.personName);
            personImage = itemView.findViewById(R.id.personImage);
        }

        public void bind(String personname) {
            personName.setText(personname);
        }
    }
}
