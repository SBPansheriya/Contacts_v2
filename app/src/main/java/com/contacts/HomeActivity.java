package com.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.contacts.Fragment.ContactsFragment;
import com.contacts.Fragment.FavoritesFragment;
import com.contacts.Fragment.GroupsFragment;
import com.contacts.Fragment.KeypadFragment;
import com.contacts.Fragment.RecentsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        bottomNavigationView.setSelectedItemId(R.id.fav);
        bottomNavigationView.setOnItemSelectedListener(this);

    }
    FavoritesFragment  favoritesFragment = new FavoritesFragment();
    RecentsFragment recentsFragment = new RecentsFragment();
    ContactsFragment contactsFragment = new ContactsFragment();
    GroupsFragment groupsFragment = new GroupsFragment();
    KeypadFragment keypadFragment = new KeypadFragment();

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
//            Intent intent = new Intent(Intent.ACTION_DIAL);
//            startActivity(intent);
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