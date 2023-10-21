package com.contacts.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
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
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.contacts.Adapter.RecentListAdapter;
import com.contacts.Activity.HomeActivity;
import com.contacts.Class.Constant;
import com.contacts.Model.Recent;
import com.contacts.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecentsFragment extends Fragment {

    RecentListAdapter recentListAdapter;
    RecyclerView recyclerView;
    ImageView back;
    LinearLayout no_recents_linear;
    ArrayList<Recent> recentArrayList = new ArrayList<>();
    int position;

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

        recentArrayList = new ArrayList<>();

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
                String contactId = cursor.getString(contactColumn);
                String contactName = cursor.getString(nameColumn);
                String contactNumber = cursor.getString(numberColumn);
                int contactType = cursor.getInt(typeColumn);
                @SuppressLint("Range") long contactDate = cursor.getLong(callDate);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
                String formattedDate = dateFormat.format(new Date(contactDate));

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

//                cursorLog.moveToFirst();
//                String number = cursorLog.getString(cursorLog.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
//                contactId = getContactIdFromNumber(contactNumber);
//                Contacts.People.loadContactPhoto(getContext(),
//                        ContentUris.withAppendedId(Contacts.People.CONTENT_URI, Long.parseLong((contactId))),0,null);

//                String image = getContactImage(getContext(),contactId);

//                Bitmap image = loadContactPhoto(getContext(),contactNumber);

                Log.d("AAA", "ID: " +contactId +", Image: " + "" + ", Name: " + contactName + ", Number: " + contactNumber + ", Date: " + formattedDate + ", Type: " + callType);
                Recent recent = new Recent(contactId, "", contactName, contactNumber, formattedDate, callType);
                recentArrayList.add(recent);
            } while (cursor.moveToNext());
            cursor.close();
        }
        recentListAdapter = new RecentListAdapter(getContext(), recentArrayList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(recentListAdapter);
    }

//    public static Bitmap loadContactPhoto(Context context, String contactNumber) {
//        long contactId = getContactIdFromNumber(context, contactNumber);
//
//        if (contactId != -1) {
//            Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
//            Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
//
//            try (Cursor cursor = context.getContentResolver().query(photoUri, new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null)) {
//                if (cursor != null && cursor.moveToFirst()) {
//                    byte[] data = cursor.getBlob(0);
//                    if (data != null) {
//                        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), data);
//                    }
//                }
//            } catch (Exception e) {
//                Log.e("ContactImageLoader", "Error loading contact photo: " + e.getMessage());
//            }
//        }
//
//        return null;
//    }

    private static long getContactIdFromNumber(Context context, String contactNumber) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(contactNumber));

        try (Cursor cursor = context.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup._ID}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumn = cursor.getColumnIndex(ContactsContract.PhoneLookup._ID);
                return cursor.getLong(idColumn);
            }
        }

        return -1;
    }

    private String getContactIdFromNumber(String number) {
        String[] projection = new String[]{Contacts.Phones._ID};
        Uri contactUri = Uri.withAppendedPath(Contacts.Phones.CONTENT_FILTER_URL,
                Uri.encode(number));
        Cursor c = getContext().getContentResolver().query(contactUri, projection,
                null, null, null);
        if (c.moveToFirst()) {
            @SuppressLint("Range") String contactId=c.getString(c.getColumnIndex(Contacts.Phones._ID));
            return contactId;
        }
        return null;
    }


    private static String getContactImage(Context context, String contactId) {
        // Query the contact image using the contact ID
        String contactImage = null;
        Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor photoCursor = context.getContentResolver().query(photoUri, new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);

        if (photoCursor != null && photoCursor.moveToFirst()) {
            byte[] photoData = photoCursor.getBlob(0);
            contactImage = getBase64Image(photoData);
            photoCursor.close();
        }

        return contactImage;
    }

    private static String getBase64Image(byte[] photoData) {
        if (photoData != null) {
            return android.util.Base64.encodeToString(photoData, android.util.Base64.DEFAULT);
        }
        return null;
    }


//    public String getContactImage(Context context, String contactId) {
//        String imageBase64 = null;
//
//        Cursor cursor = context.getContentResolver().query(
//                ContactsContract.Contacts.CONTENT_URI,
//                null,
//                ContactsContract.Contacts._ID + " = ?",
//                new String[] { contactId },
//                null
//        );
//
//        if (cursor != null && cursor.moveToFirst()) {
//            @SuppressLint("Range") byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO));
//            if (imageBytes != null) {
//                Bitmap contactImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//
//                if (contactImage != null) {
//                    // Convert the Bitmap to a Base64 encoded string
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    contactImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                    byte[] byteArray = baos.toByteArray();
//                    imageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
//                }
//            }
//
//            cursor.close();
//        }
//
//        return imageBase64;
//    }

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