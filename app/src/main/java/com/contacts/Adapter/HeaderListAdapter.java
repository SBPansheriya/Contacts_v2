package com.contacts.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
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

public class HeaderListAdapter extends RecyclerView.Adapter<HeaderListAdapter.headerviewHolder> implements SectionIndexer {

    ArrayList<Users> list;
    ArrayList<Header> headerlist;
    ContactsFragment contactsFragment;
    private List<String> mDataArray;
    private ArrayList<Integer> mSectionPositions;

    public HeaderListAdapter(ArrayList<Users> list, ContactsFragment contactsFragment) {
        this.list = list;
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

        holder.header.setText(list.get(position).getFirst());


//        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.contactrecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
//        layoutManager.setInitialPrefetchItemCount(header.getChildItemList().size());
//
//        ContactListAdapter contactListAdapter = new ContactListAdapter(contactsFragment,header.getChildItemList());
//        holder.contactrecyclerView.setLayoutManager(layoutManager);
//        holder.contactrecyclerView.setAdapter(contactListAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);
        for (int i = 0, size = mDataArray.size(); i < size; i++) {
            String section = String.valueOf(mDataArray.get(i).charAt(0)).toUpperCase();
            if (!sections.contains(section)) {
                sections.add(section);
                mSectionPositions.add(i);
            }
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int i) {
        return mSectionPositions.get(i);
    }

    @Override
    public int getSectionForPosition(int i) {
        return 0;
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
