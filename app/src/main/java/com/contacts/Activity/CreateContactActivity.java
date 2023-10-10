package com.contacts.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.ContactsManager;
import com.contacts.Fragment.ContactsFragment;
import com.contacts.Model.Users;
import com.contacts.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class CreateContactActivity extends AppCompatActivity {

    TextView save;
    EditText firstname,lastname,pphone,ophone;
    ImageView addPersonImage,cancel;
    ContactsManager contactsManager;
    ArrayList<Users> usersArrayList;
    String imagename,imagepath;
    private static final int CAMERA_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        init();
        contactsManager = new ContactsManager(getApplicationContext());



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstname.getText().toString().isEmpty() && lastname.getText().toString().isEmpty()){
                    Toast.makeText(CreateContactActivity.this, "Please Fill Data", Toast.LENGTH_SHORT).show();
                }
                else {
                    createContact();
                    onBackPressed();
                    Toast.makeText(CreateContactActivity.this, "Saved Data To SharedPreferences", Toast.LENGTH_SHORT).show();
//                Fragment fragment = new ContactsFragment();
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                Bundle bundle = new Bundle();
//                bundle.putParcelableArrayList("list", usersArrayList);
//                fragment.setArguments(bundle);
//                fragmentTransaction.replace(R.id.create_contact_activity,fragment).commit();
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

    public void createContact(){
        imagepath = imagepath+"/"+imagename;
        String first = firstname.getText().toString();
        String last = lastname.getText().toString();
        String personPhone = pphone.getText().toString();
        String officePhone = ophone.getText().toString();
        Users users = new Users(imagepath,first,last,personPhone,officePhone);
//        usersArrayList.add(users);
        contactsManager.AddContact(users);
    }

    private int generateTaskId() {
        return (int) System.currentTimeMillis();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            addPersonImage.setImageBitmap(bitmap);
            imagepath=saveToInternalStorage(bitmap);
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        imagename = "img"+new Random().nextInt(100000)+".png";
        File mypath=new File(directory,imagename);

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

    private void init(){
        cancel = findViewById(R.id.cancel);
        addPersonImage = findViewById(R.id.addPersonImage);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        pphone = findViewById(R.id.pphone);
        ophone = findViewById(R.id.ophone);
        save = findViewById(R.id.saveContact);
    }
}