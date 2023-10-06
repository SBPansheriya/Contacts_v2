package com.contacts.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.contacts.Adapter.ContactListAdapter;
import com.contacts.Model.UsersModel;
import com.contacts.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {

    ContactListAdapter contactListAdapter;
    RecyclerView recyclerView;
    List<UsersModel> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);

//        contactListAdapter = new ContactListAdapter();
//        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(contactListAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.show_contact_recyclerview);
    }

//    private List<UsersModel> getData()
//    {
//       list.add(new UsersModel("First Exam"));
//       list.add(new UsersModel("Second Exam"));
//       list.add(new UsersModel("My Test Exam"));
//        return list;
//    }
}