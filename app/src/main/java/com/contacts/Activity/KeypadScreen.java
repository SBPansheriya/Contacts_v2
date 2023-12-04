package com.contacts.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Adapter.KeypadListAdapter;
import com.contacts.Class.Constant;
import com.contacts.Model.Users;
import com.contacts.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.Serializable;
import java.util.ArrayList;

public class KeypadScreen extends AppCompatActivity implements Serializable {

    public static KeypadListAdapter keypadListAdapter;
    public static RecyclerView recyclerView;
    String selectedPhoneNUmber;
    ImageView open_keypad;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_keypad);
        getSupportActionBar().hide();
        Window window = KeypadScreen.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(KeypadScreen.this, R.color.white));

        init();

        LinearLayoutManager manager = new LinearLayoutManager(KeypadScreen.this);
        keypadListAdapter = new KeypadListAdapter(KeypadScreen.this, Constant.usersArrayList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(keypadListAdapter);

        showBottomSheetDialog();

        open_keypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);

        bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        ImageView btn_1 = bottomSheetDialog.findViewById(R.id.btn_1);
        ImageView btn_2 = bottomSheetDialog.findViewById(R.id.btn_2);
        ImageView btn_3 = bottomSheetDialog.findViewById(R.id.btn_3);
        ImageView btn_4 = bottomSheetDialog.findViewById(R.id.btn_4);
        ImageView btn_5 = bottomSheetDialog.findViewById(R.id.btn_5);
        ImageView btn_6 = bottomSheetDialog.findViewById(R.id.btn_6);
        ImageView btn_7 = bottomSheetDialog.findViewById(R.id.btn_7);
        ImageView btn_8 = bottomSheetDialog.findViewById(R.id.btn_8);
        ImageView btn_9 = bottomSheetDialog.findViewById(R.id.btn_9);
        ImageView btn_0 = bottomSheetDialog.findViewById(R.id.btn_0);
        ImageView btn_hash = bottomSheetDialog.findViewById(R.id.btn_hash);
        ImageView btn_star = bottomSheetDialog.findViewById(R.id.btn_star);
        ImageView btn_call = bottomSheetDialog.findViewById(R.id.btn_call);
        ImageView btn_backpress = bottomSheetDialog.findViewById(R.id.btn_backpress);
        editText = bottomSheetDialog.findViewById(R.id.dailer_show);
        TextView add_contact_by_keypad = bottomSheetDialog.findViewById(R.id.add_contact_by_keypad);
        final String[] s = new String[1];

        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s[0] = number + "0";
                editText.setText(s[0]);
            }
        });

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s[0] = number + "1";
                editText.setText(s[0]);
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s[0] = number + "2";
                editText.setText(s[0]);
            }
        });

        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s[0] = number + "3";
                editText.setText(s[0]);
            }
        });

        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s[0] = number + "4";
                editText.setText(s[0]);
            }
        });

        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s[0] = number + "5";
                editText.setText(s[0]);
            }
        });

        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s[0] = number + "6";
                editText.setText(s[0]);
            }
        });

        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s[0] = number + "7";
                editText.setText(s[0]);
            }
        });

        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s[0] = number + "8";
                editText.setText(s[0]);
            }
        });

        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s[0] = number + "9";
                editText.setText(s[0]);
            }
        });

        btn_hash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s[0] = number + "#";
                editText.setText(s[0]);
            }
        });

        btn_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s[0] = number + "*";
                editText.setText(s[0]);
            }
        });

        btn_backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.length() > 0) {
                    String t = s[0].substring(0, editText.length() - 1);
                    editText.setText("" + t);
                } else {
                    editText.setText("");
                }
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] permissions = new String[]{Manifest.permission.CALL_PHONE};

                if (ContextCompat.checkSelfPermission(KeypadScreen.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(KeypadScreen.this, permissions, 100);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + editText.getText().toString()));
                    startActivity(intent);
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    recyclerView.setVisibility(View.GONE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                ArrayList<Users> filteredList = new ArrayList<>();

                for (int i = 0; i < Constant.usersArrayList.size(); i++) {
                    final String number = Constant.usersArrayList.get(i).getPersonPhone().toLowerCase();
                    String numericPhoneNumber = number.replaceAll("[^0-9]", "");
                    if (numericPhoneNumber.contains(query)) {
                        filteredList.add(Constant.usersArrayList.get(i));
                    }
                }
                if (filteredList.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    keypadListAdapter.setFilteredList(filteredList);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });

        add_contact_by_keypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KeypadScreen.this, CreateContactActivity.class);
                intent.putExtra("number", editText.getText().toString());
                startActivity(intent);
            }
        });

        bottomSheetDialog.setOnDismissListener(dialogInterface -> {
            if (recyclerView.getVisibility() == View.VISIBLE) {
                bottomSheetDialog.dismiss();
            } else {
                onBackPressed();
            }
        });
        bottomSheetDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private void init() {
        recyclerView = findViewById(R.id.keypadrecyclerview);
        open_keypad = findViewById(R.id.open_keypad);
    }

    public void call(String phoneNumber) {
        selectedPhoneNUmber = phoneNumber;
        String[] permissions = new String[]{Manifest.permission.CALL_PHONE};
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permissions, 100);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        }
    }

    private void checkPermissions() {
        String[] permissions = new String[]{Manifest.permission.CALL_PHONE};

        if (ContextCompat.checkSelfPermission(KeypadScreen.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(KeypadScreen.this, permissions, 123);
        } else {
            call(selectedPhoneNUmber);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 123 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            call(selectedPhoneNUmber);
        } else if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + editText.getText().toString()));
            startActivity(intent);
        } else {
            showPermissionDialog();
        }
    }

    private void showPermissionDialog() {
        Dialog dialog = new Dialog(this);
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
        ImageView dismiss_dialog = dialog.findViewById(R.id.dismiss_dialog);
        dismiss_dialog.setVisibility(View.VISIBLE);
        TextView textView = dialog.findViewById(R.id.txt1);

        textView.setText("This app needs Call phone permissions to use this feature. You can grant them in app settings.");

        dismiss_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        gotosettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE) ||
                        shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                    checkPermissions();
                } else {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 123);
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 123) {
                checkPermissions();
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}

