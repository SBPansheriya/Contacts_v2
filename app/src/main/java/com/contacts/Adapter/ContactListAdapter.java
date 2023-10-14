package com.contacts.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Activity.ContactDetailActivity;
import com.contacts.Fragment.ContactsFragment;
import com.contacts.Model.Users;
import com.contacts.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {

    ContactsFragment contactsFragment;
    ArrayList<Users> usersList;

    public ContactListAdapter(ContactsFragment contactsFragment, ArrayList<Users> usersList) {
        this.contactsFragment = contactsFragment;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item,parent,false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {


        holder.personName.setText(usersList.get(position).first +" "+ usersList.get(position).last);
        loadImageFromStorage(usersList.get(position).getImage(),holder.personImage);
        System.out.println(usersList.get(position).image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contactsFragment.getActivity(), ContactDetailActivity.class);
                intent.putExtra("contactId",usersList.get(position).contactId);
                intent.putExtra("image",usersList.get(position).image);
                intent.putExtra("first",usersList.get(position).first);
                intent.putExtra("last",usersList.get(position).last);
                intent.putExtra("pphone",usersList.get(position).personPhone);
                intent.putExtra("ophone",usersList.get(position).officePhone);
                contactsFragment.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView personName;
        ImageView personImage;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            personName =  itemView.findViewById(R.id.personName);
            personImage =  itemView.findViewById(R.id.personImage);
        }
    }

    private void loadImageFromStorage(String path,ImageView personImage)
    {
        try {
            File f = new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            personImage.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
