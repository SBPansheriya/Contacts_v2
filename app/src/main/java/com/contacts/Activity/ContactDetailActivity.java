package com.contacts.Activity;

import static android.content.Context.ACTIVITY_SERVICE;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.Adapter.FavListAdapter;
import com.contacts.Class.Constant;
import com.contacts.Model.Users;
import com.contacts.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class ContactDetailActivity extends AppCompatActivity {

    ImageView edit, call, messenger, favourites, unfavourites, selected_person_image, back;
    LinearLayout whatsapp, office_contact_details_linear;
    TextView selected_person_name, selected_person_pnum, selected_person_onum, message_whatsapp;
    int favorite;
    Users user;
    ArrayList<Users> usersArrayList = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        Window window = ContactDetailActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(ContactDetailActivity.this, R.color.white));

        init();

        checkPermission();

        user = (Users) getIntent().getSerializableExtra("user");

        selected_person_name.setText("" + user.first + " " + "" + user.last);
        selected_person_pnum.setText(user.personPhone);
        if (user.image == null) {
            selected_person_image.setImageResource(R.drawable.person_placeholder);
        } else {
            Picasso.get().load(user.image).into(selected_person_image);
        }
        message_whatsapp.setText(user.personPhone);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + user.personPhone));
//                if (ActivityCompat.checkSelfPermission(ContactDetailActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                    Toast.makeText(ContactDetailActivity.this, "Calling " + user.first + " " + user.last, Toast.LENGTH_SHORT).show();
//                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactDetailActivity.this, UpdateContactActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
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

        if (Constant.favoriteList.size() > 0) {
            boolean isMatch = false;
            for (int i = 0; i < Constant.favoriteList.size(); i++) {
                if (user.contactId.equals(Constant.favoriteList.get(i).contactId)) {
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
                addToFavorites(ContactDetailActivity.this, user.contactId,favorite);
                Toast.makeText(ContactDetailActivity.this, "Add into favourites", Toast.LENGTH_SHORT).show();
            }
        });

        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favourites.setVisibility(View.GONE);
                unfavourites.setVisibility(View.VISIBLE);
                favorite = 0;
                addToFavorites(ContactDetailActivity.this, user.contactId,favorite);
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

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(ContactDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 100);
        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            Toast.makeText(ContactDetailActivity.this, "Permission Denied.", Toast.LENGTH_SHORT).show();
            checkPermission();
        }
    }

    public static void addToFavorites(Context context, String contactId,int favorite) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        Uri rawContactUri = Uri.withAppendedPath(ContactsContract.RawContacts.CONTENT_URI, contactId);
        values.put(ContactsContract.CommonDataKinds.Phone.STARRED, favorite); // 1 for favorite, 0 for not favorite
        contentResolver.update(rawContactUri, values, null, null);
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
}