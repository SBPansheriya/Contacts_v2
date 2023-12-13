package com.contacts.Activity;

import static com.contacts.Class.Constant.phoneTypeArrayList;
import static com.contacts.Class.Constant.typeArrayList;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
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
import com.contacts.Class.Constant;
import com.contacts.Model.PhoneType;
import com.contacts.Model.Users;
import com.contacts.Model.Phone;
import com.contacts.R;
import com.contacts.Adapter.UpdateNumberAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
    ImageView personimage, addNewUpdate;
    RecyclerView recyclerView;
    String imagename, imagepath;
    Uri newUri;
    Button update_contact;
    UpdateNumberAdapter updateNumberAdapter;
    private static final int CAMERA_REQUEST = 100;
    Users user;
    Bitmap bitmap;
    ActivityResultLauncher<Intent> launchSomeActivity;
    ActivityResultLauncher<CropImageContractOptions> cropImage;
    ArrayList<Phone> phoneArrayList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);
        getSupportActionBar().hide();
        Window window = UpdateContactActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(UpdateContactActivity.this, R.color.white));
        init();

        user = (Users) getIntent().getSerializableExtra("user");
        String listget = getIntent().getStringExtra("phone");
        Gson gson = new Gson();
        phoneArrayList2 = gson.fromJson(listget, new TypeToken<ArrayList<Phone>>() {
        }.getType());

        LinearLayoutManager manager = new LinearLayoutManager(UpdateContactActivity.this);
        updateNumberAdapter = new UpdateNumberAdapter(UpdateContactActivity.this, phoneArrayList2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(updateNumberAdapter);

        if (user.image == null) {
            personimage.setImageResource(R.drawable.person_placeholder);
        } else {
            Picasso.get().load(user.image).into(personimage);
        }

        update_firstname.setText(user.first);
        update_lastname.setText(user.last);

        addNewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneArrayList2.add(new Phone());
                updateNumberAdapter.updateList(phoneArrayList2);
            }
        });

        show_person_name.setText(user.getFullName());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        personimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionsForCamera();
            }
        });

        update_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionsForSave();
            }
        });

        launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                newUri = data.getData();
                startCrop(newUri);
            }
        });

        cropImage = registerForActivityResult(new CropImageContract(), result -> {
            if (result.isSuccessful()) {
                newUri = result.getUriContent();
                personimage.setImageURI(newUri);
            }
        });
    }

    public void getposition(PhoneType type, int position) {
        typeArrayList.get(position).setType(type.getType());
        phoneArrayList2.get(position).setPhoneType(type.getType());
        phoneArrayList2.get(position).setLabel(type.getLabel());
        updateNumberAdapter.updateList(phoneArrayList2);
    }

    public void getData(String number, int position) {
        phoneArrayList2.get(position).setPhonenumber(number);
    }

    @SuppressLint("Range")
    public void getUpdateContactList(String contactId, String newFirstName, String newLastName, Uri newImage) {

        ContentResolver contentResolver = getContentResolver();
        ArrayList<ContentProviderOperation> ops1 = new ArrayList<ContentProviderOperation>();

        for (Phone phone : phoneArrayList2) {
            String number = phone.getPhonenumber();
            int phoneType = phone.getPhoneType();
            
            ops1.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(
                            ContactsContract.Data.CONTACT_ID + " = ? AND " +
                                    ContactsContract.Data.MIMETYPE + " = ? AND " +
                                    ContactsContract.CommonDataKinds.Phone.TYPE + " = ?",
                            new String[]{contactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, String.valueOf(phoneType)})
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.Data.DATA1, number)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, phoneType)
                    .withValue(ContactsContract.CommonDataKinds.Phone.LABEL, phone.getLabel())
                    .build());
        }
        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops1);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (OperationApplicationException e) {
            throw new RuntimeException(e);
        }


//        Cursor cursor = contentResolver.query(
//                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                null,
//                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
//                new String[]{contactId},
//                null
//        );
//
//        // Update the first phone number
//
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                for (Phone phone : phoneArrayList2) {
//                    String number = phone.getPhonenumber();
//                    int phoneType = phone.getPhoneType();
//
//                    ContentValues phoneValues1 = new ContentValues();
//                    phoneValues1.put(ContactsContract.CommonDataKinds.Phone.NUMBER, number);
//                    phoneValues1.put(ContactsContract.CommonDataKinds.Phone.TYPE, phoneType);
//
//                    String phoneSelection1 = ContactsContract.Data.CONTACT_ID + " = ? AND " +
//                            ContactsContract.Data.MIMETYPE + " = ? AND " +
//                            ContactsContract.CommonDataKinds.Phone.TYPE + " = ?";
//
//                    String[] phoneSelectionArgs1 = new String[]{contactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
//                            String.valueOf(phoneType)};
//
//                    int updatedRows = contentResolver.update(ContactsContract.Data.CONTENT_URI, phoneValues1, phoneSelection1, phoneSelectionArgs1);
//
//                    if (updatedRows > 0) {
//                        Log.d("ContactUpdater", "Contact updated successfully");
//                    } else {
//                        Log.e("ContactUpdater", "Failed to update contact");
//                    }
//                }
//            }
//            cursor.close();
//        }

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
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), newImage);
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
            } else {
                try {
                    ContentValues values = new ContentValues();
                    ContentResolver contentResolver3 = getContentResolver();

                    bitmap = null;

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
        }
        user.first = newFirstName;
        user.last = newLastName;

        for (int i = 0; i < Constant.usersArrayList.size(); i++) {

            if (Constant.usersArrayList.get(i).contactId.equalsIgnoreCase(contactId)) {
                Constant.usersArrayList.remove(i);
                Constant.usersArrayList.add(i, user);
                break;
            }
        }

        Toast.makeText(UpdateContactActivity.this, "Contact saved", Toast.LENGTH_SHORT).show();
        onBackPressed();
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
        Dialog dialog = new Dialog(UpdateContactActivity.this);
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

    private void updatedData() {
        imagepath = imagepath + "/" + imagename;
        String firstname = update_firstname.getText().toString();
        String lastname = update_lastname.getText().toString();

        getUpdateContactList(user.contactId, firstname, lastname, newUri);
        Toast.makeText(UpdateContactActivity.this, "Contact saved", Toast.LENGTH_SHORT).show();

    }

    private void checkPermissionsForSave() {

        String[] permissions = new String[]{Manifest.permission.WRITE_CONTACTS};

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(UpdateContactActivity.this, permissions, 101);
        } else {
            updatedData();
        }
    }

    private void cameraPermission() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, CAMERA_REQUEST);
    }

    private void checkPermissionsForCamera() {

        String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(UpdateContactActivity.this, permissions, 100);
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
            updatedData();
        } else {
            showPermissionDialog();
        }
    }

    private void showPermissionDialog() {
        Dialog dialog = new Dialog(UpdateContactActivity.this);
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
                    Toast.makeText(UpdateContactActivity.this, "Setting", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            personimage.setImageBitmap(bitmap);
            newUri = saveToInternalStorage(bitmap);
            imagepath = newUri.getPath();
            startCrop(newUri);
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
//        update_pphone = findViewById(R.id.update_pphone);
//        update_ophone = findViewById(R.id.update_ophone);
        update_contact = findViewById(R.id.update_contact);
        show_person_name = findViewById(R.id.show_personName);
        recyclerView = findViewById(R.id.update_number_recyclerview);
        addNewUpdate = findViewById(R.id.addNewUpdate);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("user", user);
        Gson gson = new Gson();
        String list = gson.toJson(phoneArrayList2);
        intent.putExtra("phone", list);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}