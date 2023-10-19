package com.contacts.Fragment;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.contacts.Class.Constant.usersArrayList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
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
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.Adapter.ContactListAdapter;
import com.contacts.Adapter.HeaderListAdapter;
import com.contacts.Activity.CreateContactActivity;
import com.contacts.Model.Header;
import com.contacts.Model.Users;
import com.contacts.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ContactsFragment extends Fragment {

    HeaderListAdapter headerListAdapter;
    RecyclerView recyclerView;
    LinearLayout no_contcat_found_linear, Contact_found_linear;
    ImageView edit, cancel, share, delete;
    Button create_btn;
    SearchView searchView;
    TextView selectall, totalcontact, deselectall;
    FloatingActionButton floatingActionButton;
    ViewGroup viewGroup;
    Context context;
    SpinKitView spin_kit;
    RelativeLayout progressLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        init(view);
        searchView.clearFocus();
        progressLayout.setVisibility(View.VISIBLE);
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

        if (usersArrayList.isEmpty()) {
            no_contcat_found_linear.setVisibility(View.VISIBLE);
            Contact_found_linear.setVisibility(View.INVISIBLE);
        }

        if (usersArrayList.isEmpty()) {
            totalcontact.setText("0 Conatcts");
        } else {
            totalcontact.setText(usersArrayList.size() + " " + "Contacts");
        }

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
                        dialog.dismiss();
                    }
                });

                movetobin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteSelectedItems();

                        dialog.dismiss();
                    }
                });
            }
        });

        selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAllItems();
                selectall.setVisibility(View.GONE);
                deselectall.setVisibility(View.VISIBLE);
            }
        });

        deselectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllItems();
                selectall.setVisibility(View.VISIBLE);
                deselectall.setVisibility(View.GONE);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareSelected();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        return view;
    }

    private void filterList(String text) {
        ArrayList<Users> filteredList = new ArrayList<>();
        for (Users users : usersArrayList) {
            String name = users.getFirst() + " " + users.getLast();
            if (users.getFirst().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(users);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            headerListAdapter.setFilteredList(filteredList);
        }
    }

    private void deleteSelectedItems() {
        List<Users> itemsToRemove = new ArrayList<>();
        for (Users users : usersArrayList) {
            if (users.isSelected()) {
                itemsToRemove.add(users);
                deleteContact(getContext(), users.contactId);

            }
        }
        usersArrayList.removeAll(itemsToRemove);
        headerListAdapter.notifyDataSetChanged();
    }

    private void selectAllItems() {
        for (Users item : usersArrayList) {
            item.setSelected(true);
        }
        headerListAdapter.notifyDataSetChanged();
    }

    private void shareSelected() {
        Uri shareUri = null;
        for (Users users : usersArrayList) {
            if (users.isSelected()) {
                String lookupKey = getLookupKey(users.contactId);
                if (TextUtils.isEmpty(lookupKey)) {
                    Log.e(TAG, "couldn't get lookupKey");
                    return;
                }
                shareUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
//                contactInfo  = "Name: " + users.getFirst() + " " + users.getLast() + "\n" +
//                        "Phone: " + users.getPersonPhone();
            }
        }
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setType("text/plain");
//        shareIntent.putExtra(Intent.EXTRA_TEXT, contactInfo);
//        startActivity(shareIntent);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(ContactsContract.Contacts.CONTENT_VCARD_TYPE);
        intent.putExtra(Intent.EXTRA_STREAM, shareUri);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share a contact");
        startActivity(intent);
    }

    private String getLookupKey(String contactId) {
        String lookupKey = null;
        String[] projection = new String[]{ContactsContract.Contacts.LOOKUP_KEY};
        String selection = ContactsContract.Contacts._ID + "=" + contactId;
        Cursor c = getContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projection, selection, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                lookupKey = c.getString(0);
            }
            c.close();
        }
        return lookupKey;
    }

    private void deselectAllItems() {
        for (Users item : usersArrayList) {
            item.setSelected(false);
        }
        headerListAdapter.notifyDataSetChanged();
    }

    public static void deleteContact(Context context, String contactId) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));

        // First, delete the contact's data entries
        contentResolver.delete(ContactsContract.RawContacts.CONTENT_URI,
                ContactsContract.RawContacts.CONTACT_ID + " = ?",
                new String[]{String.valueOf(contactId)});

        // Then, delete the contact itself
        contentResolver.delete(contactUri, null, null);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getContactList() {

        usersArrayList = new ArrayList<>();

        Sprite threeBounce = new ThreeBounce();
        spin_kit.setIndeterminateDrawable(threeBounce);

        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                @SuppressLint("Range") String phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                @SuppressLint("Range") String photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));

                String firstName = "";
                String lastName = "";
                if (!TextUtils.isEmpty(phoneName)) {
                    if (phoneName.contains(" ")) {
                        String currentString = phoneName;
                        String[] separated = currentString.split(" ");
                        firstName = separated[0];
                        lastName = separated[1];
                    } else {
                        firstName = phoneName;
                        lastName = "";
                    }
                }


                // Get phone numbers
                List<String> phoneNumbers = getPhoneNumbers(contentResolver, contactId);
                String phoneNumber = "";
                String officeNumber = "";
                if (phoneNumbers.size() > 0) {
                    if (phoneNumbers.size() > 2) {
                        phoneNumber = phoneNumbers.get(0);
                        officeNumber = phoneNumbers.get(1);
                    } else {
                        phoneNumber = phoneNumbers.get(0);
                        officeNumber = "";
                    }
                }

                // Create a User object with the retrieved data and add it to the ArrayList
                Users user = new Users(contactId, photoUri, firstName, lastName, phoneNumber, officeNumber);
                usersArrayList.add(user);
            }
            cursor.close();
        }

        if (usersArrayList.size() > 0) {
            Comparator<Users> nameComparator = new Comparator<Users>() {
                @Override
                public int compare(Users user1, Users user2) {
                    // Use compareTo() to compare the names in alphabetical order
                    return user1.getFirst().compareTo(user2.getFirst());
                }
            };

            Collections.sort(usersArrayList, nameComparator);
        }

        if (usersArrayList.size() > 0) {
            ArrayList<Header> headerArrayList = new ArrayList<>();

            for (char i = 'A'; i <= 'Z'; i++) {
                Header header = new Header(String.valueOf(i), new ArrayList<>());
                headerArrayList.add(header);
            }
            Header header1 = new Header("#", new ArrayList<>());
            headerArrayList.add(header1);
            for (int i = 0; i < usersArrayList.size(); i++) {
                if (!TextUtils.isEmpty(usersArrayList.get(i).first)) {
                    boolean isMatch = false;
                    for (int i1 = 0; i1 < headerArrayList.size(); i1++) {
                        String header = headerArrayList.get(i1).header;
                        String firstLetter = String.valueOf(usersArrayList.get(i).first.toUpperCase().charAt(0));

                        if (Objects.equals(header, firstLetter)) {
                            isMatch = true;
                            headerArrayList.get(i1).usersList.add(usersArrayList.get(i));
                            break;
                        }
                    }

                    if (!isMatch) {
                        headerArrayList.get(headerArrayList.size() - 1).usersList.add(usersArrayList.get(i));
                    }
                }
            }

            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            headerListAdapter = new HeaderListAdapter(ContactsFragment.this, headerArrayList);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(headerListAdapter);
        }

        headerListAdapter.notifyDataSetChanged();
        progressLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private List<String> getPhoneNumbers(ContentResolver contentResolver, String contactId) {
        List<String> phoneNumbers = new ArrayList<>();

        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] phoneProjection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
        String phoneSelection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        Cursor phoneCursor = contentResolver.query(phoneUri, phoneProjection, phoneSelection, new String[]{contactId}, null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                @SuppressLint("Range") String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneNumbers.add(phoneNumber);
            }
            phoneCursor.close();
        }

        return phoneNumbers;
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);
        } else {
            getContactList();
            progressLayout.setVisibility(View.GONE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getContactList();
            progressLayout.setVisibility(View.GONE);
        } else {
            Toast.makeText(context, "Permission Denied.", Toast.LENGTH_SHORT).show();
            checkPermission();
        }
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.show_contact_recyclerview);
        floatingActionButton = view.findViewById(R.id.add_contact);
        edit = view.findViewById(R.id.edit);
        cancel = view.findViewById(R.id.cancel);
        share = view.findViewById(R.id.share);
        delete = view.findViewById(R.id.trash);
        selectall = view.findViewById(R.id.selectall);
        deselectall = view.findViewById(R.id.deselectall);
        create_btn = view.findViewById(R.id.create_contact);
        viewGroup = view.findViewById(android.R.id.content);
        no_contcat_found_linear = view.findViewById(R.id.no_contcat_found_linear);
        Contact_found_linear = view.findViewById(R.id.Contact_found_linear);
        totalcontact = view.findViewById(R.id.totalcontact);
        progressLayout = view.findViewById(R.id.progressLayout);
        spin_kit = view.findViewById(R.id.spin_kit);
        searchView = view.findViewById(R.id.search_contact);
    }
}