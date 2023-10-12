package com.contacts.Fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.Adapter.HeaderListAdapter;
import com.contacts.Activity.CreateContactActivity;
import com.contacts.Model.Header;
import com.contacts.Model.Users;
import com.contacts.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactsFragment extends Fragment {

    HeaderListAdapter headerListAdapter;
    RecyclerView recyclerView;
    ImageView edit, cancel, share, delete;
    Button create_btn;
    TextView selectall;
    FloatingActionButton floatingActionButton;
    ViewGroup viewGroup;
    Context context;
    ArrayList<Users> usersArrayList = new ArrayList<>();
    ArrayList<Object> items = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        init(view);

        checkPermission();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateContactActivity.class);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.setVisibility(View.GONE);
                selectall.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                share.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateContactActivity.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(ContactsFragment.this.getContext());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dailog_layout);
                dialog.setCancelable(false);
                dialog.show();

                Button cancel = dialog.findViewById(R.id.canceldialog);
                Button movetobin = dialog.findViewById(R.id.movetobin);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), getActivity().getClass());
                        startActivity(intent);
                    }
                });

                movetobin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        });

        return view;
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);
        } else {
            getContactList();
        }
    }

    private void getContactList() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;

        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, sort);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts._ID
                ));

                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                ));

                Uri phoneuri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
                Cursor phonecursor = getContext().getContentResolver().query(
                        phoneuri, null, selection, new String[]{id}, null
                );

                if (phonecursor.moveToNext()) {
                    @SuppressLint("Range") String number = phonecursor.getString(phonecursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    ));

                    Users users = new Users(name, number);
                    usersArrayList.add(users);
                    phonecursor.close();
                }
            }
            cursor.close();
        }

        items = getList();

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        headerListAdapter = new HeaderListAdapter(ContactsFragment.this, items);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(headerListAdapter);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getContactList();
        } else {
            Toast.makeText(context, "Permission Denied.", Toast.LENGTH_SHORT).show();
            checkPermission();
        }
    }

    private ArrayList<Object> getList() {
        ArrayList<Object> data = new ArrayList<>();
        Header header = new Header("A",usersArrayList);
        data.add(header);
//        data.add(usersArrayList);
        return data;
    }

//    private void getHeaderListLatter(ArrayList<Header> usersList) {
//
//        Collections.sort(usersList, new Comparator<Header>() {
//            int position = 0;
//
//            @Override
//            public int compare (Header user1, Header user2) {
//                return String.valueOf(user1.header.charAt(0)).toUpperCase().compareTo(String.valueOf(user2.getUsersList().get(position).getFirst().charAt(0)).toUpperCase());
//            }
//        });
//
//        String lastHeader = "";
//
//        int size = usersList.size();
//
//        for (int i = 0; i < size; i++) {
//
//            Header header1 = usersList.get(i);
//            String header = String.valueOf(header1.header.charAt(0)).toUpperCase();
//
//            if (!TextUtils.equals(lastHeader, header)) {
//                lastHeader = header;
//                mSectionList.add(new Header(header,true,usersArrayList));
//            }
//
//            mSectionList.add(header1);
//        }
//    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.show_contact_recyclerview);
        floatingActionButton = view.findViewById(R.id.add_contact);
        edit = view.findViewById(R.id.edit);
        cancel = view.findViewById(R.id.cancel);
        share = view.findViewById(R.id.share);
        delete = view.findViewById(R.id.trash);
        selectall = view.findViewById(R.id.selectall);
        create_btn = view.findViewById(R.id.create_contact);
        viewGroup = view.findViewById(android.R.id.content);
    }
}