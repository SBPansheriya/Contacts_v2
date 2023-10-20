package com.contacts.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Fragment.ContactsFragment;
import com.contacts.Fragment.FavoritesFragment;
import com.contacts.Model.Header;
import com.contacts.Model.Users;
import com.contacts.R;

import java.util.ArrayList;
import java.util.List;

public class HeaderListAdapter extends RecyclerView.Adapter<HeaderListAdapter.HeaderViewHolder> implements SectionIndexer {

    ContactsFragment contactsFragment;
    ArrayList<Header> headerArrayList;
    ArrayList<Users> usersList;
    ContactListAdapter contactListAdapter;
    private List<String> mDataArray;
    private ArrayList<Integer> mSectionPositions;

    public HeaderListAdapter(ContactsFragment contactsFragment, ArrayList<Header> headerArrayList) {
        this.contactsFragment = contactsFragment;
        this.headerArrayList = headerArrayList;
    }


    public void setFilteredList(ArrayList<Users> filteredList){
        this.usersList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contactsFragment.getContext()).inflate(R.layout.header_list_item,parent,false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeaderViewHolder holder, int position) {

        if (headerArrayList.get(position).usersList == null){
            holder.header.setVisibility(View.GONE);
        }
        else {
            holder.header.setVisibility(View.VISIBLE);
            holder.header.setText(headerArrayList.get(position).header);
        }

        LinearLayoutManager manager = new LinearLayoutManager(contactsFragment.getContext());
        contactListAdapter = new ContactListAdapter(contactsFragment, headerArrayList.get(position).usersList);
        holder.Contact_recyclerView.setLayoutManager(manager);
        holder.Contact_recyclerView.setAdapter(contactListAdapter);
    }

    @Override
    public int getItemCount() {
        return headerArrayList.size();
    }

    @Override
    public Object[] getSections() {

        for (char c = 'A'; c <= 'Z'; c++) {
            mDataArray.add(String.valueOf(c));
        }

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

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView header;
        RecyclerView Contact_recyclerView;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
            Contact_recyclerView = itemView.findViewById(R.id.contact_recyclerview);

        }
    }
}