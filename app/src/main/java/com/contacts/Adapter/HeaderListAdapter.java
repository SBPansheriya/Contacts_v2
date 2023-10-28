package com.contacts.Adapter;

import static com.contacts.Class.Constant.usersArrayList;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Fragment.ContactsFragment;
import com.contacts.Model.Header;
import com.contacts.Model.Users;
import com.contacts.R;

import java.util.ArrayList;
import java.util.List;

public class HeaderListAdapter extends RecyclerView.Adapter<HeaderListAdapter.HeaderViewHolder> {

    ContactsFragment contactsFragment;
    ArrayList<Header> headerArrayList;
    ContactListAdapter contactListAdapter;
    boolean isEdit = false;

    public HeaderListAdapter(ContactsFragment contactsFragment, ArrayList<Header> headerArrayList) {
        this.contactsFragment = contactsFragment;
        this.headerArrayList = headerArrayList;
    }

    @NonNull
    @Override
    public HeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contactsFragment.getContext()).inflate(R.layout.header_list_item, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeaderViewHolder holder, int position) {

        holder.header.setText(headerArrayList.get(position).header);

        LinearLayoutManager manager = new LinearLayoutManager(contactsFragment.getContext());
        contactListAdapter = new ContactListAdapter(contactsFragment, headerArrayList.get(position).usersList, isEdit);
        holder.Contact_recyclerView.setLayoutManager(manager);
        holder.Contact_recyclerView.setAdapter(contactListAdapter);
        holder.Contact_recyclerView.setNestedScrollingEnabled(false);
        holder.Contact_recyclerView.scrollToPosition(getItemCount());
    }

    public void setHeaderArrayList(ArrayList<Header> headerArrayList) {
        this.headerArrayList = headerArrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return headerArrayList.size();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView header;
        RecyclerView Contact_recyclerView;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
            Contact_recyclerView = itemView.findViewById(R.id.contact_recyclerview);
        }
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
        notifyDataSetChanged();
    }
}