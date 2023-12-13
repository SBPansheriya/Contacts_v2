package com.contacts.Activity;

import static com.contacts.Class.Constant.typeArrayList;
import static com.contacts.Class.Constant.usersArrayList;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
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
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.contacts.Adapter.AddNewPhoneNumberAdapter;
import com.contacts.Model.PhoneType;
import com.contacts.Model.Users;
import com.contacts.Model.Phone;
import com.contacts.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class CreateContactActivity extends AppCompatActivity {

    TextView save;
    EditText firstname, lastname, pphone, ophone;
    ImageView addPersonImage, cancel, addNew;
    String imagename;
    String imagepath;
    RecyclerView recyclerView;
    Users user;
    Phone phone;
    Bitmap bitmap;
    int typevalue;
    AddNewPhoneNumberAdapter addNewPhoneNumberAdapter;
    ArrayList<Phone> phoneArrayList = new ArrayList<>();
    ActivityResultLauncher<Intent> launchSomeActivity;
    ActivityResultLauncher<CropImageContractOptions> cropImage;
    private static final int CAMERA_REQUEST = 100;
    private int selectedPosition = RecyclerView.NO_POSITION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        getSupportActionBar().hide();
        Window window = CreateContactActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(CreateContactActivity.this, R.color.white));
        init();

        user = (Users) getIntent().getSerializableExtra("user");

        phoneArrayList.add(new Phone());

        LinearLayoutManager manager = new LinearLayoutManager(CreateContactActivity.this);
        addNewPhoneNumberAdapter = new AddNewPhoneNumberAdapter(CreateContactActivity.this, phoneArrayList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(addNewPhoneNumberAdapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionsForSave();
            }
        });

        addPersonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsForCamera();
            }
        });

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneArrayList.add(new Phone());

                addNewPhoneNumberAdapter.updateList(phoneArrayList);
            }
        });

        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Uri selectedImageUri = result.getData().getData();
                startCrop(selectedImageUri);
            }
        });

        cropImage = registerForActivityResult(new CropImageContract(), result -> {
            if (result.isSuccessful()) {
                Uri uriContent = result.getUriContent();
                bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (bitmap != null) {
                    addPersonImage.setImageBitmap(bitmap);
                }
            } else {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startCrop(Uri selectedImageUri) {
        CropImageOptions options = new CropImageOptions();
        options.guidelines = CropImageView.Guidelines.ON;
        CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(selectedImageUri, options);
        cropImage.launch(cropImageContractOptions);
    }

    private void openImagePicker() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        launchSomeActivity.launch(i);
    }

    private void dialog() {
        Dialog dialog = new Dialog(CreateContactActivity.this);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(false);
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_camera_and_gallery);
        dialog.setCancelable(false);
        dialog.show();

        ImageView camera = dialog.findViewById(R.id.camera);
        ImageView gallery = dialog.findViewById(R.id.gallery);
        Button cancel1 = dialog.findViewById(R.id.cancel);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPermission();
                dialog.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
                dialog.dismiss();
            }
        });

        cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void getData(String number, int position) {
        phoneArrayList.get(position).setPhonenumber(number);
    }

    public void getposition(PhoneType type, int position) {
        typevalue = type.getType();
        typeArrayList.get(position).setType(type.getType());
        phoneArrayList.get(position).setPhoneType(type.getType());
        phoneArrayList.get(position).setLabel(type.getLabel());
        addNewPhoneNumberAdapter.updateList(phoneArrayList);
    }

    public void createContact() {
        String first = firstname.getText().toString();
        String last = lastname.getText().toString();

        ContentValues values = new ContentValues();
        ContentResolver contentResolver = getContentResolver();

// Add the contact's name
        values.put(ContactsContract.RawContacts.ACCOUNT_TYPE, (String) null);
        values.put(ContactsContract.RawContacts.ACCOUNT_NAME, (String) null);
        Uri rawContactUri = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, values);

        String rawContactId = String.valueOf(ContentUris.parseId(rawContactUri));

// Add the contact's name
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, first + " " + last);
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, values);

// Add the contact's phone number
        for (Phone phone : phoneArrayList) {
            String phoneNumber = phone.getPhonenumber();
            int type = phone.getPhoneType();
            if (!TextUtils.isEmpty(phoneNumber)) {
                values.clear();
                values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
                values.put(ContactsContract.CommonDataKinds.Phone.TYPE,type);
                contentResolver.insert(ContactsContract.Data.CONTENT_URI, values);
            }
        }

        ByteArrayOutputStream image = null;
        String path = null;
        if (bitmap != null) {
            values.clear();
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
            image = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, image);
            path = (MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null));
            values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, path);
            values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, image.toByteArray());
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, values);
        }

        user = new Users(rawContactId, path, (first + last), first, last, phoneArrayList, "", "");
        usersArrayList.add(user);

        onBackPressed();

        finish();

    }

    private void savedData() {
//        if (TextUtils.isEmpty(firstname.getText().toString()) || TextUtils.isEmpty(pphone.getText().toString())) {
//            Toast.makeText(CreateContactActivity.this, "Please Fill Data", Toast.LENGTH_SHORT).show();
//        } else {
        createContact();
        Toast.makeText(CreateContactActivity.this, "Contact saved", Toast.LENGTH_SHORT).show();
//        }
    }

    private void checkPermissionsForSave() {
        String[] permissions = new String[]{Manifest.permission.WRITE_CONTACTS};

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(CreateContactActivity.this, permissions, 101);
        } else {
            savedData();
        }
    }

    private void cameraPermission() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, CAMERA_REQUEST);

    }

    private void checkPermissionsForCamera() {
        String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(CreateContactActivity.this, permissions, 100);
        } else {
            dialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dialog();
        } else if (requestCode == 101 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createContact();
        } else {
            showPermissionDialog();
        }
    }

    private void showPermissionDialog() {
        Dialog dialog = new Dialog(CreateContactActivity.this);
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

        textView.setText("This app needs Camera permissions to use this feature. You can grant them in app settings.");

        dismiss_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        gotosettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    checkPermissionsForCamera();
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    checkPermissionsForSave();
                } else {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 100);
                    Toast.makeText(CreateContactActivity.this, "Setting", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            addPersonImage.setImageBitmap(bitmap);
            imagepath = saveToInternalStorage(bitmap);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
            startCrop(Uri.parse(path));
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
//        pphone = findViewById(R.id.pphone);
//        ophone = findViewById(R.id.ophone);
        save = findViewById(R.id.save_Contact);
//        remove_phone_number = findViewById(R.id.remove_phone_number);
//        remove_office_number = findViewById(R.id.remove_office_number);
        addNew = findViewById(R.id.addNew);
        recyclerView = findViewById(R.id.phone_add_recyclerview);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("user", user);
        setResult(RESULT_OK, intent);
        finish();
    }
}