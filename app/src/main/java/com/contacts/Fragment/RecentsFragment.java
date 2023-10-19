package com.contacts.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.contacts.Adapter.RecentListAdapter;
import com.contacts.Activity.HomeActivity;
import com.contacts.Model.Recent;
import com.contacts.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecentsFragment extends Fragment {

    RecentListAdapter recentListAdapter;
    RecyclerView recyclerView;
    ImageView back;
    LinearLayout no_recents_linear;
    ArrayList<Recent> recentArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recents, container, false);

        init(view);

        checkPermission();
        checkPermission1();

        if (recentArrayList.isEmpty()) {
            no_recents_linear.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getRecentContacts() {
        ContentResolver contentResolver = getContext().getContentResolver();
        String[] projection = {
                CallLog.Calls._ID,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE
        };

        String sortOrder = CallLog.Calls.DATE + " DESC";
        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, projection, null, null, sortOrder);

        if (cursor != null && cursor.moveToFirst()) {
            int contactColumn = cursor.getColumnIndex(CallLog.Calls._ID);
            int nameColumn = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
            int numberColumn = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            int typeColumn = cursor.getColumnIndex(CallLog.Calls.TYPE);
            int callDate = cursor.getColumnIndex(CallLog.Calls.DATE);

            do {
                String contcatId = cursor.getString(contactColumn);
                String contactName = cursor.getString(nameColumn);
                String contactNumber = cursor.getString(numberColumn);
                int contactType = cursor.getInt(typeColumn);

                CharSequence formattedDate = DateUtils.getRelativeTimeSpanString(callDate, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
                String contactDate = cursor.getString(callDate);

                String callType = "Unknown";
                switch (contactType) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callType = "Incoming";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callType = "Outgoing";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callType = "Missed";
                        break;
                }

               String image = getContactImage();

                Log.d("AAA", "Name: " + contactName + ", Number: " + contactNumber + ", Date: " + formattedDate + ", Type: " + callType);
                Recent recent = new Recent(contcatId, image, contactName, contactNumber, contactDate, callType);
                recentArrayList.add(recent);

            } while (cursor.moveToNext());
            cursor.close();
        }
        recentListAdapter = new RecentListAdapter(getContext(), recentArrayList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(recentListAdapter);
    }

    @SuppressLint("Range")
    public String getContactImage(){

        String imagePath = null;

        ContentResolver contentResolver = getContext().getContentResolver();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA};
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";
        Cursor cursor = contentResolver.query(uri, projection, null, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return imagePath;
    }

    @SuppressLint("Range")
    public static Bitmap getContactImage(Context context, String phoneNumber) {
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        };

        String selection = ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?";
        String[] selectionArgs = {phoneNumber};

        Cursor contactCursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        long contactId = -1;

        if (contactCursor != null && contactCursor.moveToFirst()) {
            contactId = contactCursor.getLong(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            contactCursor.close();
        }

        if (contactId != -1) {
            Bitmap contactImage = loadContactPhoto(context, contactId);
            return contactImage;
        }
        return null;
    }

    public static Bitmap loadContactPhoto(Context context, long contactId) {
        // Load the contact photo using the contact ID
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO},
                ContactsContract.Data.CONTACT_ID + " = ? AND " +
                        ContactsContract.Data.MIMETYPE + " = ?",
                new String[]{String.valueOf(contactId),
                        ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE},
                null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                cursor.close();
                return BitmapFactory.decodeByteArray(data, 0, data.length);
            }
            cursor.close();
        }
        return null;
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CALL_LOG}, 100);
        } else {
            getRecentContacts();
        }
    }

    private void checkPermission1() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 100);
        }
        else {
            getRecentContacts();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getRecentContacts();
        } else {
            Toast.makeText(getContext(), "Permission Denied.", Toast.LENGTH_SHORT).show();
            checkPermission();
            checkPermission1();
        }
    }

    private void init(View view) {
        back = view.findViewById(R.id.back);
        recyclerView = view.findViewById(R.id.recents_recyclerview);
        no_recents_linear = view.findViewById(R.id.no_recents);
    }
}