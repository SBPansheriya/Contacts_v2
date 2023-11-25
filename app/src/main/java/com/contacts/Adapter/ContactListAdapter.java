//package com.contacts.Adapter;
//
//import static com.contacts.Class.Constant.favoriteList;
//
//import android.content.ContentResolver;
//import android.content.ContentValues;
//import android.content.Context;
//import android.net.Uri;
//import android.provider.ContactsContract;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.contacts.Class.Constant;
//import com.contacts.Fragment.ContactsFragment;
//import com.contacts.Model.Header;
//import com.contacts.Model.Users;
//import com.contacts.R;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//
//public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {
//
//    ContactsFragment contactsFragment;
//    ArrayList<Users> usersList;
//    ArrayList<Users> filteredData;
//    int favorites;
//    boolean isEdit = false;
//    String button;
//
//    public ContactListAdapter(ContactsFragment contactsFragment, ArrayList<Users> usersList, boolean isEdit, String button) {
//        this.contactsFragment = contactsFragment;
//        this.usersList = usersList;
//        this.filteredData = new ArrayList<>(usersList);
//        this.isEdit = isEdit;
//        this.button = button;
//    }
//
//    @NonNull
//    @Override
//    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);
//        return new ContactViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
//
//
////        if (Constant.favoriteList.size() > 0) {
////            boolean isMatch = false;
////            for (int i = 0; i < Constant.favoriteList.size(); i++) {
////                if (usersList.get(position).contactId.equals(Constant.favoriteList.get(i).contactId)) {
////                    isMatch = true;
////                    break;
////                }
////            }
////            if (isMatch) {
////                holder.fav_add.setVisibility(View.VISIBLE);
////                holder.no_fav_add.setVisibility(View.GONE);
////            } else {
////                holder.no_fav_add.setVisibility(View.VISIBLE);
////                holder.fav_add.setVisibility(View.GONE);
////            }
////        }
////        else {
////            holder.no_fav_add.setVisibility(View.VISIBLE);
////            holder.fav_add.setVisibility(View.GONE);
////        }
////
////        if (button.equals("fav")){
////            holder.no_fav_add.setVisibility(View.VISIBLE);
////        }
////        if (button.equals("no_fav_found")){
////            holder.no_fav_add.setVisibility(View.VISIBLE);
////        }
////        if (button.equals("contact")){
////            holder.no_fav_add.setVisibility(View.GONE);
////            holder.fav_add.setVisibility(View.GONE);
////        }
////
////        holder.no_fav_add.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                holder.fav_add.setVisibility(View.VISIBLE);
////                holder.no_fav_add.setVisibility(View.GONE);
////                favorites = 1;
////                addToFavorites(contactsFragment.getContext(), usersList.get(position).getContactId(),usersList.get(position).image,usersList.get(position).first,usersList.get(position).last,usersList.get(position).personPhone,favorites);
////            }
////        });
////
////        holder.fav_add.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                holder.fav_add.setVisibility(View.GONE);
////                holder.no_fav_add.setVisibility(View.VISIBLE);
////                favorites = 0;
////                addToFavorites(contactsFragment.getContext(), usersList.get(position).getContactId(),usersList.get(position).image,usersList.get(position).first,usersList.get(position).last,usersList.get(position).personPhone,favorites);
////            }
////        });
//
//        holder.personName.setText(usersList.get(position).first + " " + usersList.get(position).last);
//
//        if (TextUtils.isEmpty(usersList.get(position).image)) {
//            holder.personImage.setImageResource(R.drawable.person_placeholder);
//        } else {
//            Picasso.get().load(usersList.get(position).image).into(holder.personImage);
//        }
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((ContactsFragment)contactsFragment).intentPass(usersList.get(position));
//            }
//        });
//
//        holder.checkBox.setChecked(usersList.get(position).isSelected());
//        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            usersList.get(position).setSelected(isChecked);
//        });
//
//        if (isEdit) {
//            holder.personImage.setVisibility(View.GONE);
//            holder.checkBox.setVisibility(View.VISIBLE);
//            holder.itemView.setClickable(false);
//        }
//        else {
//            holder.personImage.setVisibility(View.VISIBLE);
//            holder.checkBox.setVisibility(View.GONE);
//            holder.itemView.setClickable(true);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return usersList.size();
//    }
//
//    public static void addToFavorites(Context context, String contactId,String image,String first,String last,String phone,int favorite) {
//        ContentResolver contentResolver = context.getContentResolver();
//        ContentValues values = new ContentValues();
//        Uri rawContactUri = Uri.withAppendedPath(ContactsContract.RawContacts.CONTENT_URI, contactId);
//        values.put(ContactsContract.CommonDataKinds.Phone.STARRED, favorite); // 1 for favorite, 0 for not favorite
//        contentResolver.update(rawContactUri, values, null, null);
//
//        Users users = new Users(contactId,image,first,last,phone,"");
//        if (favorite == 1) {
//            favoriteList.add(users);
//        }
//        else {
//            for (int i = 0; i < favoriteList.size(); i++) {
//                if (favoriteList.get(i).contactId.equalsIgnoreCase(users.contactId)) {
//                    favoriteList.remove(i);
//                    break;
//                }
//            }
//        }
//    }
//
//    public class ContactViewHolder extends RecyclerView.ViewHolder {
//        TextView personName;
//        ImageView personImage,fav_add,no_fav_add;
//        CheckBox checkBox;
//
//        public ContactViewHolder(@NonNull View itemView) {
//            super(itemView);
//            personName = itemView.findViewById(R.id.personName);
//            personImage = itemView.findViewById(R.id.personImage);
//            checkBox = itemView.findViewById(R.id.checkBox1);
//            fav_add = itemView.findViewById(R.id.fav_add);
//            no_fav_add = itemView.findViewById(R.id.no_fav_add);
//        }
//    }
//}
