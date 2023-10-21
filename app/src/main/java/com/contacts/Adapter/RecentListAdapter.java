package com.contacts.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.contacts.Model.Recent;
import com.contacts.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecentListAdapter extends RecyclerView.Adapter<RecentListAdapter.recentviewholder> {

    Context context;
    ArrayList<Recent> recentArrayList;

    public RecentListAdapter(Context context, ArrayList<Recent> recentArrayList) {
        this.context = context;
        this.recentArrayList = recentArrayList;
    }

    @NonNull
    @Override
    public recentviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_list_item, parent, false);
        return new recentviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recentviewholder holder, int position) {

        if (recentArrayList.get(position).getIamge() == null) {
            holder.recent_personImage.setImageResource(R.drawable.person_placeholder);
        } else {
            Picasso.get().load(recentArrayList.get(position).getIamge()).into(holder.recent_personImage);
        }

        if (recentArrayList.get(position).getStatus().equals("Outgoing")) {
            holder.outgoing.setVisibility(View.VISIBLE);
            holder.missedCall.setVisibility(View.GONE);
            holder.incoming.setVisibility(View.GONE);
        } else if (recentArrayList.get(position).getStatus().equals("Incoming")) {
            holder.outgoing.setVisibility(View.GONE);
            holder.missedCall.setVisibility(View.GONE);
            holder.incoming.setVisibility(View.VISIBLE);
        } else if (recentArrayList.get(position).getStatus().equals("Missed")) {
            holder.outgoing.setVisibility(View.GONE);
            holder.missedCall.setVisibility(View.VISIBLE);
            holder.incoming.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(recentArrayList.get(position).getContactname())) {
            holder.recent_name.setText(recentArrayList.get(position).getContactname());
        } else {
            holder.recent_name.setText(recentArrayList.get(position).getContactnumber());
        }

        holder.recent_date.setText(recentArrayList.get(position).getDate());

        holder.recent_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              Toast.makeText(context, "Calling " + recentArrayList.get(position).getContactname(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + recentArrayList.get(position).getContactnumber()));
                context.startActivity(intent);
                Toast.makeText(context, "Calling " + recentArrayList.get(position).getContactname(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recentArrayList.size();
    }

    public class recentviewholder extends RecyclerView.ViewHolder {
        ImageView recent_personImage, recent_call, missedCall, incoming, outgoing;
        TextView recent_name, recent_date;

        public recentviewholder(@NonNull View itemView) {
            super(itemView);
            recent_personImage = itemView.findViewById(R.id.recent_personImage);
            recent_call = itemView.findViewById(R.id.recents_call);
            recent_name = itemView.findViewById(R.id.recent_name);
            recent_date = itemView.findViewById(R.id.recent_date);
            missedCall = itemView.findViewById(R.id.missedcall);
            incoming = itemView.findViewById(R.id.incoming);
            outgoing = itemView.findViewById(R.id.outgoing);
        }
    }
}
