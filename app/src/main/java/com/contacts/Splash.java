package com.contacts;

import static com.contacts.Class.Constant.favoriteList;
import static com.contacts.Class.Constant.recentArrayList;
import static com.contacts.Class.Constant.usersArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.contacts.Activity.CreateContactActivity;
import com.contacts.Activity.HomeActivity;
import com.contacts.Model.Recent;
import com.contacts.Model.Users;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Splash extends AppCompatActivity {

    String[] permissions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Window window = Splash.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(Splash.this, R.color.white));

        checkPermissions();
    }

    private void checkPermissions() {
       permissions = new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CONTACTS};

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(Splash.this, permissions, 123);
        } else {
            getContactList();
            getRecentContacts();
            readFavoriteContacts();
            navigateToHomeActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == 123 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getContactList();
            getRecentContacts();
            readFavoriteContacts();
            navigateToHomeActivity();
        }
        else {
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
        }, 500);
    }

    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Required");
        builder.setMessage("This app requires access to contacts and call logs to function properly.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.READ_CALL_LOG) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)){
                    checkPermissions();
                } else {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent,123);
                    Toast.makeText(Splash.this, "Setting", Toast.LENGTH_SHORT).show();
                }

                dialog.cancel();
            }
        });

        builder.setCancelable(true);
        builder.show();
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
        dialog.setCancelable(false);
        dialog.show();

        Button gotosettings = dialog.findViewById(R.id.gotosettings);

        gotosettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.READ_CALL_LOG) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)){
                    checkPermissions();
                } else {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent,123);
                    Toast.makeText(Splash.this, "Setting", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 123) {
                checkPermissions();
            }
        } catch (Exception ex) {
            Toast.makeText(Splash.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }

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

    private void readFavoriteContacts() {

        favoriteList = new ArrayList<>();

        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        ContentResolver contentResolver = getContentResolver();

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
        String[] projection = {
                ContactsContract.Contacts._ID,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls.CACHED_PHOTO_URI
        };

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

    private Bitmap getContactPhoto(String contactId) {
//        Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactId));
//        InputStream photoInputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), contactUri);
//
//        if (photoInputStream != null) {
//            return BitmapFactory.decodeStream(photoInputStream);
//        } else {
//            return BitmapFactory.decodeResource(getResources(), R.drawable.person_placeholder);
//        }

        Uri contactUri = null;
        if (usersArrayList.size() > 0) {
            for (int i = 0; i < usersArrayList.size(); i++) {

                if (usersArrayList.get(i).contactId.equalsIgnoreCase(contactId)) {
                    contactUri = Uri.parse(usersArrayList.get(i).getImage());
                    break;
                }
            }


            if (contactUri != null) {
                InputStream photoInputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), contactUri);

                if (photoInputStream != null) {
                    return BitmapFactory.decodeStream(photoInputStream);
                } else {
                    return BitmapFactory.decodeResource(getResources(), R.drawable.person_placeholder);
                }
            } else {
                return BitmapFactory.decodeResource(getResources(), R.drawable.person_placeholder);
            }
        } else {
            return BitmapFactory.decodeResource(getResources(), R.drawable.person_placeholder);
        }

    }
}