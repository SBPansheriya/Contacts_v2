package com.contacts.Activity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Adapter.KeypadListAdapter;
import com.contacts.Class.Constant;
import com.contacts.Fragment.FavoritesFragment;
import com.contacts.Model.Users;
import com.contacts.MyBottomSheetDialog;
import com.contacts.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class KeypadScreen extends AppCompatActivity {

    public static KeypadListAdapter keypadListAdapter;
    public static RecyclerView recyclerView;
    String s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_keypad);
        Window window = KeypadScreen.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(KeypadScreen.this, R.color.white));

        init();

        String button = getIntent().getStringExtra("check");

        LinearLayoutManager manager = new LinearLayoutManager(KeypadScreen.this);
        keypadListAdapter = new KeypadListAdapter(KeypadScreen.this, Constant.usersArrayList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(keypadListAdapter);
        keypadListAdapter.notifyDataSetChanged();

        BottomSheetDialogFragment bottomSheetDialog = new MyBottomSheetDialog(button);
        bottomSheetDialog.getShowsDialog();
        bottomSheetDialog.show(getSupportFragmentManager(), bottomSheetDialog.getTag());

    }

    private void init() {
        recyclerView = findViewById(R.id.keypadrecyclerview);
    }
}

