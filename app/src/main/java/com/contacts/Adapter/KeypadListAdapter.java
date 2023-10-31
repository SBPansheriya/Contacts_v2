package com.contacts.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Fragment.KeypadFragment;
import com.contacts.KeypadScreen;
import com.contacts.Model.Users;
import com.contacts.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class KeypadListAdapter extends RecyclerView.Adapter<KeypadListAdapter.keypadviewholder> {

    KeypadScreen keypadFragment;
    ArrayList<Users> usersArrayList;

    public KeypadListAdapter(KeypadScreen keypadFragment, ArrayList<Users> usersArrayList) {
        this.keypadFragment = keypadFragment;
        this.usersArrayList = usersArrayList;
    }

    public void setFilteredList(ArrayList<Users> filteredList){
        this.usersArrayList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public KeypadListAdapter.keypadviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(keypadFragment).inflate(R.layout.keypad_list_item, parent, false);
        return new keypadviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KeypadListAdapter.keypadviewholder holder, int position) {
        Users user = usersArrayList.get(position);

        if (user.image == null) {
            holder.keypad_image.setImageResource(R.drawable.person_placeholder);
        } else {
            Picasso.get().load(user.image).into(holder.keypad_image);
        }

        holder.keypad_name.setText(user.first + " " + user.last);
        holder.keypad_number.setText(user.personPhone);

        holder.keypad_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + user.personPhone));
                keypadFragment.startActivity(intent);
                Toast.makeText(keypadFragment, "Calling " + user.first + " " + user.last, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class keypadviewholder extends RecyclerView.ViewHolder {
        ImageView keypad_image, keypad_call;
        TextView keypad_name, keypad_number;

        public keypadviewholder(@NonNull View itemView) {
            super(itemView);
            keypad_image = itemView.findViewById(R.id.keypad_personImage);
            keypad_call = itemView.findViewById(R.id.keypad_call);
            keypad_name = itemView.findViewById(R.id.keypad_name);
            keypad_number = itemView.findViewById(R.id.keypad_number);

        }
    }
}