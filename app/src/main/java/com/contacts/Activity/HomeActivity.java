package com.contacts.Activity;

import static com.contacts.Class.Constant.favoriteList;
import static com.contacts.Class.Constant.recentArrayList;
import static com.contacts.Class.Constant.usersArrayList;
import static com.contacts.Activity.Splash.KEY;
import static com.contacts.Activity.Splash.PREFS_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.contacts.Class.App;
import com.contacts.Fragment.FavoritesFragment;
import com.contacts.Fragment.GroupsFragment;
import com.contacts.Fragment.NewRecyclerviewFragment;
import com.contacts.Fragment.RecentsFragment;
import com.contacts.Class.GetModelClass;
import com.contacts.Model.Recent;
import com.contacts.Model.Users;
import com.contacts.R;
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
    String fragment = "";
    GetModelClass getModelClass;
    public static boolean isGetData = false;
    public boolean isstep = false;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        Window window = HomeActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(HomeActivity.this, R.color.white));
        init();

        App.context = this;

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        bottomNavigationView.setSelectedItemId(R.id.fav);
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

        boolean isSet = sharedPreferences.getBoolean(KEY, false);

        if (isSet) {
            fragment = "fav";
            editor.putBoolean(KEY, false);
            editor.apply();
            openFragment(new FavoritesFragment());
        }

        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected String doInBackground(Void... voids) {
//                getRecentContacts();
                return "Success";
            }

            @Override
            protected void onPostExecute(String result) {
                isGetData = true;
                getModelClass.NewClass();
            }
        }.execute();
        getModelClass = new GetModelClass(this, this);
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framelayout, fragment)
                .commit();
    }

    private void init() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    @Override
    public void getResult() {
        if (fragment.equals("recents")) {
            openFragment(new RecentsFragment());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRecentContacts();
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
}
