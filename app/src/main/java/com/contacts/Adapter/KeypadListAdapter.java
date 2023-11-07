package com.contacts.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.contacts.Activity.KeypadScreen;
import com.contacts.Model.Users;
import com.contacts.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class KeypadListAdapter extends RecyclerView.Adapter<KeypadListAdapter.keypadviewholder> {

    KeypadScreen keypadScreen;
    ArrayList<Users> usersArrayList;

    public KeypadListAdapter(KeypadScreen keypadScreen, ArrayList<Users> usersArrayList) {
        this.keypadScreen = keypadScreen;
        this.usersArrayList = usersArrayList;
    }

    public void setFilteredList(ArrayList<Users> filteredList){
        this.usersArrayList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public KeypadListAdapter.keypadviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(keypadScreen).inflate(R.layout.keypad_list_item, parent, false);
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
                ((KeypadScreen) keypadScreen).call(usersArrayList.get(position).getPersonPhone());
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