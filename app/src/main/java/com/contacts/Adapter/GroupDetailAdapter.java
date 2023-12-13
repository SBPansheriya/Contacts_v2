package com.contacts.Adapter;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Activity.GroupDetailActivity;
import com.contacts.Model.Contacts;
import com.contacts.Model.Group;
import com.contacts.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GroupDetailAdapter extends RecyclerView.Adapter<GroupDetailAdapter.viewholder> {

    GroupDetailActivity groupDetailActivity;
    ArrayList<Contacts> contactsArrayList;
    Group group;

    public GroupDetailAdapter(GroupDetailActivity groupDetailActivity, ArrayList<Contacts> contactsArrayList, Group group) {
        this.groupDetailActivity = groupDetailActivity;
        this.contactsArrayList = contactsArrayList;
        this.group = group;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_group_contact_list_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        if (TextUtils.isEmpty(contactsArrayList.get(position).getImage())){
            holder.show_group_personImage.setImageResource(R.drawable.person_placeholder);
        }
        else {
            Picasso.get().load(contactsArrayList.get(position).getImage()).into(holder.show_group_personImage);
        }

        holder.show_group_personName.setText(contactsArrayList.get(position).getName());

        holder.remove_groupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(groupDetailActivity);
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(false);
                }
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.setContentView(R.layout.dailog_layout);
                dialog.setCancelable(false);
                dialog.show();

                Button cancel1 = dialog.findViewById(R.id.canceldialog);
                Button movetobin = dialog.findViewById(R.id.movetobin);

                cancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                movetobin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((GroupDetailActivity) groupDetailActivity).removeContactFromGroup(contactsArrayList.get(position).getId(), group.getGroupId(),position);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void updateList(ArrayList<Contacts> contactsArrayList){
        this.contactsArrayList = contactsArrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return contactsArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        ImageView show_group_personImage,remove_groupName;
        TextView show_group_personName;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            show_group_personImage = itemView.findViewById(R.id.show_group_personImage);
            show_group_personName = itemView.findViewById(R.id.show_group_personName);
            remove_groupName = itemView.findViewById(R.id.remove_groupName);
        }
    }
//        public void removeContactFromGroup(String contactId, String groupId) {
//
//            ContentResolver contentResolver = groupDetailActivity.getContentResolver();
//
//            Uri uri = ContactsContract.Data.CONTENT_URI;
//            String selection = ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "=? AND " +
//                    ContactsContract.Data.RAW_CONTACT_ID + "=? AND " +
//                    ContactsContract.Data.MIMETYPE + "=?";
//            String[] selectionArgs = new String[]{String.valueOf(groupId), String.valueOf(contactId),
//                    ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE};
//
//            contentResolver.delete(uri, selection, selectionArgs);
//
//            contactsArrayList.remove(contactId);
//        }
}
