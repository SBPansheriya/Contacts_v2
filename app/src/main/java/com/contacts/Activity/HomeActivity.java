package com.contacts.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;


import com.contacts.Fragment.FavoritesFragment;
import com.contacts.Fragment.GroupsFragment;
import com.contacts.Fragment.NewRecyclerviewFragment;
import com.contacts.Fragment.NewContactsFragment;
import com.contacts.Fragment.RecentsFragment;
import com.contacts.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    public boolean isstep = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        Window window = HomeActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(HomeActivity.this, R.color.white));
        init();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.fav) {
                    openFragment(new FavoritesFragment());
                    return true;
                }
                if (item.getItemId() == R.id.recents) {
                    openFragment(new RecentsFragment());
                    return true;
                }

                if (item.getItemId() == R.id.contacts) {
                    openFragment(new NewRecyclerviewFragment());
                    return true;
                }
                if (item.getItemId() == R.id.group) {
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

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.framelayout);
        if (fragment instanceof FavoritesFragment) {
            super.onBackPressed();
        } else {
            if (isstep) {
                super.onBackPressed();
            }
            else {
                Fragment mFragment = new FavoritesFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, mFragment).setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
            }
        }
    }

    private void init() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }
}