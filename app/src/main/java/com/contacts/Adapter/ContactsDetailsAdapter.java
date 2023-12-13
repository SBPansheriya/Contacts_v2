package com.contacts.Adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Activity.ContactDetailActivity;
import com.contacts.Model.Users;
import com.contacts.Model.Phone;
import com.contacts.R;

import java.util.ArrayList;

public class ContactsDetailsAdapter extends RecyclerView.Adapter<ContactsDetailsAdapter.viewHolder> {

    ContactDetailActivity contactDetailActivity;
    ArrayList<Phone> phoneArrayList;
    Users users;

    public ContactsDetailsAdapter(ContactDetailActivity contactDetailActivity, ArrayList<Phone> phoneArrayList) {
        this.contactDetailActivity = contactDetailActivity;
        this.phoneArrayList = phoneArrayList;
//        this.users = users;
    }

    @NonNull
    @Override
    public ContactsDetailsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_details_item_list, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsDetailsAdapter.viewHolder holder, int position) {

        if (TextUtils.isEmpty(phoneArrayList.get(position).getPhonenumber())){
            holder.linearLayout.setVisibility(View.GONE);
        }
        else {
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.selected_person_pnum.setText(phoneArrayList.get(position).getPhonenumber());
        }
        holder.savedContactStatus.setText(phoneArrayList.get(position).getLabel());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ContactDetailActivity) contactDetailActivity).getCall(phoneArrayList.get(position).getPhonenumber());
            }
        });
    }

    @Override
    public int getItemCount() {
        return phoneArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView selected_person_pnum;
        TextView savedContactStatus;
        LinearLayout linearLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            selected_person_pnum = itemView.findViewById(R.id.selected_person_pnum);
            savedContactStatus = itemView.findViewById(R.id.savedContactStatus);
            linearLayout = itemView.findViewById(R.id.title5);
        }
    }
}
