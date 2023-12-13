package com.contacts.Fragment;

import static android.content.ContentValues.TAG;

import static com.contacts.Class.Constant.contactsArrayList;
import static com.contacts.Class.Constant.groupArrayList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.contacts.Adapter.GroupDetailAdapter;
import com.contacts.Adapter.GroupListAdapter;
import com.contacts.Model.Contacts;
import com.contacts.Model.Group;
import com.contacts.Activity.GroupDetailActivity;
import com.contacts.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class GroupsFragment extends Fragment {

    ImageView create_group;
    Button create_new_group;
    LinearLayout no_group_found_linear;
    String[] permissions;
    RecyclerView recyclerView;
    GroupListAdapter groupListAdapter;
    Group group;
    Contacts contacts;
    ActivityResultLauncher<Intent> launchSomeActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);

        init(view);

        checkpermission();

        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    group = (Group) result.getData().getSerializableExtra("group");
                    if (group != null) {
                        getContactGroups(getContext());
                    }
                }
            }
            getContactGroups(getContext());
        });

        create_new_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(GroupsFragment.this.getContext());
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(false);
                }
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.setContentView(R.layout.create_group_dailog_layout);
                dialog.setCancelable(false);
                dialog.show();

                EditText groupName = dialog.findViewById(R.id.add_groupName);
                Button cancel1 = dialog.findViewById(R.id.cancel);
                Button create = dialog.findViewById(R.id.create);

                cancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String groupname = groupName.getText().toString();
                        addGroup(getContext(), groupname);
                        dialog.dismiss();
                    }
                });
            }
        });

        create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getContext());
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(false);
                }
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.setContentView(R.layout.create_group_dailog_layout);
                dialog.setCancelable(false);
                dialog.show();

                EditText groupName = dialog.findViewById(R.id.add_groupName);
                Button cancel1 = dialog.findViewById(R.id.cancel);
                Button create = dialog.findViewById(R.id.create);

                cancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String groupname = groupName.getText().toString();
                        if (TextUtils.isEmpty(groupname)){
                            Toast.makeText(getContext(), "Group name is not empty", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            addGroup(getContext(), groupname);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        return view;
    }

    public void checkpermission() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            getContactGroups(getContext());
        } else {
            permissions = new String[]{Manifest.permission.READ_CONTACTS};
            ActivityCompat.requestPermissions(getActivity(), permissions, 123);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123 && grantResults.length > 0) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                getContactGroups(getContext());
            } else {
                checkpermission();
            }
        } else {
            checkpermission();
        }
    }

    public void addGroup(Context context, String groupName) {
        try {
            ContentResolver contentResolver = context.getContentResolver();

            // Create a new group
            ContentValues contentValues = new ContentValues();
            contentValues.put(ContactsContract.Groups.TITLE, groupName);
            Uri groupUri = contentResolver.insert(ContactsContract.Groups.CONTENT_URI, contentValues);

            // Display a success message
            if (groupUri != null) {
                Toast.makeText(context, "Group added successfully", Toast.LENGTH_SHORT).show();
                Group group = new Group("", groupName, 0);
                groupArrayList.add(group);

            } else {
                Toast.makeText(context, "Failed to add group", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error adding group: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NewApi")
    public void getContactGroups(Context context) {

        groupArrayList = new ArrayList<>();

        ContentResolver contentResolver = context.getContentResolver();

        // Fetching groups
        Cursor groupCursor = contentResolver.query(ContactsContract.Groups.CONTENT_URI, null, null, null, null);

        HashSet<String> uniqueGroupNames = new HashSet<>();

        if (groupCursor != null && groupCursor.getCount() > 0) {
            while (groupCursor.moveToNext()) {
                @SuppressLint("Range") String groupName = groupCursor.getString(groupCursor.getColumnIndex(ContactsContract.Groups.TITLE));
                @SuppressLint("Range") String groupId = groupCursor.getString(groupCursor.getColumnIndex(ContactsContract.Groups._ID));

                int member = getMemberCountForGroup(getContext(), groupId);
                if (uniqueGroupNames.add(groupName) && member >= 0) {
                    Log.d(TAG, "Group Name: " + groupName + ", Group ID: " + groupId);
                    if (groupName.contains("My Contacts") || groupName.contains("Starred in Android")) {
                        groupArrayList.remove(groupName);
                    } else {
                        group = new Group(groupId, groupName, member);
                        groupArrayList.add(group);
                    }
                }
                else {
                    group = new Group(groupId, groupName, member);
                    groupArrayList.remove(group);
                }
            }
            groupCursor.close();

            if(groupArrayList.isEmpty()){
                no_group_found_linear.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                create_group.setVisibility(View.GONE);
            }
            else {
                no_group_found_linear.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                create_group.setVisibility(View.VISIBLE);

                Comparator<Group> nameComparator = Comparator.comparing(Group::getGroupName);
                groupArrayList.sort(nameComparator);

                LinearLayoutManager manager = new LinearLayoutManager(GroupsFragment.this.getContext(), LinearLayoutManager.VERTICAL, false);
                groupListAdapter = new GroupListAdapter(GroupsFragment.this, groupArrayList);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(groupListAdapter);
            }
        } else {
            Log.d(TAG, "No groups found");
        }
    }

    public int getMemberCountForGroup(Context context, String groupId) {
        int memberCount = 0;

        // Define the columns you want to retrieve
        String[] projection = {ContactsContract.CommonDataKinds.Phone.CONTACT_ID};

        // Specify the group id for the selection
        String selection = ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + " = ?";
        String[] selectionArgs = new String[]{groupId};

        // Query the ContactsContract for contacts in the specified group
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        if (cursor != null) {
            try {
                memberCount = cursor.getCount();
            } finally {
                cursor.close();
            }
        }
        return memberCount;
    }

    public void groupIntentPass(Group group) {
        Intent intent = new Intent(getContext(), GroupDetailActivity.class);
        intent.putExtra("group", group);
        launchSomeActivity.launch(intent);
    }

    private void init(View view) {
        create_group = view.findViewById(R.id.create_group);
        create_new_group = view.findViewById(R.id.create_new_group);
        recyclerView = view.findViewById(R.id.group_recyclerView);
        no_group_found_linear = view.findViewById(R.id.no_group_found_linear);
    }
}