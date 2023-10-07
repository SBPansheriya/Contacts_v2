package com.contacts.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Model.Users;
import com.contacts.R;

import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.contactviewholder> {

    List<Users> childItemList;

    public ContactListAdapter(List<Users> childItemList) {
        this.childItemList = childItemList;
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
    }

    @Override
    public int getItemCount() {
        return childItemList.size();
    }

    public class contactviewholder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView personimage;

        public contactviewholder(@NonNull View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.personName);
            personimage =  itemView.findViewById(R.id.personImage);
        }
    }
}
