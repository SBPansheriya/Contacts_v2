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

//               String image = getContactImage(getContext(),contcatId);

                Log.d("AAA", "Name: " + contactName + ", Number: " + contactNumber + ", Date: " + formattedDate + ", Type: " + callType);
                Recent recent = new Recent(contcatId, "", contactName, contactNumber, formattedDate, callType);
                recentArrayList.add(recent);

            } while (cursor.moveToNext());
            cursor.close();
        }
        recentListAdapter = new RecentListAdapter(getContext(), recentArrayList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(recentListAdapter);
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