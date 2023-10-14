package com.contacts.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.R;

public class RecentListAdapter extends RecyclerView.Adapter<RecentListAdapter.recentviewholder> {

    Context context;
    String[] shayariname;

    public RecentListAdapter(Context context, String[] shayariname) {
        this.context = context;
        this.shayariname = shayariname;
    }

    @NonNull
    @Override
    public recentviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_list_item,parent,false);
        return new recentviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recentviewholder holder, int position) {
        holder.recent_name .setText(shayariname[position]);
    }

    @Override
    public int getItemCount() {
        return shayariname.length;
    }

    public class recentviewholder extends RecyclerView.ViewHolder {
        ImageView recent_personImage,recenet_call;
        TextView recent_name;
        public recentviewholder(@NonNull View itemView) {
            super(itemView);
            recent_personImage = itemView.findViewById(R.id.recent_personImage);
            recenet_call = itemView.findViewById(R.id.recents_call);
            recent_name = itemView.findViewById(R.id.recent_name);
        }
    }
}
