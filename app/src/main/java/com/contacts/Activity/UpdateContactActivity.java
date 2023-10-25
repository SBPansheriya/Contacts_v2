package com.contacts.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.Class.Constant;
import com.contacts.Fragment.ContactsFragment;
import com.contacts.Model.Users;
import com.contacts.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class UpdateContactActivity extends AppCompatActivity {

    ImageView cancel;
    TextView show_person_name;
    EditText update_firstname, update_lastname, update_pphone, update_ophone;
    ImageView personimage;
    String imagename, imagepath;
    Uri newUri;
    Button update_contact;
    private static final int CAMERA_REQUEST = 100;
    Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);
        Window window = UpdateContactActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(UpdateContactActivity.this, R.color.white));
        init();

        user = (Users) getIntent().getSerializableExtra("user");

        if (user.image == null) {
            personimage.setImageResource(R.drawable.person_placeholder);
        } else {
            Picasso.get().load(user.image).into(personimage);
        }

        ActivityCompat.requestPermissions(UpdateContactActivity.this, new String[]{Manifest.permission.WRITE_CONTACTS}, 100);

        update_firstname.setText(user.first);
        update_lastname.setText(user.last);
        update_pphone.setText(user.personPhone);
        update_ophone.setText(user.officePhone);
        show_person_name.setText(user.first + " " + user.last);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        personimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, CAMERA_REQUEST);
            }
        });

        update_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagepath = imagepath + "/" + imagename;
                String firstname = update_firstname.getText().toString();
                String lastname = update_lastname.getText().toString();
                String pphone = update_pphone.getText().toString();
                String ophone = update_ophone.getText().toString();

                getUpdateContactList(user.contactId, firstname, lastname, pphone, ophone, newUri);
            }
        });
    }

    public void getUpdateContactList(String contactId, String newFirstName, String newLastName, String newPersonalPhoneNumber, String newOfficePhoneNumber, Uri newImage) {

        ContentResolver contentResolver = getContentResolver();

        // Update the first phone number
        ContentValues phoneValues1 = new ContentValues();
        phoneValues1.put(ContactsContract.CommonDataKinds.Phone.NUMBER, newPersonalPhoneNumber);

        String phoneSelection1 = ContactsContract.Data.CONTACT_ID + " = ? AND " +
                ContactsContract.Data.MIMETYPE + " = ? AND " +
                ContactsContract.CommonDataKinds.Phone.TYPE + " = ?";

        String[] phoneSelectionArgs1 = new String[]{contactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)};

        contentResolver.update(ContactsContract.Data.CONTENT_URI, phoneValues1, phoneSelection1, phoneSelectionArgs1);

        // Update the second phone number
        ContentValues phoneValues2 = new ContentValues();
        phoneValues2.put(ContactsContract.CommonDataKinds.Phone.NUMBER, newOfficePhoneNumber);

        String phoneSelection2 = ContactsContract.Data.CONTACT_ID + " = ? AND " +
                ContactsContract.Data.MIMETYPE + " = ? AND " +
                ContactsContract.CommonDataKinds.Phone.TYPE + " = ?";

        String[] phoneSelectionArgs2 = new String[]{contactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_WORK)};

        contentResolver.update(ContactsContract.Data.CONTENT_URI, phoneValues2, phoneSelection2, phoneSelectionArgs2);


        // update name
        ContentResolver contentResolver1 = getContentResolver();
        ContentValues contactNameValues = new ContentValues();
        contactNameValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, newFirstName);
        contactNameValues.put(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, newLastName);

        String selection1 = ContactsContract.Data.CONTACT_ID + " = ? AND " +
                ContactsContract.Data.MIMETYPE + " = ?";

        String[] selectionArgs1 = new String[]{contactId, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
        contentResolver1.update(ContactsContract.Data.CONTENT_URI, contactNameValues, selection1, selectionArgs1);


        // update photo
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);

        if (newImage != null) {
            if (user.image != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), newImage);
                    ByteArrayOutputStream image = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, image);

                    builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);
                    builder.withSelection(ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?", new String[]{String.valueOf(contactId),
                            ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE});
                    builder.withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, image.toByteArray());
                    ops.add(builder.build());
                    getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    ContentValues values = new ContentValues();
                    ContentResolver contentResolver3 = getContentResolver();

                    Bitmap bitmap = null;

                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), newImage);
                    ByteArrayOutputStream image = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, image);
                    values.put(ContactsContract.Data.RAW_CONTACT_ID, contactId);
                    values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
                    values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, image.toByteArray());
                    contentResolver3.insert(ContactsContract.Data.CONTENT_URI, values);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            user.image = newImage.toString();
        } else {
            user.image = null;
        }
        user.first = newFirstName;
        user.last = newLastName;
        user.personPhone = newPersonalPhoneNumber;
        user.officePhone = "";

        for (int i = 0; i < Constant.usersArrayList.size(); i++) {

            if (Constant.usersArrayList.get(i).contactId.equalsIgnoreCase(contactId)) {
                Constant.usersArrayList.remove(i);
                Constant.usersArrayList.add(i,user);
                break;
            }
        }

        Toast.makeText(UpdateContactActivity.this, "Contact saved", Toast.LENGTH_SHORT).show();

        onBackPressed();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
            checkPermission();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            personimage.setImageBitmap(bitmap);
            newUri = saveToInternalStorage(bitmap);
            imagepath = newUri.getPath();
        }
    }

    private Uri saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        imagename = "img" + new Random().nextInt(100000) + ".png";
        File mypath = new File(directory, imagename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Uri.fromFile(mypath);
    }

    private void init() {
        cancel = findViewById(R.id.update_cancel);
        personimage = findViewById(R.id.update_image);
        update_firstname = findViewById(R.id.update_firstname);
        update_lastname = findViewById(R.id.update_lastname);
        update_pphone = findViewById(R.id.update_pphone);
        update_ophone = findViewById(R.id.update_ophone);
        update_contact = findViewById(R.id.update_contact);
        show_person_name = findViewById(R.id.show_personName);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("user", user);
        setResult(RESULT_OK, intent);
        finish();
    }
}