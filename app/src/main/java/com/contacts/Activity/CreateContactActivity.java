package com.contacts.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.Fragment.ContactsFragment;
import com.contacts.Model.Users;
import com.contacts.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class CreateContactActivity extends AppCompatActivity {

    TextView save;
    EditText firstname, lastname, pphone, ophone;
    ImageView addPersonImage, cancel;
    List<Users> usersList;
    String imagename;
    String imagepath;
    private static final int CAMERA_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        Window window = CreateContactActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(CreateContactActivity.this, R.color.white));
        init();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstname.getText().toString().isEmpty() && pphone.getText().toString().isEmpty()) {
                    Toast.makeText(CreateContactActivity.this, "Please Fill Data", Toast.LENGTH_SHORT).show();
                } else {
                    createContact();
                    ContactsFragment fragment = new ContactsFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // Replace the content of the activity with the fragment
                    fragmentTransaction.replace(R.id.framelayout, fragment);
                    // Commit the transaction
                    fragmentTransaction.commit();
                    Toast.makeText(CreateContactActivity.this, "Saved Data To SharedPreferences", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addPersonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, CAMERA_REQUEST);
            }
        });
    }

    public void createContact() {
        imagepath = imagepath + "/" + imagename;
        String first = firstname.getText().toString();
        String last = lastname.getText().toString();
        String personPhone = pphone.getText().toString();
        String officePhone = ophone.getText().toString();

        ContentValues values = new ContentValues();
        ContentResolver contentResolver = getContentResolver();

// Add the contact's name
        values.put(ContactsContract.RawContacts.ACCOUNT_TYPE, (String) null);
        values.put(ContactsContract.RawContacts.ACCOUNT_NAME, (String) null);
        Uri rawContactUri = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, values);

        long rawContactId = ContentUris.parseId(rawContactUri);

// Add the contact's name
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, first + " " + last);
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, values);

// Add the contact's phone number
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, personPhone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, values);

        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, officePhone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, values);

        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, imagepath);
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, values);

//        if (imagepath != null) {
//            ContentValues photoValues = new ContentValues();
//            photoValues.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
//            photoValues.put(ContactsContract.CommonDataKinds.Photo.PHOTO, bitmapToByteArray(imagepath));
//            photoValues.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId);
//            contentResolver.insert(ContactsContract.Data.CONTENT_URI, photoValues);
//        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            addPersonImage.setImageBitmap(bitmap);
            imagepath = saveToInternalStorage(bitmap);
        }
    }

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
        cancel = findViewById(R.id.cancel);
        addPersonImage = findViewById(R.id.addPersonImage);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        pphone = findViewById(R.id.pphone);
        ophone = findViewById(R.id.ophone);
        save = findViewById(R.id.save_Contact);
    }
}