package com.contacts.Adapter;

import static com.contacts.Class.Constant.typeArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Activity.CreateContactActivity;
import com.contacts.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class PhoneTypeSelectionAdapter extends RecyclerView.Adapter<PhoneTypeSelectionAdapter.viewholder> {

    CreateContactActivity createContactActivity;
    int positionget;
    BottomSheetDialog dialog;

    public PhoneTypeSelectionAdapter(CreateContactActivity createContactActivity, int positon, BottomSheetDialog dialog) {
        this.createContactActivity = createContactActivity;
        this.positionget = positon;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public PhoneTypeSelectionAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_type_selection_list_item, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneTypeSelectionAdapter.viewholder holder, int position) {

        holder.phone_type.setText(typeArrayList.get(position).getLabel());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CreateContactActivity) createContactActivity).getposition(typeArrayList.get(position),positionget);
                dialog.dismiss();
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CreateContactActivity) createContactActivity).getposition(typeArrayList.get(position),positionget);
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return typeArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView phone_type;
        CheckBox checkBox;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            phone_type = itemView.findViewById(R.id.phone_type_selection);
            checkBox = itemView.findViewById(R.id.checkBox_phone_type);
        }
    }
}
