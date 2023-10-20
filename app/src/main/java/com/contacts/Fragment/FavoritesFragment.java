package com.contacts.Fragment;

import static com.contacts.Class.Constant.favoriteList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.Adapter.FavListAdapter;
import com.contacts.Activity.HomeActivity;
import com.contacts.Adapter.ItemTouchHelperCallback;
import com.contacts.Model.Users;
import com.contacts.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {

    FavListAdapter favListAdapter;
    RecyclerView recyclerView;
    FloatingActionButton add_fav_contact;
    LinearLayout no_fav_found_linear;
    TextView done;
    ImageView back;
    Button addfav;
    ImageView edit;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        init(view);

        checkPermission();

        if (favoriteList.isEmpty()) {
            no_fav_found_linear.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            edit.setVisibility(View.INVISIBLE);
            add_fav_contact.setVisibility(View.INVISIBLE);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

        addfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactsFragment contactsFragment = new ContactsFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.framelayout, contactsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        add_fav_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment mFragment = new ContactsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("btn","fav");
                mFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.framelayout, mFragment)
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        });

//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                edit.setVisibility(View.GONE);
//                done.setVisibility(View.VISIBLE);
//                scrollcontact.setVisibility(View.VISIBLE);
//                info_icon.setVisibility(View.GONE);
//            }
//        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDestroyView();
            }
        });
        return view;
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);
        } else {
            readFavoriteContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readFavoriteContacts();
            } else {
                Toast.makeText(getContext(), "Permission denied. Cannot read contacts.", Toast.LENGTH_SHORT).show();
                checkPermission();
            }
        }
    }

    private void readFavoriteContacts() {

        favoriteList = new ArrayList<>();

        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        ContentResolver contentResolver = getContext().getContentResolver();

        String[] projection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_URI
        };

        String selection = ContactsContract.Contacts.STARRED + "=?";
        String[] selectionArgs = {"1"};

        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                @SuppressLint("Range") String contactImageUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                String phoneNumber = getPhoneNumber(contactId);

                Bitmap contactImage = null;
                if (contactImageUri != null) {
                    try {
                        contactImage = BitmapFactory.decodeStream(contentResolver.openInputStream(Uri.parse(contactImageUri)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                String firstName = "";
                String lastName = "";
                if (!TextUtils.isEmpty(contactName)) {
                    if (contactName.contains(" ")) {
                        String currentString = contactName;
                        String[] separated = currentString.split(" ");
                        firstName = separated[0];
                        lastName = separated[1];
                    } else {
                        firstName = contactName;
                        lastName = "";
                    }
                }

                Users user = new Users(contactId, contactImageUri, firstName, lastName, phoneNumber, "");
                favoriteList.add(user);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        favListAdapter = new FavListAdapter(FavoritesFragment.this, favoriteList);
        recyclerView.setLayoutManager(manager);

//        favoriteList = favListAdapter.loadListFromSharedPreferences(getContext());

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(favListAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(favListAdapter);

        favListAdapter.notifyDataSetChanged();
    }

    @SuppressLint("Range")
    private String getPhoneNumber(String contactId) {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
        String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
        String[] selectionArgs = {contactId};

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, selection, selectionArgs, null);

        String phoneNumber = null;
        if (cursor != null && cursor.moveToNext()) {
            phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        if (cursor != null) {
            cursor.close();
        }
        return phoneNumber;
    }

    private void init(View view) {
        back = view.findViewById(R.id.back);
        addfav = view.findViewById(R.id.create_fav);
        edit = view.findViewById(R.id.fav_edit);
        done = view.findViewById(R.id.fav_done);
        recyclerView = view.findViewById(R.id.recyclerView);
        no_fav_found_linear = view.findViewById(R.id.no_fav_found_linear);
        add_fav_contact = view.findViewById(R.id.add_fav_contact);
    }
}