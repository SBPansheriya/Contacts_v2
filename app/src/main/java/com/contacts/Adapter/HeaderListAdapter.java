package com.contacts.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Fragment.ContactsFragment;
import com.contacts.Model.Header;
import com.contacts.R;

import java.util.List;

public class HeaderListAdapter extends RecyclerView.Adapter<HeaderListAdapter.headerviewHolder> {

    List<Header> headerlist;
    ContactsFragment contactsFragment;
    public HeaderListAdapter(List<Header> headerlist, ContactsFragment contactsFragment) {
        this.headerlist = headerlist;
        this.contactsFragment = contactsFragment;
    }

    @NonNull
    @Override
    public headerviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_list_item,parent,false);
        return new headerviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull headerviewHolder holder, int position) {
        Header header = headerlist.get(position);

        holder.header.setText(header.getHeader());

        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.contactrecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setInitialPrefetchItemCount(header.getChildItemList().size());

        ContactListAdapter contactListAdapter = new ContactListAdapter(contactsFragment,header.getChildItemList());
        holder.contactrecyclerView.setLayoutManager(layoutManager);
        holder.contactrecyclerView.setAdapter(contactListAdapter);
    }

    @Override
    public int getItemCount() {
        return headerlist.size();
    }

    public class headerviewHolder extends RecyclerView.ViewHolder {
        TextView header;
        RecyclerView contactrecyclerView;
        public headerviewHolder(@NonNull View itemView) {
            super(itemView);

            header = itemView.findViewById(R.id.header);
            contactrecyclerView = itemView.findViewById(R.id.show_contact_recyclerview);
        }
    }
}
