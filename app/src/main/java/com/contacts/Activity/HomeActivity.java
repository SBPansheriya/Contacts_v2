package com.contacts.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RelativeLayout;

import com.contacts.Fragment.ContactsFragment;
import com.contacts.Fragment.FavoritesFragment;
import com.contacts.Fragment.GroupsFragment;
import com.contacts.Fragment.KeypadFragment;
import com.contacts.Fragment.RecentsFragment;
import com.contacts.MyBottomSheetDialog;
import com.contacts.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    FavoritesFragment  favoritesFragment = new FavoritesFragment();
    RecentsFragment recentsFragment = new RecentsFragment();
    ContactsFragment contactsFragment = new ContactsFragment();
    GroupsFragment groupsFragment = new GroupsFragment();
    KeypadFragment keypadFragment = new KeypadFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Window window = HomeActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(HomeActivity.this, R.color.white));
        init();
        bottomNavigationView.setSelectedItemId(R.id.fav);
        bottomNavigationView.setOnItemSelectedListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framelayout, favoritesFragment)
                .commit();
    }

    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.fav) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.framelayout, favoritesFragment)
                    .commit();
            return true;
        }
        if (item.getItemId() == R.id.recents) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.framelayout, recentsFragment)
                    .commit();
            return true;
        }
        if (item.getItemId() == R.id.contact) {
            Bundle bundle = new Bundle();
            bundle.putString("btn","contact");
            contactsFragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.framelayout, contactsFragment)
                    .commit();
            return true;
        }
        if (item.getItemId() == R.id.group) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.framelayout, groupsFragment)
                    .commit();
            return true;
        }
        if (item.getItemId() == R.id.keypad) {
            BottomSheetDialogFragment bottomSheetDialog = new MyBottomSheetDialog();
            bottomSheetDialog.show(getSupportFragmentManager(), bottomSheetDialog.getTag());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.framelayout, keypadFragment)
                    .commit();
            return true;
        }
        return  false;
    }
    private void  init(){
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }
}