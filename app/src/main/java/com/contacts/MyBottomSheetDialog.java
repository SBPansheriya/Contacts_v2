package com.contacts;

import static com.contacts.Fragment.KeypadFragment.keypadListAdapter;
import static com.contacts.Fragment.KeypadFragment.recyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.contacts.Activity.CreateContactActivity;
import com.contacts.Adapter.KeypadListAdapter;
import com.contacts.Class.Constant;
import com.contacts.Fragment.KeypadFragment;
import com.contacts.Model.Users;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class MyBottomSheetDialog extends BottomSheetDialogFragment {

    String s;

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog_layout, container, false);

        onStart();

        ImageView btn_1 = view.findViewById(R.id.btn_1);
        ImageView btn_2 = view.findViewById(R.id.btn_2);
        ImageView btn_3 = view.findViewById(R.id.btn_3);
        ImageView btn_4 = view.findViewById(R.id.btn_4);
        ImageView btn_5 = view.findViewById(R.id.btn_5);
        ImageView btn_6 = view.findViewById(R.id.btn_6);
        ImageView btn_7 = view.findViewById(R.id.btn_7);
        ImageView btn_8 = view.findViewById(R.id.btn_8);
        ImageView btn_9 = view.findViewById(R.id.btn_9);
        ImageView btn_0 = view.findViewById(R.id.btn_0);
        ImageView btn_hash = view.findViewById(R.id.btn_hash);
        ImageView btn_star = view.findViewById(R.id.btn_star);
        ImageView btn_call = view.findViewById(R.id.btn_call);
        ImageView btn_backpress = view.findViewById(R.id.btn_backpress);
        EditText editText = view.findViewById(R.id.dailer_show);
        TextView add_contact_by_keypad = view.findViewById(R.id.add_contact_by_keypad);

        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "0";
                editText.setText(s);
            }
        });

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "1";
                editText.setText(s);
            }
        });

        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "2";
                editText.setText(s);
            }
        });

        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "3";
                editText.setText(s);
            }
        });

        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "4";
                editText.setText(s);
            }
        });

        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "5";
                editText.setText(s);
            }
        });

        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "6";
                editText.setText(s);
            }
        });

        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "7";
                editText.setText(s);
            }
        });

        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "8";
                editText.setText(s);
            }
        });

        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "9";
                editText.setText(s);
            }
        });

        btn_hash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "#";
                editText.setText(s);
            }
        });

        btn_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = String.valueOf(editText.getText());
                s = number + "*";
                editText.setText(s);
            }
        });

        btn_backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.length() > 0){
                    String t = s.substring(0,editText.length() -1);
                    editText.setText(""+t);
                }
                else {
                    editText.setText("");
                }
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + editText.getText()));
                startActivity(intent);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)){
                    recyclerView.setVisibility(View.GONE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                ArrayList<Users> filteredList = new ArrayList<>();

                for (int i = 0; i < Constant.usersArrayList.size(); i++) {

                    final String number = Constant.usersArrayList.get(i).getPersonPhone().toLowerCase();
                    if (number.contains(query)) {

                        filteredList.add(Constant.usersArrayList.get(i));
                    }
                }
                if (filteredList.size() > 0){
                    recyclerView.setVisibility(View.VISIBLE);
                    keypadListAdapter.setFilteredList(filteredList);
                }
                else {
                    recyclerView.setVisibility(View.GONE);
                }

            }
        });

        add_contact_by_keypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateContactActivity.class);
                intent.putExtra("number",editText.getText().toString());
                startActivity(intent);
            }
        });

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
    }

}

