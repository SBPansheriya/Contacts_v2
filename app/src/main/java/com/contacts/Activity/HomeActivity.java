package com.contacts.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.framelayout, new FavoritesFragment());
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        if (item.getItemId() == R.id.recents) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.framelayout, new RecentsFragment());
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        if (item.getItemId() == R.id.contact) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.framelayout, contactsFragment);
            Bundle bundle = new Bundle();
            bundle.putString("btn","contact");
            contactsFragment.setArguments(bundle);
            transaction.addToBackStack(null);
            transaction.commit();

            return true;
        }
        if (item.getItemId() == R.id.group) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.framelayout, new GroupsFragment());
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        if (item.getItemId() == R.id.keypad) {
            BottomSheetDialogFragment bottomSheetDialog = new MyBottomSheetDialog();
            bottomSheetDialog.getShowsDialog();
            bottomSheetDialog.show(getSupportFragmentManager(), bottomSheetDialog.getTag());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.framelayout, new KeypadFragment());
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }

        return  true;
    }
    private void  init(){
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }
}