package com.contacts.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contacts.R;

public class ContactDetailActivity extends AppCompatActivity {

    ImageView edit,call,messenger,favourites,selected_person_image,back;
    LinearLayout whatsapp;
    TextView selected_person_name,selected_person_pnum,selected_person_onum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        init();

        String image = getIntent().getStringExtra("image");
        String firstname = getIntent().getStringExtra("first");
        String lastname = getIntent().getStringExtra("last");
        String pphone = getIntent().getStringExtra("pphone");
        String ophone = getIntent().getStringExtra("ophone");

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactDetailActivity.this,UpdateContactActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        messenger.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View view) {
                String textnum = "12345";
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.setData(Uri.parse("sms:" + textnum));
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager packageManager = ContactDetailActivity.this.getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);

                try {
                    String url = "https://api.whatsapp.com/send?phone="+// something here;
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        ContactDetailActivity.this.startActivity(i);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void init(){
        edit = findViewById(R.id.edit_contact_details);
        call = findViewById(R.id.call);
        messenger = findViewById(R.id.messenger);
        favourites = findViewById(R.id.favourites);
        back = findViewById(R.id.back);
        whatsapp = findViewById(R.id.whatsapp);
        selected_person_image = findViewById(R.id.selected_person_image);
        selected_person_name = findViewById(R.id.selected_person_name);
        selected_person_pnum = findViewById(R.id.selected_person_pnum);
        selected_person_onum = findViewById(R.id.selected_person_onum);
    }
}