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
ImageView back;
Button addfav;
String shayariname[] = {"Dosti Shayari","God Shayari","Love Shayari","Brithday Shayari","Holi Shayari","Sharab Shayari","Politics Shayari","Bewfa Shayari","Other Shayari"};
public static ImageView edit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        init(view);

        favListAdapter = new FavListAdapter(FavoritesFragment.this,shayariname);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(favListAdapter);

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

//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                edit.setVisibility(View.GONE);
//                done.setVisibility(View.VISIBLE);
////                scrollcontact.setVisibility(View.VISIBLE);
////                info_icon.setVisibility(View.GONE);
//            }
//        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDestroyView();
            }
        });
        return view;
    }

    private void  init(View view){
        back = view.findViewById(R.id.back);
        addfav = view.findViewById(R.id.create_fav);
        edit = view.findViewById(R.id.fav_edit);
        done = view.findViewById(R.id.fav_done);
        recyclerView = view.findViewById(R.id.recyclerView);
    }
}