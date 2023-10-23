package com.contacts.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.contacts.Adapter.HeaderListAdapter;
import com.contacts.Adapter.KeypadListAdapter;
import com.contacts.Class.Constant;
import com.contacts.Model.Users;
import com.contacts.MyBottomSheetDialog;
import com.contacts.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class KeypadFragment extends Fragment {

    public static KeypadListAdapter keypadListAdapter;
    public static RecyclerView recyclerView;
    String s;
    ArrayList<Users> filteredList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keypad, container, false);

        init(view);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        keypadListAdapter = new KeypadListAdapter(KeypadFragment.this, Constant.usersArrayList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(keypadListAdapter);
        keypadListAdapter.notifyDataSetChanged();

//        BottomSheetDialogFragment bottomSheetDialog = new MyBottomSheetDialog();
//        bottomSheetDialog.show(getActivity().getSupportFragmentManager(), bottomSheetDialog.getTag());

        return view;
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.keypadrecyclerview);
    }
}