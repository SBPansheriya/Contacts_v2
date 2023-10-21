package com.contacts.Fragment;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.contacts.Adapter.HeaderListAdapter;
import com.contacts.Adapter.KeypadListAdapter;
import com.contacts.Class.Constant;
import com.contacts.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class KeypadFragment extends Fragment {

    KeypadListAdapter keypadListAdapter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keypad, container, false);

        init(view);

        @SuppressLint("ResourceType") BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(),R.color.blackforsheet);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);
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