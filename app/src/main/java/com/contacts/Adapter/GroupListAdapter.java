package com.contacts.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Fragment.GroupsFragment;
import com.contacts.Model.Group;
import com.contacts.R;

import java.util.ArrayList;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupViewHolder> {
    GroupsFragment groupsFragment;
    ArrayList<Group> groupArrayList;

    public GroupListAdapter(GroupsFragment groupsFragment, ArrayList<Group> groupArrayList) {
        this.groupsFragment = groupsFragment;
        this.groupArrayList = groupArrayList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.groups_list_item,parent,false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.group_Image.setImageResource(R.drawable.groups_placeholder);
        holder.group_Name.setText(groupArrayList.get(position).getGroupName());
        holder.group_member_number.setText("(" + "0" +groupArrayList.get(position).getGroupMemberNumber() + ")");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GroupsFragment) groupsFragment).groupIntentPass(groupArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupArrayList.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {

        TextView group_Name, group_member_number;
        ImageView group_Image;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            group_Name = itemView.findViewById(R.id.group_Name);
            group_member_number = itemView.findViewById(R.id.group_member_number);
            group_Image = itemView.findViewById(R.id.group_Image);
        }
    }
}
