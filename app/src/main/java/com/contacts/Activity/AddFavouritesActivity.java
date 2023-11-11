package com.contacts.Activity;

import static com.contacts.Class.Constant.usersArrayList;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.contacts.Adapter.AddFavouritesListAdapter;
import com.contacts.Model.Users;
import com.contacts.R;

import java.util.Comparator;

public class AddFavouritesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView back;
    AddFavouritesListAdapter addFavouritesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favourites);
        getSupportActionBar().hide();
        Window window = AddFavouritesActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(AddFavouritesActivity.this, R.color.white));

        init();

        getContactList();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void getContactList() {
        if (usersArrayList.size() > 0) {
            Comparator<Users> nameComparator = Comparator.comparing(Users::getFirst);
            usersArrayList.sort(nameComparator);

            LinearLayoutManager manager = new LinearLayoutManager(AddFavouritesActivity.this);
            addFavouritesListAdapter = new AddFavouritesListAdapter(AddFavouritesActivity.this, usersArrayList);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(addFavouritesListAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void init(){
        recyclerView = findViewById(R.id.addfavouritesrecyclerview);
        back = findViewById(R.id.back);
    }
}