package com.contacts.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.contacts.Adapter.ContactListAdapter;
import com.contacts.Adapter.HeaderListAdapter;
import com.contacts.Adapter.RecentListAdapter;
import com.contacts.CreateContactActivity;
import com.contacts.Model.Header;
import com.contacts.Model.Users;
import com.contacts.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {

    HeaderListAdapter headerListAdapter;

    RecyclerView recyclerView;
    ImageView edit;
    TextView selectall;
    FloatingActionButton floatingActionButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        init(view);

        List<Header> headerlist;
        headerlist = getData();

        headerListAdapter = new HeaderListAdapter(headerlist);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(headerListAdapter);




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
                selectall.setVisibility(View.VISIBLE);
            }
        });


        return view;
    }

    private List<Header> getData() {
        List<Header> headerlist = new ArrayList<>();
        headerlist.add(new Header("A",contactListData()));
        headerlist.add(new Header("B",contactListData()));
        headerlist.add(new Header("C",contactListData()));
        return headerlist;
    }

    private List<Users> contactListData()
    {
        List<Users> ChildItemList = new ArrayList<>();
        ChildItemList.add(new Users("Alicia",""));
        ChildItemList.add(new Users("John",""));
        ChildItemList.add(new Users("Arlene McCoy",""));
        ChildItemList.add(new Users("Brooklyn Simmons",""));
        return ChildItemList;
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.show_contact_recyclerview);
        floatingActionButton = view.findViewById(R.id.add_contact);
        edit = view.findViewById(R.id.edit);
        selectall = view.findViewById(R.id.selectall);
    }
}