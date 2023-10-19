package com.contacts.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Fragment.KeypadFragment;
import com.contacts.R;

public class KeypadListAdapter extends RecyclerView.Adapter<KeypadListAdapter.keypadviewholder> {

    KeypadFragment keypadFragment;

    public KeypadListAdapter(KeypadFragment keypadFragment) {
        this.keypadFragment = keypadFragment;
    }

    @NonNull
    @Override
    public KeypadListAdapter.keypadviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(keypadFragment.getContext()).inflate(R.layout.keypad_list_item,parent,false);
        return new keypadviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KeypadListAdapter.keypadviewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class
    keypadviewholder extends RecyclerView.ViewHolder {
        public keypadviewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
