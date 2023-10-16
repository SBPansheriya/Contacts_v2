package com.contacts.Activity;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.Fragment.ContactsFragment;
import com.contacts.Model.Users;
import com.contacts.R;
import com.squareup.picasso.Picasso;

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
    Button update_contact;
    ArrayList<Users> usersArrayList = new ArrayList<>();
    private static final int CAMERA_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        init();

        String contactId = getIntent().getStringExtra("contactId1");
        String image = getIntent().getStringExtra("image1");
        String firstname = getIntent().getStringExtra("first1");
        String lastname = getIntent().getStringExtra("last1");
        String pphone = getIntent().getStringExtra("pphone1");
        String ophone = getIntent().getStringExtra("ophone1");

        if (image == null){
            personimage.setImageResource(R.drawable.person_placeholder);
        }
        else {
            Picasso.get().load(image).into(personimage);
        }

        update_firstname.setText(firstname);
        update_lastname.setText(lastname);
        update_pphone.setText(pphone);
        update_ophone.setText(ophone);
        show_person_name.setText(firstname + " " + lastname);

        ActivityCompat.requestPermissions(UpdateContactActivity.this, new String[]{Manifest.permission.WRITE_CONTACTS}, PackageManager.PERMISSION_GRANTED);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        personimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, CAMERA_REQUEST);
            }
        });

        update_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagepath = imagepath+"/"+imagename;
                String firstname = update_firstname.getText().toString();
                String lastname = update_lastname.getText().toString();
                String pphone = update_pphone.getText().toString();
                String ophone = update_ophone.getText().toString();

                getUpdateContactList(contactId, firstname,lastname, pphone,ophone,imagepath);

                Toast.makeText(UpdateContactActivity.this, "Updated Contact Successfully", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    public void getUpdateContactList(String contactId, String newFirstName, String newLastName,String newPersonalPhoneNumber,String newOfficePhoneNumber,String newImage) {

//        ContentResolver contentResolver = getContentResolver();
//        ContentValues phoneNumberValues = new ContentValues();
//        phoneNumberValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, newPersonalPhoneNumber);
//
//        String selection = ContactsContract.Data.CONTACT_ID + " = ? AND " +
//                ContactsContract.Data.MIMETYPE + " = ?";
//
//        String[] selectionArgs = new String[]{contactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};
//        contentResolver.update(ContactsContract.Data.CONTENT_URI, phoneNumberValues, selection, selectionArgs);
//
//        ContentValues phoneValues2 = new ContentValues();
//        phoneValues2.put(ContactsContract.CommonDataKinds.Phone.NUMBER, newOfficePhoneNumber);
//
//        String phoneSelection2 = ContactsContract.Data.CONTACT_ID + " = ? AND " +
//                ContactsContract.Data.MIMETYPE + " = ? AND " +
//                ContactsContract.CommonDataKinds.Phone.TYPE + " = ?";
//
//        String[] phoneSelectionArgs2 = new String[] { contactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
//                String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE) };
//
//        contentResolver.update(ContactsContract.Data.CONTENT_URI, phoneValues2, phoneSelection2, phoneSelectionArgs2);

        ContentResolver contentResolver = getContentResolver();

        // Update the first phone number
        ContentValues phoneValues1 = new ContentValues();
        phoneValues1.put(ContactsContract.CommonDataKinds.Phone.NUMBER, newPersonalPhoneNumber);

        String phoneSelection1 = ContactsContract.Data.CONTACT_ID + " = ? AND " +
                ContactsContract.Data.MIMETYPE + " = ? AND " +
                ContactsContract.CommonDataKinds.Phone.TYPE + " = ?";

        String[] phoneSelectionArgs1 = new String[] { contactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) };

        contentResolver.update(ContactsContract.Data.CONTENT_URI, phoneValues1, phoneSelection1, phoneSelectionArgs1);

        // Update the second phone number
        ContentValues phoneValues2 = new ContentValues();
        phoneValues2.put(ContactsContract.CommonDataKinds.Phone.NUMBER, newOfficePhoneNumber);

        String phoneSelection2 = ContactsContract.Data.CONTACT_ID + " = ? AND " +
                ContactsContract.Data.MIMETYPE + " = ? AND " +
                ContactsContract.CommonDataKinds.Phone.TYPE + " = ?";

        String[] phoneSelectionArgs2 = new String[] { contactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_WORK) };

        contentResolver.update(ContactsContract.Data.CONTENT_URI, phoneValues2, phoneSelection2, phoneSelectionArgs2);


        ContentResolver contentResolver1 = getContentResolver();
        ContentValues contactNameValues = new ContentValues();
        contactNameValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, newFirstName);
        contactNameValues.put(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, newLastName);

        String selection1 = ContactsContract.Data.CONTACT_ID + " = ? AND " +
                ContactsContract.Data.MIMETYPE + " = ?";

        String[] selectionArgs1 = new String[]{contactId, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
        contentResolver1.update(ContactsContract.Data.CONTENT_URI, contactNameValues, selection1, selectionArgs1);


        if (newImage != null) {
            ContentResolver contentResolver2 = getContentResolver();
            ContentValues imageValues = new ContentValues();
            imageValues.put(ContactsContract.CommonDataKinds.Photo.PHOTO, newImage);

            String imageSelection = ContactsContract.Data.CONTACT_ID + " = ? AND " +
                    ContactsContract.Data.MIMETYPE + " = ?";
            String[] imageSelectionArgs = new String[] { contactId, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE };

            contentResolver2.update(ContactsContract.Data.CONTENT_URI, imageValues, imageSelection, imageSelectionArgs);
        }

        Users user = new Users(contactId,"",newFirstName,newLastName, newPersonalPhoneNumber,"");
        usersArrayList.add(user);
    }

    //    private void checkPermission() {
//        if (ContextCompat.checkSelfPermission(UpdateContactActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(UpdateContactActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 100);
//        }
//    }


//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//        } else {
//            Toast.makeText(UpdateContactActivity.this, "Permission Denied.", Toast.LENGTH_SHORT).show();
//            checkPermission();
//        }
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            personimage.setImageBitmap(bitmap);
            imagepath = saveToInternalStorage(bitmap);
        }
    }

//    private void pickFrom() {
//        CropImage.activity()
//                .start(getContext(), this);    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
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
        return directory.getAbsolutePath();
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
}