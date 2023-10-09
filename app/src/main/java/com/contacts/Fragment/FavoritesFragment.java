package com.contacts.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.contacts.Adapter.FavListAdapter;
import com.contacts.Adapter.HeaderListAdapter;
import com.contacts.CreateContactActivity;
import com.contacts.HomeActivity;
import com.contacts.R;

public class FavoritesFragment extends Fragment {

FavListAdapter favListAdapter;
RecyclerView recyclerView;
ImageView back;
Button addfav;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        init(view);

//        favListAdapter = new FavListAdapter(FavoritesFragment.this);
//        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(favListAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

        addfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateContactActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void  init(View view){
        back = view.findViewById(R.id.back);
        addfav = view.findViewById(R.id.create_fav);
    }
}