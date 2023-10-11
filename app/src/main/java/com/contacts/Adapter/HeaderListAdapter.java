package com.contacts.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Activity.ContactDetailActivity;
import com.contacts.Fragment.ContactsFragment;
import com.contacts.Model.Header;
import com.contacts.Model.Users;
import com.contacts.R;

import java.util.ArrayList;

public class HeaderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    ContactsFragment contactsFragment;
    ArrayList<Header> usersArrayList;

    public HeaderListAdapter(ContactsFragment contactsFragment, ArrayList<Header> usersArrayList) {
        this.contactsFragment = contactsFragment;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 0:
                View v1 = inflater.inflate(R.layout.header_list_item, parent, false);
                viewHolder = new HeaderViewHolder(v1);
                break;
            case 1:
                View v2 = inflater.inflate(R.layout.contact_list_item, parent, false);
                viewHolder = new ItemViewHolder(v2);
        }
        return  viewHolder;


//        if (viewType == TYPE_ITEM) {
//            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);
//            return new ItemViewHolder(itemView);
//        }
//        else if (viewType == TYPE_HEADER) {
//            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_list_item, parent, false);
//            return new HeaderViewHolder(itemView);
//        }
//        else {
//            return ;
//        }
    }

    @Override
    public int getItemViewType(int position) {
//        System.out.println("ViewType Position : " + position + "\n" + objectList.size());
        Header header = usersArrayList.get(position);
        if (header instanceof Header) {
            return 0;
        } else if (header instanceof Header) {
            return 1;
        }else {
            return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.contactHead.setText(usersArrayList.get(position).getHeader());
        }
        else if (holder instanceof ItemViewHolder){
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
//            itemViewHolder.personName.setText(usersArrayList.get(position).getUsersList().get(position).getFirst());
            itemViewHolder.personName.setText(usersArrayList.get(position).getUsersList().get(position).getFirst());
            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(contactsFragment.getActivity(), ContactDetailActivity.class);
//                    intent.putExtra("image",usersArrayList.get(position).getUsersList().get(position).getImage());
//                    intent.putExtra("first",usersArrayList.get(position).getUsersList().get(position).getFirst());
//                    intent.putExtra("last",usersArrayList.get(position).getUsersList().get(position).getLast());
//                    intent.putExtra("pphone",usersArrayList.get(position).getUsersList().get(position).getPersonPhone());
//                    intent.putExtra("ophone",usersArrayList.get(position).getUsersList().get(position).getOfficePhone());
//                    contactsFragment.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (usersArrayList.get(position)) {
//            return TYPE_HEADER;
//        } else {
//            return TYPE_ITEM;
//        }
//    }
//
//    private boolean isPositionHeader(int position) {
//        return usersArrayList.get(position) != null;
//    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView contactHead;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            contactHead = itemView.findViewById(R.id.header);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView personName;
        ImageView personImage;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            personName = itemView.findViewById(R.id.personName);
            personImage = itemView.findViewById(R.id.personImage);
        }
    }
}

////    ArrayList<Users> list;
//    ArrayList<Header> headerlist;
//    ContactsFragment contactsFragment;
//    private List<String> mDataArray;
//    private ArrayList<Integer> mSectionPositions;
//
//    public HeaderListAdapter(ArrayList<Users> list, ContactsFragment contactsFragment) {
////        this.list = list;
//        this.contactsFragment = contactsFragment;
//    }
//
//    @NonNull
//    @Override
//    public headerviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_list_item,parent,false);
//        return new headerviewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull headerviewHolder holder, int position) {
//        Header header = headerlist.get(position);
//
////        holder.header.setText(list.get(position).getFirst());
//
//
////        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.contactrecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
////        layoutManager.setInitialPrefetchItemCount(header.getChildItemList().size());
////
////        ContactListAdapter contactListAdapter = new ContactListAdapter(contactsFragment,header.getChildItemList());
////        holder.contactrecyclerView.setLayoutManager(layoutManager);
////        holder.contactrecyclerView.setAdapter(contactListAdapter);
//    }
//
//    @Override
//    public int getItemCount() {
//        return headerlist.size();
//    }
//
//    @Override
//    public Object[] getSections() {
//        List<String> sections = new ArrayList<>(26);
//        mSectionPositions = new ArrayList<>(26);
//        for (int i = 0, size = mDataArray.size(); i < size; i++) {
//            String section = String.valueOf(mDataArray.get(i).charAt(0)).toUpperCase();
//            if (!sections.contains(section)) {
//                sections.add(section);
//                mSectionPositions.add(i);
//            }
//        }
//        return sections.toArray(new String[0]);
//    }
//
//    @Override
//    public int getPositionForSection(int i) {
//        return mSectionPositions.get(i);
//    }
//
//    @Override
//    public int getSectionForPosition(int i) {
//        return 0;
//    }
//
//    public class headerviewHolder extends RecyclerView.ViewHolder {
//        TextView header;
//        RecyclerView contactrecyclerView;
//        public headerviewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            header = itemView.findViewById(R.id.header);
//            contactrecyclerView = itemView.findViewById(R.id.show_contact_recyclerview);
//        }
//    }
//}
