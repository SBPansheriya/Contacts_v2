package com.contacts.Activity;

import static com.contacts.Class.Constant.favoriteList;
import static com.contacts.Class.Constant.recentArrayList;
import static com.contacts.Class.Constant.usersArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.content.ContentResolver;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.contacts.Fragment.FavoritesFragment;
import com.contacts.Fragment.GroupsFragment;
import com.contacts.Fragment.NewContactsFragment;
import com.contacts.Fragment.NewRecyclerviewFragment;
import com.contacts.Fragment.RecentsFragment;
import com.contacts.GetModelClass;
import com.contacts.Listner;
import com.contacts.Model.Recent;
import com.contacts.Model.Users;
import com.contacts.R;
import com.contacts.Splash;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements Listner {

    BottomNavigationView bottomNavigationView;
    String fragment;
    GetModelClass getModelClass;
    public static boolean isGetData = false;
    public boolean isstep = false;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        Window window = HomeActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(HomeActivity.this, R.color.white));
        init();


        
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {

            }

            @Override
            protected String doInBackground(Void... voids) {
                getContactList();
                getRecentContacts();
                readFavoriteContacts();
                return "Success";
            }

            @Override
            protected void onPostExecute(String result) {
                isGetData = true;
                getModelClass.NewClass();
            }
        }.execute();

        getModelClass = new GetModelClass(this,this);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.fav) {
                    fragment = "fav";
                    openFragment(new FavoritesFragment());
                    return true;
                }
                if (item.getItemId() == R.id.recents) {
                    fragment = "recents";
                    openFragment(new RecentsFragment());
                    return true;
                }

                if (item.getItemId() == R.id.contacts) {
                    fragment = "contacts";
                    openFragment(new NewRecyclerviewFragment());
                    return true;
                }

                if (item.getItemId() == R.id.group) {
                    fragment = "group";
                    openFragment(new GroupsFragment());
                    return true;
                }
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.fav);
        openFragment(new FavoritesFragment());
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framelayout, fragment)
                .commit();
    }

//    @Override
//    public void onBackPressed() {
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.framelayout);
//        if (fragment instanceof FavoritesFragment) {
//            super.onBackPressed();
//        } else {
//            if (isstep) {
//                super.onBackPressed();
//            }
//            else {
//                Fragment mFragment = new FavoritesFragment();
//                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, mFragment).setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
//            }
//        }
//    }

    private void init() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    @Override
    public void getResult() {
        if (fragment.equals("contacts")) {
            openFragment(new NewRecyclerviewFragment());
        } else if (fragment.equals("fav")) {
            openFragment(new FavoritesFragment());
        }else if (fragment.equals("recents")) {
            openFragment(new RecentsFragment());
        }
    }

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
                Users user = new Users(contactId, photoUri, firstName, lastName, phoneNumber, officeNumber);
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

//                Bitmap path1 = getContactPhoto(contactId);
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                path1.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//                String path2 = MediaStore.Images.Media.insertImage(getContentResolver(), path1, "title", null);

                Recent recent = new Recent(contactId, path, contactName, contactNumber, formattedDate, callType);
                recentArrayList.add(recent);

            } while (cursor.moveToNext());
            cursor.close();
        }
    }

}