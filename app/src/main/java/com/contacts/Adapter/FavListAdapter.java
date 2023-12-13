package com.contacts.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Fragment.FavoritesFragment;
import com.contacts.Model.Users;
import com.contacts.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.favouriteviewholder>{

    FavoritesFragment favoritesFragment;
    ArrayList<Users> favoriteList;

    public FavListAdapter(FavoritesFragment favoritesFragment, ArrayList<Users> favoriteList) {
        this.favoritesFragment = favoritesFragment;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public favouriteviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_item_list,parent,false);
        return new favouriteviewholder(view);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull favouriteviewholder holder, int position) {

        if (favoriteList.get(position).image == null) {
            holder.fav_person_image.setImageResource(R.drawable.person_placeholder);
        } else {
            Picasso.get().load(favoriteList.get(position).image).into(holder.fav_person_image);
        }

        holder.fav_person_name.setText(favoriteList.get(position).getFullName());

//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @SuppressLint({"RestrictedApi", "NewApi"})
//            @Override
//            public boolean onLongClick(View view) {
//                MenuPopupHelper menuPopupHelper = new MenuPopupHelper(favoritesFragment.getContext(),menuBuilder,view);
//                menuPopupHelper.setForceShowIcon(true);
//
//                menuBuilder.setCallback(new MenuBuilder.Callback() {
//                    @Override
//                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
//                        return false;
//                    }
//
//                    @Override
//                    public void onMenuModeChange(@NonNull MenuBuilder menu) {
//
//                    }
//                });
//
//                PopupMenu popupMenu = new PopupMenu(favoritesFragment.getContext(),view);
//                popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupMenu.getMenu());
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        if (menuItem.getItemId() == R.id.remove){
//
//                        }
//                        if (menuItem.getItemId() == R.id.contactinfo) {
//                            ((FavoritesFragment)favoritesFragment).intentPassFav(favoriteList.get(position));
//                        }
//
//                        notifyDataSetChanged();
//                        return false;
//                    }
//                });
//
//                popupMenu.show();
//                return true;
//            }
//        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FavoritesFragment) favoritesFragment).call(favoriteList.get(position).getPhoneArrayList().get(position).getPhonenumber());
            }
        });

        holder.info_plite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FavoritesFragment) favoritesFragment).intentPassFav(favoriteList.get(position),favoriteList.get(position).getPhoneArrayList());
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public class favouriteviewholder extends RecyclerView.ViewHolder {
        ImageView fav_person_image,info_plite,scrollcontact;
        TextView fav_person_name;

        public favouriteviewholder(@NonNull View itemView) {
            super(itemView);
            fav_person_image = itemView.findViewById(R.id.fav_personImage);
            info_plite = itemView.findViewById(R.id.info_icon);
            scrollcontact = itemView.findViewById(R.id.scrollcontact);
            fav_person_name = itemView.findViewById(R.id.fav_personName);
        }
    }
}
