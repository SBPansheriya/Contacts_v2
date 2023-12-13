package com.contacts.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Activity.ContactDetailActivity;
import com.contacts.Model.Phone;
import com.contacts.R;

import java.util.ArrayList;

public class WhatsAppAdapter extends RecyclerView.Adapter<WhatsAppAdapter.viewHolder> {

    ContactDetailActivity contactDetailActivity;
    ArrayList<Phone> phoneArrayList1;

    public WhatsAppAdapter(ContactDetailActivity contactDetailActivity, ArrayList<Phone> phoneArrayList1) {
        this.contactDetailActivity = contactDetailActivity;
        this.phoneArrayList1 = phoneArrayList1;
    }

    @NonNull
    @Override
    public WhatsAppAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.whatsapp_list_item, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WhatsAppAdapter.viewHolder holder, int position) {

        holder.message_whatsapp.setText("Message" + " " +phoneArrayList1.get(position).getPhonenumber());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://api.whatsapp.com/send?phone=" + phoneArrayList1.get(position).getPhonenumber();
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    contactDetailActivity.startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(contactDetailActivity, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return phoneArrayList1.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView message_whatsapp;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            message_whatsapp = itemView.findViewById(R.id.message_whatsapp);
        }
    }
}
