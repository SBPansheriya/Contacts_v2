package com.contacts.Adapter;

import static com.contacts.Class.Constant.typeArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Activity.UpdateContactActivity;
import com.contacts.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class PhoneTypeSelectionUpdateAdapter extends RecyclerView.Adapter<PhoneTypeSelectionUpdateAdapter.viewholder> {

    UpdateContactActivity updateContactActivity;
    int positionget;
    BottomSheetDialog dialog;

    public PhoneTypeSelectionUpdateAdapter(UpdateContactActivity updateContactActivity, int positon, BottomSheetDialog dialog) {
        this.updateContactActivity = updateContactActivity;
        this.positionget = positon;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public PhoneTypeSelectionUpdateAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_type_selection_list_item, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneTypeSelectionUpdateAdapter.viewholder holder, int position) {
        holder.phone_type.setText(typeArrayList.get(position).getLabel());

//        holder.checkBox.setChecked(position == selectedPosition);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((UpdateContactActivity) updateContactActivity).getposition(typeArrayList.get(position), positionget);
                dialog.dismiss();
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((UpdateContactActivity) updateContactActivity).getposition(typeArrayList.get(position), positionget);
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
