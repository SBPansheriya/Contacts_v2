package com.contacts;

import static com.contacts.Class.Constant.contactsArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Activity.AddFavouritesActivity;

import java.util.ArrayList;
import java.util.HashSet;

public class GroupDetailActivity extends AppCompatActivity {

    ImageView add_group_contacts, back;
    Button add_new_group_contacts;
    LinearLayout no_group_contacts_found_linear;
    RecyclerView recyclerView;
    GroupDetailAdapter groupDetailAdapter;
    Group group = new Group();
    Contacts contacts;
    ActivityResultLauncher<Intent> launchSomeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        getSupportActionBar().hide();
        Window window = GroupDetailActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(GroupDetailActivity.this, R.color.white));

        init();

        group = (Group) getIntent().getSerializableExtra("group");

        getContacts(group.groupId);

        if (contactsArrayList.size() > 0) {
            no_group_contacts_found_linear.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            add_group_contacts.setVisibility(View.VISIBLE);
        } else {
            no_group_contacts_found_linear.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            add_group_contacts.setVisibility(View.GONE);
        }

        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    group = (Group) result.getData().getSerializableExtra("group");
                    contacts = (Contacts) result.getData().getSerializableExtra("contacts");
                    if (contacts != null) {
                        getContacts(group.groupId);
                    }
                }
            }
            getContacts(group.groupId);
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        add_group_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupDetailActivity.this, AddNewMemberActivity.class);
                intent.putExtra("group", group);
                intent.putExtra("contacts", contacts);
                launchSomeActivity.launch(intent);
            }
        });

        add_new_group_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupDetailActivity.this, AddNewMemberActivity.class);
                intent.putExtra("group", group);
                intent.putExtra("contacts", contacts);
                launchSomeActivity.launch(intent);
            }
        });
    }

    public void getContacts(String id) {

        contactsArrayList = new ArrayList<>();

        String groupId = id;
        String[] cProjection = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI
        };

        Cursor groupCursor = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                cProjection,
                ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "= ?" + " AND "
                        + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "='"
                        + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'",
                new String[]{String.valueOf(groupId)}, null);

        HashSet<String> uniqueGroupNames = new HashSet<>();

        if (groupCursor != null && groupCursor.moveToFirst()) {
            do {
                int nameCoumnIndex = groupCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int photoUriColumnIndex = groupCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);

                String name = groupCursor.getString(nameCoumnIndex);
                String photoUri = groupCursor.getString(photoUriColumnIndex);

                @SuppressLint("Range") String contactId = groupCursor.getString(groupCursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));

                Cursor numberCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);


                if (numberCursor.moveToFirst()) {
                    int numberColumnIndex = numberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    do {
                        String phoneNumber = numberCursor.getString(numberColumnIndex);
                        Log.d("your tag", "contact " + name + ":" + phoneNumber);
                        if (uniqueGroupNames.add(name)) {
                            contacts = new Contacts(contactId, photoUri, name, phoneNumber);
                            contactsArrayList.add(contacts);
                        }
                    } while (numberCursor.moveToNext());
                    numberCursor.close();
                }
            } while (groupCursor.moveToNext());
            groupCursor.close();

            if (contactsArrayList.size() > 0) {
                no_group_contacts_found_linear.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                add_group_contacts.setVisibility(View.VISIBLE);
                LinearLayoutManager manager = new LinearLayoutManager(GroupDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                groupDetailAdapter = new GroupDetailAdapter(GroupDetailActivity.this, contactsArrayList,group);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(groupDetailAdapter);
            } else {
                no_group_contacts_found_linear.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                add_group_contacts.setVisibility(View.GONE);
            }
        }
    }

    public void removeContactFromGroup(String contactId, String groupId,int position) {

            ContentResolver contentResolver = getContentResolver();

            Uri uri = ContactsContract.Data.CONTENT_URI;
            String selection = ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "=? AND " +
                    ContactsContract.Data.RAW_CONTACT_ID + "=? AND " +
                    ContactsContract.Data.MIMETYPE + "=?";
            String[] selectionArgs = new String[]{String.valueOf(groupId), String.valueOf(contactId),
                    ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE};

            contentResolver.delete(uri, selection, selectionArgs);

            contactsArrayList.remove(position);
            groupDetailAdapter.updateList(contactsArrayList);
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("group", group);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void init() {
        add_group_contacts = findViewById(R.id.add_group_contacts);
        add_new_group_contacts = findViewById(R.id.add_new_group_contacts);
        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.group_detail_recyclerView);
        no_group_contacts_found_linear = findViewById(R.id.no_group_contacts_found_linear);
    }
}
