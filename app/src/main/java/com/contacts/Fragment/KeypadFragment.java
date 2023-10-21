package com.contacts.Fragment;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.contacts.Adapter.HeaderListAdapter;
import com.contacts.Adapter.KeypadListAdapter;
import com.contacts.Class.Constant;
import com.contacts.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class KeypadFragment extends Fragment {

    KeypadListAdapter keypadListAdapter;
    RecyclerView recyclerView;
    SearchView searchView;
//    ImageView btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_0,btn_hash,btn_star,btn_call,btn_backpress;
    String s;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keypad, container, false);

        init(view);

        @SuppressLint("ResourceType") BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(),R.color.blackforsheet);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);

        ImageView btn_1 = bottomSheetDialog.findViewById(R.id.btn_1);
        ImageView btn_2 = bottomSheetDialog.findViewById(R.id.btn_2);
        ImageView btn_3 = bottomSheetDialog.findViewById(R.id.btn_3);
        ImageView btn_4 = bottomSheetDialog.findViewById(R.id.btn_4);
        ImageView btn_5 = bottomSheetDialog.findViewById(R.id.btn_5);
        ImageView btn_6 = bottomSheetDialog.findViewById(R.id.btn_6);
        ImageView btn_7 = bottomSheetDialog.findViewById(R.id.btn_7);
        ImageView btn_8 = bottomSheetDialog.findViewById(R.id.btn_8);
        ImageView btn_9 = bottomSheetDialog.findViewById(R.id.btn_9);
        ImageView btn_0 = bottomSheetDialog.findViewById(R.id.btn_0);
        ImageView btn_hash = bottomSheetDialog.findViewById(R.id.btn_hash);
        ImageView btn_star = bottomSheetDialog.findViewById(R.id.btn_star);
        ImageView btn_call = bottomSheetDialog.findViewById(R.id.btn_call);
        ImageView btn_backpress = bottomSheetDialog.findViewById(R.id.btn_backpress);
//        SearchView searchView = bottomSheetDialog.findViewById(R.id.dailer_show);
        EditText editText = bottomSheetDialog.findViewById(R.id.dailer_show);

        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("0");
            }
        });

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = "1";
                editText.setText(s);
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = "2";
                editText.setText(s);
            }
        });

        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("3");
            }
        });

        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("4");
            }
        });

        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("5");
            }
        });

        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("6");
            }
        });

        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("7");
            }
        });

        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("8");
            }
        });

        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("9");
            }
        });

        btn_hash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("#");
            }
        });

        btn_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("*");
            }
        });

        btn_backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });

        bottomSheetDialog.show();

        Constant.usersArrayList = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        keypadListAdapter = new KeypadListAdapter(KeypadFragment.this, Constant.usersArrayList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(keypadListAdapter);

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        return view;
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.keypadrecyclerview);
    }
}