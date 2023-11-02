package com.contacts.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.contacts.Activity.KeypadScreen;
import com.contacts.R;

public class GroupsFragment extends Fragment {

    ImageView open_keypad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);

       init(view);

        open_keypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), KeypadScreen.class);
                intent.putExtra("check","group");
                startActivity(intent);
            }
        });

        return view;
    }
    private void init(View view) {
        open_keypad = view.findViewById(R.id.open_keypad);
    }
}