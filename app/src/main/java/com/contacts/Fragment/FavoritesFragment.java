package com.contacts.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.contacts.Activity.ContactDetailActivity;
import com.contacts.Adapter.FavListAdapter;
import com.contacts.Activity.CreateContactActivity;
import com.contacts.Activity.HomeActivity;
import com.contacts.R;

public class FavoritesFragment extends Fragment {

FavListAdapter favListAdapter;
RecyclerView recyclerView;
TextView done;
ImageView back,info_icon,edit,scrollcontact;
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
                Intent intent = new Intent(getActivity(), ContactDetailActivity.class);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.setVisibility(View.INVISIBLE);
                done.setVisibility(View.VISIBLE);
                scrollcontact.setVisibility(View.VISIBLE);
                info_icon.setVisibility(View.INVISIBLE);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

    private void  init(View view){
        back = view.findViewById(R.id.back);
        addfav = view.findViewById(R.id.create_fav);
        info_icon = view.findViewById(R.id.info_icon);
        edit = view.findViewById(R.id.fav_edit);
        done = view.findViewById(R.id.fav_done);
        scrollcontact = view.findViewById(R.id.scrollcontact);
    }
}