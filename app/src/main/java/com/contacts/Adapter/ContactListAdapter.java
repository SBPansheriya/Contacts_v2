package com.contacts.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.EditScreenActivity;
import com.contacts.Fragment.ContactsFragment;
import com.contacts.Model.Users;
import com.contacts.R;

import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.contactviewholder> {

    List<Users> childItemList;
    ContactsFragment contactsFragment;

    public ContactListAdapter(ContactsFragment contactsFragment, List<Users> childItemList) {
        this.childItemList = childItemList;
        this.contactsFragment = contactsFragment;
    }

    @NonNull
    @Override
    public contactviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item,parent,false);
        return new contactviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull contactviewholder holder, int position) {
        holder.name.setText(childItemList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contactsFragment.getActivity(), EditScreenActivity.class);
                contactsFragment.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return childItemList.size();
    }

    public class contactviewholder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView personImage;

        public contactviewholder(@NonNull View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.personName);
            personImage =  itemView.findViewById(R.id.personImage);
        }
    }
}
