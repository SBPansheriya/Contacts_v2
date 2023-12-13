package com.contacts.Adapter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Activity.CreateContactActivity;
import com.contacts.Activity.UpdateContactActivity;
import com.contacts.Model.Phone;
import com.contacts.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class UpdateNumberAdapter extends RecyclerView.Adapter<UpdateNumberAdapter.viewHolder> {

    UpdateContactActivity updateContactActivity;
    ArrayList<Phone> phoneArrayList;
    PhoneTypeSelectionUpdateAdapter phoneTypeSelectionUpdateAdapter;

    public UpdateNumberAdapter(UpdateContactActivity updateContactActivity, ArrayList<Phone> phoneArrayList) {
        this.updateContactActivity = updateContactActivity;
        this.phoneArrayList = phoneArrayList;
    }

    @NonNull
    @Override
    public UpdateNumberAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_new_phone_number_item_list, parent, false);
        return new UpdateNumberAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateNumberAdapter.viewHolder holder, int position) {

        if (TextUtils.isEmpty(phoneArrayList.get(position).getLabel())) {
            holder.phone_type_other_number_create.setText("Mobile");
        } else {
            holder.phone_type_other_number_create.setText(phoneArrayList.get(position).getLabel());
        }

        holder.otherNumber.setText(phoneArrayList.get(position).getPhonenumber());

        holder.otherNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                phoneArrayList.get(position).setPhonenumber(charSequence.toString());
                ((UpdateContactActivity) updateContactActivity).getData(charSequence.toString(), position);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.remove_other_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (position > 0) {
                    phoneArrayList.remove(position);
                    updateList(phoneArrayList);
                }
            }
        });

        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showbottomdialog(position);
            }
        });

        holder.selectionphone_other_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showbottomdialog(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return phoneArrayList.size();
    }

    private void showbottomdialog(int position) {
        BottomSheetDialog dialog = new BottomSheetDialog(updateContactActivity);
        dialog.setContentView(R.layout.phone_type_selection_dialog);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        RecyclerView recyclerView = dialog.findViewById(R.id.phone_type_recyclerview);

        LinearLayoutManager manager = new LinearLayoutManager(updateContactActivity);
        phoneTypeSelectionUpdateAdapter = new PhoneTypeSelectionUpdateAdapter(updateContactActivity, position,dialog);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(phoneTypeSelectionUpdateAdapter);

        dialog.show();
    }

    public void updateList(ArrayList<Phone> phoneArrayList) {
        this.phoneArrayList = phoneArrayList;
        notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView phone_type_other_number_create;
        ImageView selectionphone_other_create, remove_other_number;
        EditText otherNumber;
        LinearLayout otherNumberLinear;
        RelativeLayout show;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            phone_type_other_number_create = itemView.findViewById(R.id.phone_type_other_number_create);
            selectionphone_other_create = itemView.findViewById(R.id.selectionphone_other_create);
            remove_other_number = itemView.findViewById(R.id.remove_other_number);
            otherNumber = itemView.findViewById(R.id.otherNumber);
            otherNumberLinear = itemView.findViewById(R.id.otherNumberLinear);
            show = itemView.findViewById(R.id.show);
        }
    }
}
