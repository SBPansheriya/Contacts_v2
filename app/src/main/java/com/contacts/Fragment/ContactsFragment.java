package com.contacts.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.contacts.Adapter.HeaderListAdapter;
import com.contacts.Activity.CreateContactActivity;
import com.contacts.Model.Header;
import com.contacts.Model.Users;
import com.contacts.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {

    HeaderListAdapter headerListAdapter;
    RecyclerView recyclerView;
    ImageView edit,cancel,share,delete;
    Button create_btn;
    TextView selectall;
    FloatingActionButton floatingActionButton;
    ViewGroup viewGroup;
    Context context;
    ArrayList<Users> usersArrayList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        init(view);

//        getArguments().getParcelableArrayList("list");

//        headerListAdapter = new HeaderListAdapter(list,ContactsFragment.this);
//        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(headerListAdapter);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateContactActivity.class);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.setVisibility(View.GONE);
                selectall.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                share.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateContactActivity.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(ContactsFragment.this.getContext());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dailog_layout);
                dialog.setCancelable(false);
                dialog.show();

                Button cancel = dialog.findViewById(R.id.canceldialog);
                Button movetobin = dialog.findViewById(R.id.movetobin);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), getActivity().getClass());
                        startActivity(intent);
                    }
                });

                movetobin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        });


        return view;
    }

//    private List<Header> getData() {
//        List<Header> headerlist = new ArrayList<>();
//        headerlist.add(new Header("A",contactListData()));
//        headerlist.add(new Header("B",contactListData()));
//        headerlist.add(new Header("C",contactListData()));
//        return headerlist;
//    }

//    private List<Users> contactListData()
//    {
//        List<Users> ChildItemList = new ArrayList<>();
//        ChildItemList.add(new Users());
//        ChildItemList.add(new Users());
//        ChildItemList.add(new Users());
//        ChildItemList.add(new Users());
//        return ChildItemList;
//    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.show_contact_recyclerview);
        floatingActionButton = view.findViewById(R.id.add_contact);
        edit = view.findViewById(R.id.edit);
        cancel = view.findViewById(R.id.cancel);
        share = view.findViewById(R.id.share);
        delete = view.findViewById(R.id.trash);
        selectall = view.findViewById(R.id.selectall);
        create_btn = view.findViewById(R.id.create_contact);
        viewGroup = view.findViewById(android.R.id.content);
    }
}