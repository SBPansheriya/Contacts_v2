package com.contacts.Activity;

import static androidx.core.content.PermissionChecker.checkSelfPermission;
import static com.contacts.Class.Constant.favoriteList;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.Adapter.ContactsDetailsAdapter;
import com.contacts.Adapter.WhatsAppAdapter;
import com.contacts.Model.Users;

import com.contacts.Model.Phone;
import com.contacts.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ContactDetailActivity extends AppCompatActivity {

    ImageView edit, call, messenger, favourites, unfavourites, selected_person_image, back;
    LinearLayout whatsapp, office_contact_details_linear;
    TextView selected_person_name, message_whatsapp;
    int favorite;
    Users user;
    Phone phone;
    RecyclerView recyclerView;
    RecyclerView whatsapprecyclerView;
    ActivityResultLauncher<Intent> launchSomeActivity;
    ContactsDetailsAdapter contactsDetailsAdapter;
    WhatsAppAdapter whatsAppAdapter;
    String selectedPhoneNUmber;
    ArrayList<Phone> phoneArrayList1;
    String listget;
    Gson gson;

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
        listget = getIntent().getStringExtra("phone");
        gson = new Gson();

        phoneArrayList1 = gson.fromJson(listget, new TypeToken<ArrayList<Phone>>() {
        }.getType());
        setData();

        LinearLayoutManager manager = new LinearLayoutManager(ContactDetailActivity.this);
        contactsDetailsAdapter = new ContactsDetailsAdapter(ContactDetailActivity.this, phoneArrayList1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(contactsDetailsAdapter);

        LinearLayoutManager manager1 = new LinearLayoutManager(ContactDetailActivity.this);
        whatsAppAdapter = new WhatsAppAdapter(ContactDetailActivity.this, phoneArrayList1);
        whatsapprecyclerView.setLayoutManager(manager1);
        whatsapprecyclerView.setAdapter(whatsAppAdapter);

        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    user = (Users) result.getData().getSerializableExtra("user");
                    listget = getIntent().getStringExtra("phone");
                    gson = new Gson();
                    phoneArrayList1 = gson.fromJson(listget, new TypeToken<ArrayList<Phone>>() {
                    }.getType());
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
                Gson gson = new Gson();
                String list = gson.toJson(phoneArrayList1);
                intent.putExtra("phone", list);
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

//        whatsapp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String contact = user.personPhone; // use country code with your phone number
//                String url = "https://api.whatsapp.com/send?phone=" + contact;
//                try {
//                    Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse(url));
//                    startActivity(i);
//                } catch (ActivityNotFoundException e) {
//                    Toast.makeText(ContactDetailActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
//            }
//        });

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
        selected_person_name.setText(user.getFullName());
        if (user.image == null) {
            selected_person_image.setImageResource(R.drawable.person_placeholder);
        } else {
            Picasso.get().load(user.image).into(selected_person_image);
        }
//        message_whatsapp.setText("");
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
            if (!TextUtils.isEmpty(selectedPhoneNUmber)) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + selectedPhoneNUmber));
                startActivity(intent);
            }
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

    public void call() {
        int position = 0;
        if (phoneArrayList1 != null && position >= 0 && position < phoneArrayList1.size()) {
            String firstPhoneNumber = phoneArrayList1.get(position).getPhonenumber();
            // Now you have the first phone number
            Log.d("PhoneList", "First phone number: " + firstPhoneNumber);
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + firstPhoneNumber));
            startActivity(intent);
        } else {
            Log.e("PhoneList", "Invalid position or empty list");
        }


    }

    public void getCall(String phoneNumber) {
        selectedPhoneNUmber = phoneNumber;
        String[] permissions = new String[]{Manifest.permission.CALL_PHONE};
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permissions, 100);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        }
    }

    public void addToFavorites(Context context, String contactId, int favorite) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        Uri rawContactUri = Uri.withAppendedPath(ContactsContract.RawContacts.CONTENT_URI, contactId);
        values.put(ContactsContract.CommonDataKinds.Phone.STARRED, favorite); // 1 for favorite, 0 for not favorite
        contentResolver.update(rawContactUri, values, null, null);

        user = new Users(contactId, user.image, (user.first + user.last), user.first, user.last, null, user.personPhone, "");
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
        message_whatsapp = findViewById(R.id.message_whatsapp);
        office_contact_details_linear = findViewById(R.id.office_contact_details_linear);
        recyclerView = findViewById(R.id.contacts_details_recyclerview);
        whatsapprecyclerView = findViewById(R.id.whatsapp_recyclerview);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("user", user);
        setResult(RESULT_OK, intent);
        finish();
    }
}