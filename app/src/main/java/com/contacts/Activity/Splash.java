package com.contacts.Activity;

import static com.contacts.Class.Constant.favoriteList;
import static com.contacts.Class.Constant.recentArrayList;
import static com.contacts.Class.Constant.usersArrayList;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.contacts.Model.Recent;
import com.contacts.Model.Users;
import com.contacts.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Splash extends AppCompatActivity {

    String[] permissions;
    public static String PREFS_NAME = "MyPrefsFile";
    public static String KEY = "IsSet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        Window window = Splash.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(Splash.this, R.color.white));

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(KEY, true);
        editor.commit();

        checkPermissions();
    }

    @SuppressLint("StaticFieldLeak")
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            navigateToHomeActivity();
            readFavoriteContacts();
            getContactList();
            getRecentContacts();
        } else {
            permissions = new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS};
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED)
                permissions = new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS};
            else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED)
                permissions = new String[]{Manifest.permission.READ_CALL_LOG};
            else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED)
                permissions = new String[]{Manifest.permission.READ_CONTACTS};
            ActivityCompat.requestPermissions(Splash.this, permissions, 123);
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123 && grantResults.length > 0) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                navigateToHomeActivity();
                readFavoriteContacts();
                getContactList();
                getRecentContacts();
            } else {
                dialog();
            }
        } else {
            dialog();
        }
    }

    private void navigateToHomeActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }, 400);
    }

    private void dialog() {
        Dialog dialog = new Dialog(Splash.this);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(false);
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_permission);

        TextView textView = dialog.findViewById(R.id.txt1);
        Button gotosettings = dialog.findViewById(R.id.gotosettings);

        textView.setText("This app needs Call logs and Contacts permissions to use this feature. You can grant them in app settings.");

        gotosettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CALL_LOG) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                    checkPermissions();
                } else {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    someActivityResultLauncher.launch(intent);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent data = result.getData();
                    if (ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        readFavoriteContacts();
                        getRecentContacts();
                        navigateToHomeActivity();
                    }
                    else {
                        dialog();
                    }
                }
            });

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        try {
//            super.onActivityResult(requestCode, resultCode, data);
//            if (requestCode == 123) {
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED &&
//                        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
//                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//                    readFavoriteContacts();
//                    getRecentContacts();
//                    navigateToHomeActivity();
//                } else {
//                    dialog();
//                }
//            }
//        } catch (Exception ex) {
//            Toast.makeText(Splash.this, ex.toString(), Toast.LENGTH_SHORT).show();
//        }
//    }

    @SuppressLint("NotifyDataSetChanged")
    private void getContactList() {
        usersArrayList = new ArrayList<>();

        ContentResolver contentResolver = getContentResolver();
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
                        phoneNumber = phoneNumbers.get(0).replaceAll(" ", "").trim();
                        officeNumber = phoneNumbers.get(1).replaceAll(" ", "").trim();
                    } else {
                        phoneNumber = phoneNumbers.get(0).replaceAll(" ", "").trim();
                        officeNumber = "";
                    }
                }

                // Create a User object with the retrieved data and add it to the ArrayList
                Users user = new Users(contactId, photoUri, phoneName,firstName, lastName, phoneNumber, officeNumber);
                usersArrayList.add(user);

                Comparator<Users> nameComparator = Comparator.comparing(Users::getFirst);
                usersArrayList.sort(nameComparator);
            }
            cursor.close();
        }
    }

    private List<String> getPhoneNumbers(ContentResolver contentResolver, String contactId) {
        List<String> phoneNumbers = new ArrayList<>();

        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] phoneProjection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
        String phoneSelection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        Cursor phoneCursor = contentResolver.query(phoneUri, phoneProjection, phoneSelection, new String[]{contactId}, null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                @SuppressLint("Range") String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).trim();
                phoneNumbers.add(phoneNumber);
            }
            phoneCursor.close();
        }
        return phoneNumbers;
    }

    private void readFavoriteContacts() {

        favoriteList = new ArrayList<>();

        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        ContentResolver contentResolver = getContentResolver();

        String[] projection = {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_URI};

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

                Users user = new Users(contactId, contactImageUri, contactName ,firstName, lastName, phoneNumber, "");
                favoriteList.add(user);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    @SuppressLint("Range")
    private String getPhoneNumber(String contactId) {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
        String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
        String[] selectionArgs = {contactId};

        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null);

        String phoneNumber = null;
        if (cursor != null && cursor.moveToNext()) {
            phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        if (cursor != null) {
            cursor.close();
        }
        return phoneNumber;
    }

    public void getRecentContacts() {

        recentArrayList = new ArrayList<>();

        ContentResolver contentResolver = getContentResolver();
        String[] projection = {ContactsContract.Contacts._ID, CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DATE, CallLog.Calls.CACHED_PHOTO_URI};

        String sortOrder = CallLog.Calls.DATE + " DESC";
        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, projection, null, null, sortOrder);

        if (cursor != null && cursor.moveToFirst()) {
            int contactColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            int nameColumn = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
            int numberColumn = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            int typeColumn = cursor.getColumnIndex(CallLog.Calls.TYPE);
            int callDate = cursor.getColumnIndex(CallLog.Calls.DATE);
            int image = cursor.getColumnIndex(CallLog.Calls.CACHED_PHOTO_URI);

            do {
                String contactId = cursor.getString(contactColumn);
                String contactName = cursor.getString(nameColumn);
                String contactNumber = cursor.getString(numberColumn);
                String image_str = cursor.getString(image);

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

                String path = "";
                if (TextUtils.isEmpty(image_str)) {
                    path = "";
                } else {
                    path = image_str;
                }

                Recent recent = new Recent(contactId, path, contactName, contactNumber, formattedDate, callType);
                recentArrayList.add(recent);

            } while (cursor.moveToNext());
            cursor.close();
        }
    }

}