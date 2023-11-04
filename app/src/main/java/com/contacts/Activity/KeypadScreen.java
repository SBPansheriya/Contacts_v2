package com.contacts.Activity;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

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
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Adapter.KeypadListAdapter;
import com.contacts.Class.Constant;
import com.contacts.Fragment.FavoritesFragment;
import com.contacts.Fragment.RecentsFragment;
import com.contacts.Model.Users;
import com.contacts.MyBottomSheetDialog;
import com.contacts.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class KeypadScreen extends AppCompatActivity {

    public static KeypadListAdapter keypadListAdapter;
    public static RecyclerView recyclerView;
    String selectedPhoneNUmber;
    ImageView open_keypad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_keypad);
        getSupportActionBar().hide();
        Window window = KeypadScreen.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(KeypadScreen.this, R.color.white));

        init();

        String button = getIntent().getStringExtra("check");

        LinearLayoutManager manager = new LinearLayoutManager(KeypadScreen.this);
        keypadListAdapter = new KeypadListAdapter(KeypadScreen.this, Constant.usersArrayList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(keypadListAdapter);
        keypadListAdapter.notifyDataSetChanged();

        BottomSheetDialogFragment bottomSheetDialog = new MyBottomSheetDialog();
        bottomSheetDialog.getShowsDialog();
        bottomSheetDialog.show(getSupportFragmentManager(), bottomSheetDialog.getTag());

        open_keypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialogFragment bottomSheetDialog = new MyBottomSheetDialog();
                bottomSheetDialog.getShowsDialog();
                bottomSheetDialog.show(getSupportFragmentManager(), bottomSheetDialog.getTag());
            }
        });

    }

    private void init() {
        recyclerView = findViewById(R.id.keypadrecyclerview);
        open_keypad = findViewById(R.id.open_keypad);
    }

    public boolean checkPermission1() {
        String[] permissions = new String[]{Manifest.permission.CALL_PHONE};

//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(getActivity(), permissions, 100);
//        } else {
//            getRecentContacts();
//        }

        return ContextCompat.checkSelfPermission(KeypadScreen.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (!TextUtils.isEmpty(selectedPhoneNUmber)) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + selectedPhoneNUmber));
                startActivity(intent);
            }
        } else {
            showPermissionDialog();
        }
    }

    private void showPermissionDialog() {
        Dialog dialog = new Dialog(KeypadScreen.this);
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
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 100);
                    Toast.makeText(KeypadScreen.this, "Setting", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 100) {
                checkPermission1();
            }
        } catch (Exception ex) {
            Toast.makeText(KeypadScreen.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void call(String phoneNumber) {
        selectedPhoneNUmber = phoneNumber;
        String[] permissions = new String[]{Manifest.permission.CALL_PHONE};
        if (checkSelfPermission( Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permissions, 100);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        }

    }
}

