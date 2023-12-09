package com.contacts;

import static com.contacts.Class.Constant.contactsArrayList;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Model.Users;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddNewMemberAdapter extends RecyclerView.Adapter<AddNewMemberAdapter.viewholder> {

    AddNewMemberActivity addNewMemberActivity;
    ArrayList<Users> usersArrayList;
    Group group;

    public AddNewMemberAdapter(AddNewMemberActivity addNewMemberActivity, ArrayList<Users> usersArrayList, Group group) {
        this.addNewMemberActivity = addNewMemberActivity;
        this.usersArrayList = usersArrayList;
        this.group = group;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_new_member_list_item, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        Users users = usersArrayList.get(position);

        if (TextUtils.isEmpty(usersArrayList.get(position).getImage())) {
            holder.group_personImage.setImageResource(R.drawable.person_placeholder);
        } else {
            Picasso.get().load(usersArrayList.get(position).getImage()).into(holder.group_personImage);
        }

        holder.group_personName.setText(usersArrayList.get(position).getFullName());

        boolean isMatch = false;

        for (Contacts contact : contactsArrayList) {
            if (contact.getName().equals(users.getFullName())) {
                isMatch = true;
                break;
            }
        }

        holder.checkBox.setChecked(isMatch);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox.setChecked(!holder.checkBox.isChecked());
            }
        });

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ((AddNewMemberActivity) addNewMemberActivity).addContactToGroup(addNewMemberActivity, usersArrayList.get(position).contactId, group.groupId);
            } else {
//                Dialog dialog = new Dialog(addNewMemberActivity);
//                if (dialog.getWindow() != null) {
//                    dialog.getWindow().setGravity(Gravity.CENTER);
//                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                    dialog.setCancelable(false);
//                }
//                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                dialog.setContentView(R.layout.dailog_layout);
//                dialog.setCancelable(false);
//                dialog.show();
//
//                Button cancel1 = dialog.findViewById(R.id.canceldialog);
//                Button movetobin = dialog.findViewById(R.id.movetobin);
//
//                cancel1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        holder.checkBox.setChecked(true);
//                        dialog.dismiss();
//                    }
//                });
//
//                movetobin.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        ((AddNewMemberActivity) addNewMemberActivity).removeContactFromGroup(usersArrayList.get(position).contactId, group.getGroupId(),position);
//                        dialog.dismiss();
//                    }
//                });
//                Toast.makeText(addNewMemberActivity, "isremove", Toast.LENGTH_SHORT).show();
                ((AddNewMemberActivity) addNewMemberActivity).removeContactFromGroup(usersArrayList.get(position).contactId, group.groupId, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView group_personName;
        ImageView group_personImage, notcheckbox, checkbox;
        CheckBox checkBox;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            group_personImage = itemView.findViewById(R.id.group_personImage);
            group_personName = itemView.findViewById(R.id.group_personName);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
