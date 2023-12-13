package com.contacts.Activity;

import static com.contacts.Class.Constant.contactsArrayList;
import static com.contacts.Class.Constant.usersArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Adapter.AddNewMemberAdapter;
import com.contacts.Model.Contacts;
import com.contacts.Model.Group;
import com.contacts.Model.Users;
import com.contacts.R;

public class AddNewMemberActivity extends AppCompatActivity {

    ImageView back;
    TextView save_group_member;
    AddNewMemberAdapter addNewMemberAdapter;
    RecyclerView recyclerView;
    Users users;
    Group group;
    Contacts contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_member);
        getSupportActionBar().hide();
        Window window = AddNewMemberActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(AddNewMemberActivity.this, R.color.white));

        init();

        users = new Users();

        group = (Group) getIntent().getSerializableExtra("group");
        contacts = (Contacts) getIntent().getSerializableExtra("contacts");

        getContactList();

        save_group_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void isChecked(boolean isChecked,int position,String contactId, String groupId){
        if (isChecked){
            addContactToGroup(this,contactId,groupId);
        }
        else {
            removeContactFromGroup(contactId,groupId,position);
        }
    }

    public void addContactToGroup(Context context, String contactId, String groupId) {

        ContentResolver resolver = context.getContentResolver();

        ContentValues values = new ContentValues();
        values.put(ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID, contactId);
        values.put(ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE, ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID, groupId);

        resolver.insert(ContactsContract.Data.CONTENT_URI, values);

        contacts = new Contacts(contactId, "", (users.getFirst() + users.getLast()), "");
        contactsArrayList.add(contacts);
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

        if (position >= 0 && position < contactsArrayList.size()) {
            contactsArrayList.remove(position);
        } else {
            Log.e("YourTag", "Invalid position provided: " + position);
        }
    }

//    @SuppressLint("NotifyDataSetChanged")
//    private void getContactList() {
//        usersArrayList = new ArrayList<>();
//
//        ContentResolver contentResolver = getContentResolver();
//        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//
//        if (cursor != null && cursor.getCount() > 0) {
//            while (cursor.moveToNext()) {
//                @SuppressLint("Range") String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//                @SuppressLint("Range") String phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                @SuppressLint("Range") String photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
//
//                String firstName = "";
//                String lastName = "";
//                if (!TextUtils.isEmpty(phoneName)) {
//                    if (phoneName.contains(" ")) {
//                        String currentString = phoneName;
//                        String[] separated = currentString.split(" ");
//                        firstName = separated[0];
//                        lastName = separated[1];
//                    } else {
//                        firstName = phoneName;
//                        lastName = "";
//                    }
//                }
//
//                // Get phone numbers
//                List<String> phoneNumbers = getPhoneNumbers(contentResolver, contactId);
//                String phoneNumber = "";
//                String officeNumber = "";
//                if (phoneNumbers.size() > 0) {
//                    if (phoneNumbers.size() > 2) {
//                        phoneNumber = phoneNumbers.get(0).replaceAll(" ", "").trim();
//                        officeNumber = phoneNumbers.get(1).replaceAll(" ", "").trim();
//                    } else {
//                        phoneNumber = phoneNumbers.get(0).replaceAll(" ", "").trim();
//                        officeNumber = "";
//                    }
//                }
//
//                users = new Users(contactId, photoUri, phoneName, lastName, phoneNumber, officeNumber);
//                usersArrayList.add(users);
//
////                Comparator<Users> nameComparator = Comparator.comparing(Users::getFirst);
////                usersArrayList.sort(nameComparator);
//
//            }
//            cursor.close();
//
//            LinearLayoutManager manager = new LinearLayoutManager(AddNewMemberActivity.this, LinearLayoutManager.VERTICAL, false);
//            addNewMemberAdapter = new AddNewMemberAdapter(AddNewMemberActivity.this, usersArrayList,group);
//            recyclerView.setLayoutManager(manager);
//            recyclerView.setAdapter(addNewMemberAdapter);
//        }
//    }

    private void getContactList() {
        LinearLayoutManager manager = new LinearLayoutManager(AddNewMemberActivity.this, LinearLayoutManager.VERTICAL, false);
        addNewMemberAdapter = new AddNewMemberAdapter(AddNewMemberActivity.this, usersArrayList,group);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(addNewMemberAdapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("group", group);
        intent.putExtra("contacts", contacts);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void init() {
        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.add_new_member_recyclerView);
        save_group_member = findViewById(R.id.save_group_member);
    }
}