package com.contacts.Activity;

import static com.contacts.Class.Constant.favoriteList;
import static com.contacts.Class.Constant.phoneTypeArrayList;
import static com.contacts.Class.Constant.recentArrayList;
import static com.contacts.Class.Constant.typeArrayList;
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

import com.contacts.Class.Constant;
import com.contacts.Model.PhoneType;
import com.contacts.Model.Recent;
import com.contacts.Model.Users;
import com.contacts.Model.Phone;
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

        getAllPhoneNumberLabelsAndTypes(this);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(KEY, true);
        editor.commit();

        checkPermissions();
    }

    @SuppressLint("StaticFieldLeak")
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            navigateToHomeActivity();
            readFavoriteContacts();
            getContactList();
            getRecentContacts();
        } else {
            permissions = new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS};
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED)
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
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
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
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CALL_LOG) && shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
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

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            if (ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                readFavoriteContacts();
                getRecentContacts();
                navigateToHomeActivity();
            } else {
                dialog();
            }
        }
    });

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

                phoneTypeArrayList = getAllPhoneNumbers(this, contactId);

                Users user = new Users(contactId, photoUri, phoneName, firstName, lastName, phoneTypeArrayList, "", "");
                usersArrayList.add(user);
                Comparator<Users> nameComparator = Comparator.comparing(Users::getFirst);
                usersArrayList.sort(nameComparator);
            }
            cursor.close();
        }
    }

    public static String mapTypeToCustomLabel(int phoneType) {

        typeArrayList = new ArrayList<>();

        typeArrayList.add(new PhoneType(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, "Mobile"));
        typeArrayList.add(new PhoneType(ContactsContract.CommonDataKinds.Phone.TYPE_HOME, "Home"));
        typeArrayList.add(new PhoneType(ContactsContract.CommonDataKinds.Phone.TYPE_WORK, "Work"));
        typeArrayList.add(new PhoneType(ContactsContract.CommonDataKinds.Phone.TYPE_MAIN, "Main"));
        typeArrayList.add(new PhoneType(ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK, "Work Fax"));
        typeArrayList.add(new PhoneType(ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME, "Home Fax"));
        typeArrayList.add(new PhoneType(ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE, "Work Mobile"));
        typeArrayList.add(new PhoneType(ContactsContract.CommonDataKinds.Phone.TYPE_PAGER, "Pager"));
        typeArrayList.add(new PhoneType(-1, "Other"));

        switch (phoneType) {
            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                return "Mobile";
            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                return "Home";
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                return "Work";
            case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                return "Main";
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                return "Work Fax";
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                return "Home Fax";
            case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                return "Pager";
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                return "Work Mobile";
            default:
                return "Other";
        }
    }

    public ArrayList<Phone> getAllPhoneNumbers(Context context, String contactId) {

        ContentResolver contentResolver = context.getContentResolver();

        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.LABEL};

        String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        String[] selectionArgs = {contactId};

        String sortOrder = null;

        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, selection, selectionArgs, sortOrder);

        phoneTypeArrayList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                @SuppressLint("Range") int phoneType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                @SuppressLint("Range") int phoneLabelResId = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));

                CharSequence phoneLabel = ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.getResources(), phoneType, null);

                String type = mapTypeToCustomLabel(phoneType);

                Phone phone = new Phone(phoneNumber, phoneType, type);
                phoneTypeArrayList.add(phone);

            } while (cursor.moveToNext());

            cursor.close();
        }
        return phoneTypeArrayList;
    }

    public static void getAllPhoneNumberLabelsAndTypes(Context context) {
        // Define the columns you want to retrieve from the contacts database
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.LABEL
        };

        // Set up the query
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;

        // Execute the query
        try (Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
        )) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Retrieve the phone number, type, and label
                    @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    @SuppressLint("Range") int phoneType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                    @SuppressLint("Range") String phoneLabel = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));

                    // Map the phone type to a string (you can customize this mapping as needed)
//                    String phoneTypeString = getPhoneNumberTypeString(phoneType);

                    // Do something with the phone number, type, and label
//                    String message = "Phone Number: " + phoneNumber + "\nType: " + phoneTypeString + "\nLabel: " + phoneLabel;
//                    showToast(context, message);

                } while (cursor.moveToNext());
            } else {
                // Handle case where no phone numbers are found
//                showToast(context, "No phone numbers found in contacts.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
//                String phoneNumber = getPhoneNumber(contactId);

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

                phoneTypeArrayList = getAllPhoneNumbers(this, contactId);

                Users user = new Users(contactId, contactImageUri, contactName, firstName, lastName, phoneTypeArrayList, "", "");
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