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
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.contacts.Fragment.NewContactsFragment;
import com.contacts.Fragment.RecentsFragment;
import com.contacts.MyBottomSheetDialog;
import com.contacts.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity{

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        Window window = HomeActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(HomeActivity.this, R.color.white));
        init();

//        MenuItem menuItem = menu.findItem(R.id.menu_item_1); // Change to the appropriate item
//        Drawable icon = menuItem.getIcon();
//        icon.setColorFilter(ContextCompat.getColor(this, R.color.selected_icon_color), PorterDuff.Mode.SRC_IN);
//        menuItem.setIcon(icon);

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
                if (item.getItemId() == R.id.contact) {

                    Fragment fragment = new NewContactsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("btn", "contact");
                    fragment.setArguments(bundle);
                    openFragment(fragment);
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

//    @Override
//    public boolean
//    onNavigationItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.fav) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.framelayout, new FavoritesFragment());
//            transaction.addToBackStack(null);
//            transaction.commit();
//            return true;
//        }
//        if (item.getItemId() == R.id.recents) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.framelayout, new RecentsFragment());
//            transaction.addToBackStack(null);
//            transaction.commit();
//            return true;
//        }
//        if (item.getItemId() == R.id.contact) {
//
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.framelayout, contactsFragment);
//            Bundle bundle = new Bundle();
//            bundle.putString("btn", "contact");
//            contactsFragment.setArguments(bundle);
//            transaction.addToBackStack(null);
//            transaction.commit();
//
//            return true;
//        }
//        if (item.getItemId() == R.id.group) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.framelayout, new GroupsFragment());
//            transaction.addToBackStack(null);
//            transaction.commit();
//            return true;
//        }
////        if (item.getItemId() == R.id.keypad) {
////            BottomSheetDialogFragment bottomSheetDialog = new MyBottomSheetDialog();
////            bottomSheetDialog.getShowsDialog();
////            bottomSheetDialog.show(getSupportFragmentManager(), bottomSheetDialog.getTag());
////            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
////            transaction.replace(R.id.framelayout, new KeypadFragment());
////            transaction.addToBackStack(null);
////            transaction.commit();
////            return true;
////        }
//        return true;
//    }

    private void init() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }
}