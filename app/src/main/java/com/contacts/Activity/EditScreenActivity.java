package com.contacts.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.contacts.R;

public class EditScreenActivity extends AppCompatActivity {

    ImageView edit,call,messenger,favourites,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_screen);

        init();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditScreenActivity.this,UpdateContactActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void init(){
        edit = findViewById(R.id.edit_contact_details);
        call = findViewById(R.id.call);
        messenger = findViewById(R.id.messenger);
        favourites = findViewById(R.id.favourites);
        back = findViewById(R.id.back);
    }
}