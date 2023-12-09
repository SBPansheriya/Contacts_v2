package com.contacts.Activity;

import static com.contacts.Class.Constant.favoriteList;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.Model.Users;

import com.contacts.R;
import com.squareup.picasso.Picasso;

public class ContactDetailActivity extends AppCompatActivity {

    ImageView edit, call, messenger, favourites, unfavourites, selected_person_image, back;
    LinearLayout whatsapp, office_contact_details_linear;
    TextView selected_person_name, selected_person_pnum, selected_person_onum, message_whatsapp;
    int favorite;
    Users user;
    ActivityResultLauncher<Intent> launchSomeActivity;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        getSupportActionBar().hide();
        Window window = ContactDetailActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(ContactDetailActivity.this, R.color.white));

        init();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, 100);

        user = (Users) getIntent().getSerializableExtra("user");
        setData();

        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    user = (Users) result.getData().getSerializableExtra("user");
                    if (user != null) {
                        setData();
                    }
                }
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactDetailActivity.this, UpdateContactActivity.class);
                intent.putExtra("user", user);
                launchSomeActivity.launch(intent);
            }
        });

        messenger.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View view) {
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.setData(Uri.parse("sms:" + user.personPhone));
                startActivity(smsIntent);
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact = user.personPhone; // use country code with your phone number
                String url = "https://api.whatsapp.com/send?phone=" + contact;
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ContactDetailActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        if (favoriteList.size() > 0) {
            boolean isMatch = false;
            for (int i = 0; i < favoriteList.size(); i++) {
                if (user.contactId.equals(favoriteList.get(i).contactId)) {
                    isMatch = true;
                    break;
                }
            }
            if (isMatch) {
                favourites.setVisibility(View.VISIBLE);
                unfavourites.setVisibility(View.GONE);
            } else {
                unfavourites.setVisibility(View.VISIBLE);
                favourites.setVisibility(View.GONE);
            }
        } else {
            unfavourites.setVisibility(View.VISIBLE);
            favourites.setVisibility(View.GONE);
        }

        unfavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favourites.setVisibility(View.VISIBLE);
                unfavourites.setVisibility(View.GONE);
                favorite = 1;
                addToFavorites(ContactDetailActivity.this, user.contactId, favorite);
                Toast.makeText(ContactDetailActivity.this, "Add into favourites", Toast.LENGTH_SHORT).show();
            }
        });

        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favourites.setVisibility(View.GONE);
                unfavourites.setVisibility(View.VISIBLE);
                favorite = 0;
                addToFavorites(ContactDetailActivity.this, user.contactId, favorite);
                Toast.makeText(ContactDetailActivity.this, "Remove from favourites", Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setData() {
        selected_person_name.setText(user.first + " " + user.last);
        selected_person_pnum.setText(user.personPhone);
        if (user.image == null) {
            selected_person_image.setImageResource(R.drawable.person_placeholder);
        } else {
            Picasso.get().load(user.image).into(selected_person_image);
        }
        message_whatsapp.setText(user.personPhone);

    }

    private void checkPermissions() {
        String[] permissions = new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE};

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ContactDetailActivity.this, permissions, 123);
        } else {
            call();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 123 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            call();
        } else if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            showPermissionDialog();
        }
    }

    private void showPermissionDialog() {
        Dialog dialog = new Dialog(ContactDetailActivity.this);
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
                    Toast.makeText(ContactDetailActivity.this, "Setting", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 123) {
                checkPermissions();
            }
        } catch (Exception ex) {
            Toast.makeText(ContactDetailActivity.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + user.personPhone));
        startActivity(intent);
    }

    public void addToFavorites(Context context, String contactId, int favorite) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        Uri rawContactUri = Uri.withAppendedPath(ContactsContract.RawContacts.CONTENT_URI, contactId);
        values.put(ContactsContract.CommonDataKinds.Phone.STARRED, favorite); // 1 for favorite, 0 for not favorite
        contentResolver.update(rawContactUri, values, null, null);

        user = new Users(contactId, user.image, (user.first + user.last),user.first, user.last, user.personPhone, "");
        if (favorite == 1) {
            favoriteList.add(user);
        } else {
            for (int i = 0; i < favoriteList.size(); i++) {
                if (favoriteList.get(i).contactId.equalsIgnoreCase(contactId)) {
                    favoriteList.remove(i);
                    break;
                }
            }
        }
    }

    private void init() {
        edit = findViewById(R.id.edit_contact_details);
        call = findViewById(R.id.call);
        messenger = findViewById(R.id.messenger);
        favourites = findViewById(R.id.favourites);
        unfavourites = findViewById(R.id.unfavourites);
        back = findViewById(R.id.back);
        whatsapp = findViewById(R.id.whatsapp);
        selected_person_image = findViewById(R.id.selected_person_image);
        selected_person_name = findViewById(R.id.selected_person_name);
        selected_person_pnum = findViewById(R.id.selected_person_pnum);
        selected_person_onum = findViewById(R.id.selected_person_onum);
        message_whatsapp = findViewById(R.id.message_whatsapp);
        office_contact_details_linear = findViewById(R.id.office_contact_details_linear);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("user", user);
        setResult(RESULT_OK, intent);
        finish();
    }
}