package com.contacts.Adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Fragment.NewRecyclerviewFragment;
import com.contacts.Model.Users;
import com.contacts.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewRecyclerAdapter extends RecyclerView.Adapter<NewRecyclerAdapter.ViewHolder> {

    NewRecyclerviewFragment newRecyclerviewFragment;
    ArrayList<Users> usersArrayList;
    boolean isEdit = false;

    public NewRecyclerAdapter(NewRecyclerviewFragment newRecyclerview, ArrayList<Users> usersArrayList,boolean isEdit) {
        this.newRecyclerviewFragment = newRecyclerview;
        this.usersArrayList = usersArrayList;
        this.isEdit = isEdit;
    }

    public void filterList(ArrayList<Users> filterlist) {
        usersArrayList = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewRecyclerAdapter.ViewHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewRecyclerviewFragment) newRecyclerviewFragment).intentPass(usersArrayList.get(position));
            }
        });

        holder.personName.setText(usersArrayList.get(position).first + " " + usersArrayList.get(position).last);

        if (TextUtils.isEmpty(usersArrayList.get(position).image)) {
            holder.personImage.setImageResource(R.drawable.person_placeholder);
        } else {
            Picasso.get().load(usersArrayList.get(position).image).into(holder.personImage);
        }

        holder.checkBox.setChecked(usersArrayList.get(position).isSelected());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            usersArrayList.get(position).setSelected(isChecked);
        });

        if (isEdit) {
            holder.personImage.setVisibility(View.GONE);
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.itemView.setClickable(false);
        }
        else {
            holder.personImage.setVisibility(View.VISIBLE);
            holder.checkBox.setVisibility(View.GONE);
            holder.itemView.setClickable(true);
        }
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView personImage;
        TextView personName;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            personName = itemView.findViewById(R.id.personNewName);
            personImage = itemView.findViewById(R.id.personNewImage);
            checkBox = itemView.findViewById(R.id.checkBox1);
        }
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
        notifyDataSetChanged();
    }
}
