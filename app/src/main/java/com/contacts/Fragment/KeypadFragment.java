package com.contacts.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.contacts.R;

public class KeypadFragment extends Fragment {

    KeypadListAdapter keypadListAdapter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keypad, container, false);

        init(view);

//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
//        bottomSheetDialog.setContentView(R.layout.fragment_keypad);
//        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        keypadListAdapter  = new KeypadListAdapter(getContext());
//        recyclerView = bottomSheetDialog.findViewById(R.id.framelayout);
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(keypadListAdapter);
//        bottomSheetDialog.show();

        return view;
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.keypadrecyclerview);
    }
}