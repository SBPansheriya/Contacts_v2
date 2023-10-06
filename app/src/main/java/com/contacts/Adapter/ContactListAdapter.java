package com.contacts.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Fragment.ContactsFragment;
import com.contacts.Model.UsersModel;
import com.contacts.R;

import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.viewholder> {


    public ContactListAdapter(List<UsersModel> list) {

    }

    @NonNull
    @Override
    public ContactListAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListAdapter.viewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView name,header;
        ImageView personimage;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
            name =  itemView.findViewById(R.id.personName);
            personimage =  itemView.findViewById(R.id.personImage);
        }
    }
}
