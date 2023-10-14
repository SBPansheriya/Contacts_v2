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

import com.contacts.Adapter.RecentListAdapter;
import com.contacts.Activity.HomeActivity;
import com.contacts.R;

public class RecentsFragment extends Fragment {

    RecentListAdapter recentListAdapter;
    RecyclerView recyclerView;
    ImageView back;
    RecentsFragment recentsFragment;
    String shayariname[] = {"Dosti Shayari","God Shayari","Love Shayari","Brithday Shayari","Holi Shayari","Sharab Shayari","Politics Shayari","Bewfa Shayari","Other Shayari"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recents, container, false);

        init(view);

        recentListAdapter = new RecentListAdapter(getContext(),shayariname);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(recentListAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


    private void  init(View view){
        back = view.findViewById(R.id.back);
        recyclerView = view.findViewById(R.id.recents_recyclerview);
    }
}